# coronaMAP 


## How To Use 1. IDManger 추가하기

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


##  How To Use 2. 매니페스트 수정
AndroidManifest.xml <- 
```
<meta-data
 android:name="com.google.android.gms.ads.APPLICATION_ID"
android:value="ca-app-pub-2294544540507821~1853517193" />
   ```         
            삭제 혹은 value 부분을 자신의 앱ID로 변경

##  How To Use 3. FIREBASE JSON 파일 삽입
FIREBASE 홈페이지 참고

https://firebase.google.com/docs/android/setup?authuser=0


# About App

![종합](https://user-images.githubusercontent.com/36880919/77613773-e064a780-6f6e-11ea-8617-b96fcb2973d5.png)


## 개발자
이산하(tksgk77@gmail.com) / 박희상 (show7159@gmail.com)

## 개발기간 
2020.02.08 ~ 마켓 업데이트중

## 플레이 스토어 링크
https://play.google.com/store/apps/details?id=com.sanha.coronamap


## 현재 상황 
재난 관련 어플에 광고를 달았다는 이유로 어플이 플레이 스토어에서 삭제 된 상태라

정보 확인이 전혀 불가능 한 상태입니다... 

업데이트 또한 불가능..

수입으로 관련 기부라도 해 볼까 했는데 저번주 금요일에 갑작스런 삭제..

아직 03.17 구글에 답변을 기다리고있네요...


## 플레이 스토어 패치 노트

5.00 (2020.03.12) - 약국 마스크 잔여 수량 업데이트
4.04 (2020.03.09) - UI 업데이트 / 확진자수 오류 업데이트 / 뉴스메뉴 이제 스와핑(옆으로밀기) 로 이동 가능 / 타이틀바 제거 
4.03 (2020.03.03) - 뉴스 글자 색 변경, 받아오는 방법 안정화/ 지역별 뉴스 입력시 다른창 딸려오는것 제거 
4.02 (2020.03.02) - 확진자수를 받아오지 못하는 현상 수정. ( 아직 완전히 해결 안되었다면 확인되는대로 바로 고치겠습니다.)
4.01 (2020.02.28) - 카카오톡으로 어플 공유 기능 추가 (도움말 페이지) , 원할한 채팅 속도를 위해 글자 수 30자 제한
4.00 (2020.02.25)- 지역별 뉴스 자동 검색 기능 제공, 지역별 확진자 수 지도 업데이트 ( 더 이상 경로를 제공하지 않습니다. 지역별 확진자 수와 사시는 동, 구 등의 뉴스를 최신순으로 받아서 확인을 권장합니다. ) 
닉네임 변경 기능 추가 
확진자수, 완치자수, 사망자수 자동 업데이트
뉴스 자동 업데이트


## 개발 동기/ 목적 
코로나19의 발병으로 인해 많은 사람들이 불안해 하는 가운데, 빠르게 정보를 전달 하기 위해. 

## 레거시 액티비티
![Screenshot_20200207-165135](https://user-images.githubusercontent.com/36880919/76296499-9090aa00-62f9-11ea-945e-bd3f8ea80f67.png)
 - 이유) 확진자 수가 기하 급수적으로 늘어나며, 일일이 확진자 이동 경로를 찍는데 어려움이 생김. 
  
  *현재 MAP ACTIVITY 를 마스크 위치 알림으로 수정하여 재사용 하고 있음*
 
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
(사용하지 않는 리스너는 지워져있음. 본문은 소스코드 참고)

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

## MainActivity
### 확진자 수 크롤링
```
private void showCountList() {
        numOfePeople = (TextView) findViewById(R.id.main_num_of_people);

        new AsyncTask() {//AsyncTask객체 생성
            @Override
            protected Object doInBackground(Object[] params) {
                try {
                    doc = Jsoup.connect("http://ncov.mohw.go.kr/index_main.jsp")
                            .userAgent("Mozilla")
                            .get(); 
                    contents = doc.select("div.livenum").select("li");//셀렉터로 div태그중 class값이 livenum인 내용을 가져옴
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int cnt = 0;//숫자를 세기위한 변수 
                strNumOfPeople = "";
                if(!contents.isEmpty()) {
                    for (Element element : contents) {
                        cnt++;
                        switch(cnt){
                            case 1:
                                strNumOfPeople += "\n 확진 - ";
                                break;
                            case 2:
                                strNumOfPeople += "\n 완치 - ";
                                break;
                            case 3:
                                strNumOfPeople += "\n 격리 - ";
                                break;
                            case 4:
                                strNumOfPeople += "\n 사망 - ";
                                break;
                        }
                        strNumOfPeople += element.select("span.num").text().replace("(누적)","")
                                + "\n           " + element.select("span.before").text().replace("전일대비 ","");
                    }
                }
                return null;
            }
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if(!strNumOfPeople.matches(""))
                    numOfePeople.setText(strNumOfPeople);
            }
        }.execute();
    }
```

### hashkey
```
private void getHashKey(){
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null)
            Log.e("KeyHash", "KeyHash:null");

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            } catch (NoSuchAlgorithmException e) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
    }
```
 현재 사용은 하지 않음. sha hashkey를 얻기 위해 사용. 

 kakao는, 릴리즈 해시 키 또한 요구함으로, 릴리즈 해시 키를 얻기 위해 사용
 
 (릴리즈로 apk 파일 만든 후, 테스트 디바이스에 넣어 추출)
 
질병관리 본부 홈페이지에서 크롤링
![Screenshot 2020-03-10 at 20 08 46](https://user-images.githubusercontent.com/36880919/76306854-fdf90680-630a-11ea-8e55-4fae7f30cfca.jpg)


## ChatActivity
### send message
```
buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!editTextMessage.getText().toString().matches("")) {
                    Message message = new Message(nickName, editTextMessage.getText().toString(), sender.getUid());
                    databaseReference.child("groupchat").push().setValue(message);
                    editTextMessage.setText("");
                    chat_view.setSelection(adapter.getCount() - 1);
                }

            }
        });
```
firebase realtime database 에 메세지 객체 전달

*제약*
 - 빈 메세지 보내지 않도록, 
 - 최대 길이 30글자 까지로 제한 (xml)
 
### load nickname
```
private void loadNickName(){
        FirebaseDatabase.getInstance().getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User presentUser = snapshot.getValue(User.class);
                    if(presentUser.getUid().equals(sender.getUid().toString())){
                        nickName = presentUser.getName();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
```
 닉네임 변경한 사람을 위해, 구글 이름을 받아오는것이 아닌, 
 
 firebase realtime database 에서, USER 클래스에서 user id hash값을 통해 개인 이름을 받아옴. 
 
### load message
```
 private void showChat() {
      adapter  = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        chat_view.setAdapter(adapter);
        databaseReference.child("groupchat").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message message = dataSnapshot.getValue(Message.class);
                adapter.add(message.Name + " : " + message.Message);
                chat_view.setSelection(adapter.getCount() - 1);
            }
        });
    }
```
채팅을 불러옴. 새로운 메시지가 도착 할 때, 채팅 리스트뷰의 맨 아래로 시야 고침
(사용하지 않는 리스너는 지워져있음. 본문은 소스코드 참고)

TODO : 채팅이 많아짐에 따라, 로드 속도가 너무 느림. 

예상 해결 방안 : groupchat 내부에, 날짜별로 폴더를 다시 만들어, 최근 3일 메시지만 받아오도록 처리. 

TODO : 현재 리스트뷰에 단순 채팅만 구현 되어있음. 앞으로 프로필, 아이디, 메시지 3개 나누어 레이아웃 설정 예정.

TODO : 프로필 보기 기능 + 신고 기능 => 신고된 계정 확인 후 채팅 제한 기능 

## NewsRoomActivity
### tab 페이지뷰어 생성
```
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
        
        public NewsPageAdapter(FragmentManager fm){super(fm);}
        @Override
        public Fragment getItem(int position) { return mFragmentList.get(position); }
        @Override
        public int getCount() {return mFragmentList.size() ;}

    }
```
```
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
```
뷰 페이저가 자동으로 프래그먼트 이동 기능을 만들어줌. 

(스와이프 기능 포함 )

## NewsFragments
### localnews - 지역 선택
```
setLocaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.deleteAll();
                spEditor.putString("local", localNameEditText.getText().toString());
                spEditor.commit();
                crawling(localNameEditText.getText().toString());
            }
        });
```
edit text의 내용을 localname 으로 지정. 

다음에 방문시에도, 번거롭게 한번 더 입력을 하지 않아도 되도록

SharedPreferences로 지역 명 저장

-(oncreate)
```
localNameEditText = (EditText) rootView.findViewById(R.id.edittext_place);
localNameEditText.setText( spPref.getString("local",""));
```

### 크롤링
```
 private void crawling(final String localName){
        new AsyncTask() {//AsyncTask객체 생성
            @Override
            protected Object doInBackground(Object[] params) {
                try {
                    String txtbef = "https://m.search.daum.net/search?w=news&q=";
                    String middle = localName;
                    String textaft = "%20코로나&sd=&localNameEditText=&period=&DA=23A" ;
                    
                    // local name 을 에딧텍스트에서 받아와 local name + 코로나 19 키워드로 
                    // 다음 뉴스 검색의 결과를 크롤링으로 받아옴.
                    
                    doc = Jsoup.connect(txtbef + middle + textaft)
                            .userAgent("Mozilla")
                            .get();
                    contents = doc.select("a.info_item");

                } catch (IOException e) {
                    e.printStackTrace();
                }
                int cnt = 0;//숫자를 세기위한 변수
                for(Element element: contents) {
                    cnt++;
                    news = element.select("div.compo-exacttit").text();
                    newsUrl = element.attr("href");
                    adapter.addItem(news,newsUrl,"");
                    if(cnt == 12) // news 12개만 가져오기
                        break;
                }
                return null;
            }
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                adapter.noti();
            }
        }.execute();
    }
```
크롤링 결과로 news title / news url 을 받아옴. 

local - 다음 뉴스 검색 결과 크롤링
main - 다음 뉴스 '코로나 19' 이슈 카테고리 크롤링

### news adapter
```
private void setNewsAdapter(){
        adapter = new ListviewAdapter() ;
        news_viewr.setAdapter(adapter);
        news_viewr.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                News item = (News) parent.getItemAtPosition(position) ;
                String lk = item.getNewsLink();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(lk)); startActivity(intent);
            }
        }) ;
    }
```
 뉴스 제목 클릭시 해당 링크로 이동하도록 변경. 
 
 TODO : 차후에 새로운 액티비티 만들어서, url 을 intent로 넘겨 웹뷰로 보여 줄 예정.
  어플 이탈율을 줄이기 위해


## ChangeNickName
```
sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nickName.getText().toString() != ""){
                    databaseReference.child("users").child(cureentUser.getUid()).child("name").setValue(nickName.getText().toString());
                    AlertDialog alertDialog = new AlertDialog.Builder(ChangeNickNameActivity.this).create();
                    alertDialog.setTitle("알림");
                    alertDialog.setMessage("닉네임이 변경 되었습니다.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                    finish();
                                }
                            });
                    alertDialog.show();
                }
            }
        });
```
USER - UID - NAME 세팅

## HelpActivity
### kakaotalk share
```
public void shareKakao(){

        FeedTemplate params = FeedTemplate
                .newBuilder(ContentObject.newBuilder("코로나19",
                        "https://user-images.githubusercontent.com/36880919/75602852-53b00080-5b0c-11ea-9749-4d5e1d9cc7e8.png",
                        LinkObject.newBuilder().setWebUrl("https://play.google.com/store/apps/details?id=com.sanha.coronamap")
                                .setMobileWebUrl("https://play.google.com/store/apps/details?id=com.sanha.coronamap").build())
                        .setDescrption("코로나 19 어플 ( 플레이 스토어에서 다운로드 ) ")
                        .setImageHeight(300)
                        .setImageWidth(200)
                        .build())
                .addButton(new ButtonObject("어플다운로드", LinkObject.newBuilder().setWebUrl("https://play.google.com/store/apps/details?id=com.sanha.coronamap").setMobileWebUrl("https://play.google.com/store/apps/details?id=com.sanha.coronamap").build()))

                .build();

        Map<String, String> serverCallbackArgs = new HashMap<String, String>();
        serverCallbackArgs.put("user_id", "${current_user_id}");
        serverCallbackArgs.put("product_id", "${shared_product_id}");

        KakaoLinkService.getInstance().sendDefault(this, params, serverCallbackArgs, new ResponseCallback<KakaoLinkResponse>() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                Logger.e(errorResult.toString());
            }

            @Override
            public void onSuccess(KakaoLinkResponse result) {
              
            }
        });
    }
```
 카카오링크 api 사용
