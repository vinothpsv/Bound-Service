package com.vinoth.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.Random;

public class MyService extends Service {

    private int mRandomNumber;
    private boolean mIsRandomGeneratorOn;

    private final int MIN = 0;
    private final int MAX = 100;
    private IBinder mIBinder = new MyBinder();

    class MyBinder extends Binder {

        public MyService geyService() {
            return MyService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mIBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("MyService.onStartCommand : Thread : " + Thread.currentThread().getId());
        mIsRandomGeneratorOn = true;

        new Thread(new Runnable() {

            @Override
            public void run() {
                startRandomGenerator();
            }
        }).start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRandomGenerator();
    }

    private void startRandomGenerator() {
        while (mIsRandomGeneratorOn) {
            try {
                Thread.sleep(1000);
                if (mIsRandomGeneratorOn) {
                    mRandomNumber = new Random().nextInt(MAX) + MIN;
                    System.out.println("MyService.startRandomGenerator : "
                            + Thread.currentThread().getId()
                            + " : Random Number : "
                            + mRandomNumber);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void stopRandomGenerator() {
        mIsRandomGeneratorOn = false;
    }

    public int getmRandomNumber() {
        return mRandomNumber;
    }
}