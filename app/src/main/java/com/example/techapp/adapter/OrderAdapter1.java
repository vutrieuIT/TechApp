package com.example.techapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.example.techapp.AsyncTack.DeleteOrderAsync;
import com.example.techapp.Constant;
import com.example.techapp.R;
import com.example.techapp.database.MyDatabase;
import com.example.techapp.database.Order;
import com.example.techapp.model.Order1;

import java.util.List;

public class OrderAdapter1 extends RecyclerView.Adapter<OrderAdapter1.Holder> {
    Context context;
    List<Order1> list;
    private OnItemDeleteListener deleteListener;

    public interface OnItemDeleteListener {
        void onItemDeleted(Long orderId);
    }

    public void setOnItemDeleteListener(OnItemDeleteListener listener) {
        this.deleteListener = listener;
    }

    public OrderAdapter1(Context context, List<Order1> list) {
        this.context = context;
        this.list = list;
    }

    public void setData(List<Order1> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, null);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Order1 order = list.get(position);
        holder.productName.setText(order.getProductName());
        holder.amount.setText(String.valueOf(order.getQuantity()));
        holder.totalPrice.setText(String.valueOf(order.getTotalMoney()));

        Glide.with(context)
                .load(order.getImage())
                .into(holder.imgProduct);

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(holder.getAdapterPosition());
                notifyDataSetChanged();
                if (deleteListener != null){
                    deleteListener.onItemDeleted(order.getOrderId());
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }


    public class Holder extends RecyclerView.ViewHolder {
        public ImageView imgProduct;
        public TextView productName, amount, totalPrice;
        public ImageButton btnDelete;

        public Holder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            productName = itemView.findViewById(R.id.productName);
            amount = itemView.findViewById(R.id.amount);
            totalPrice = itemView.findViewById(R.id.totalPrice);

            btnDelete = itemView.findViewById(R.id.btnDelete);

        }
    }
}
