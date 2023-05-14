package com.example.techapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.techapp.AsyncTack.GetAllOrderAsync;
import com.example.techapp.Constant;
import com.example.techapp.R;
import com.example.techapp.adapter.OrderAdapter;
import com.example.techapp.database.MyDatabase;
import com.example.techapp.database.Order;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class CartActivity extends AppCompatActivity {

    RecyclerView cartRVProduct;

    OrderAdapter orderAdapter;

    List<Order> orders;

    Button btnBack, btnPay;

    TextView tvTotalMoney;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        anhXa();
        configAdapter();

        try {
            getData();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        back();
    }

    void anhXa(){
        cartRVProduct = findViewById(R.id.cartRVProduct);
        btnBack = findViewById(R.id.btnBack);
        btnPay = findViewById(R.id.btnPay);
        tvTotalMoney = findViewById(R.id.tvTotalMoney);
    }
    void configAdapter(){
        orderAdapter = new OrderAdapter(CartActivity.this, orders);
        cartRVProduct.setAdapter(orderAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                RecyclerView.VERTICAL, false);

        cartRVProduct.setLayoutManager(layoutManager);
        cartRVProduct.setHasFixedSize(true);
    }

    void getData() throws ExecutionException, InterruptedException {
        MyDatabase myDatabase = Room.databaseBuilder(
                getApplicationContext(),
                MyDatabase.class,
                Constant.database_name
        ).build();

        Order.OrderDAO dao = myDatabase.orderDAO();
        orders = new GetAllOrderAsync(dao).execute().get();
        orderAdapter.setData(orders);
        orderAdapter.notifyDataSetChanged();
    }

    void back(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    void pay(){
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                builder.setTitle("Thông báo");
                builder.setMessage("bạn có muốn đặt hàng không \n giá trị đơn hàng là: "+ tvTotalMoney.getText().toString());
                builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Toast.makeText(CartActivity.this, "gửi dơn hàng lên server, xóa orders", Toast.LENGTH_SHORT).show();
                        // todo: gửi đơn hàng lên server
                    }
                });

                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}