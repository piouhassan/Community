package com.pollen.community.Database.DataSource;

import com.pollen.community.Database.CartModel.Cart;

import java.util.List;

import io.reactivex.Flowable;

public interface ICartDataSource {


    Flowable<List<Cart>> getCartItems();

    Flowable<List<Cart>> getCartItemById(int cartItemId);

    int countCartItems();

    int sumPrice();

    void emptyCart();

    void insertToCart(Cart... carts);

    void updateCart(Cart... carts);

    void deleteCartItem(Cart cart);
}

