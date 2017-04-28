package com.cw.slideswitch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cw.slideswitch.view.SlideButton;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SlideButton sv_but = (SlideButton) findViewById(R.id.sv_but);

        sv_but.setOnSlideButtonChangeListener(new SlideButton.OnSlideButtonChangeListener() {
            @Override
            public void onButtonChange(SlideButton view, boolean isOpen) {

            }
        });

    }
}
