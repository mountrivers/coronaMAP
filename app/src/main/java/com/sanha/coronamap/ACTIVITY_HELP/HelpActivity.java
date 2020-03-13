package com.sanha.coronamap.ACTIVITY_HELP;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.ButtonObject;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.FeedTemplate;
import com.kakao.message.template.LinkObject;
import com.kakao.message.template.SocialObject;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.util.KakaoParameterException;
import com.kakao.util.helper.log.Logger;
import com.sanha.coronamap.ACTIVITY_CHAT.FeedBackActivity;
import com.sanha.coronamap.MODULES.IDManger;
import com.sanha.coronamap.R;

import java.util.HashMap;
import java.util.Map;

public class HelpActivity extends AppCompatActivity {

    private ListView help_list;
    private Button intoFeedBackRoom;
    private Button intoChangeNickRoom;
    private Button sharekakaoButton;
    private  ArrayAdapter<String> adapter;

    private static final int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView();
        setAdapter();
        setButton();
    }

    private void setView(){

        setContentView(R.layout.activity_help);
        help_list = (ListView)findViewById(R.id.help_view);
        intoFeedBackRoom = (Button) findViewById(R.id.main_feedback_button);
        intoChangeNickRoom = (Button) findViewById(R.id.help_change_nicname);
        sharekakaoButton = (Button) findViewById(R.id.help_sharekakao_button);
    }
    private void setAdapter(){
        final ArrayAdapter<String> adapter
                = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        help_list.setAdapter(adapter);
        adapter.add("개발자 : 이산하(tksgk77@gmail.com) \n박희상(show7159@gmail.com)");
        adapter.add("사용 API : 구글 Firebase / jsoup /\n카카오링크 / 네이버 MAP API");
        adapter.add("확진자 지도 : 네이버 지식백과 \n확진자 수 : 질병관리본부 홈페이지 \n뉴스 : 다음 \n 마스크데이터 : 공공데이터포털 건강보험심사평가원");
        adapter.add("기본 닉네임은 구글 닉네임입니다. 아래 버튼으로 수정 하세요");
        adapter.add(" PS. 아직 부족한 점이 많습니다. \n개선할점은 아래 피드백 버튼으로 건의해주세요.");
        adapter.add(" 이 어플이 마음에 들었다면 오른쪽 아래 \n 카카오톡으로 공유 해주세요!");
    }
    private void setButton(){
        intoFeedBackRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HelpActivity.this, FeedBackActivity.class);
                startActivity(intent);
            }
        });
        intoChangeNickRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HelpActivity.this, ChangeNickNameActivity.class);
                startActivity(intent);
            }
        });
        sharekakaoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareKakao();
            }
        });
    }
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
                // 템플릿 밸리데이션과 쿼터 체크가 성공적으로 끝남. 톡에서 정상적으로 보내졌는지 보장은 할 수 없다. 전송 성공 유무는 서버콜백 기능을 이용하여야 한다.
            }
        });
    }
}
