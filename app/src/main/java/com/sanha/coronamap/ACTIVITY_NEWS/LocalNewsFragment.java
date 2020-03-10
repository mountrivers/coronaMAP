package com.sanha.coronamap.ACTIVITY_NEWS;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    Elements contents; Document doc = null;
    String news,  newsUrl;
    EditText localNameEditText;
    Button setLocaleButton;
    public SharedPreferences spPref; public SharedPreferences.Editor spEditor;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.localnews, container, false);
        setSharePref();
        setView(rootView);
        setButton();
        setNewsAdapter();
        crawling(spPref.getString("local",""));
        return rootView;
    }

    private void setSharePref(){
        Context context = getActivity();
        spPref = context.getSharedPreferences("spPref",MODE_PRIVATE);
        spEditor = spPref.edit();
    }
    private void setView(ViewGroup rootView){
        localNameEditText = (EditText) rootView.findViewById(R.id.edittext_place);
        localNameEditText.setText( spPref.getString("local",""));

        setLocaleButton = (Button) rootView.findViewById(R.id.renew_news);
        news_viewr = (ListView) rootView.findViewById(R.id.news_view);
    }
    private void setButton(){
        setLocaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.deleteAll();
                spEditor.putString("local", localNameEditText.getText().toString());
                spEditor.commit();
                crawling(localNameEditText.getText().toString());
            }
        });
    }
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
    private void crawling(final String localName){
        new AsyncTask() {//AsyncTask객체 생성
            @Override
            protected Object doInBackground(Object[] params) {
                try {
                    String txtbef = "https://m.search.daum.net/search?w=news&q=";
                    String middle = localName;
                    String textaft = "%20코로나&sd=&localNameEditText=&period=&DA=23A" ;
                    // local name 을 에딧텍스트에서 받아와 local name + 코로나 19 검색의 결과를 크롤링으로 받아옴.
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
}