package com.bam.testwhole;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


public class WelcomeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        new Thread(new Runnable(){
            @Override
            public void run() {
                try{
                    Thread.sleep(2000);
                    startActivity(new Intent().setClass(WelcomeActivity.this,Login.class));
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }).start();

    }


}