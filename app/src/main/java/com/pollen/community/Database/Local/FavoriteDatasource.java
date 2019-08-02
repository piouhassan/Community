package com.pollen.community.Database.Local;

import com.pollen.community.Database.CartModel.Favorite;
import com.pollen.community.Database.DataSource.IFavoriteDataSource;

import java.util.List;

import io.reactivex.Flowable;

public class FavoriteDatasource implements IFavoriteDataSource {

    private  FavoriteDAO favoriteDAO;
    private static FavoriteDatasource instance;

    public FavoriteDatasource(FavoriteDAO favoriteDAO) {
        this.favoriteDAO = favoriteDAO;
    }


    public  static FavoriteDatasource getInstance(FavoriteDAO favoriteDAO){
        if (instance == null)
            instance = new FavoriteDatasource(favoriteDAO);
        return instance;
    }

    @Override
    public Flowable<List<Favorite>> getFavItems() {
        return favoriteDAO.getFavItems();
    }

    @Override
    public int isFavorite(int itemId) {
        return favoriteDAO.isFavorite(itemId);
    }

    @Override
    public void insertFav(Favorite... favorites) {
        favoriteDAO.insertFav(favorites);
    }

    @Override
    public void delete(Favorite favorite) {
       favoriteDAO.delete(favorite);
    }
}
