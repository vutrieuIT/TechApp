package com.example.techapp.AsyncTack;

import android.os.AsyncTask;

import com.example.techapp.database.Order;

import java.util.List;

public class GetAllOrderAsync extends AsyncTask<Void, Void, List<Order>> {
    private Order.OrderDAO dao;

    public GetAllOrderAsync(Order.OrderDAO dao) {
        this.dao = dao;
    }

    @Override
    protected List<Order> doInBackground(Void... voids) {
        List<Order> list = dao.getALl();
        return list;
    }
}
