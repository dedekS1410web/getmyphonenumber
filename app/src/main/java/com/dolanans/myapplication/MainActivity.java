package com.dolanans.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Flow;

import static android.Manifest.permission.READ_PHONE_NUMBERS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;

public class MainActivity extends AppCompatActivity {

    private TextView sdn, sn;
    private static final int PERMISSION_REQUEST_CODE = 100;
    TelephonyManager telephonyManager;

    private String myimei, serial, mynumber;

    String TAG = "PhoneActivityTAG";

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.provider.Settings.Secure.getString(getApplicationContext().getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        sdn = findViewById(R.id.sdn);
        sn = findViewById(R.id.sn);


//        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        myimei = getSimNumber(MainActivity.this);
        serial = getSimSerial(MainActivity.this);


        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        int permissionCheck2 = ContextCompat.checkSelfPermission(this, READ_PHONE_NUMBERS);
        int permissionCheck3 = ContextCompat.checkSelfPermission(this, READ_PHONE_STATE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_SMS},
                    PERMISSION_REQUEST_CODE); // define this constant yourself
        }
        if(permissionCheck2 != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{READ_PHONE_NUMBERS},
                    PERMISSION_REQUEST_CODE); // define this constant yourself
        }
        if(permissionCheck3 != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{READ_PHONE_STATE},
                    PERMISSION_REQUEST_CODE); // define this constant yourself
        }


//        if(myimei == "not_found"){
//            sdn.setText("SIM NUMBER :" + myimei);
//        }else{
//            sdn.setText("SIM NUMBER :" + myimei);
//        }


        TelephonyManager tMgr = (TelephonyManager)
                getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();
        String test3;
        String test2 = "";

        if(mPhoneNumber.length() > 10) {
            test3 = "0"+mPhoneNumber.substring(3, 13);
        }
        else {
            test3 = mPhoneNumber;
        }
        for(int i = 0; i<test3.length(); i++) {
            if (String.valueOf(test3.charAt(i)).equals("-") || String.valueOf(test3.charAt(i)).equals(" ")) {
            }
            else {
                test2 += String.valueOf(test3.charAt(i));
            }
        }


        Log.e("NUMBER TES 3","=" + test3);
        Log.e("NUMBER TES 2","=" + test2);


//        sdn.setText("SIM NUMBER : " + tMgr.getLine1Number());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            List<SubscriptionInfo> subscription = SubscriptionManager.from(getApplicationContext()).getActiveSubscriptionInfoList();

            for (int i = 0; i < subscription.size(); i++) {
                SubscriptionInfo info = subscription.get(0);
                Log.e(TAG, "number " + info.getNumber());

                mynumber = info.getNumber();
           }
        }


        sdn.setText("SIM NUMBER : " + mynumber);
        sn.setText("SIM SERIAL NUMBER : " + serial);
    }


    @Override
    protected void onResume() {
        super.onResume();
//        sdn.setText("SIM NUMBER :" + mynumber);
//        sn.setText("SIM SERIAL NUMBER : " + serial);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        sdn.setText("SIM NUMBER :" + mynumber);
//        sn.setText("SIM SERIAL NUMBER : " + serial);
    }

    private static String getSimSerial(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED) {
                return "";
            }
            String simserial;
            if (android.os.Build.VERSION.SDK_INT >= 26 && android.os.Build.VERSION.SDK_INT < 29) {
//                simserial=telephonyManager.getSimSerialNumber();
                simserial = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            }
            else if(android.os.Build.VERSION.SDK_INT >= 29){
                simserial = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
//                simserial = Build.getSerial();

            }
            else
            {
//                simserial=telephonyManager.getSimSerialNumber();
                simserial = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            }

            Log.e("imei", "=" + simserial);
            if (simserial != null && !simserial.isEmpty()) {
                return simserial;
            } else {
                return android.os.Build.SERIAL;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "not_found";
    }

    private static String getSimNumber(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED) {
                return "";
            }
            String simserial;
            if (android.os.Build.VERSION.SDK_INT >= 26 && android.os.Build.VERSION.SDK_INT < 29) {
                simserial= telephonyManager.getLine1Number();
            }
            else if (android.os.Build.VERSION.SDK_INT >= 29) {
                simserial= telephonyManager.getLine1Number();
            }
            else
            {
                simserial= telephonyManager.getLine1Number();
            }

            Log.e("imei", "=" + simserial);
            if (simserial != null && !simserial.isEmpty()) {
                return simserial;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "not_found";
    }
}