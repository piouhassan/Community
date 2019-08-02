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

import com.pollen.community.Database.CartModel.Cart;
import com.pollen.community.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder> {

    Context context;
    List<Cart> cartList;

    public OrderDetailAdapter(Context context, List<Cart> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_order_detail,  viewGroup, false);
        return new OrderDetailViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderDetailViewHolder cardViewHolder, final int i) {
        Picasso.get().load(cartList.get(i).link).into(cardViewHolder.cart_image);
        cardViewHolder.cart_name.setText(new StringBuilder(String.valueOf(cartList.get(i).name)).append(" x ").append(cartList.get(i).amount).toString());
        cardViewHolder.cart_price.setText(String.valueOf(cartList.get(i).price));
        final int priceofOne = cartList.get(i).price / cartList.get(i).amount;

    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }



    public  class OrderDetailViewHolder extends RecyclerView.ViewHolder{
        ImageView cart_image;
        TextView cart_name, cart_price;
        public LinearLayout view_forgrounder;
        public RelativeLayout view_backgrounder;


        public OrderDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            cart_image = itemView.findViewById(R.id.cart_image);
            cart_name = itemView.findViewById(R.id.cart_name);
            cart_price = itemView.findViewById(R.id.cart_price);
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
