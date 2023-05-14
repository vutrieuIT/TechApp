package com.example.techapp.AsyncTack;

import android.os.AsyncTask;

import com.example.techapp.database.Order;

public class DeleteOrderAsync extends AsyncTask<Integer, Void, Void> {
    private Order.OrderDAO dao;

    public DeleteOrderAsync(Order.OrderDAO dao) {
        this.dao = dao;
    }

    @Override
    protected Void doInBackground(Integer... integers) {
        dao.deleteOrderById(integers[0]);
        return null;
    }
}
