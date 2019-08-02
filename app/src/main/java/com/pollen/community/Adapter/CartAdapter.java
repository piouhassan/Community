package com.pollen.community.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.pollen.community.Database.CartModel.Cart;
import com.pollen.community.R;
import com.pollen.community.helper.Common;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CardViewHolder> {

    Context context;
    List<Cart> cartList;

    public CartAdapter(Context context, List<Cart> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup,  int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_cart,  viewGroup, false);
        return new CardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CardViewHolder cardViewHolder, final int i) {
        Picasso.get().load(cartList.get(i).link).into(cardViewHolder.cart_image);
        cardViewHolder.cart_amount.setNumber(String.valueOf(cartList.get(i).amount));
        cardViewHolder.cart_name.setText(new StringBuilder(String.valueOf(cartList.get(i).name)).append(" x ").append(cartList.get(i).amount).toString());
        cardViewHolder.cart_price.setText(String.valueOf(cartList.get(i).price));


        final int priceofOne = cartList.get(i).price / cartList.get(i).amount;

     cardViewHolder.cart_amount.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
         @Override
         public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
              Cart cart = cartList.get(i);
              cart.amount = newValue;
              cart.price = Math.round(priceofOne * newValue);

             Common.cartRepository.updateCart(cart);

             cardViewHolder.cart_price.setText(String.valueOf(cartList.get(i).price));
         }
     });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }



    public  class CardViewHolder extends RecyclerView.ViewHolder{
          ImageView cart_image;
          TextView cart_name, cart_price;
          ElegantNumberButton cart_amount;
        public   LinearLayout view_forgrounder;
         public  RelativeLayout view_backgrounder;


        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            cart_image = itemView.findViewById(R.id.cart_image);
            cart_name = itemView.findViewById(R.id.cart_name);
            cart_price = itemView.findViewById(R.id.cart_price);
            cart_amount = itemView.findViewById(R.id.cart_amount);
            view_backgrounder = itemView.findViewById(R.id.view_backgrounder);
            view_forgrounder = itemView.findViewById(R.id.view_forgrounder);


        }
    }


    public void removeItem(int position) {
        cartList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Cart item , int position){
        cartList.add(position,item);
        notifyItemInserted(position);
    }

}
