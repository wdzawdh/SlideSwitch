package com.cw.slideswitch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cw.slideswitch.view.SwitchView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SwitchView sv_but = (SwitchView) findViewById(R.id.sv_but);

        if (sv_but != null) {
            sv_but.setOnSlideButtonChangeListener(new SwitchView.OnSlideButtonChangeListener() {
                @Override
                public void onButtonChange(SwitchView view, boolean isOpen) {
                    if(isOpen){
                        view.setBackground(R.drawable.switch_background);
                    }else{
                        view.setBackground(R.drawable.switch_background_2);
                    }
                }
            });
        }

    }
}
