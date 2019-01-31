package com.mahidanehal21.letstalk3;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class chatwindo extends AppCompatActivity {

    private RecyclerView recyclerViewchat;
    private Toolbar chattooToolbar;
    private EditText type_msg;
    private TextView user_name_textview, tmymsgtext, tyourmsgtext;
    private String vi_id, vi_name, vi_phonnumber, my_id,vi_img,vi_stat ;
    private FirebaseAuth chatAuth;
    private ImageButton button;

    private String mymsg, youmsg;
    private List<msg_class> recivemsg = new ArrayList<>();
    private DatabaseReference chatDatabaseReferenceMe, chatDatabaseReferenceYou, savedata;
    public visitnum chatobj = new visitnum();
    private List<msg_class> msglist = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatwindo);
        chatDatabaseReferenceMe = FirebaseDatabase.getInstance().getReference();
        recyclerViewchat = (RecyclerView) findViewById(R.id.chat_recycle_lay);
        type_msg = (EditText) findViewById(R.id.my_msg);
        button = (ImageButton) findViewById(R.id.msgsend);
        vi_id = (String) getIntent().getExtras().get("id");
        vi_name = (String) getIntent().getExtras().get("name");
        vi_phonnumber = (String) getIntent().getExtras().get("phnum");
        vi_img = (String)getIntent().getExtras().get("image");
        vi_stat = (String)getIntent().getExtras().get("status");
        chattooToolbar = (Toolbar) findViewById(R.id.chat_appbar);
        visitmember obj = new visitmember();
        obj.setId(vi_id);
        obj.setName(vi_name);
        obj.setNumber(vi_phonnumber);
        obj.setImage(vi_img);
        obj.setStatus(vi_stat);

        visitmemberlist.mem.add(obj);
        chatAuth = FirebaseAuth.getInstance();
        my_id = chatAuth.getCurrentUser().getUid();
        recyclerViewchat = (RecyclerView)findViewById(R.id.chat_recycle_lay);
        messageAdapter = new MessageAdapter((ArrayList<msg_class>) msglist);
        recyclerViewchat.setHasFixedSize(true);
        recyclerViewchat.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewchat.setAdapter(messageAdapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendmsg();
            }
        });
        getmessage();


        setSupportActionBar(chattooToolbar);
        getSupportActionBar().setTitle(vi_name);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

       // LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //View action_bar_view = layoutInflater.inflate(R.layout.chat_custom_bar, null);
        //actionBar.setCustomView(action_bar_view);

       // user_name_textview = (TextView) findViewById(R.id.custom_user_name);
       // user_name_textview.setText(vi_name);
    }

    private void getmessage() {
        chatDatabaseReferenceMe.child("Users").child(my_id).child("Messages").child(vi_id)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        msg_class object = dataSnapshot.getValue(msg_class.class);
                        msglist.add(object);
                        messageAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void sendmsg() {
        if(!type_msg.getText().toString().equals("")) {
            DatabaseReference key_msg = null;
            String myref = "Users/" + my_id + "/Messages/" + vi_id;
            String recref = "Users/" + vi_id + "/Messages/" + my_id;


            key_msg = chatDatabaseReferenceMe.child("User")
                    .child(my_id).child("Messages").child(my_id).push();

            String key = key_msg.getKey();

            Map mydata = new HashMap();

            mydata.put("name", "I");
            mydata.put("message", type_msg.getText().toString());
            mydata.put("From", my_id);

            Map msgdetail = new HashMap();

            msgdetail.put(myref + "/" + key, mydata);
            msgdetail.put(recref + "/" + key, mydata);

            chatDatabaseReferenceMe.updateChildren(msgdetail, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        Log.d("Chat Windo", databaseError.getMessage().toString());
                    }
                    type_msg.setText("");

                }


            });

        }}
}

