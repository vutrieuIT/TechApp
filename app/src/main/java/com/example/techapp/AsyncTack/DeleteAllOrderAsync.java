package com.example.techapp.AsyncTack;

import android.os.AsyncTask;

import com.example.techapp.database.Order;

public class DeleteAllOrderAsync extends AsyncTask<Void, Void, Void> {

    Order.OrderDAO dao;

    public DeleteAllOrderAsync(Order.OrderDAO dao) {
        this.dao = dao;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        dao.deleteAllOrders();
        return null;
    }
}
