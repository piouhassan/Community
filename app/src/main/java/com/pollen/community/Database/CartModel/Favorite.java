package com.pollen.community.Database.CartModel;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import io.reactivex.annotations.NonNull;

@Entity(tableName = "Favorite")
public class Favorite {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id" )
    public int id;

    @ColumnInfo(name = "name" )
    public String name;

    @ColumnInfo(name = "link" )
    public String link;

    @ColumnInfo(name = "price" )
    public String price;

    @ColumnInfo(name = "categoryId" )
    public int categoryId;

    @ColumnInfo(name = "restaurantId" )
    public int restaurantId;

}
