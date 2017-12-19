package com.jstech.acnc.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;

import static android.content.Context.MODE_PRIVATE;

public class MyReceiver extends BroadcastReceiver {

    public static int pState = TelephonyManager.CALL_STATE_IDLE;
    public TelephonyManager tm;
    public Context mContext;

    public MyReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        mContext = context;
        tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        tm.listen(new PhoneStateListener(){
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                if(state != pState)
                {
                    if(state == TelephonyManager.CALL_STATE_IDLE)
                    {
                        Log.e("ACNC", "idle: " + incomingNumber);

                        //  차단 여부 검사 후, 차단된 번호이면 전화 끊기.
                        if(CheckValidPhoneNumber(incomingNumber) == false)
                        {
                            try{
                                Class<?> c = Class.forName(tm.getClass().getName());
                                Method m = c.getDeclaredMethod("getITelephony");
                                m.setAccessible(true);
                                ITelephony telephonyService = (ITelephony)m.invoke(tm);
                                telephonyService.endCall();

                                Toast.makeText(mContext, "ACNC : 차단된 전화번호입니다.", Toast.LENGTH_SHORT).show();

                            }catch(Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                    else if(state == TelephonyManager.CALL_STATE_RINGING)
                    {
                      Log.e("ACNC", "ringing: " + incomingNumber);

                        //  차단 여부 검사 후, 차단된 번호이면 전화 끊기.
                        if(CheckValidPhoneNumber(incomingNumber) == false)
                        {
                            try{
                                Class<?> c = Class.forName(tm.getClass().getName());
                                Method m = c.getDeclaredMethod("getITelephony");
                                m.setAccessible(true);
                                ITelephony telephonyService = (ITelephony)m.invoke(tm);
                                telephonyService.endCall();

                                Toast.makeText(mContext, "ACNC : 차단된 전화번호입니다.", Toast.LENGTH_SHORT).show();

                            }catch(Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                    else if(state == TelephonyManager.CALL_STATE_OFFHOOK)
                    {
                        Log.e("ACNC", "offhook: " + incomingNumber);

                        //  차단 여부 검사 후, 차단된 번호이면 전화 끊기.
                        if(CheckValidPhoneNumber(incomingNumber) == false)
                        {
                            try{
                                Class<?> c = Class.forName(tm.getClass().getName());
                                Method m = c.getDeclaredMethod("getITelephony");
                                m.setAccessible(true);
                                ITelephony telephonyService = (ITelephony)m.invoke(tm);
                                telephonyService.endCall();

                                Toast.makeText(mContext, "ACNC : 차단된 전화번호입니다.", Toast.LENGTH_SHORT).show();

                            }catch(Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }

                    pState = state;
                }
            }
        }, PhoneStateListener.LISTEN_CALL_STATE);

        if(intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)){
            Log.e("ACNC", intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER));
        }
    }

    public boolean CheckValidPhoneNumber(String phone)
    {

        boolean bRet = true;
        String DestPhone = MakeNumberFormat(phone);
        try{
            SQLiteDatabase ReadDB = mContext.openOrCreateDatabase("acnc_db", MODE_PRIVATE, null);
            Cursor cursor = ReadDB.rawQuery("SELECT * FROM acnc_table", null);

            if(cursor != null)
            {
                if(cursor.moveToFirst())
                {
                    do{
                        String strPhone = cursor.getString(cursor.getColumnIndex("phone"));
                        strPhone = MakeNumberFormat(strPhone);
                        if(DestPhone.equals(strPhone))
                        {
                            bRet = false;
                            break;
                        }

                    }while(cursor.moveToNext());
                }
            }

            ReadDB.close();

        }catch(SQLiteException se)
        {
            se.printStackTrace();
        }

        return bRet;
    }

    public String MakeNumberFormat(String PhoneNumber)
    {
        StringBuffer strBuf = new StringBuffer();
        for(int i = 0; i < PhoneNumber.length(); i++)
        {
            if(Character.isDigit(PhoneNumber.charAt(i)))
            {
                strBuf.append(PhoneNumber.charAt(i));
            }
        }
        return strBuf.toString();
    }

}
