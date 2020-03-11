package com.sanha.coronamap.ACTIVITY_MAP;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
import com.sanha.coronamap.ACTIVITY_HELP.ChangeNickNameActivity;
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
    private Button seeMaskButton, helpButton;
    private FusedLocationSource locationSource;
    public Double lat = 37.383980;
    public Double lng = 126.636617;
    public String lange = "3000";
    private LocationManager locationManager;
    Location userLocation;
    private static final int REQUEST_CODE_LOCATION = 2;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);
        seeMaskButton = (Button) findViewById(R.id.map_loadmask);
        IDManger.SetBannerAd(this,findViewById(R.id.map_adview));
        IDManger.SetNaverSdkClientId(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
       userLocation = getMyLocation();
        if( userLocation != null ) {
            lat = userLocation.getLatitude();
            lng = userLocation.getLongitude();
        }


        NaverMapOptions options = new NaverMapOptions()
                .camera(new CameraPosition(new LatLng(lat, lng), 12))
                .mapType(NaverMap.MapType.Basic)
                .compassEnabled(true)
                .zoomControlEnabled(true)
                ;

        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment)fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance(options);
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
        locationSource =
                new FusedLocationSource(this, 1000);

        seeMaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraPosition cameraPosition = naverMap.getCameraPosition();
                lat = cameraPosition.target.latitude;
                lng = cameraPosition.target.longitude;
                for(int i =0 ; i < count; i++){
                    if(marker[i]!=null)
                     marker[i].setMap(null);
                }
                try {
                    doit();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        helpButton = (Button)findViewById(R.id.map_help_button);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(MapActivity.this).create();
                alertDialog.setTitle("안내");
                alertDialog.setMessage("해당버전은 베타버전이라, 오작동이 있을 수 있습니다.\n 데이터는 5~10분간격으로 업데이트 되며, 갱신을 눌러 확인하시면 됩니다.\n 건강 보험심사원에서 데이터를 그대로 받아오지만, 실제와 상이할 수 있습니다.\n 초록:100개이상/ 노랑:30~99개 / 빨강 2~29개/ 회색 0~2개 ");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        });
                alertDialog.show();
            }
        });
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
        uiSettings.setZoomControlEnabled(true);
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

    private void makeMark(Double lat, Double lng, String storeName,String stockTime, String UpdateTime,String remain,int i){
        if(!stockTime.matches("null") && !UpdateTime.matches("null")){
            stockTime = stockTime.substring(5,16);
            UpdateTime = UpdateTime.substring(5,16);
            marker[i] = new Marker();
            marker[i].setPosition(new LatLng(lat,lng));
            String temp = "가게명 : " + storeName + "\n입고시간 : " + stockTime + "\n업데이트 : "+ UpdateTime +"\n";

            //	string
            //재고 상태[100개 이상(녹색): 'plenty' / 30개 이상 100개미만(노랑색): 'some' / 2개 이상 30개 미만(빨강색): 'few' / 1개 이하(회색): 'empty']
            switch(remain){
                case "plenty":
                    temp+="100개 이상";
                    marker[i].setTag(temp);
                    marker[i].setIcon(MarkerIcons.GREEN);
                    break;
                case "some":
                    temp+="30~99개";
                    marker[i].setTag(temp);
                    marker[i].setIcon(MarkerIcons.YELLOW);
                    break;
                case "few":
                    temp+="2~29개";
                    marker[i].setTag(temp);
                    marker[i].setIcon(MarkerIcons.RED);
                    break;
                case "empty":
                    temp+="0~1개";
                    marker[i].setTag(temp);
                    marker[i].setIcon(MarkerIcons.GRAY);
                    break;
                default:
                    marker[i].setIcon(MarkerIcons.BLACK);
                    break;
            }
            marker[i].setMap(naverMap);
            marker[i].setOnClickListener(listener);
        }

    }
    private void doit() throws IOException, JSONException {
        new AsyncTask() {//AsyncTask객체 생성
            @Override
            protected Object doInBackground(Object[] params) {

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
                    count = 0;
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
                                maskMarks[i].stockTime,maskMarks[i].UpdateTime,maskMarks[i].remain,i);
                    }
            }
        }.execute();

    }

        private Location getMyLocation() {
            Location currentLocation = null;
            // Register the listener with the Location Manager to receive location updates
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                System.out.println("////////////사용자에게 권한을 요청해야함");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, this.REQUEST_CODE_LOCATION);
                getMyLocation(); //이건 써도되고 안써도 되지만, 전 권한 승인하면 즉시 위치값 받아오려고 썼습니다!
            }
            else {
                System.out.println("////////////권한요청 안해도됨");

                // 수동으로 위치 구하기
                String locationProvider = LocationManager.GPS_PROVIDER;
                currentLocation = locationManager.getLastKnownLocation(locationProvider);

            }
            return currentLocation;
        }

}

/* 테스트용 데이터
mark[0] = new Marks("1","헬레니아 와 벨리카 사이",37.5702317, 126.9834601,0,"2020-02-05");
 mark[1] = new Marks("1","바로 거기",37.5603174, 126.9635914,1,"2020-02-05");
  mark[2] = new Marks("2","바로 거기 옆에 있는 거기",37.588469, 127.034147,2,"2020-02-05");
*/
