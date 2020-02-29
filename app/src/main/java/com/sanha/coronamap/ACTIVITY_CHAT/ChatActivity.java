package com.sanha.coronamap.ACTIVITY_CHAT;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.android.gms.ads.AdView;
import com.google.firebase.database.ValueEventListener;
import com.sanha.coronamap.CLASS.Message;
import com.sanha.coronamap.CLASS.User;
import com.sanha.coronamap.MODULES.IDManger;
import com.sanha.coronamap.R;

public class ChatActivity extends AppCompatActivity {

    private ListView chat_view;
    private Button buttonSend;
    private EditText editTextMessage;
    private FirebaseUser sender;
    private FirebaseAuth firebaseauth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    private AdView mAdView;
    public String nickName;
    public ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        IDManger.SetBannerAd(this,findViewById(R.id.chat_adview));

        chat_view = (ListView) findViewById(R.id.chat_view);
        buttonSend = (Button) findViewById(R.id.buttonSend);
        editTextMessage = (EditText) findViewById(R.id.editTextMessage);
        sender = FirebaseAuth.getInstance().getCurrentUser();

        nickName = sender.getDisplayName();
        FirebaseDatabase.getInstance().getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User tuser = snapshot.getValue(User.class);
                   // Log.d("MainActivity", " na : " + tuser.getUid() + " ma : " + sender.getUid());
                    if(tuser.getUid().equals(sender.getUid().toString())){
                       // Log.d("MainActivity", "★★★★★★ " + tuser.getName());
                        nickName = tuser.getName();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        showChat();

    }

    @Override
    protected void onStart() {
        super.onStart();
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!editTextMessage.getText().toString().matches("")) {
                    Message message = new Message(nickName, editTextMessage.getText().toString(), sender.getUid());
                    databaseReference.child("groupchat").push().setValue(message);
                    editTextMessage.setText("");
                    chat_view.setSelection(adapter.getCount() - 1);
                }

            }
        });
    }

    private void showChat() {
      adapter
                = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        chat_view.setAdapter(adapter);
        databaseReference.child("groupchat").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message message = dataSnapshot.getValue(Message.class);
                adapter.add(message.Name + " : " + message.Message);
                chat_view.setSelection(adapter.getCount() - 1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


}
