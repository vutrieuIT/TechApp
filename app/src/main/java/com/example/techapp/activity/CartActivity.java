package com.example.techapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.os.Bundle;

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
    }

    void anhXa(){
        cartRVProduct = findViewById(R.id.cartRVProduct);
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
}