# coronaMAP

# 사용방법

## 1. IDManger 추가하기

MODULES 폴더 안에 IDManger.java 추가

--------------------
```
package com.sanha.coronamap.MODULES;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.naver.maps.map.NaverMapSdk;
import com.sanha.coronamap.R;

public class IDManger {


    public static void SetBannerAd(Context context, FrameLayout av) {
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdView mAdView = new AdView(context);
        mAdView.setAdSize(new AdSize(300, 50));
        mAdView.setAdUnitId("ca-app-pub-3940256099942544/6300978111"); // TEST AD HERE ------------------
        FrameLayout frameLayout = av;
        frameLayout.addView(mAdView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);

    }

    public static InterstitialAd SetPopUpAd(Context context) {
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        InterstitialAd popupAd = new InterstitialAd(context);
        popupAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712"); //TEST AD HERE ------------------
        popupAd.loadAd(new AdRequest.Builder().build());
        return popupAd;

    }

    public static void SetNaverSdkClientId(Context context) {
        NaverMapSdk.getInstance(context).setClient(
                new NaverMapSdk.NaverCloudPlatformClient("YOUR_NAVER_ID")); // NAVER CLIENT ID HERE
    }
}

```
광고 ID, 네이버 ID 설정. 
필요 없을시 관련 함수들 전부 삭제


## 2. 매니페스트 수정
AndroidManifest.xml <- 
```
<meta-data
 android:name="com.google.android.gms.ads.APPLICATION_ID"
android:value="ca-app-pub-2294544540507821~1853517193" />
   ```         
            삭제 혹은 value 부분을 자신의 앱ID로 변경

## 3. FIREBASE JSON 파일 삽입
FIREBASE 홈페이지 참고

https://firebase.google.com/docs/android/setup?authuser=0


# About App
## 개발자
이산하(tksgk77@gmail.com) / 박희상 (show7159@gmail.com)

## 개발기간 
2020.02.08 ~ 마켓 업데이트중

## 플레이 스토어 링크
https://play.google.com/store/apps/details?id=com.sanha.coronamap

## 개발 동기/ 목적 
코로나19의 발병으로 인해 많은 사람들이 불안해 하는 가운데, 빠르게 정보를 전달 하기 위해. 

## 레거시 액티비티
![Screenshot_20200207-165135](https://user-images.githubusercontent.com/36880919/76296499-9090aa00-62f9-11ea-945e-bd3f8ea80f67.png)
 - 이유) 확진자 수가 기하 급수적으로 늘어나며, 일일이 확진자 이동 경로를 찍는데 어려움이 생김. 
 
