package com.example.bbangmap.map.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Bakery.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract BakeryDao bakeryDao();
}
