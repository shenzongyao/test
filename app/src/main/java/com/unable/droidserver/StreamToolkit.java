package com.unable.droidserver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamToolkit {

    public static String readline(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        int c1 = 0;
        int c2 = 0;
        while(c2 != -1 && !(c1 == '\r' && c2 == '\n')){
            c1 = c2;
            c2 = is.read();
            sb.append((char) c2);
        }
        if (sb.length() == 0) {
            return null;
        }
        return sb.toString();
    }

    public static byte[] streamToByte(InputStream is) throws IOException {
        byte[] buf = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = is.read(buf)) != -1){
            bos.write(buf,0,len);
        }
        buf = bos.toByteArray();
        bos.close();
        is.close();
        return buf;
    }
}
