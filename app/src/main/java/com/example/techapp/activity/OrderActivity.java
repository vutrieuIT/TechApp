package com.example.techapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.techapp.Constant;
import com.example.techapp.R;
import com.example.techapp.adapter.OrderAdapter1;
import com.example.techapp.api.APIBuilder;
import com.example.techapp.api.APIService;
import com.example.techapp.model.Order1;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends AppCompatActivity implements OrderAdapter1.OnItemDeleteListener{

    RecyclerView rvOrder;

    OrderAdapter1 orderAdapter1;

    Button btnBack, btnDeleteOrders;

    List<Order1> list;
    APIService apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        anhXa();
        configAdapter();
        getData();

        back();
        deleteOrders();
    }

    void anhXa(){
        rvOrder = findViewById(R.id.RVOrder);
        btnBack = findViewById(R.id.btnBack);
        btnDeleteOrders = findViewById(R.id.btnDeleteOrders);
        apiService = APIBuilder.createAPI(APIService.class, Constant.url);
    }

    void configAdapter(){
        orderAdapter1 = new OrderAdapter1(OrderActivity.this, list);
        orderAdapter1.setOnItemDeleteListener(this);
        rvOrder.setAdapter(orderAdapter1);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                RecyclerView.VERTICAL, false);
        rvOrder.setLayoutManager(layoutManager);
        rvOrder.setHasFixedSize(true);
    }

    void getData(){
        apiService.getUserOrders(3).enqueue(new Callback<List<Order1>>() {
            @Override
            public void onResponse(Call<List<Order1>> call, Response<List<Order1>> response) {
                if (response.isSuccessful()){
                    list = response.body();
                    orderAdapter1.setData(list);
                    orderAdapter1.notifyDataSetChanged();
                } else {
                    int code = response.code();
                    Log.e("order api", "call fail " + code);
                }
            }

            @Override
            public void onFailure(Call<List<Order1>> call, Throwable t) {
                Log.e("order api", "call fail: " + t.getMessage());
            }
        });
    }

    void back(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(OrderActivity.this, CartActivity.class));
            }
        });
    }

    @Override
    public void onItemDeleted(Long orderId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Long> ids = new ArrayList<>();
                ids.add(orderId);
                apiService.deleteOrders(ids).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()){
                            Toast.makeText(OrderActivity.this, "Đã xóa đơn", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("Order api delete", "call fail " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("Order api delete", "call fail " + t.getMessage());
                    }
                });
            }
        }).start();
    }

    void deleteOrders(){
        btnDeleteOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Long> ids = new ArrayList<>();
                for (Order1 o:list){
                    ids.add(o.getOrderId());
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                    }
                }).start();
            }
        });

    }

}