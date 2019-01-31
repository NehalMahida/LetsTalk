package com.mahidanehal21.letstalk3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class Status_change_activity extends AppCompatActivity {
    TextView t1;
    Button b1;
    private FirebaseAuth mAuth;
    private DatabaseReference getuserdatareference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_change_activity);
        t1 = (TextView)findViewById(R.id.edit_status);
        b1 = (Button)findViewById(R.id.bt_change_sttus);
        MainActivity m = new MainActivity();
        t1.setText(m.status_string);
        mAuth = FirebaseAuth.getInstance();

    }

    public void ChangedStatus(View view) {
        String s1 = t1.getText().toString();
        String user_id = mAuth.getCurrentUser().getUid();
        getuserdatareference = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        getuserdatareference.child("status").setValue(s1);
        Toast.makeText(Status_change_activity.this, "Status changed succesfully.... " , Toast.LENGTH_SHORT).show();

    }
}
