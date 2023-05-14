package com.example.techapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.techapp.AsyncTack.InsertOrderAsyncTack;
import com.example.techapp.Constant;
import com.example.techapp.R;
import com.example.techapp.api.APIBuilder;
import com.example.techapp.api.APIService;
import com.example.techapp.database.MyDatabase;
import com.example.techapp.database.Order;
import com.example.techapp.model.Product;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    TextView tvProductName, tvProductPrice, tvProductDesc;
    ImageView imageDetail;
    ImageButton btnMinus, btnPlus;
    Button btnBack;
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
        event();
    }
    void anhXa(){
        tvProductName = findViewById(R.id.tvProductName);
        tvProductPrice = findViewById(R.id.tvProductPrice);
        tvProductDesc = findViewById(R.id.tvProductDesc);

        imageDetail = findViewById(R.id.imageDetail);

        btnMinus = findViewById(R.id.btnMinus);
        btnPlus = findViewById(R.id.btnPlus);
        btnBack = findViewById(R.id.btnBack);

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
                                npAmount.setValue(1);

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
                MyDatabase myDatabase = Room.databaseBuilder(
                        getApplicationContext(),
                        MyDatabase.class,
                        Constant.database_name
                ).build();
                Order.OrderDAO dao = myDatabase.orderDAO();
                Order order = new Order(
                  product.getId(),
                  product.getImage(),
                  product.getName(),
                  npAmount.getValue(),
                        (npAmount.getValue() * Integer.parseInt(product.getGia()))
                );

                new InsertOrderAsyncTack(dao).execute(order);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(DetailActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}