package com.unable.droidserver;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

public class ResourceInAssetsHandler implements IResourceUriHandler {

    private String acceptPrefix = "/static/";
    private Context mContext;

    public ResourceInAssetsHandler(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public boolean accept(String uri) {
        return uri.startsWith(acceptPrefix);
    }

    @Override
    public void handle(String uri, HttpContext httpContext) throws IOException {
        OutputStream os = httpContext.getUnderlySocket().getOutputStream();
//        PrintWriter printWriter = new PrintWriter(os);
        InputStream open = mContext.getAssets().open("aaa.html");
        byte[] bytes = StreamToolkit.streamToByte(open);
        open.close();
        PrintStream printStream = new PrintStream(os);
        printStream.println("HTTP/1.1 200 OK");
        printStream.println("Content-Length:"+bytes.length);
        printStream.println("Content-Type:text/html");
        printStream.println();
        printStream.write(bytes);
        printStream.flush();
        printStream.close();
    }
}
