package com.mahidanehal21.letstalk3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class send extends AppCompatActivity implements View.OnClickListener {

    private TextView t1;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        t1 = findViewById(R.id.st1);
        t1.setOnClickListener(this);
        firebaseAuth = FirebaseAuth.getInstance();
        String id = firebaseAuth.getCurrentUser().getUid();
     databaseReference = FirebaseDatabase.getInstance().getReference().child("USERS").child(id);
     databaseReference.setValue(t1.getText().toString());
            }

    @Override
    public void onClick(View view) {



    }
}
