package com.mahidanehal21.letstalk3;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Authentication extends AppCompatActivity {
    private static final String TAG= "phoneAUTH";
    private EditText otp;
    private EditText phonenumber;
    private Button button2,button3,button1,button4;
    private FirebaseAuth pauth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks varificationcall;
    private PhoneAuthProvider.ForceResendingToken resendingToken;
    private String phoneVerificationId;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        otp = findViewById(R.id.t2);
        phonenumber  =  findViewById(R.id.t1);
        button2 = findViewById(R.id.b2);
        button3 = findViewById(R.id.b3);
        button1 =  findViewById(R.id.b1);
        button4 =  (Button) findViewById(R.id.b4);
        button3.setEnabled(false);
        button2.setEnabled(false);
        button4.setEnabled(true);
        pauth = FirebaseAuth.getInstance();

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendcode(view);

            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendcode(view);

            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyCode(view);

            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(Authentication.this, LoginActivity.class);
                startActivity(intent2);
                finish();

            }
        });

    }
    public void sendcode(View view) {
        String ph = phonenumber.getText().toString().trim();
        setUpVerifi();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                ph,
                60,
                TimeUnit.SECONDS,
                this,
                varificationcall);
        Toast.makeText(Authentication.this, "check otp", Toast.LENGTH_SHORT).show();

    }


    private void setUpVerifi(){
        varificationcall = new PhoneAuthProvider.OnVerificationStateChangedCallbacks()
        {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                button2.setEnabled(false);
                button3.setEnabled(false);
                signinwithphone(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                if(e instanceof FirebaseAuthInvalidUserException){
                    Log.d(TAG,"Invalid cradential"+ e.getLocalizedMessage());
                }
                else if(e instanceof FirebaseAuthInvalidUserException){
                    Log.d(TAG,"Sms Quota exceeded");
                }

            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                phoneVerificationId = s;
                resendingToken = forceResendingToken;
                button3.setEnabled(true);
                button1.setEnabled(false);
                button2.setEnabled(true);

            }
        };
    }

    public  void verifyCode(View view){
        String code = otp.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(phoneVerificationId,code);
        signinwithphone(credential);
        Toast.makeText(Authentication.this, "process....", Toast.LENGTH_SHORT).show();

    }

    private void signinwithphone(PhoneAuthCredential cre){
        pauth.signInWithCredential(cre)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            button2.setEnabled(false);
                            button3.setEnabled(false);
                            FirebaseUser user = task.getResult().getUser();

                            Intent intent = new Intent(Authentication.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {if(task.getException()instanceof FirebaseAuthInvalidCredentialsException){

                        }

                        }
                    }
                });
    }
    public void resendcode(View view){
        String ph  = phonenumber.getText().toString();
        setUpVerifi();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                ph,60,
                TimeUnit.SECONDS,
                this,
                varificationcall);

    }

}

