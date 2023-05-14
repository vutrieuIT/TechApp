package com.example.techapp.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Order.class}, version = 1)
public abstract class MyDatabase extends RoomDatabase {
    public abstract Order.OrderDAO orderDAO();
}
