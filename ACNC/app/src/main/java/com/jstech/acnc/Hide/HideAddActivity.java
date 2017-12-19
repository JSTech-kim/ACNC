package com.jstech.acnc.Hide;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.jstech.acnc.R;

import java.util.ArrayList;

public class HideAddActivity extends AppCompatActivity {

    public final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    public final int MY_PERMISSIONS_REQUEST_WRITE_CONTACTS = 2;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<AddData> mAddDataSet;
    private Context mContext;

    public TextView mtv_subject_name;
    public TextView mtv_subject_phone;
    public TextView mtv_subject_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hide_add);

        //  메인 컨텍스트
        mContext = this.getApplicationContext();

        //  목록 폰트 설정
        mtv_subject_name = (TextView)findViewById(R.id.tv_subject_name);
        mtv_subject_name.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/3Dventure.ttf"));
        mtv_subject_phone = (TextView)findViewById(R.id.tv_subject_phone);
        mtv_subject_phone.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/3Dventure.ttf"));
        mtv_subject_email = (TextView)findViewById(R.id.tv_subject_email);
        mtv_subject_email.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/3Dventure.ttf"));

        //  RecyclerView 초기화
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_add);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        Intent intent = getIntent();
        mAddDataSet = (ArrayList<AddData>)intent.getSerializableExtra("PhoneBookList");
        mAdapter = new RecyclerViewAdapter_Add(mAddDataSet);
        mRecyclerView.setAdapter(mAdapter);

        //  알림메시지 - 권한 있을 때, 정상 동작
        Toast.makeText(this, "숨김 또는 차단할 연락처를 클릭해주세요.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, HideActivity.class);
        startActivity(intent);
        finish();
    }
}


