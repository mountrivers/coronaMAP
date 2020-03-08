package com.sanha.coronamap;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;
import com.sanha.coronamap.ACTIVITY_CHAT.ChatActivity;
import com.sanha.coronamap.ACTIVITY_HELP.HelpActivity;
import com.sanha.coronamap.ACTIVITY_MAP.MapActivity;
import com.sanha.coronamap.ACTIVITY_MAP.NewMapActivity;
import com.sanha.coronamap.ACTIVITY_NEWS.NewsActivity;
import com.sanha.coronamap.ACTIVITY_NEWS.NewsRoomActivity;
import com.sanha.coronamap.MODULES.IDManger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends FragmentActivity {

    private Button intoMapButton, intoChatRoom, intoHelpRoom, intoNewsRoom;
    private TextView numOfePeople;

    Elements contents;
    Document doc = null;
    String strNumOfPeople;//결과를 저장할 문자열변수

    private AdView mAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* 광고 세팅 */
        IDManger.SetBannerAd(this,findViewById(R.id.main_adview));

        /* 버튼 세팅 */
        setButton();


        showCountList();
    }


    public void onStart() {
        super.onStart();


    }

    private void showCountList() {
        numOfePeople = (TextView) findViewById(R.id.main_num_of_people);

        new AsyncTask() {//AsyncTask객체 생성
            @Override
            protected Object doInBackground(Object[] params) {
                try {
                    doc = Jsoup.connect("http://ncov.mohw.go.kr/index_main.jsp")
                            .userAgent("Mozilla")
                            .get(); //naver페이지를 불러옴
                    contents = doc.select("div.livenum").select("li");//셀렉터로 span태그중 class값이 ah_k인 내용을 가져옴
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

    private void setButton(){
        intoMapButton = (Button) findViewById(R.id.intoMapButton);
        intoChatRoom = (Button)findViewById(R.id.enter_chat_room);
        intoHelpRoom = (Button)findViewById(R.id.help);
        intoNewsRoom = (Button)findViewById(R.id.intonews_button);

        intoMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewMapActivity .class);
                startActivity(intent);
            }
        });

        intoChatRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                startActivity(intent);
            }
        });
        intoHelpRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HelpActivity.class);
                startActivity(intent);
            }
        });
        intoNewsRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewsRoomActivity.class);
                startActivity(intent);
            }
        });
    }
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
}
