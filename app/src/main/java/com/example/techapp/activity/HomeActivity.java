package com.example.techapp.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.techapp.Constant;
import com.example.techapp.R;
import com.example.techapp.adapter.CategoryAdapter;
import com.example.techapp.adapter.ProductAdapter;
import com.example.techapp.api.APIBuilder;
import com.example.techapp.api.APIService;
import com.example.techapp.model.Category;
import com.example.techapp.model.Product;
import com.example.techapp.model.ResponseModel;
import com.example.techapp.model.User;
import com.example.techapp.storage.SharedPrefManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements CategoryAdapter.OnItemClickListener{

    RecyclerView recyclerViewCategory, recyclerViewProduct;
    //
    CategoryAdapter categoryAdapter;

    ProductAdapter productAdapter;
    APIService apiService;
    List<Category> categoryList = new ArrayList<>();
    List<Product> productList = new ArrayList<>();
    ImageButton cartBtn;

    TextView name;
    ImageView avatar;
    EditText editTextSearch;

    int pageNumber = 0, pageSize = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        anhXa();
        welcome();
        configRecycleView();
        getCategory();
        loadProduct();
        event();

        // event

    }

    void anhXa(){
        recyclerViewCategory = findViewById(R.id.recyclerViewCategory);
        recyclerViewProduct = findViewById(R.id.recyclerViewProduct);
        name = findViewById(R.id.username);
        avatar = findViewById(R.id.imageViewAvatar);
        cartBtn = findViewById(R.id.cartBtn);
        editTextSearch = findViewById(R.id.editTextSearch);

        // api
        apiService = APIBuilder.createAPI(APIService.class, Constant.url);
    }

    void configRecycleView(){
        // RV CATEGORY
        categoryAdapter = new CategoryAdapter(HomeActivity.this, categoryList);
        categoryAdapter.setOnItemClickListener(this);
        recyclerViewCategory.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCategory.setLayoutManager(layoutManager);
        recyclerViewCategory.setAdapter(categoryAdapter);
        // Tạo một đối tượng DividerItemDecoration với hướng vertical
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewCategory.getContext(), LinearLayoutManager.HORIZONTAL);

        // Đặt drawable cho phần đường phân cách
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(recyclerViewCategory.getContext(), R.drawable.divider));

        // Thêm đối tượng DividerItemDecoration vào RecyclerView của bạn
        recyclerViewCategory.addItemDecoration(dividerItemDecoration);

        // RV Product
        productAdapter = new ProductAdapter(this, productList);
        recyclerViewProduct.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getApplicationContext(),
                RecyclerView.VERTICAL, false);
        recyclerViewProduct.setLayoutManager(layoutManager1);
        recyclerViewProduct.setAdapter(productAdapter);
    }

    void welcome(){
        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        name.setText(user.getFullName());
        if(user.getAvatar() != null){
            Glide.with(HomeActivity.this)
                    .load(user.getAvatar())
                    .into(avatar);
        }
    }
    void getCategory(){
        apiService.getCategories().enqueue(
                new Callback<ResponseModel<List<Category>>>() {
                    @Override
                    public void onResponse(Call<ResponseModel<List<Category>>> call, Response<ResponseModel<List<Category>>> response) {
                        if (response.isSuccessful()){
                            ResponseModel<List<Category>> responseModel = response.body();
                            categoryList = responseModel.getData();
                            categoryAdapter.setData(categoryList);
                            categoryAdapter.notifyDataSetChanged();
                        } else {
                            int statusCode = response.code();
                            Log.e("home api", String.valueOf(statusCode));
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel<List<Category>>> call, Throwable t) {
                        Log.e("home api", t.getMessage());
                    }
                }
        );
    }

    void loadProduct(){
        productList.clear();
        productAdapter.notifyDataSetChanged();

        getProduct(pageNumber);
    }

    void getProduct(final int pageNumber){
        new Thread(new Runnable() {
            @Override
            public void run() {
                apiService.getProductsPaging(pageNumber,pageSize).enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                        if(response.isSuccessful()){
                            List<Product> list = response.body();
                            if (list != null &&!list.isEmpty()){
                                productList.addAll(list);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        productAdapter.setData(productList);
                                        productAdapter.notifyDataSetChanged();
                                    }
                                });
                                getProduct(pageNumber+1);
                            }
                        } else {
                            Log.e("home product api", "response fail");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Product>> call, Throwable t) {
                        Log.e("home product api", t.getMessage());
                    }
                });
            }
        }).start();
    }


    void event(){
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(HomeActivity.this, InfoActivity.class);
                startActivity(intent);
            }
        });

        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String keyword = s.toString().trim();
                productAdapter.search(keyword);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onItemClick(int id) {
        apiService.getProducts(id).enqueue(
                new Callback<List<Product>>() {
                    @Override
                    public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                        if (response.isSuccessful()){
                            productList = response.body();
                            productAdapter.setData(productList);
                            productAdapter.notifyDataSetChanged();
                        } else {

                        }
                    }
                    @Override
                    public void onFailure(Call<List<Product>> call, Throwable t) {

                    }
                }
        );
    }
}