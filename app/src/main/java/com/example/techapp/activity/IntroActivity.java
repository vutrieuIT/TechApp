package com.example.techapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.techapp.R;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int n = 0;
                try {
                    do {
                        if (n>=2000){
                            finish();
                            Intent intent = new Intent(
                                    getApplicationContext(),
                                    LoginActivity.class
                            );
                            startActivity(intent);
                            return;
                        }
                        Thread.sleep(100);
                        n+=100;
                    } while (true);
                } catch (InterruptedException interruptedException) {
                    finish();
                    Intent intent = new Intent(
                            getApplicationContext(),
                            LoginActivity.class
                    );
                    startActivity(intent);
                    return;
                }
                catch (Throwable throwable){
                    finish();
                    Intent intent = new Intent(
                            getApplicationContext(),
                            LoginActivity.class
                    );
                    startActivity(intent);
                    throw throwable;
                }
            }
        });
        thread.start();
    }
}