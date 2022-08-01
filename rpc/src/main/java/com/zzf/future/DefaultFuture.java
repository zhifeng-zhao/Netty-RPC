package com.zzf.future;

import com.zzf.model.ClientRequest;
import com.zzf.model.Response;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zzf
 * @date 2022/7/30 4:40 下午
 */
public class DefaultFuture {

    public static final ConcurrentHashMap<Long, DefaultFuture> allDefaultFuture = new ConcurrentHashMap<Long, DefaultFuture>();
    final ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private Response response;
    private long timeOut = 2 * 60 * 1000;
    private long startTime = System.currentTimeMillis();

    public DefaultFuture(ClientRequest request) {
        allDefaultFuture.put(request.getId(), this);
    }

    public long getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(long timeOut) {
        this.timeOut = timeOut;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public Response get() {
        lock.lock();
        try {
            while (!done()) {
                condition.await();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return this.response;
    }

    public Response get(long time) {
        lock.lock();
        try {
            while (!done()) {
                condition.await(time, TimeUnit.SECONDS);
                if (System.currentTimeMillis() - startTime > time) {
                    System.out.println("请求超时");
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return this.response;
    }

    public static void receive(Response response) {
        DefaultFuture future = allDefaultFuture.get(response.getId());
        future.lock.lock();
        try {
            future.setResponse(response);
            future.condition.signal();
            allDefaultFuture.remove(future);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            future.lock.unlock();
        }
    }

    public Boolean done() {
        return this.response != null;
    }

    /**
     * 清理链路线程
     */
    static class FutureThread extends Thread {
        @Override
        public void run() {
            allDefaultFuture.keySet().forEach(key -> {
                DefaultFuture df = allDefaultFuture.get(key);
                if (df == null) {
                    allDefaultFuture.remove(key);
                } else if (df.getTimeOut() < System.currentTimeMillis() - df.getStartTime()) {
                    Response response = new Response();
                    response.setCode("33333");
                    response.setMsg("链路超时");
                    receive(response);
                }
            });
        }
    }

    static {
        FutureThread futureThread = new FutureThread();
        futureThread.setDaemon(true);
        futureThread.start();
    }
}
