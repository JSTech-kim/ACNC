package com.jstech.acnc.Hide;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.jstech.acnc.R;

public class SetHideInfo extends AppCompatActivity {

    public TextView mtv_label_name;
    public TextView mtv_label_phone;
    public TextView mtv_label_email;
    public TextView mtv_label_start;
    public TextView mtv_label_expire;

    private Context mContext;
    private int m_Id;
    private TextView mtv_Name;
    private TextView mtv_Phone;
    private TextView mtv_Email;
    private DatePicker mdp_Start;
    private TimePicker mtp_Start;
    private DatePicker mdp_Expire;
    private TimePicker mtp_Expire;
    private SQLiteDatabase m_Database_acnc;

    private String m_DatabaseName;
    private String m_TableName;

    private String m_strStartDate;
    private String m_strExpireDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_hide_info);

        //jinsub.kim//  Init
        mContext = this.getApplicationContext();
        mtv_Name = (TextView)findViewById(R.id.setinfo_label_name);
        mtv_Phone = (TextView)findViewById(R.id.setinfo_label_phone);
        mtv_Email = (TextView)findViewById(R.id.setinfo_label_email);
        mdp_Start = (DatePicker)findViewById(R.id.datepicker_start);
        mtp_Start = (TimePicker)findViewById(R.id.timepicker_start);
        mdp_Expire = (DatePicker)findViewById(R.id.datepicker_expire);
        mtp_Expire = (TimePicker)findViewById(R.id.timepicker_expire);

        mtv_label_name = (TextView)findViewById(R.id.label_name);
        mtv_label_name.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/3Dventure.ttf"));
        mtv_label_name.setTextColor(Color.WHITE);
        mtv_label_phone = (TextView)findViewById(R.id.label_phone);
        mtv_label_phone.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/3Dventure.ttf"));
        mtv_label_phone.setTextColor(Color.WHITE);
        mtv_label_email = (TextView)findViewById(R.id.label_email);
        mtv_label_email.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/3Dventure.ttf"));
        mtv_label_email.setTextColor(Color.WHITE);
        mtv_label_start = (TextView)findViewById(R.id.label_start_date);
        mtv_label_start.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/3Dventure.ttf"));
        mtv_label_start.setTextColor(Color.WHITE);
        mtv_label_expire = (TextView)findViewById(R.id.label_expire_date);
        mtv_label_expire.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/3Dventure.ttf"));
        mtv_label_expire.setTextColor(Color.WHITE);

        Intent intent = getIntent();
        m_Id = Integer.parseInt(intent.getStringExtra("ID"));
        mtv_Name.setText(intent.getStringExtra("NAME"));
        mtv_Phone.setText(intent.getStringExtra("PHONE"));
        mtv_Email.setText(intent.getStringExtra("EMAIL"));

        m_DatabaseName = getString(R.string.database_name);
        m_TableName = getString(R.string.table_name);
    }

    //  확인 버튼 이벤트
    //  정보를 데이터베이스에 저장하고, 전화번호부에서 삭제한다.
    public void Click_Ok(View v)
    {
        //  DB 저장.
        if(ActionForDatabase() == false)
        {
            Toast.makeText(v.getContext(), "Database에 입력을 실패하였습니다.", Toast.LENGTH_SHORT).show();
            finish();
            return ;
        }

        //  전화번호부에서 삭제.
        if(ActionForContact() == false)
        {
            Toast.makeText(v.getContext(), "전화번호부에서 삭제하지 못했습니다.", Toast.LENGTH_SHORT).show();
            finish();
            return ;
        }

        Intent intent = new Intent(v.getContext(), HideActivity.class);
        startActivity(intent);
        finish();
    }

    // DB에 데이터 삽입.
    public boolean ActionForDatabase()
    {
        GetDateFromPicker();
        try{
            m_Database_acnc = this.openOrCreateDatabase(m_DatabaseName, MODE_PRIVATE, null);
//            m_Database_acnc.execSQL("DROP TABLE " + m_TableName);
            m_Database_acnc.execSQL("CREATE TABLE IF NOT EXISTS " + m_TableName
                    + " (name VARCHAR(512), phone VARCHAR(128), email VARCHAR(256), startdate VARCHAR(128), expiredate VARCHAR(128) );");

            m_Database_acnc.execSQL("INSERT INTO " + m_TableName
                + " (name, phone, email, startdate, expiredate) Values"
                + " ('"+ mtv_Name.getText() + "', '" + mtv_Phone.getText() + "', '" + mtv_Email.getText()
                + "', '" + m_strStartDate + "', '" + m_strExpireDate + "');");

            m_Database_acnc.close();

        }catch(SQLiteException se)
        {
            Toast.makeText(getApplicationContext(), se.getMessage(), Toast.LENGTH_SHORT).show();
            se.printStackTrace();
            return false;
        }

        return true;
    }

    // jinsub.kim// Picker로부터 날짜, 시간 정보를 가져온다.
    public void GetDateFromPicker()
    {
        String strStartYear = Integer.toString(mdp_Start.getYear());
        String strStartMonth = Integer.toString(mdp_Start.getMonth()+1);
        String strStartDay = Integer.toString(mdp_Start.getDayOfMonth());
        String strExpireYear = Integer.toString(mdp_Expire.getYear());
        String strExpireMonth = Integer.toString(mdp_Expire.getMonth()+1);
        String strExpireDay = Integer.toString(mdp_Expire.getDayOfMonth());

        String strStartHour = "";
        String strStartMinute = "";
        String strExpireHour = "";
        String strExpireMinute = "";

        if(Build.VERSION.SDK_INT >= 23)
        {
            strStartHour = Integer.toString(mtp_Start.getHour());
            strStartMinute = Integer.toString(mtp_Start.getMinute());
            strExpireHour = Integer.toString(mtp_Expire.getHour());
            strExpireMinute = Integer.toString(mtp_Expire.getMinute());
        }
        else
        {
            strStartHour = Integer.toString(mtp_Start.getCurrentHour());
            strStartMinute = Integer.toString(mtp_Start.getCurrentMinute());
            strExpireHour = Integer.toString(mtp_Expire.getCurrentHour());
            strExpireMinute = Integer.toString(mtp_Expire.getCurrentMinute());
        }

        m_strStartDate = strStartYear + "-" + strStartMonth + "-" + strStartDay + " " +
                strStartHour + ":" + strStartMinute + ":00";

        m_strExpireDate = strExpireYear + "-" + strExpireMonth + "-" + strExpireDay + " " +
                strExpireHour + ":" + strExpireMinute + ":00";

        return;
    }

    //jinsub.kim//  전화번호부에 Contact ID를 통해 삭제하는 쿼리 날림.
    public boolean ActionForContact()
    {
        try{
            mContext.getContentResolver().delete(ContactsContract.RawContacts.CONTENT_URI,
                    ContactsContract.RawContacts.CONTACT_ID + "=" + m_Id, null);
        }catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }
    public void Click_Cancel(View v)
    {
        Intent intent = new Intent(this, HideActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, HideActivity.class);
        startActivity(intent);
        finish();
    }
}
