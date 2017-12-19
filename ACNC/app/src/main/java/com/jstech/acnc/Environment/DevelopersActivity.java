package com.jstech.acnc.Environment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.jstech.acnc.R;

public class DevelopersActivity extends AppCompatActivity {

    public TextView  mtv_developers;
    public TextView  mtv_developers_mention;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developers);

        mtv_developers = (TextView)findViewById(R.id.tv_developers);
        mtv_developers.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/VCR_OSD_MONO_1.001.ttf"));
        mtv_developers_mention = (TextView)findViewById(R.id.tv_developers_mention);
        mtv_developers_mention.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/VCR_OSD_MONO_1.001.ttf"));
    }
}
