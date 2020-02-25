package com.sanha.coronamap.ACTIVITY_NEWS;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.sanha.coronamap.MODULES.IDManger;
import com.sanha.coronamap.R;

public class NewsRoomActivity extends AppCompatActivity {

    MainNewsFragment mainNewsFragment;
    LocalNewsFragment localNewsFragment;

    Button localNewsButton, mainNewsButton;
    int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsroom);
        IDManger.SetBannerAd(this,findViewById(R.id.newsroom_adview));
        //fragment 객체
        localNewsFragment = (LocalNewsFragment) getSupportFragmentManager().findFragmentById(R.id.localNewsFragment);
         mainNewsFragment = new MainNewsFragment();
        buttonSet();
    }

    //fragment 교체

    public void buttonSet(){
        mainNewsButton = (Button)findViewById(R.id.newsroom_mainnews_button);
        localNewsButton = (Button) findViewById(R.id.newsroom_localnews_button);

        mainNewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(page == 0) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, mainNewsFragment).commit();
                    page = 1;
                }

            }
        });
        localNewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(page == 1) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, localNewsFragment).commit();
                    page = 0;
                }
            }
        });
    }
}
