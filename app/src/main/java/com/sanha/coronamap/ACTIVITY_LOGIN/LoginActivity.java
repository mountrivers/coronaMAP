package com.sanha.coronamap.ACTIVITY_LOGIN;


/*
 * 로그인 과정을 총괄하는 액티비티입니다. 현재는 Google만 구현되어 있습니다.
 *
 *
 */


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.sanha.coronamap.MODULES.GoogleLogin;
import com.sanha.coronamap.MainActivity;
import com.sanha.coronamap.R;

public class LoginActivity extends AppCompatActivity {
    private SignInButton mSignInButton;
    private GoogleLogin gl;   //GoogleLogin 모듈. 구글 로그인 인증에 대한 모든 작업을 담당하는 객체를 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setView();
        setButton();
        checkLogined();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        gl.handlingOnActivityResult(requestCode, resultCode, data);
        //받아온 인텐트 결과를 바탕으로 마무리 작업을 하는 메소드.
    }

    private void setView(){
        setContentView(R.layout.activity_login);

        mSignInButton = (SignInButton) findViewById(R.id.google_sign_in_btn);
    }
    private void setButton(){
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gl = new GoogleLogin(LoginActivity.this, MainActivity.class);
                //GoogleLogin 객체는 생성자로 현재 context와 이동할 액티비티의 class를 요구한다.
                gl.startCertification();
                //인증 과정을 시작하는 메소드
                startActivityForResult(gl.signIn(), gl.getSignInIntentCode());
                //인증 과정에서 필요한 Intent를 전송하고, 결과를 가져온다.
            }
        });
    }
    private void checkLogined(){
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
