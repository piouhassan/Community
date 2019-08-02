package com.pollen.community.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.pollen.community.Database.UserDbHelper;
import com.pollen.community.Models.Comment;
import com.pollen.community.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import static com.facebook.accountkit.internal.AccountKitController.getApplicationContext;

public class CommentRestaurantAdapter extends RecyclerView.Adapter<CommentRestaurantAdapter.MyViewHolder> {

    private Context mContext;
    List<Comment> mData;
    public CommentRestaurantAdapter(Context mContext, List<Comment> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.resto_detail_review,viewGroup, false);

        final  MyViewHolder viewHolder =  new MyViewHolder(view);


        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.comm_user.setText(new StringBuilder(mData.get(i).getFirstname()).append(" ").append(mData.get(i).getLastname()).toString());
        myViewHolder.commentaire.setText(mData.get(i).getCommentaire());
        myViewHolder.comment_rating.setRating(Float.parseFloat(mData.get(i).getRating()));
        Picasso.get().load(mData.get(i).getUserCover()).into(myViewHolder.comm_profil);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public  static class MyViewHolder extends RecyclerView.ViewHolder {

        CircularImageView comm_profil;
        TextView comm_user, commentaire;
        RatingBar  comment_rating;
        ImageView comment_more;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            comm_profil = itemView.findViewById(R.id.comm_profil);
            commentaire = itemView.findViewById(R.id.commentaire);
            comm_user = itemView.findViewById(R.id.comm_user);
            comment_rating = itemView.findViewById(R.id.comment_rating);
            comment_more = itemView.findViewById(R.id.comment_more);


        }
    }

}
