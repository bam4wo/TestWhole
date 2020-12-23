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
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Login extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    String IMEINumber;
    TextView imei;
    private static final int REQUEST_CODE = 101;

    TextInputEditText textInputEditTextIDorEmail, textInputEditTextPassword;
    Button buttonLogin;
    TextView textViewSignUp;
    ProgressBar progressBar;

    TextInputLayout textInputLayout;
    boolean isNext = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textInputEditTextIDorEmail = findViewById(R.id.IDorEmail);
        textInputEditTextPassword = findViewById(R.id.password);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewSignUp = findViewById(R.id.signUpText);
        progressBar = findViewById(R.id.progress);
        textInputLayout = findViewById(R.id.textInputLayoutEmployeeId);
        //隱藏密碼
        textInputEditTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

        handleSSLHandshake();

        imei = findViewById(R.id.ed_imei);
        getImei();

        //切換登入方式
        Switch sw = findViewById(R.id.sw);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isNext = isChecked;
                textInputLayout.setHint(isNext ? getString(R.string.employeeid ): getString(R.string.email));
            }
        });

        CheckBox cb = findViewById(R.id.checkBox2);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    //顯示密碼
                    textInputEditTextPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //隱藏密碼
                    textInputEditTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        //還沒有帳號嗎？點此註冊
        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
                finish();
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idoremail, password;
                idoremail = String.valueOf(textInputEditTextIDorEmail.getText());
                password = String.valueOf(textInputEditTextPassword.getText());
                if (isNext) {
                    if (!idoremail.equals("") && !password.equals("")) {
                        //Start ProgressBar first (Set visibility VISIBLE)
                        progressBar.setVisibility(View.VISIBLE);
                        Handler handler = new Handler();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                //Starting Write and Read data with URL
                                //Creating array for parameters
                                String[] field = new String[2];
                                field[0] = "employee_id";
                                field[1] = "password";
                                //Creating array for data
                                String[] data = new String[2];
                                data[0] = idoremail;
                                data[1] = password;
                                PutData putData = new PutData("http://192.168.1.109/Hospital/idlogin.php", "POST", field, data); //網址要改成自己的php檔位置及自己的ip
                                if (putData.startPut()) {
                                    if (putData.onComplete()) {
                                        progressBar.setVisibility(View.GONE);
                                        String result = putData.getResult();
                                        if (result.equals("Login Success")) {
                                            Toast.makeText(getApplicationContext(), "登入成功", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "會員編號或密碼錯誤", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "All fields require", Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (!idoremail.equals("") && !password.equals("")) {
                        //Start ProgressBar first (Set visibility VISIBLE)
                        progressBar.setVisibility(View.VISIBLE);
                        Handler handler = new Handler();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                //Starting Write and Read data with URL
                                //Creating array for parameters
                                String[] field = new String[2];
                                field[0] = "email";
                                field[1] = "password";
                                //Creating array for data
                                String[] data = new String[2];
                                data[0] = idoremail;
                                data[1] = password;
                                PutData putData = new PutData("http://192.168.1.109/Hospital/login.php", "POST", field, data); //網址要改成自己的php檔位置及自己的ip
                                if (putData.startPut()) {
                                    if (putData.onComplete()) {
                                        progressBar.setVisibility(View.GONE);
                                        String result = putData.getResult();
                                        if (result.equals("Login Success")) {
                                            Toast.makeText(getApplicationContext(), "登入成功", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "email或密碼錯誤", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "All fields require", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    //忽略https的證書校驗
    public static void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        } catch (Exception ignored) {
        }
    }

    //取得imei
    public void getImei(){
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(Login.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Login.this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_CODE);
            return;
        }
        IMEINumber = telephonyManager.getDeviceId();
        imei.setText(IMEINumber);
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