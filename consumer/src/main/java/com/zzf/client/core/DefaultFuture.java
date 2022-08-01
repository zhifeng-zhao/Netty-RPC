package com.zzf.client.core;


import com.zzf.client.parm.ClientRequest;
import com.zzf.client.parm.Response;

import java.util.concurrent.ConcurrentHashMap;
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

    public DefaultFuture(ClientRequest request) {
        allDefaultFuture.put(request.getId(), this);
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
}
