package com.unable.droidserver;


import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleHttpServer {

    private final ExecutorService threadPool;
    private final WebConfiguration webConfig;
    private boolean isEnable;
    private ServerSocket socket;
    private Set<IResourceUriHandler> resourceUriHandlers;

    public SimpleHttpServer(WebConfiguration webConfig) {
        this.webConfig = webConfig;
        threadPool = Executors.newCachedThreadPool();
        resourceUriHandlers = new HashSet<>();
    }

    /**
     * 启动
     */
    public void startAsync(){
        isEnable = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                doProcSync();
            }
        }).start();
    }

    /**
     * 停止
     */
    public void stopAsync() throws IOException {
        if (!isEnable) {
            return;
        }
        isEnable = false;
        if (socket != null) {
            socket.close();
            socket = null;
        }
    }

    private void doProcSync() {
        try {
            InetSocketAddress socketAddress = new InetSocketAddress(webConfig.getPort());
            socket = new ServerSocket();
            socket.bind(socketAddress);
            while (isEnable){
                final Socket remotePeer = socket.accept();
                threadPool.submit(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("sss","a remote peer accept..."+remotePeer.getRemoteSocketAddress().toString());
                        onAcceptRemotePeer(remotePeer);
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerResourceHandler(IResourceUriHandler handler){
        resourceUriHandlers.add(handler);
    }

    private void onAcceptRemotePeer(Socket remotePeer) {
        try {
            HttpContext httpContext = new HttpContext();
            httpContext.setUnderlySocket(remotePeer);

            InputStream inputStream = remotePeer.getInputStream();
            String headerLine;
            String resourceUri = StreamToolkit.readline(inputStream);
            if (resourceUri != null) {
                resourceUri = resourceUri.split(" ")[1];
            }
            Log.d("sss",resourceUri);
            while((headerLine = StreamToolkit.readline(inputStream))!=null){
                if (headerLine.equals("\r\n")) {
                    break;
                }
                String[] pair = headerLine.split(": ");
                httpContext.addRequestHeader(pair[0],pair[1]);
//                Log.i("sss",headerLine);
            }

            for (IResourceUriHandler resourceUriHandler : resourceUriHandlers) {
                if(!resourceUriHandler.accept(resourceUri)){
                    continue;
                }
                resourceUriHandler.handle(resourceUri,httpContext);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
