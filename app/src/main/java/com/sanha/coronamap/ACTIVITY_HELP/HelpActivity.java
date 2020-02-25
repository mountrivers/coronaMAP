package com.sanha.coronamap.ACTIVITY_HELP;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.sanha.coronamap.ACTIVITY_CHAT.FeedBackActivity;
import com.sanha.coronamap.MODULES.IDManger;
import com.sanha.coronamap.R;

public class HelpActivity extends AppCompatActivity {

    private ListView help_list;
    private AdView mAdView;
    private Button intoFeedBackRoom;
    private Button intoChangeNickRoom;

    private static final int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 1;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_help);

        IDManger.SetBannerAd(this,findViewById(R.id.help_adview));

        mInterstitialAd = IDManger.SetPopUpAd(this);


        help_list = (ListView)findViewById(R.id.help_view);
        intoFeedBackRoom = (Button) findViewById(R.id.main_feedback_button);
        intoChangeNickRoom = (Button) findViewById(R.id.help_change_nicname);
        final ArrayAdapter<String> adapter
                = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        help_list.setAdapter(adapter);
        adapter.add("개발자 : 박희상 / 이산하");
        adapter.add("메일 : show7159@gmail.com / tksgk77@gmail.com");
        adapter.add("사용 API : 네이버 Maps API / 구글 Firebase");
        adapter.add("자료 출처 : 질병관리본부");
        adapter.add("개인정보는 구글 계정 관하여 공개된 거삼ㄴ 받습니다. \n ex) 이메일,구글닉네임 등 ");
        adapter.add("기본 닉네임은 구글 닉네임입니다. 원하시면 아래 \n 버튼으로 수정 하실 수 있습니다. ");
        adapter.add("현재 뉴스는 다음에서 크롤링 해오고 있습니다. ");
        adapter.add(" PS. 아직 부족한 점이 많습니다. \n 개선할점은 메인화면에서 개발자 건의하기로 해주세요.");

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
                if (mInterstitialAd.isLoaded())
                    mInterstitialAd.show();
                Intent intent = new Intent(HelpActivity.this, ChangeNickNameActivity.class);
                startActivity(intent);
            }
        });

    }
}
