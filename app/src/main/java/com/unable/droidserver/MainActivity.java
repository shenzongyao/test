package com.unable.droidserver;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private SimpleHttpServer httpServer;
    private byte[] numm = {0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        WebConfiguration wc = new WebConfiguration();
//        wc.setPort(8088);
//        wc.setMaxParallels(10);
//        httpServer = new SimpleHttpServer(wc);
//        httpServer.registerResourceHandler(new ResourceInAssetsHandler(this));
//        httpServer.startAsync();
        new MyThread(numm).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (numm[0] >= 0) {
                    SystemClock.sleep(1000);
                    numm[0] = (byte) (new Random().nextInt(10)-1);
                    System.out.println(numm[0]);
                }
            }
        }).start();
    }

    class MyThread extends Thread{
        byte[] num = {};
        public MyThread(byte[] num) {
            this.num = num;
        }

            @Override
        public void run() {
            super.run();
            while(true){
                if (num[0] >= 0) {
                    System.out.println("线程"+getId()+"打印:"+num[0]);
                    SystemClock.sleep(1000);
                }else {
                    break;
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        try {
            httpServer.stopAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}
