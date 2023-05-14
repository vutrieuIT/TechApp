package com.example.techapp.AsyncTack;

import android.os.AsyncTask;

import com.example.techapp.database.Order;

public class InsertOrderAsyncTack extends AsyncTask<Order, Void, Void> {
    private Order.OrderDAO orderDAO;

    public InsertOrderAsyncTack(Order.OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    @Override
    protected Void doInBackground(Order... orders) {
        orderDAO.insert(orders[0]);
        return null;
    }
}
