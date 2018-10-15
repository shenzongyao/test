package com.unable.droidserver;

import java.net.Socket;
import java.util.HashMap;

public class HttpContext {
    private final HashMap<String, String> requestHeaders;
    private Socket underlySocket;

    public HttpContext() {
        requestHeaders = new HashMap<>();
    }

    public void setUnderlySocket(Socket socket) {
        this.underlySocket = socket;
    }

    public Socket getUnderlySocket() {
        return underlySocket;
    }

    public void addRequestHeader(String headerName, String headerValue) {
        requestHeaders.put(headerName,headerValue);
    }

    public String getRequestHeaderValue(String headerName){
        return requestHeaders.get(headerName);
    }
}
