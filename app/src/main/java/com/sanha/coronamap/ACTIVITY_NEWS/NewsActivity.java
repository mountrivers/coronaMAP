package com.sanha.coronamap.ACTIVITY_NEWS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sanha.coronamap.ADAPTER.ListviewAdapter;
import com.sanha.coronamap.CLASS.News;
import com.sanha.coronamap.MODULES.IDManger;
import com.sanha.coronamap.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class NewsActivity extends AppCompatActivity {
    /*
     이 파일은 현재 사용 되지 않는 파일입니다.

     변천사 )
     직접 뉴스를 골라 사용자들에게 뿌려주는 방식
     (Firebase 사용, 관리자 프로그램에서 링크, 날짜, 제목 전송 -> 사용자들이 뉴스 내용을 받는 방식.)

     => 다음 뉴스 "코로나19" 이슈 카테고리 에서 크롤링 해오는 방식

     => 현재는 NewsRoomActivity 로 옮겨, 두개의 프래그먼트로 하나는 지역별, 하나는 기존의 뉴스 카테고리를 보여주는
     기능으로 변경. 

     */
    private ListView news_viewr;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    public ListviewAdapter adapter;
    Elements contents;
    Document doc = null;
    String news;
    String newsUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_news);
        showNews( );


    }
    private void crawling(){
        new AsyncTask() {//AsyncTask객체 생성
            @Override
            protected Object doInBackground(Object[] params) {
                try {
                    doc = Jsoup.connect("https://media.daum.net/issue/5008621").get(); //naver페이지를 불러옴
                    contents = doc.select("a.link_txt");//셀렉터로 span태그중 class값이 ah_k인 내용을 가져옴

                } catch (IOException e) {
                    e.printStackTrace();
                }
                int cnt = 0;//숫자를 세기위한 변수
                for(Element element: contents) {
                    cnt++;
                    news = element.text();
                    newsUrl = element.attr("href");
                    adapter.addItem(news,newsUrl,"");
                    if(cnt == 12)//10위까지 파싱하므로
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
    private void showNews() {

        adapter = new ListviewAdapter() ;
        news_viewr = (ListView) findViewById(R.id.news_view);
        news_viewr.setAdapter(adapter);

        news_viewr.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                News item = (News) parent.getItemAtPosition(position) ;

                String lk = item.getNewsLink();

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(lk)); startActivity(intent);

            }
        }) ;
        crawling();
    }
}
