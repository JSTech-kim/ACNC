package com.jstech.acnc.Environment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.jstech.acnc.R;

public class EnvActivity extends AppCompatActivity {

    public TextView mtv_about_appname;
    public TextView mtv_about_certificate;
    public TextView mtv_about_mail;
    public TextView mtv_about_developers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_env);

        mtv_about_appname = (TextView)findViewById(R.id.tv_about_appname);
        mtv_about_appname.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/3Dventure.ttf"));
        mtv_about_certificate = (TextView)findViewById(R.id.tv_about_certificate);
        mtv_about_certificate.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/VCR_OSD_MONO_1.001.ttf"));
        mtv_about_mail = (TextView)findViewById(R.id.tv_about_mail);
        mtv_about_mail.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/VCR_OSD_MONO_1.001.ttf"));
        mtv_about_developers = (TextView)findViewById(R.id.tv_about_developers);
        mtv_about_developers.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/VCR_OSD_MONO_1.001.ttf"));

        MobileAds.initialize(this, "ca-app-pub-2244213128770020~6289637805");

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    public void Click_Certificate(View v)
    {
        Intent intent = new Intent(this, CertificateActivity.class);
        startActivity(intent);
    }

    public void Click_Mail(View v)
    {
        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(this);
        alert_confirm.setMessage("sujinsub@naver.com");
        alert_confirm.setPositiveButton("확인", null);

        AlertDialog alert = alert_confirm.create();
        alert.setIcon(R.drawable.ic_acnc);
        alert.setTitle("문의사항");
        alert.show();
    }

    public void Click_Developers(View v)
    {
        Intent intent = new Intent(this, DevelopersActivity.class);
        startActivity(intent);
    }
}
