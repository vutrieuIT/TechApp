package com.example.techapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.techapp.Constant;
import com.example.techapp.R;
import com.example.techapp.adapter.CategoryAdapter;
import com.example.techapp.adapter.ProductAdapter;
import com.example.techapp.api.APIBuilder;
import com.example.techapp.api.APIService;
import com.example.techapp.model.Category;
import com.example.techapp.model.Product;
import com.example.techapp.model.ResponseModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    RecyclerView recyclerViewCategory, recyclerViewPopular;
    //
    CategoryAdapter categoryAdapter;

    ProductAdapter productAdapter;
    APIService apiService;
    List<Category> categoryList = new ArrayList<>();
    List<Product> productList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        anhXa();

        getCategory();
        getProduct();
    }

    void anhXa(){
        recyclerViewCategory = findViewById(R.id.recyclerViewCategory);
        recyclerViewPopular = findViewById(R.id.recyclerViewPopular);
        // gán adapter
        categoryAdapter = new CategoryAdapter(HomeActivity.this, categoryList);
        recyclerViewCategory.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCategory.setLayoutManager(layoutManager);
        recyclerViewCategory.setAdapter(categoryAdapter);
        //
        productAdapter = new ProductAdapter(this, productList);
        recyclerViewPopular.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getApplicationContext(),
                RecyclerView.VERTICAL, false);
        recyclerViewPopular.setLayoutManager(layoutManager1);
        recyclerViewPopular.setAdapter(productAdapter);

        // api
        apiService = APIBuilder.createAPI(APIService.class, Constant.url);
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

    void getProduct(){
        apiService.getProduct().enqueue(
                new Callback<List<Product>>() {
                    @Override
                    public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                        if (response.isSuccessful()){
                            productList = response.body();
                            productAdapter.setData(productList);
                            productAdapter.notifyDataSetChanged();
                        } else {
                            Log.e("home product api", "response fail");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Product>> call, Throwable t) {
                        Log.e("home product api", t.getMessage());
                    }
                }
        );
    }
}