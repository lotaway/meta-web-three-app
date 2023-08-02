package com.metawebthree.keykeeper.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED"))
            return;
        //获取发送短信意图对象携带的数据
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            //可使用动态调试，查看短信数据结构
            Object[] pdus = (Object[]) bundle.get("pdus");
            //发送方的一条短信可能被分割、分多次发送
            SmsMessage[] msgs = new SmsMessage[pdus.length];
            int msgIndex = 0;
            for (Object pdu : pdus) {
                //获取分段的短信
                msgs[msgIndex++] = SmsMessage.createFromPdu((byte[]) pdu, bundle.getString("format"));
            }
            //构建短信相关信息字符串
            StringBuilder strb = new StringBuilder();
            for (SmsMessage msg : msgs) {
                strb.append("\n发短信人电话：\n")
                        .append(msg.getDisplayOriginatingAddress())
                        .append("\n短信内容：\n")
                        .append(msg.getMessageBody());
                //接收时间
                Date date = new Date(msg.getTimestampMillis());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                strb.append("\n短信接收时间：\n").append(sdf.format(date));
            }
            //在context指示的上下文（就是模块的MainActivity）里打Toast消息
            Toast.makeText(context, strb, Toast.LENGTH_LONG).show();
        }
    }
}


