package com.sanha.coronamap.ACTIVITY_NEWS;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sanha.coronamap.ADAPTER.ListviewAdapter;
import com.sanha.coronamap.CLASS.News;
import com.sanha.coronamap.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainNewsFragment extends Fragment {

    private ListView news_viewr;
    public ListviewAdapter adapter;
    Elements contents;
    Document doc = null;
    String news;
    String newsUrl;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //화면 inflate
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.mainnews, container, false);
        news_viewr = (ListView) rootView.findViewById(R.id.news_view);


        showNews( );

        return rootView;
    }
    private void crawling(){
        new AsyncTask() {//AsyncTask객체 생성
            @Override
            protected Object doInBackground(Object[] params) {
                try {
                    doc = Jsoup.connect("https://media.daum.net/issue/5008621")
                            .userAgent("Mozilla")
                            .get(); //naver페이지를 불러옴
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

