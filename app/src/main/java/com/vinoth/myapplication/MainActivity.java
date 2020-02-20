package com.vinoth.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button startService;
    Button stopService;
    TextView textView;
    Intent intent;
    Button bindService;
    Button unBindService;
    Button getRandomNumber;
    MyService myService;
    boolean isServiceBound;
    ServiceConnection serviceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService = findViewById(R.id.startService);
        stopService = findViewById(R.id.stopService);
        textView = findViewById(R.id.textView);
        bindService = findViewById(R.id.bindService);
        unBindService = findViewById(R.id.unBindService);
        getRandomNumber = findViewById(R.id.getRandomNumber);

        startService.setOnClickListener(this);
        stopService.setOnClickListener(this);

        bindService.setOnClickListener(this);
        unBindService.setOnClickListener(this);
        getRandomNumber.setOnClickListener(this);

        System.out.println("MainActivity.onCreate : Thread : " + Thread.currentThread().getId());
        intent = new Intent(this, MyService.class);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.startService:
                startService(intent);
                break;
            case R.id.stopService:
                stopService(intent);
                break;
            case R.id.bindService:
                bindServiceMethod();
                break;
            case R.id.unBindService:
                unBindServiceMethod();
                break;
            case R.id.getRandomNumber:
                getRandomNumberMethod();
                break;
        }
    }

    private void bindServiceMethod() {

        if (serviceConnection == null) {
            serviceConnection = new ServiceConnection() {

                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    MyService.MyBinder myBinder = (MyService.MyBinder) service;
                    myService = myBinder.geyService();
                    isServiceBound = true;
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    isServiceBound = false;
                }
            };
        }
        bindService(intent, serviceConnection, Context.BIND_ABOVE_CLIENT);
    }

    private void unBindServiceMethod() {
        if (isServiceBound) {
            unbindService(serviceConnection);
            isServiceBound = false;
        }
    }

    private void getRandomNumberMethod() {
        if (isServiceBound) {
            textView.setText(" " + myService.getmRandomNumber());
        } else {
            textView.setText("Service not bound");
        }
    }
}