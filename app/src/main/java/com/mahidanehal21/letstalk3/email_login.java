package com.mahidanehal21.letstalk3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class email_login extends AppCompatActivity
{
    private FirebaseAuth auth;
    private EditText email;
    private EditText password;
    private EditText name;
    private EditText mo_num;
    private Button button3;
    private TextView signin_option;
   private DatabaseReference logindataref;
   private ProgressDialog loadingbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login);
        loadingbar = new ProgressDialog(this);
        try{
            auth  = FirebaseAuth.getInstance();}
        catch (Exception e){
            Toast.makeText(email_login.this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }

        email = (EditText) findViewById(R.id.et_user_email);
        password = (EditText) findViewById(R.id.et_user_password);
        name = (EditText) findViewById(R.id.et_user_name);
        mo_num = (EditText)findViewById(R.id.et_user_num);
        signin_option = (TextView) findViewById(R.id.tv2);
        button3 = (Button) findViewById(R.id.bt_signup);
        signin_option.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(email_login.this, email_registration.class);
                startActivity(intent);
                finish();
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String Stpass = password.getText().toString().trim();
                String Stemail = email.getText().toString().trim();
                String Stname = name.getText().toString().trim();
                String Stnum = mo_num.getText().toString().trim();
                final String phon = Stname.replaceAll(" ","").trim();
                registr(Stemail, Stpass,Stname , Stnum );
            }
        });
    }


    private void registr(final String stemail, String stpass, final String stname, final String stnum) {
        boolean go = true;
        if (stemail.equals("")) {
            email.setError("email is required!!");
        go = false;}
        if (stpass.equals("")) {
            password.setError("password is required!!");
            go = false;
        }
        if(stname.equals(""))
        {
            name.setError("name is required!!");
            go = false;
        }
        if(stnum.equals(""))
        {
            mo_num.setError("phone number  is required!!");
            go = false;
        }
        if(stnum.length()!=10)
        {
            mo_num.setError("Enter valid number");
            go = false;
        }

        if(go)
        {
            try {
                loadingbar.setTitle("Creating  Account");
                loadingbar.setMessage("Please wait...");
                loadingbar.show();
                auth.createUserWithEmailAndPassword(stemail, stpass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    try {
                                        loadingbar.dismiss();
                                       String u_id = auth.getCurrentUser().getUid().toString();
                                        logindataref = FirebaseDatabase.getInstance().getReference().child("Users").child(u_id);
                                        logindataref.child("phone_num").setValue(stnum);
                                        logindataref.child("user_name").setValue(stname);
                                        logindataref.child("status").setValue("Hey there! i am using LetsTalk");
                                        logindataref.child("image").setValue("default_image");
                                        logindataref.child("thumb_image").setValue("default_thumb_image");
                                    Intent intent = new Intent(email_login.this, profile_acti.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                        startActivity(intent);
                                    loadingbar.dismiss();
                                    finish();
                                }
                                 catch (Exception e) {
                                     Toast.makeText(email_login.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                 }} else {
                                    loadingbar.dismiss();
                                                        Toast.makeText(email_login.this, "please Enter valid detail", Toast.LENGTH_SHORT).show();

                                                    }

                                            }
                        });
            }
            catch (Exception e){
                Toast.makeText(email_login.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }


    }
}
