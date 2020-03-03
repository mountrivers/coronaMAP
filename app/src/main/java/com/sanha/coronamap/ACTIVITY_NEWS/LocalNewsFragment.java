package com.sanha.coronamap.ACTIVITY_NEWS;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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

import static android.content.Context.MODE_PRIVATE;

public class LocalNewsFragment extends Fragment {

    private ListView news_viewr;
    public ListviewAdapter adapter;
    Elements contents;
    Document doc = null;
    String news;
    String newsUrl;
    EditText ed;
    Button bt;
    public Resources r;

    public SharedPreferences spPref; public SharedPreferences.Editor spEditor;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.localnews, container, false);

        Context context = getActivity();
        spPref = context.getSharedPreferences("spPref",MODE_PRIVATE);
        spEditor = spPref.edit();
        r = getResources();




        ed = (EditText) rootView.findViewById(R.id.edittext_place);
        bt = (Button) rootView.findViewById(R.id.renew_news);
        news_viewr = (ListView) rootView.findViewById(R.id.news_view);
        ed.setText( spPref.getString("local",""));


        showNews();


        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.deleteAll();
                spEditor.putString("local",ed.getText().toString());
                spEditor.commit();
                crawling(ed.getText().toString());
            }
        });

        return rootView;
    }


    private void showNews() {

        // Adapter 생성
        adapter = new ListviewAdapter() ;

        // 리스트뷰 참조 및 Adapter달기

        news_viewr.setAdapter(adapter);
        news_viewr.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                News item = (News) parent.getItemAtPosition(position) ;

                String nc = item.getNewsContent() ;
                String lk = item.getNewsLink();
                String nd = item.getNewsDate();

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(lk)); startActivity(intent);

            }
        }) ;
        crawling(spPref.getString("local",""));

    }

    private void crawling(final String m){
        new AsyncTask() {//AsyncTask객체 생성
            @Override
            protected Object doInBackground(Object[] params) {
                try {
                    String txtbef = "https://m.search.daum.net/search?w=news&q=";
                    String middle = m;
                    String textaft = "%20코로나&sd=&ed=&period=&DA=23A" ;

                    doc = Jsoup.connect(txtbef + middle + textaft)
                            .userAgent("Mozilla")
                            .get(); //naver페이지를 불러옴
                    contents = doc.select("a.info_item");//셀렉터로 span태그중 class값이 ah_k인 내용을 가져옴


                } catch (IOException e) {
                    e.printStackTrace();
                }
                int cnt = 0;//숫자를 세기위한 변수
                for(Element element: contents) {
                    cnt++;
                    news = element.select("div.compo-exacttit").text();
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
}