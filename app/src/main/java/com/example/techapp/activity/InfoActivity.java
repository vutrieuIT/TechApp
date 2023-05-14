package com.example.techapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.techapp.R;
import com.example.techapp.model.User;
import com.example.techapp.storage.SharedPrefManager;

public class InfoActivity extends AppCompatActivity {

    ImageView infoAvatar;
    TextView infoId, infoUsername, infoName, infoEmail, infoPhone;
    Button infoBtnLogout, infoBtnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        if (SharedPrefManager.getInstance(getApplicationContext()).isLogined()){
            anhXa();
            showInfo();
            event();
        } else {
            finish();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }

    }

    void anhXa(){
        infoAvatar = findViewById(R.id.infoAvatar);
        infoId = findViewById(R.id.textViewId);
        infoName = findViewById(R.id.textViewName);
        infoUsername = findViewById(R.id.textViewUserName);
        infoEmail = findViewById(R.id.textViewEmail);
        infoPhone = findViewById(R.id.textViewPhone);

        infoBtnBack = findViewById(R.id.infoBtnBack);
        infoBtnLogout = findViewById(R.id.infoBtnLogout);
    }

    void showInfo() {
        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        infoId.setText(String.valueOf(user.getId()));
        infoName.setText(user.getFullName());
        infoUsername.setText(user.getUserName());
        infoEmail.setText(user.getEmail());
        infoPhone.setText(user.getSdt());

        if (user.getAvatar() != null) {
            Glide.with(getApplicationContext())
                    .load(user.getAvatar())
                    .into(infoAvatar);
        }
    }

    void event(){
        infoBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(InfoActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        infoBtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefManager.getInstance(getApplicationContext()).logout();
                finish();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        infoAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(InfoActivity.this, AvatarActivity.class);
                startActivity(intent);
            }
        });
    }

}