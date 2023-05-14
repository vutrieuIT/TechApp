package com.example.techapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.techapp.R;
import com.example.techapp.model.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.Holder>{
    Context context;
    List<Category> list;

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int id);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public CategoryAdapter(Context context, List<Category> list) {
        this.context = context;
        this.list = list;
    }

    public void setData(List<Category> list){
        this.list = list;
    }



    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, null);
        Holder holder = new Holder(view, mListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Category category = list.get(position);
        holder.name.setText(category.getName());
        // load ảnh
        Glide.with(context)
                .load(category.getImage())
                .into(holder.image);

    }

    @Override
    public int getItemCount() {
        return list == null ? 0:list.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        public TextView name;
        public ImageView image;

        public Holder(@NonNull View itemView,final OnItemClickListener listener) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.categoryName);
            image = itemView.findViewById(R.id.categoryImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int pos =  getAdapterPosition();
                        int id = list.get(pos).getId();
                        listener.onItemClick(id);
                    }
                }
            });
        }
    }
}
