package com.example.techapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.techapp.R;
import com.example.techapp.activity.DetailActivity;
import com.example.techapp.database.Order;
import com.example.techapp.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.Holder>{
    Context context;
    List<Product> list;
    List<Product> searchList;

    public ProductAdapter(Context context, List<Product> list) {
        this.context = context;
        this.list = list;
        this.searchList = new ArrayList<>(list);
    }

    public void setData(List<Product> list) {
        this.list = list;
        this.searchList =new ArrayList<>(list);
    }

    public void search(String keyword){
        searchList.clear();
        if (keyword.isEmpty()){
            searchList.addAll(list);
        } else {
            for(Product product: list){
                if (product.getName().toLowerCase().contains(keyword.toLowerCase())){
                    searchList.add(product);
                }
            }
        }
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_popular,parent,false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Product product = searchList.get(position);

        holder.productName.setText(product.getName());

        Glide.with(context)
                .load(product.getImage())
                .into(holder.productImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("product_id", product.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchList != null ? searchList.size() : 0;
    }

    public class Holder extends RecyclerView.ViewHolder{
        public ImageView productImage;
        public TextView productName;
        public Holder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);

        }
    }

}
