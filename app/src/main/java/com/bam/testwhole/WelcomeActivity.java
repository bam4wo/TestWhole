package com.bam.testwhole;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Toast;

import com.vishnusivadas.advanced_httpurlconnection.PutData;


public class WelcomeActivity extends AppCompatActivity {
    String IMEINumber;
    private static final int REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        getImei();
        if(!IMEINumber.equals("")){
            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //Starting Write and Read data with URL
                    //Creating array for parameters
                    String[] field = new String[1];
                    field[0] = "imei_p";
                    //Creating array for data
                    String[] data = new String[1];
                    data[0] = IMEINumber;
                    PutData putData = new PutData("http://192.168.1.109/Hospital/imeiPublic.php", "POST", field, data); //網址要改成自己的php檔位置及自己的ip
                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            String result = putData.getResult();
                            if (result.equals("Get IMEI Public Success")) {
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
                            }else {
                                new Thread(new Runnable(){
                                    @Override
                                    public void run() {
                                        try{
                                            Thread.sleep(2000);
                                            startActivity(new Intent().setClass(WelcomeActivity.this,SignUp.class));
                                        }catch (InterruptedException e){
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();
                            }
                        }
                    }
                }
            });
        }


        /*
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
        }).start();*/

    }

    //取得imei
    public void getImei(){
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_CODE);
            return;
        }
        IMEINumber = telephonyManager.getDeviceId();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission denied.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}