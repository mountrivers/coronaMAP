package com.sanha.coronamap.MODULES;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sanha.coronamap.CLASS.User;
import com.sanha.coronamap.R;


public class GoogleLogin
{

    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleAPIClient;   //The main entry point for Google Play services integration.
    private static int GOOGLE_LOGIN_OPEN = 100; //Intent 교환 시 사용하는 식별코드
    private DatabaseReference mUserRef;
    private FirebaseDatabase mDatabase;
    private FirebaseUser currentUser;
    private Context context; //현재 자신이 호출한 액티비티의 context
    Class<?> destinationClass; //이동할 목적지 액티비티 클래스. (ex. MainRoomActivity.class)

    public GoogleLogin(Context context, Class<?> destinationClass)  //내 컨테스트랑, 다 완료되면 갈 목적지 액티비티 클래스
    {  //Google로그인 객체 생성에 필요한 초기화를 담당하는 생성자. 현재 context와 이동할 액티비티의 class를 요구한다.
        //GoogleLogin은 액티비티가 아니기 때문에, 자신의 context를 가지고 있지 않아서 context 요구시 this를 사용할 수 없다.
        //그래서 호출한 액티비티의 context를 가져온다.
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        this.context = context;
        this.destinationClass = destinationClass;
        mDatabase = FirebaseDatabase.getInstance();
        mUserRef = FirebaseDatabase.getInstance().getReference("users");

    }

    public void startCertification()  //인증 시작 부분. Intent 교환을 제외한 부분을 담당한다.
    {

        alreadyLoginCheck(); //이미 로그인 된 상태인지 체크. (기존 LoginActivity에도 있음)

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)  //GoogleSignInOptions is options used to configure the GOOGLE_SIGN_IN_API.
                .requestIdToken(context.getString(R.string.default_web_client_id)) // 웹클라이언트 키 전송 Specifies that an ID token for authenticated users is requested.
                .requestEmail()  //Specifies that user ID is requested by your application.
                .build();

        mGoogleAPIClient = new GoogleApiClient.Builder(context)
                .enableAutoManage( (FragmentActivity)((Activity)context), new GoogleApiClient.OnConnectionFailedListener()
                {

                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        // 실패 시 처리 하는 부분.
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    public void alreadyLoginCheck() //이미 로그인 된 상태인지 체크. (기존 LoginActivity에도 있음)
    {
        if(currentUser != null)
        {
            Log.d("GOOGLELOGIN__", "로그인되어있음");
            Toast.makeText(context, "로그인되어있음", Toast.LENGTH_LONG).show();
            context.startActivity(new Intent(context, destinationClass)); //마찬가지로 context를 가질 수 없기 때문에 호출한 액티비티의 context를 사용.
            ((Activity)context).finish(); //finish는 Activity로 강제 캐스팅 한 후 사용해야 한다.
            return;
        }
    }

    public Intent signIn() //구글에 인텐트를 보내는 부분을 담당.
    {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleAPIClient);

        return signInIntent;
    }
    public int getSignInIntentCode() //구글에 인텐트를 보낼 때, 미리 정해진 Result로 받을 코드를 얻는 get메소드.
    {
        return GOOGLE_LOGIN_OPEN;
    }

    public void handlingOnActivityResult(int requestCode, int rusultCode, Intent data) //onAcitivityResult에 삽입
    {  //onActivityResult가 이 모듈에서 처리가 안되는듯 함. 그래서 외부 액티비티에서 onActivityResult를 받고,
            //그 데이터값을 처리할 메소드를 따로 작성.

        if (requestCode == GOOGLE_LOGIN_OPEN)
        {  //Signin버튼을 눌러서, signin 메소드를 통해 보낸 Intent를 받아 구글 로그인 성공여부ㅡㄹ 확인
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);   //Helper function to extract out GoogleSignInResult from the onActivityResult(int, int, Intent) for Sign In
            //GoogleSignInResult객체를 가져오기 위한 코드.getSignInResultFromIntent
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase : 구글 계정 로그인에 성공하여, 파이어베이스에 입력해야 하는 상태.
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);  //바로 아래에 있는거(구글 인증과정, 인증성공후)
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct)
    {
        Log.d("LOGIN", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(((Activity)context), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser firebaseUser = task.getResult().getUser();  //구글 로그인이 성공하였다면, 파이어베이스의 데이터베이스에
                            final User saveUser = new User();   //해당 정보들을 저장해야 한다. User class 형태로 데이터를 만들어서 Email, Name, Uid를 입력하고 저장한다.
                            saveUser.setEmail(firebaseUser.getEmail());
                            saveUser.setName(firebaseUser.getDisplayName());
                            saveUser.setUid(firebaseUser.getUid());
                            if(firebaseUser.getPhotoUrl() != null) // 프로필 사진은 있을수도, 없을수도 있기 때문에 조건문으로 crash를 방지한다.
                            {
                                saveUser.setProfileUrl(firebaseUser.getPhotoUrl().toString());
                            }


                            mUserRef.child(saveUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener()
                            {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot)
                                {
                                    if(!dataSnapshot.exists()) //로그인 정보가 없을때
                                    {
                                        mUserRef.child(saveUser.getUid()).setValue(saveUser, new DatabaseReference.CompletionListener()
                                        {

                                            @Override
                                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) //로그인 정보가 없을때만 새로 저장된다.
                                            {
                                                Log.d("LOGINSTATUS", "정상적으로 저장되었음");
                                                Log.d("LOGINSTATUS" , "login success");
                                                //FirebaseUser user = mAuth.getCurrentUser();

                                                Intent intent = new Intent(context,destinationClass);  //저장이 끝나면 Mainroom으로 이동
                                                context.startActivity(intent);
                                                ((Activity)context).finish();
                                                return;
                                            }
                                        });
                                    }
                                    else
                                    {
                                        Intent intent = new Intent(context,destinationClass);  //로그인 정보가 있을땐 그냥 점프
                                        context.startActivity(intent);
                                        ((Activity)context).finish();
                                        return;
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError)
                                {
                                    Toast.makeText(context,"Authentication failed",Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                        else
                        {
                            // If sign in fails, display a message to the user.


                        }

                        // ...
                    }
                });
    }





}
