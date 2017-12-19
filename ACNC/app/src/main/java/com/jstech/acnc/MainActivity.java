package com.jstech.acnc;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jstech.acnc.Environment.EnvActivity;
import com.jstech.acnc.Hide.HideActivity;

/*
*   //JinSub.kim//17-02-25
*   각 이미지뷰 클릭 시 해당 기능 수행하는 액티비티로 이동.
*   단, MainActivity를 종료하지않고 Life Cycle에 살려두고,
*   기능을 마친 뒤, 해당 액티비티를 제거하는 방식으로 진행.
* */

public class MainActivity extends AppCompatActivity{

    public final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    public final int MY_PERMISSIONS_REQUEST_WRITE_CONTACTS = 2;
    Intent intent;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this.getApplicationContext();

        TextView tv_MainAppName = (TextView)findViewById(R.id.tv_MainAppName);
        tv_MainAppName.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/3Dventure.ttf"));

        TextView tv_SubAppName = (TextView)findViewById(R.id.tv_SubAppName);
        tv_SubAppName.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/VCR_OSD_MONO_1.001.ttf"));

        TextView tv_btn_nocall = (TextView)findViewById(R.id.tv_btn_nocall);
        tv_btn_nocall.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/VCR_OSD_MONO_1.001.ttf"));

        TextView tv_btn_about = (TextView)findViewById(R.id.tv_btn_about);
        tv_btn_about.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/VCR_OSD_MONO_1.001.ttf"));

        ImageView iv_btn_nocall = (ImageView)findViewById(R.id.iv_btn_nocall);
        iv_btn_nocall.bringToFront();

        ImageView iv_btn_about = (ImageView)findViewById(R.id.iv_btn_about);
        iv_btn_about.bringToFront();

        PermissionCheckAndGetUsersInfo();   //권한 관리 및 정보 가져오기.
    }


    //  권한이 없으면 권한요청을, 권한이 있으면 연락처 db로부터 정보를 가져온다.
    public void PermissionCheckAndGetUsersInfo()
    {
        int Permission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        if(Permission == PackageManager.PERMISSION_DENIED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS))
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
            else
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        }
        else
        {
            Permission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS);
            if(Permission == PackageManager.PERMISSION_DENIED)
            {
                if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_CONTACTS))
                {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CONTACTS}, MY_PERMISSIONS_REQUEST_WRITE_CONTACTS);
                }
                else
                {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CONTACTS}, MY_PERMISSIONS_REQUEST_WRITE_CONTACTS);
                }
            }
            else
            {
                return ;
            }
        }
    }

    //  권한 없을 시, 권한 요청 후 결과를 받는 콜백함수. 동의면 권한 적용된 상태로 액티비티 재호출한다.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {/*
                    Intent intent = new Intent(this, HideAddActivity.class);
                    startActivity(intent);
                    finish();*/
                }
                else
                {
                    Toast.makeText(mContext, "권한사용을 동의해주셔야 이용할 수 있습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }

                return ;
            }
            case MY_PERMISSIONS_REQUEST_WRITE_CONTACTS:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {/*
                    Intent intent = new Intent(this, HideAddActivity.class);
                    startActivity(intent);
                    finish();*/
                }
                else
                {
                    Toast.makeText(mContext, "권한사용을 동의해주셔야 이용할 수 있습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }

                return ;
            }
        }
    }

    public void Click_Btn_Hide(View v)
    {
        intent = new Intent(this, HideActivity.class);
        startActivity(intent);
    }

    public void Click_Btn_Environment(View v)
    {
        intent = new Intent(this, EnvActivity.class);
        startActivity(intent);
    }

}
