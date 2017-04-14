package com.example.zxl.testsms;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description:
 * @Author: zxl
 * @Date: 2016/12/13 17:35
 */

public class SmsObserver extends ContentObserver {
    private Context mContext;
    private Handler mHandler;
    private String code; // 验证码

    public SmsObserver(Context mContext, Handler handler) {
        super(handler);
        this.mContext = mContext;
        this.mHandler = handler;
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        Log.e("TAG", uri.toString());
        // 第一次回调 不是我们想要的 直接返回
        if (uri.toString().equals("content://sms/raw")) {
            return;
        }
        // 第二次回调 查询收件箱里的内容
        Uri inboxUri = Uri.parse("content://sms/inbox");
        // 按时间顺序排序短信数据库
        Cursor c = mContext.getContentResolver().query(inboxUri, null, null, null, "date desc");
        if (c != null) {
            if (c.moveToFirst()) {
                // 获取手机号
                String address = c.getString(c.getColumnIndex("address"));
                // 获取短信内容
                String body = c.getString(c.getColumnIndex("body"));
                Log.e("TAG", "  address==" + address + "\nbody==" + body);
                // 判断手机号是否为目标号码
                if (!address.equals("1382528")) {
                    return;
                }
                // 正则表达式截取短信中的6位验证码
                Pattern pattern = Pattern.compile("(\\d{6})");
                Matcher matcher = pattern.matcher(body);
                // 如果找到通过Handler发送给主线程
                if (matcher.find()) {
                    code = matcher.group(0);
                    mHandler.obtainMessage(1, code).sendToTarget();
                }
            }
        }
        c.close();

    }
}
