package com.jstech.acnc.Environment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.jstech.acnc.R;

public class CertificateActivity extends AppCompatActivity {

    public TextView mtv_label_open_source;
    public TextView mtv_label_fonts;
    public TextView mtv_label_sensitive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificate);

        mtv_label_open_source = (TextView)findViewById(R.id.tv_certificate_label_open_source);
        mtv_label_open_source.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/VCR_OSD_MONO_1.001.ttf"));
        mtv_label_fonts = (TextView)findViewById(R.id.tv_certificate_label_fonts);
        mtv_label_fonts.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/VCR_OSD_MONO_1.001.ttf"));
        mtv_label_sensitive = (TextView)findViewById(R.id.tv_certificate_label_personal);
        mtv_label_sensitive.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/VCR_OSD_MONO_1.001.ttf"));

    }
}
