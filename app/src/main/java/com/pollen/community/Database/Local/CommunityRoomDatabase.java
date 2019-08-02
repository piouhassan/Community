package com.pollen.community.Database.Local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import com.pollen.community.Database.CartModel.Cart;
import com.pollen.community.Database.CartModel.Favorite;

@Database(entities = {Cart.class, Favorite.class}, version = 1)
public abstract class CommunityRoomDatabase extends RoomDatabase {
    public abstract CartDAO cartDAO();
    public abstract FavoriteDAO favoriteDAO();
    private  static CommunityRoomDatabase instance;

    public  static CommunityRoomDatabase getInstance(Context context)
    {
        if (instance == null)
            instance = Room.databaseBuilder(context, CommunityRoomDatabase.class, "Community_DB")
                    .allowMainThreadQueries()
                    .build();
      return   instance;

    }
}