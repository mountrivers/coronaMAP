package com.sanha.coronamap.ACTIVITY_MAP;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.util.MarkerIcons;
import com.sanha.coronamap.CLASS.MarkerData;
import com.sanha.coronamap.CLASS.MaskMark;
import com.sanha.coronamap.MODULES.IDManger;
import com.sanha.coronamap.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MapActivity extends FragmentActivity
        implements OnMapReadyCallback {
    int n =0 ;
    Marker [] marker = new Marker[3000];
    MaskMark[] maskMarks = new MaskMark[3000];
    int count = 0;
    NaverMap naverMap;
    Overlay.OnClickListener listener;
    private FusedLocationSource locationSource;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);
        IDManger.SetBannerAd(this,findViewById(R.id.map_adview));
        IDManger.SetNaverSdkClientId(this);
        NaverMapOptions options = new NaverMapOptions()
                .camera(new CameraPosition(new LatLng(37.478717, 126.668853), 8))
                .mapType(NaverMap.MapType.Basic)
                .compassEnabled(true);

        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment)fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance(options);
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
        locationSource =
                new FusedLocationSource(this, 1000);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,  @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions, grantResults)) {
            return;
        }
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);
    }


    @UiThread
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        this.naverMap.setLocationSource(locationSource);
        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setCompassEnabled(true);
        uiSettings.setLocationButtonEnabled(true);
        setMap();
    }


    private void setMap() {

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
        listener = overlay -> {
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
        try {
            doit();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void makeMark(Double lat, Double lng, String storeName,String stockTime, String UpdateTime,String remain){
        if(!stockTime.matches("null") && !UpdateTime.matches("null")){
            stockTime = stockTime.substring(5,16);
            UpdateTime = UpdateTime.substring(5,16);
            Marker marker = new Marker();
            marker.setPosition(new LatLng(lat,lng));
            String temp = "가게명 : " + storeName + "\n입고시간 : " + stockTime + "\n업데이트 : "+ UpdateTime +"\n";

            //	string
            //재고 상태[100개 이상(녹색): 'plenty' / 30개 이상 100개미만(노랑색): 'some' / 2개 이상 30개 미만(빨강색): 'few' / 1개 이하(회색): 'empty']
            switch(remain){
                case "plenty":
                    temp+="100개 이상";
                    marker.setTag(temp);
                    marker.setIcon(MarkerIcons.GREEN);
                    break;
                case "some":
                    temp+="30~99개";
                    marker.setTag(temp);
                    marker.setIcon(MarkerIcons.YELLOW);
                    break;
                case "few":
                    temp+="2~29개";
                    marker.setTag(temp);
                    marker.setIcon(MarkerIcons.RED);
                    break;
                case "empty":
                    temp+="0~1개";
                    marker.setTag(temp);
                    marker.setIcon(MarkerIcons.GRAY);
                    break;
                default:
                    marker.setIcon(MarkerIcons.BLACK);
                    break;
            }
            marker.setMap(naverMap);
            marker.setOnClickListener(listener);
        }

    }
    private void doit() throws IOException, JSONException {
        new AsyncTask() {//AsyncTask객체 생성
            @Override
            protected Object doInBackground(Object[] params) {
                String lat = "37.383980";
                String lng = "126.636617";
                String lange = "5000";
                String urls = "https://8oi9s0nnth.apigw.ntruss.com/corona19-masks/v1/storesByGeo/json";
                urls += "?&lat="+lat + "&lng="+lng+ "&m="+lange+"&_returnType=json";
                StringBuilder urlBuilder = new StringBuilder(urls); /*URL*/

                Log.i("TM", urlBuilder.toString());

                java.net.URL url = null;
                try {
                    url = new URL(urlBuilder.toString());


                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    conn.setRequestMethod("GET");

                    conn.setRequestProperty("Content-type", "application/json");

                    Log.i("Response code: ", conn.getResponseCode() + "");

                    BufferedReader rd;

                    if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {

                        rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    } else {

                        rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));

                    }

                    StringBuilder sb = new StringBuilder();

                    String line;

                    while ((line = rd.readLine()) != null) {

                        sb.append(line);

                    }


                    JSONObject response = new JSONObject(sb.toString());

                    JSONArray jsonArray = (JSONArray) response.get("stores");
                    for (int i=0; i < jsonArray.length(); i++)
                    {
                        try {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            // Pulling items from the array
                            String addr= jsonObject.getString("addr");
                            String code= jsonObject.getString("code");
                            String created_at= jsonObject.getString("created_at");
                            Double glat= jsonObject.getDouble("lat");
                            Double glng= jsonObject.getDouble("lng");
                            String name = jsonObject.getString("name");
                            String remain_stat= jsonObject.getString("remain_stat");
                            String stock_at= jsonObject.getString("stock_at");
                            String type= jsonObject.getString("type");

                            maskMarks[count] = new MaskMark(glat,glng,name,stock_at,created_at,remain_stat);
                            count++;
                        } catch (JSONException e) {

                        }
                    }   rd.close();

                    conn.disconnect();
                }
                catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return false;
            }
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                    for(int i = 0 ; i < count ; i++){
                        makeMark(maskMarks[i].lat,maskMarks[i].lng,maskMarks[i].storeName,
                                maskMarks[i].stockTime,maskMarks[i].UpdateTime,maskMarks[i].remain);
                    }
            }
        }.execute();

    }
}

/* 테스트용 데이터
mark[0] = new Marks("1","헬레니아 와 벨리카 사이",37.5702317, 126.9834601,0,"2020-02-05");
 mark[1] = new Marks("1","바로 거기",37.5603174, 126.9635914,1,"2020-02-05");
  mark[2] = new Marks("2","바로 거기 옆에 있는 거기",37.588469, 127.034147,2,"2020-02-05");
*/
