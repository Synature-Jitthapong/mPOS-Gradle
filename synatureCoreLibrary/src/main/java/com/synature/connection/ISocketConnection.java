package com.synature.connection;

public interface ISocketConnection {
    public void send (String msg) throws Exception;
    public String receive();
    public void close();
}
