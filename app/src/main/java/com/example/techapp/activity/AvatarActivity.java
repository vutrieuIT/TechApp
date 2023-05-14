package com.example.techapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.techapp.Constant;
import com.example.techapp.R;
import com.example.techapp.api.APIBuilder;
import com.example.techapp.api.APIService;
import com.example.techapp.model.User;
import com.example.techapp.storage.SharedPrefManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AvatarActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PERMISSION_REQUEST_CODE = 1001;
    Button avatarBtnChoose, avatarBtnUpload;
    ImageView ivAvatar;

    APIService apiService;

    Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar);

        anhXa();
        chooseImage();
        uploadAvatar();
    }

    void anhXa(){
        avatarBtnChoose = findViewById(R.id.avatarBtnChoose);
        avatarBtnUpload = findViewById(R.id.avatarBtnUpload);
        ivAvatar = findViewById(R.id.ivAvatar);
        apiService = APIBuilder.createAPI(APIService.class, Constant.url);
    }

    void chooseImage(){
        avatarBtnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }


        });
    }

    void uploadAvatar(){
        avatarBtnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri != null){
                    if (ActivityCompat.checkSelfPermission(AvatarActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED ||
                            ActivityCompat.checkSelfPermission(AvatarActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    != PackageManager.PERMISSION_GRANTED) {
                        // Xin cấp quyền
                        ActivityCompat.requestPermissions(AvatarActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                PERMISSION_REQUEST_CODE);
                        return;
                    }
                    File file = new File(getRealPathFromUri(imageUri));
                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    MultipartBody.Part body = MultipartBody.Part.createFormData("avatar", file.getName(), requestFile);

                    int id = SharedPrefManager.getInstance(getApplicationContext()).getUser().getId();

                    apiService.uploadAvatar(id, body).enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                            if (response.isSuccessful()){
                                User user = response.body();
                                assert user != null;
                                SharedPrefManager.getInstance(getApplicationContext()).setAvatar(user.getAvatar());
                                Toast.makeText(AvatarActivity.this, "cập nhật thành công", Toast.LENGTH_SHORT).show();
                            } else {
                                int statusCode = response.code();
                                assert response.errorBody() != null;
                                String errorMessage = response.errorBody().toString();
                                Log.e("Avatar API", statusCode + " " + errorMessage);
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<User> call, Throwable t) {
                            Log.e("Avatar API", t.getMessage());
                        }
                    });
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            assert data != null;
            if (data.getData() != null) {
                imageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    ivAvatar.setImageBitmap(bitmap);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Quyền đã được cấp, thực hiện lại hành động đối khi xin cấp quyền
                if (requestCode == PERMISSION_REQUEST_CODE) {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // Quyền đã được cấp, thực hiện lại hành động đăng nhập
                        uploadAvatar();
                    } else {
                        // Quyền không được cấp. Hiển thị thông báo lỗi và đề xuất người dùng cấp lại quyền
                        Toast.makeText(this, "Bạn chưa cấp quyền truy cập ảnh cho ứng dụng", Toast.LENGTH_SHORT).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                        }
                    }
                }
            }
        }
    }

    public String getRealPathFromUri(Uri uri) {
        String filePath = "";
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            filePath = cursor.getString(column_index);
            cursor.close();
        }
        return filePath;
    }

}