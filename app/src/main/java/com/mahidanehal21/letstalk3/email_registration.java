package com.mahidanehal21.letstalk3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class email_registration extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private FirebaseAuth mauth;
    private Button button1;
    private ProgressDialog  loadingbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_registration);
         loadingbar = new ProgressDialog(this);
        mauth = FirebaseAuth.getInstance();
        email=(EditText)findViewById(R.id.email_sign_in);
        password=(EditText) findViewById(R.id.password_sign_in);
        button1= findViewById(R.id.log_in);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Stpass = password.getText().toString();
                String Stemail = email.getText().toString();
                login(Stemail,Stpass);

            }
        });

    }

    private void login(String stemail, String stpass) {
        boolean go = true;
        if(stemail.equals(""))
        {
            email.setError("email is required!!");

            go = false;
        }
        if(stpass.equals(""))
        {
            password.setError("password is required!!");
            go = false;
        }
        if(go) {
            try {
                loadingbar.setTitle("Login Account");
                loadingbar.setMessage("Please wait...");
                loadingbar.show();
                mauth.signInWithEmailAndPassword(stemail, stpass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    Intent i = new Intent(email_registration.this, MainActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                    startActivity(i);
                                    loadingbar.dismiss();

                                } else {

                                    Toast.makeText(email_registration.this, "please Enter valid detail", Toast.LENGTH_SHORT).show();
                                    loadingbar.dismiss();
                                }
                            }
                        });
            } catch (Exception e) {
            }
        }

    }

}


