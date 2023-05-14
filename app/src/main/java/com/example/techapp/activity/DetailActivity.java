package com.example.techapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.techapp.Constant;
import com.example.techapp.R;
import com.example.techapp.api.APIBuilder;
import com.example.techapp.api.APIService;
import com.example.techapp.model.Product;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    TextView tvProductName, tvProductPrice, tvProductDesc;
    ImageView imageDetail;
    ImageButton btnMinus, btnPlus;
    NumberPicker npAmount;
    androidx.appcompat.widget.AppCompatButton btnAdd;

    APIService apiService;

    Product product;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        anhXa();
        getDetail();
    }
    void anhXa(){
        tvProductName = findViewById(R.id.tvProductName);
        tvProductPrice = findViewById(R.id.tvProductPrice);
        tvProductDesc = findViewById(R.id.tvProductDesc);

        imageDetail = findViewById(R.id.imageDetail);

        btnMinus = findViewById(R.id.btnMinus);
        btnPlus = findViewById(R.id.btnPlus);

        npAmount = findViewById(R.id.npAmount);
        btnAdd = findViewById(R.id.btnAdd);

        apiService = APIBuilder.createAPI(APIService.class, Constant.url);
    }

    void getDetail(){
        int id = getIntent().getIntExtra("product_id", 0);
        if (id >= 1){
            apiService.getDetail(id).enqueue(
                    new Callback<Product>() {
                        @Override
                        public void onResponse(Call<Product> call, Response<Product> response) {
                            if (response.isSuccessful()){
                                product = response.body();
                                tvProductName.setText(product.getName());
                                tvProductPrice.setText(product.getGia());
                                tvProductDesc.setText(product.getMoTa());

                                npAmount.setMinValue(1);
                                npAmount.setMaxValue(product.getSoLuong());

                                Glide.with(getApplicationContext())
                                        .load(product.getImage())
                                        .into(imageDetail);
                            } else {
                                Log.e("Detail api", "call api fail");
                            }
                        }

                        @Override
                        public void onFailure(Call<Product> call, Throwable t) {
                            Log.e("Detail api", t.getMessage());
                        }
                    }
            );
        }
    }

    void event(){
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cur = npAmount.getValue();
                if(cur < product.getSoLuong()){
                    npAmount.setValue(cur+1);
                }
            }
        });

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cur = npAmount.getValue();
                if (cur > 1){
                    npAmount.setValue(cur-1);
                }
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DetailActivity.this, "mua hàng", Toast.LENGTH_SHORT).show();
            }
        });
    }
}