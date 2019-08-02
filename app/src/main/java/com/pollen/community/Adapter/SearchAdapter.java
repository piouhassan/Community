package com.pollen.community.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.pollen.community.Activity.ProductDetailActivity;
import com.pollen.community.Models.Products;
import com.pollen.community.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {

    private Context mContext;
    List<Products> mData;
    RequestOptions option;
    public SearchAdapter(Context mContext, List<Products> mData) {
        this.mContext = mContext;
        this.mData = mData;

        option = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.search_result,viewGroup, false);

        final MyViewHolder viewHolder =  new MyViewHolder(view);
             viewHolder.seeprod.setOnClickListener(new View.OnClickListener() {
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
        myViewHolder.prod_nale.setText(mData.get(i).getName());
        myViewHolder.search_price.setText(mData.get(i).getPrice());
        Picasso.get().load(mData.get(i).getCover()).into(myViewHolder.prod_im);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public  static class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView prod_im;
        private TextView prod_nale, search_price;
        private RelativeLayout seeprod;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            prod_im = itemView.findViewById(R.id.prod_im);
            prod_nale = itemView.findViewById(R.id.prod_nale);
            search_price = itemView.findViewById(R.id.search_price);
            seeprod = itemView.findViewById(R.id.seeprod);

        }
    }
}
