package com.jstech.acnc.Hide;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jstech.acnc.R;
import com.melnykov.fab.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class HideActivity extends AppCompatActivity {

    public final int MY_PERMISSIONS_REQUEST_PHONE_STATE = 1;
    public final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 2;
    public final int MY_PERMISSIONS_OUTGOING_CALLS = 3;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<MainData> mMainDataSet;
    private FloatingActionButton fab;
    private Context mContext;
    private ArrayList<AddData> mAddDataSet;

    public TextView mtv_NoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hide);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_hide);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mMainDataSet = new ArrayList<>();
        mAdapter = new RecyclerViewAdapter(mMainDataSet);
        mRecyclerView.setAdapter(mAdapter);
        mContext = this.getApplicationContext();

        mtv_NoList = (TextView)findViewById(R.id.tv_no_contents);
        mAddDataSet = new ArrayList<AddData>();

        //jinsub.kim /// 글쓰기 버튼은 Floating 버튼을 통해 작성
        fab = (FloatingActionButton) findViewById(R.id.fab_new_write);
        fab.attachToRecyclerView(mRecyclerView);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, HideAddActivity.class);
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        GetUsersInfoFromPhoneBook();
                    }
                });

                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                intent.putExtra("PhoneBookList", mAddDataSet);

                startActivity(intent);
                finish();
            }
        });

        PermissionCheckAndGetUsersInfo();   //권한 관리 및 정보 가져오기.

        if(mAdapter.getItemCount() <= 0)
        {
            mtv_NoList.setText("등록된 정보가 없습니다.");
        }
        else
        {
            mtv_NoList.setText("");
        }
    }

    //  권한이 없으면 권한요청을, 권한이 있으면 연락처 db로부터 정보를 가져온다.
    public void PermissionCheckAndGetUsersInfo()
    {
        int Permission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        if(Permission == PackageManager.PERMISSION_DENIED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE))
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, MY_PERMISSIONS_REQUEST_PHONE_STATE);
            }
            else
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, MY_PERMISSIONS_REQUEST_PHONE_STATE);
            }
        }
        else
        {
            Permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
            if(Permission == PackageManager.PERMISSION_DENIED)
            {
                if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE))
                {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                }
                else
                {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                }
            }
            else
            {
                Permission = ContextCompat.checkSelfPermission(this, Manifest.permission.PROCESS_OUTGOING_CALLS);
                if(Permission == PackageManager.PERMISSION_DENIED)
                {
                    if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.PROCESS_OUTGOING_CALLS))
                    {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.PROCESS_OUTGOING_CALLS}, MY_PERMISSIONS_OUTGOING_CALLS);
                    }
                    else
                    {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.PROCESS_OUTGOING_CALLS}, MY_PERMISSIONS_OUTGOING_CALLS);
                    }
                }
                else
                {
                    ShowHideList();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case MY_PERMISSIONS_REQUEST_PHONE_STATE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(this, HideActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(mContext, "권한사용을 동의해주셔야 이용할 수 있습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }

                return;
            }
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(this, HideActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(mContext, "권한사용을 동의해주셔야 이용할 수 있습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }

                return;
            }
            case MY_PERMISSIONS_OUTGOING_CALLS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(this, HideActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(mContext, "권한사용을 동의해주셔야 이용할 수 있습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }
        }
    }

    /*
        *  숨김 리스트 DB로부터 가져와 출력
        * */
    public void ShowHideList()
    {
        try{
            SQLiteDatabase ReadDB = this.openOrCreateDatabase(getString(R.string.database_name), MODE_PRIVATE, null);
            Cursor cursor = ReadDB.rawQuery("SELECT * FROM " + getString(R.string.table_name), null);

            if(cursor != null)
            {
                if(cursor.moveToFirst())
                {
                    do{

                        String strName = cursor.getString(cursor.getColumnIndex("name"));
                        String strPhone = cursor.getString(cursor.getColumnIndex("phone"));
                        String strEmail = cursor.getString(cursor.getColumnIndex("email"));
                        String strStartDate = cursor.getString(cursor.getColumnIndex("startdate"));
                        String strExpireDate = cursor.getString(cursor.getColumnIndex("expiredate"));

                        Date StartDate = null, ExpireDate = null;
                        SimpleDateFormat transformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        try {
                            StartDate = transformat.parse(strStartDate);
                            ExpireDate = transformat.parse(strExpireDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        mMainDataSet.add(new MainData(strName, strPhone, strEmail, StartDate, ExpireDate));

                    }while(cursor.moveToNext());
                }
            }

            ReadDB.close();

        }catch(SQLiteException se)
        {
            se.printStackTrace();
        }
    }


    //  JinSub.kim  // 연락처 DB로부터 저장된 연락처들을 불러온다.
    public void GetUsersInfoFromPhoneBook()
    {
        String[] arrProjection = {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME
        };

        String[] arrPhoneProjection = {
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };

        String[] arrEmailProjection = {
                ContactsContract.CommonDataKinds.Email.DATA
        };

        Cursor cursor = mContext.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI, arrProjection,
                ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1",
                null, null
        );

        while(cursor.moveToNext())
        {
            String strContactId = cursor.getString(0);
            String strContactName = cursor.getString(1);

            Cursor cPhone = mContext.getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    arrPhoneProjection,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + strContactId,
                    null, null
            );

            String strPhone = null;
            while(cPhone.moveToNext())
            {
                strPhone = cPhone.getString(0);
            }
            cPhone.close();

            Cursor cEmail = mContext.getContentResolver().query(
                    ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                    arrEmailProjection,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + strContactId,
                    null, null
            );

            String strMailAddr = null;
            while(cEmail.moveToNext())
            {
                strMailAddr = cEmail.getString(0);
            }
            cEmail.close();

            mAddDataSet.add(new AddData(strContactId, strContactName, strPhone, strMailAddr));

        }   //end of while

        cursor.close();

        //jinsub.kim// 이름순으로 리스트 정렬
        ListSort listsort = new ListSort();
        Collections.sort(mAddDataSet, listsort);
    }

    //리스트 정렬 위한 인터페이스
    class ListSort implements Comparator<AddData>
    {
        @Override
        public int compare(AddData o1, AddData o2) {
            return o1.strName.compareToIgnoreCase(o2.strName);
        }
    }

}
