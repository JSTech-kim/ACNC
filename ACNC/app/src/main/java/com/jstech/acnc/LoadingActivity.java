package com.jstech.acnc;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        Handler hd = new Handler();
        hd.postDelayed(new splashhandler(), 2000);
    }

    private class splashhandler implements Runnable{
        public void run()
        {
            startActivity(new Intent(getApplication(), MainActivity.class));
            finish();
        }
    }

}
