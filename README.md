# coronaMAP 

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
 

 
### 마크 정보창(설명창) 설정 + 정보창 띄우기
```
    private void setMap(@NonNull NaverMap naverMap) {

        // infowindow . 정보창
        InfoWindow infoWindow = new InfoWindow();
        infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(this) {
            @NonNull
            @Override
            public CharSequence getText(@NonNull InfoWindow infoWindow) {
                return (CharSequence)infoWindow.getMarker().getTag();
            }
        });
        // 지도 빈공간 클릭시 정보창 꺼지도록
        naverMap.setOnMapClickListener((coord, point) -> {
            infoWindow.close();
        });

        // 마커를 클릭하면:
        Overlay.OnClickListener listener = overlay -> {
            Marker marker = (Marker)overlay;

            if (marker.getInfoWindow() == null) {
                // 현재 마커에 정보 창이 열려있지 않을 경우 엶
                infoWindow.open(marker);
            } else {
                // 이미 현재 마커에 정보 창이 열려있을 경우 닫음
                infoWindow.close();
            }
            return true;
        };

        databaseReference.child("mark").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                makeMark(dataSnapshot,naverMap,n ,listener);
                n++;
            }
        });

    }
```
### 마크 생성  
```
private void makeMark(DataSnapshot dataSnapshot, @NonNull NaverMap naverMap, int k, Overlay.OnClickListener listener) {
        MarkerData tmpMark = dataSnapshot.getValue(MarkerData.class);

        markData[k] = new MarkerData(tmpMark.nNum,tmpMark.detail,tmpMark.mLatitude,tmpMark.mLongitude,tmpMark.marksNum,tmpMark.happenData);

        marker[k] = new Marker();
        marker[k].setPosition(new LatLng(markData[k].mLatitude,markData[k].mLongitude));
        String temp = markData[k].happenData +"\n" + markData[k].nNum + "번 확진자 \n" + markData[k].detail ;
        marker[k].setTag(temp);
        marker[k].setIcon(MarkerIcons.BLACK);
        // color : 0xFF000000 ~ 0xFFFFFFFF. 16777 = 0x00FFFFFF / 1000. 즉, 1000명치의 색상 자동 설정.
        marker[k].setIconTintColor(0xFFFFFFFF  - (Integer.parseInt( markData[k].nNum) *16777));
        marker[k].setMap(naverMap);
        marker[k].setOnClickListener(listener);
    }
```
 firebase로 마크 데이터 받아 사용 
### Corona Admin 마크 정보 전달
```
sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MarkerData md = new MarkerData(a.getText().toString(), b.getText().toString(),
                        c.getText().toString(),  Double.parseDouble(d.getText().toString()),
                        Double.parseDouble(e.getText().toString()), Integer.parseInt( f.getText().toString())
                        );
                databaseReference.child("mark").push().setValue(md);
                a.setText("");
                b.setText("");
                c.setText("");
                d.setText("");
                e.setText("");
                f.setText("");
            }
        });
```



-설명 계속 추가 예정-
