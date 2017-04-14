package com.example.zxl.testsms;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    SmsObserver smsObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSms();
    }

    public void initSms() {
        smsObserver = new SmsObserver(this, mHandler);
        Uri uri = Uri.parse("content://sms");
        getContentResolver().registerContentObserver(uri, true, smsObserver);
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                Log.e("TAG", "Test2Activity");
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        getContentResolver().unregisterContentObserver(smsObserver);
    }
}
