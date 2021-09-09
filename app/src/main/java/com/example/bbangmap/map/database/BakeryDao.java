package com.example.bbangmap.map.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BakeryDao {
    @Query("SELECT * FROM Bakery")
    List<Bakery> getAll();

    @Query("DELETE FROM Bakery")
    void deleteAll();

    @Insert
    void insert(Bakery bakery);

    @Update
    void update(Bakery bakery);

    @Delete
    void delete(Bakery bakery);
}
