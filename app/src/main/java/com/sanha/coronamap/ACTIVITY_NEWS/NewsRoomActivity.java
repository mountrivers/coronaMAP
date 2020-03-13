package com.sanha.coronamap.ACTIVITY_NEWS;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.sanha.coronamap.MODULES.IDManger;
import com.sanha.coronamap.R;

import java.util.ArrayList;
import java.util.List;

public class NewsRoomActivity extends AppCompatActivity {

    MainNewsFragment mainNewsFragment;
    LocalNewsFragment localNewsFragment;

    Button localNewsButton, mainNewsButton;
    int page = 0;

    // -> test viewpager
    private ViewPager mViewPager;
    NewsPageAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setView();
    }

    private void setView(){
        setContentView(R.layout.activity_newsroom);
        mViewPager = (ViewPager) findViewById(R.id.containers);
        newsViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }
    public void newsViewPager(ViewPager viewPager) {
        adapter = new NewsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new LocalNewsFragment(), "지역별 최신뉴스");
        adapter.addFragment(new MainNewsFragment(), "코로나 화재 이슈");

        viewPager.setAdapter(adapter);
    }
    public class NewsPageAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public void addFragment(Fragment fragment, String title){
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
        public NewsPageAdapter(FragmentManager fm){
            super(fm);
        }
        @Override
        public Fragment getItem(int position) { return mFragmentList.get(position); }

        @Override
        public int getCount() {
            return mFragmentList.size() ;
        }

    }
}



 /* 스와이프 가능한 뷰 페이저 사용하기 위해 바꿈. 기존엔 그냥 프래그먼트
    private void setView(){
        setContentView(R.layout.activity_newsroom);
        IDManger.SetBannerAd(this,findViewById(R.id.newsroom_adview));
        localNewsFragment = (LocalNewsFragment) getSupportFragmentManager().findFragmentById(R.id.localNewsFragment);
        mainNewsFragment = new MainNewsFragment();
    }
    private void setButton(){
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
    }*/

