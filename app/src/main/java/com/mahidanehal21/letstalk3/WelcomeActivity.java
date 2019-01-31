package com.mahidanehal21.letstalk3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Thread thread = new Thread()
        {
            public void run()
            {
                try
                {
                    sleep(1000);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    Intent i1 = new Intent(WelcomeActivity.this,MainActivity.class);
                    startActivity(i1);
                }
            }
        };
        thread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
