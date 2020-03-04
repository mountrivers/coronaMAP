package com.sanha.coronamap.ACTIVITY_HELP;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sanha.coronamap.R;

public class ChangeNickNameActivity extends AppCompatActivity {

    private EditText nickName;
    private Button sendButton;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    private FirebaseUser cureentUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    private void setView(){
        setContentView(R.layout.activity_change_nick_name);
        nickName = (EditText)findViewById(R.id.changenick_text);
        sendButton = (Button) findViewById(R.id.changenick_button);
    }
    private void setButton(){
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nickName.getText().toString() != ""){
                    databaseReference.child("users").child(cureentUser.getUid()).child("name").setValue(nickName.getText().toString());
                    AlertDialog alertDialog = new AlertDialog.Builder(ChangeNickNameActivity.this).create();
                    alertDialog.setTitle("알림");
                    alertDialog.setMessage("닉네임이 변경 되었습니다.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                    finish();
                                }
                            });
                    alertDialog.show();
                }
            }
        });
    }
}
