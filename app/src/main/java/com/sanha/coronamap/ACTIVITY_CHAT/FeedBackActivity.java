package com.sanha.coronamap.ACTIVITY_CHAT;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sanha.coronamap.CLASS.Message;
import com.sanha.coronamap.MODULES.IDManger;
import com.sanha.coronamap.R;

public class FeedBackActivity extends AppCompatActivity {

    private EditText FeedText;
    private Button FeedButton;
    private FirebaseUser sender;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setView();
        setButton();

    }
    private void setView(){
        setContentView(R.layout.activity_feed_back);
        IDManger.SetBannerAd(this,findViewById(R.id.feedback_adview));
        FeedText = (EditText)findViewById(R.id.feed_send_text);
        FeedButton = (Button)findViewById(R.id.feed_send_button);
        sender = FirebaseAuth.getInstance().getCurrentUser();
    }
    private void setButton(){
        FeedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message = new Message(sender.getDisplayName(), FeedText .getText().toString(), sender.getUid());
                databaseReference.child("feedchat").push().setValue(message);
                FeedText.setText("");
                AlertDialog.Builder alert = new AlertDialog.Builder(FeedBackActivity.this);
                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();     //닫기
                    }
                });
                alert.setMessage("개발자에게 메시지를 보냈습니다.");
                alert.show();
            }
        });
    }
}
