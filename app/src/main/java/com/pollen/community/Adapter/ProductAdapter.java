package com.pollen.community.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.pollen.community.Activity.ProductDetailActivity;
import com.pollen.community.Models.Products;
import com.pollen.community.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    private Context mContext;
    List<Products> mData;
    RequestOptions option;
    public ProductAdapter(Context mContext, List<Products> mData) {
        this.mContext = mContext;
        this.mData = mData;

        option = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.item_product,viewGroup, false);

        final MyViewHolder viewHolder =  new MyViewHolder(view);
             viewHolder.product_add.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     Intent intent = new Intent(mContext,ProductDetailActivity.class);
                     intent.putExtra("prodId", mData.get(viewHolder.getAdapterPosition()).getUid());
                     intent.putExtra("prodname", mData.get(viewHolder.getAdapterPosition()).getName());
                     intent.putExtra("prodcover", mData.get(viewHolder.getAdapterPosition()).getCover());
                     intent.putExtra("proddescription", mData.get(viewHolder.getAdapterPosition()).getDescription());
                     intent.putExtra("prodprice", mData.get(viewHolder.getAdapterPosition()).getPrice());
                     intent.putExtra("prodrating", mData.get(viewHolder.getAdapterPosition()).getRating());
                     intent.putExtra("categoryId", mData.get(viewHolder.getAdapterPosition()).getCategoryId());
                     intent.putExtra("restaurantId", mData.get(viewHolder.getAdapterPosition()).getRestaurantId());
                     intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                     mContext.startActivity(intent);
                 }
             });
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.prod_name.setText(mData.get(i).getName());
        myViewHolder.prod_price.setText(mData.get(i).getPrice());
        myViewHolder.prod_rating.setText(mData.get(i).getRating());
        Picasso.get().load(mData.get(i).getCover()).into(myViewHolder.prod_cover);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public  static class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView prod_cover;
        private TextView prod_name;
        private TextView prod_price;
        private TextView prod_rating;
        private  LinearLayout product_add;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            prod_name = itemView.findViewById(R.id.prod_name);
            prod_price = itemView.findViewById(R.id.prod_price);
            prod_rating = itemView.findViewById(R.id.prod_rating);
            prod_cover = itemView.findViewById(R.id.prod_cover);
            product_add = itemView.findViewById(R.id.product_add);
        }
    }
}
