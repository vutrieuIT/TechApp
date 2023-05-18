package com.example.techapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.techapp.Constant;
import com.example.techapp.R;
import com.example.techapp.api.APIBuilder;
import com.example.techapp.api.APIService;
import com.example.techapp.model.ResponseModel;
import com.example.techapp.model.User;
import com.example.techapp.storage.SharedPrefManager;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    TextView switchRegister;
    TextInputEditText etUsername, etPassword;
    Button btnLogin;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (SharedPrefManager.getInstance(getApplicationContext()).isLogined()){
            finish();
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
        }
        anhXa();

        switchRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void anhXa(){
        switchRegister = findViewById(R.id.switchRegister);
        btnLogin = findViewById(R.id.btnLogin);
        etUsername = findViewById(R.id.loginUsername);
        etPassword = findViewById(R.id.loginPassword);
        progressBar = findViewById(R.id.progressBar);
    }
    private void login(){
        final String username = etUsername.getText().toString();
        final String password = etPassword.getText().toString();
        Resources res = getResources();

        if (TextUtils.isEmpty(username)){
            etUsername.setError(res.getString(R.string.username_empty));
            etUsername.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)){
            etPassword.setError(res.getString(R.string.password_empty));
            etPassword.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE); // Hiển thị ProgressBar
        btnLogin.setEnabled(false); // Vô hiệu hóa nút Đăng Nhập
        APIService apiService = APIBuilder.createAPI(APIService.class, Constant.url);
        apiService.login(username, password).enqueue(
                new Callback<ResponseModel<User>>() {
                    @Override
                    public void onResponse(Call<ResponseModel<User>> call, Response<ResponseModel<User>> response) {
                        if (response.isSuccessful()){
                            ResponseModel<User> responseModel = response.body();
                            User user = responseModel.getData();
                            if (user != null){
                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                                finish();
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            } else {
                                Toast.makeText(LoginActivity.this,responseModel.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, res.getString(R.string.internet_error), Toast.LENGTH_LONG).show();
                            Log.e("Login API", response.errorBody().toString());
                        }
                        progressBar.setVisibility(View.GONE); // Ẩn ProgressBar
                        btnLogin.setEnabled(true); // Kích hoạt lại nút Đăng Nhập
                    }

                    @Override
                    public void onFailure(Call<ResponseModel<User>> call, Throwable t) {
                        Log.e("Login API", t.getMessage());
                        progressBar.setVisibility(View.GONE); // Ẩn ProgressBar
                        btnLogin.setEnabled(true); // Kích hoạt lại nút Đăng Nhập
                    }
                }
        );
    }
}