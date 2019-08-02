package com.pollen.community.Adapter;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.pollen.community.Activity.RestaurantDetailActivity;
import com.pollen.community.Models.Restaurant;
import com.pollen.community.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;


public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.MyViewHolder> {

   private Context mContext;
    List<Restaurant> mData;
RequestOptions option;
    public RestaurantAdapter(Context mContext, List<Restaurant> mData) {
        this.mContext = mContext;
        this.mData = mData;

        option = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
     View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.rest_card,viewGroup, false);

        final  MyViewHolder viewHolder =  new MyViewHolder(view);

        viewHolder.restaurant_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RestaurantDetailActivity.class);
                   intent.putExtra("retaurant_name", mData.get(viewHolder.getAdapterPosition()).getName());
                   intent.putExtra("retaurant_logo", mData.get(viewHolder.getAdapterPosition()).getLogo());
                   intent.putExtra("retaurant_phone", mData.get(viewHolder.getAdapterPosition()).getPhone());
                   intent.putExtra("retaurant_description", mData.get(viewHolder.getAdapterPosition()).getDescription());
                   intent.putExtra("retaurant_rating", mData.get(viewHolder.getAdapterPosition()).getRating());
                   intent.putExtra("retaurant_address", mData.get(viewHolder.getAdapterPosition()).getAddress());
                   intent.putExtra("retaurant_id", mData.get(viewHolder.getAdapterPosition()).getUid());
                   mContext.startActivity(intent);
            }
        });
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.name.setText(mData.get(i).getName());
        myViewHolder.resto_city.setText(mData.get(i).getAddress());
        myViewHolder.numero.setText(mData.get(i).getPhone());
        myViewHolder.rating.setRating(mData.get(i).getRating());

        Glide.with(mContext).load(mData.get(i).getLogo()).apply(option).into(myViewHolder.image);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public  static class MyViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout restaurant_id;
        private CircularImageView image;
        private TextView name;
        private TextView numero;
        private RatingBar rating;
        private TextView  type;
        private TextView time;
        private TextView resto_city;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        image = itemView.findViewById(R.id.resto_image);
        name = itemView.findViewById(R.id.resto_name);
        numero = itemView.findViewById(R.id.resto_numero);
        rating = itemView.findViewById(R.id.resto_rating);
        type = itemView.findViewById(R.id.resto_type);
        time = itemView.findViewById(R.id.resto_time);
        resto_city = itemView.findViewById(R.id.resto_city);
        restaurant_id = itemView.findViewById(R.id.restaurant_id);
    }
}

}
