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

import com.pollen.community.Database.CartModel.Favorite;
import com.pollen.community.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

   Context context;
   List<Favorite> favoriteList;


    public FavoriteAdapter(Context context, List<Favorite> favoriteList) {
        this.context = context;
        this.favoriteList = favoriteList;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

       View itemView = LayoutInflater.from(context).inflate(R.layout.item_favoris,viewGroup,false);
        return new FavoriteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int i) {
        Picasso.get().load(favoriteList.get(i).link).into(holder.fav_image);
        holder.fav_name.setText(favoriteList.get(i).name);
        holder.fav_price.setText(favoriteList.get(i).price);
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }



    public   class FavoriteViewHolder extends  RecyclerView.ViewHolder{

        ImageView fav_image;
        TextView fav_name, fav_price;
        public RelativeLayout view_background;
        public LinearLayout view_forground;

         public FavoriteViewHolder(View itemView) {
             super(itemView);
             fav_image = itemView.findViewById(R.id.favt_image);
             fav_name = itemView.findViewById(R.id.fav_name);
             fav_price = itemView.findViewById(R.id.fav_price);
             view_background = itemView.findViewById(R.id.view_background);
             view_forground = itemView.findViewById(R.id.view_foreground);

         }
     }

    public void removeItem(int position) {
         favoriteList.remove(position);
         notifyItemRemoved(position);
    }

    public void restoreItem(Favorite item , int position){
       favoriteList.add(position,item);
       notifyItemInserted(position);
    }

}
