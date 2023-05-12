package com.example.techapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class SignupActivity extends AppCompatActivity {

    TextView switchLogin;
    TextInputEditText etName, etUsername, etEmail, etPassword, etSdt;
    Button btnDangKy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        anhXa();
        switchLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Signup();
            }
        });
    }

    private void anhXa(){
        switchLogin = findViewById(R.id.switchLogin);
        etName = findViewById(R.id.nameEditText);
        etUsername = findViewById(R.id.usernameEditText);
        etEmail =findViewById(R.id.emailEditText);
        etPassword = findViewById(R.id.passwordEditText);
        etSdt = findViewById(R.id.SDTEditText);
        btnDangKy = findViewById(R.id.btnSignup);
    }

    private void Signup(){
        final String name = etName.getText().toString().trim();
        final String username = etUsername.getText().toString().trim();
        final String email = etEmail.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();
        final String sdt = etSdt.getText().toString().trim();

        Resources res = getResources();
        if(TextUtils.isEmpty(name)){
            etName.setError(res.getString(R.string.name_empty));
            etName.requestFocus();
            return;
        }
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
        //call api
        APIService apiService = APIBuilder.createAPI(APIService.class, Constant.url);
        apiService.register(name, username, password, email,sdt)
                .enqueue(new Callback<ResponseModel<User>>() {
                    @Override
                    public void onResponse(Call<ResponseModel<User>> call, Response<ResponseModel<User>> response) {
                        if (response.isSuccessful()){
                            ResponseModel<User> responseModel = response.body();
                            User user = responseModel.getData();
                            if (user != null){
                                // TODO:  chuyen sang giao dien Dang nhap
                                Toast.makeText(SignupActivity.this, "đăng ký thành công", Toast.LENGTH_LONG).show();

                                finish();
                                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                            } else {
                                Toast.makeText(SignupActivity.this,responseModel.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(SignupActivity.this, res.getString(R.string.internet_error), Toast.LENGTH_LONG).show();
                            Log.e("Login API", response.errorBody().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel<User>> call, Throwable t) {
                        Log.e("Login API", t.getMessage());
                    }
                });
    }
}