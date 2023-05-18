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

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.Holder> {
    Context context;
    List<Order> list;

    private OnItemDeleteListener deleteListener;

    public interface OnItemDeleteListener {
        void onItemDeleted();
    }

    public void setOnItemDeleteListener(OnItemDeleteListener listener) {
        this.deleteListener = listener;
    }

    public OrderAdapter(Context context, List<Order> list) {
        this.context = context;
        this.list = list;
    }

    public void setData(List<Order> list) {
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
        Order order = list.get(position);
        holder.productName.setText(order.getName());
        holder.amount.setText(String.valueOf(order.getAmount()));
        holder.totalPrice.setText(String.valueOf(order.getTotalPrice()));

        Glide.with(context)
                .load(order.getImage())
                .into(holder.imgProduct);

        MyDatabase myDatabase = Room.databaseBuilder(context, MyDatabase.class, Constant.database_name).build();
        Order.OrderDAO dao = myDatabase.orderDAO();

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DeleteOrderAsync(dao).execute(order.getId());
                list.remove(holder.getAdapterPosition());
                notifyDataSetChanged();
                if (deleteListener != null){
                    deleteListener.onItemDeleted();
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
