package com.mahidanehal21.letstalk3;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private EditText phone , otp;
    String codesent;
    FirebaseAuth auth ;
    //private PhoneAuthProvider.OnVerificationStateChangedCallbacks varificationcall;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        phone = (EditText)findViewById(R.id.e1);
        otp = (EditText)findViewById(R.id.e2);
        auth = FirebaseAuth.getInstance();
        findViewById(R.id.b1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startVerification();
            }
        });
        findViewById(R.id.b2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifySignInCode();
            }
        });


    }

    private void verifySignInCode() {
        String code = otp.getText().toString();
   PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codesent,code);
        signinwithphone(credential);
    }

    private void signinwithphone(PhoneAuthCredential cre) {
        auth.signInWithCredential(cre)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "login Successfull....", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(LoginActivity.this, "login UnSuccessfull....", Toast.LENGTH_SHORT).show();

                            }

                        }
                    }
                });

    }
    private void startVerification() {

        String ph = phone.getText().toString().trim();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                ph,
                60,
                TimeUnit.SECONDS,
                this,
                varificationcall);
    }
    PhoneAuthProvider.OnVerificationStateChangedCallbacks varificationcall = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codesent = s;
        }
    };
}
