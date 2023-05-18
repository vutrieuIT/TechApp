package com.example.techapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.techapp.AsyncTack.DeleteAllOrderAsync;
import com.example.techapp.AsyncTack.GetAllOrderAsync;
import com.example.techapp.Constant;
import com.example.techapp.R;
import com.example.techapp.adapter.OrderAdapter;
import com.example.techapp.api.APIBuilder;
import com.example.techapp.api.APIService;
import com.example.techapp.database.MyDatabase;
import com.example.techapp.database.Order;
import com.example.techapp.model.OrderRequest;
import com.example.techapp.storage.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity implements OrderAdapter.OnItemDeleteListener{

    RecyclerView cartRVProduct;

    OrderAdapter orderAdapter;

    List<Order> orders;

    Button btnBack, btnPay, btnOrder;

    TextView tvTotalMoney;

    APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        anhXa();
        configAdapter();

        try {
            getData();
            totalMoney();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        pay();
        back();
        switchOrder();
    }

    void anhXa() {
        cartRVProduct = findViewById(R.id.cartRVProduct);
        btnBack = findViewById(R.id.btnBack);
        btnPay = findViewById(R.id.btnPay);
        btnOrder = findViewById(R.id.btnOrder);
        tvTotalMoney = findViewById(R.id.tvTotalMoney);

        apiService = APIBuilder.createAPI(APIService.class, Constant.url);
    }

    void configAdapter() {
        orderAdapter = new OrderAdapter(CartActivity.this, orders);
        orderAdapter.setOnItemDeleteListener(this);
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
        totalMoney();
    }

    void deleteData() {
        MyDatabase myDatabase = Room.databaseBuilder(
                getApplicationContext(),
                MyDatabase.class,
                Constant.database_name
        ).build();
        Order.OrderDAO dao = myDatabase.orderDAO();
        new DeleteAllOrderAsync(dao).execute();
        totalMoney();
    }

    void back() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    void totalMoney() {
        if (orders == null || orders.isEmpty()) {
            tvTotalMoney.setText("0 vnd");
        } else {
            Long sum = 0L;
            for (Order o : orders) {
                sum += o.getTotalPrice();
            }
            tvTotalMoney.setText(sum + " vnd");
        }
    }

    void pay() {
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                builder.setTitle("Thông báo");
                if (orders == null || orders.isEmpty()) {
                    builder.setMessage("không có sản phẩm để đặt hàng");
                } else {
                    builder.setMessage("bạn có muốn đặt hàng không \n giá trị đơn hàng là: " + tvTotalMoney.getText().toString());
                    builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            deleteData();
                            // todo: gửi đơn hàng lên server
                            List<com.example.techapp.model.Order> orderList = new ArrayList<>();
                            for (Order order : orders) {
                                com.example.techapp.model.Order o = new com.example.techapp.model.Order();
                                o.setProductId(order.getProduct_id());
                                o.setQuantity(order.getAmount());
                                o.setTotalMoney(order.getTotalPrice());
                                orderList.add(o);
                            }

                            OrderRequest orderRequest = new OrderRequest();
                            orderRequest.setUserId(SharedPrefManager.getInstance(getApplicationContext()).getUser().getId());
                            orderRequest.setOrders(orderList);

                            apiService.userOrder(orderRequest).enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if (response.isSuccessful()) {
                                        Toast.makeText(CartActivity.this, "Đã đặt hàng", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Integer code = response.code();
                                        Log.e("cart api", code.toString());
                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Log.e("cart api", t.getMessage());
                                }
                            });
                            orders.clear();
                            orderAdapter.notifyDataSetChanged();
                            totalMoney();
                        }
                    });

                    builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                }

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    void switchOrder(){
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(CartActivity.this, OrderActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onItemDeleted() {
        totalMoney();
    }
}