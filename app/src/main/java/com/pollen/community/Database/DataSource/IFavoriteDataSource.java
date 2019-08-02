package com.pollen.community.Database.DataSource;


import com.pollen.community.Database.CartModel.Favorite;

import java.util.List;

import io.reactivex.Flowable;

public interface IFavoriteDataSource {
    Flowable<List<Favorite>> getFavItems();
    int isFavorite(int itemId);
    void insertFav(Favorite... favorites);
    void  delete(Favorite favorite);
}
