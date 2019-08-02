package com.pollen.community.Database.CartModel;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import io.reactivex.annotations.NonNull;

@Entity(tableName = "Cart")
public class Cart {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id" )
    public int id;

    @ColumnInfo(name = "name" )
    public String name;

    @ColumnInfo(name = "link" )
    public String link;

    @ColumnInfo(name = "amount" )
    public int amount;

    @ColumnInfo(name = "price" )
    public int price;
}
