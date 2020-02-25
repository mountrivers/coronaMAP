package com.sanha.coronamap.ACTIVITY_MAP;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.sanha.coronamap.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class GeoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo);
    }
    /**
     * 네이버 맵 api를 통해서 주소를 가져온다.
     * https://developers.naver.com/docs/map/overview/
     */


    private Point getPointFromNaver(String addr) {
        Point point = new Point();
        point.addr = addr;

        String json = null;
        String clientId = "rachw7sxya";// 애플리케이션 클라이언트 아이디값";
        String clientSecret = "BPuQhPMmCzYLWFb5v1dkXlvUHZHwPFclp7kpsAw4";// 애플리케이션 클라이언트 시크릿값";
        try {
            addr = URLEncoder.encode(addr, "UTF-8");
            String apiURL = "https://openapi.naver.com/v1/map/geocode?query=" + addr; // json
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else { // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            json = response.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (json == null) {
            return point;
        }

        Log.d("TEST2", "json => " + json);

        Gson gson = new Gson();
        NaverData data = new NaverData();
        try {
            data = gson.fromJson(json, NaverData.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (data.result != null) {
            point.x = data.result.items.get(0).point.x;
            point.y = data.result.items.get(0).point.y;
            point.havePoint = true;
        }

        return point;
    }
}
