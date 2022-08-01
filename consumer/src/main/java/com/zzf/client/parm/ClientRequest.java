package com.zzf.client.parm;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author zzf
 * @date 2022/7/30 4:34 下午
 */
public class ClientRequest {
    private final long id;
    private Object content;
    private final AtomicLong aid = new AtomicLong(1);
    private String command;

    public ClientRequest() {
        id = aid.incrementAndGet();
    }

    public long getId() {
        return id;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
