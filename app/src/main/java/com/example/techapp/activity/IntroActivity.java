package com.example.techapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.techapp.R;

public class IntroActivity extends AppCompatActivity {

    Button introButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        introButton = findViewById(R.id.introButton);

        introButton.setOnClickListener(view -> {
            finish();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        });

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int n = 0;
                try {
                    do {
                        if (n>=3000){
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