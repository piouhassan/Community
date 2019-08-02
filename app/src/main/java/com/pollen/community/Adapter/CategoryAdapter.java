package com.pollen.community.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pollen.community.Activity.ProductsActivity;
import com.pollen.community.Models.Category;
import com.pollen.community.R;

import java.util.List;

public class CategoryAdapter extends PagerAdapter {

    private List<Category>  categories;
    LayoutInflater layoutInflater;
    Context mcontext;
    RequestOptions option;
    public CategoryAdapter(List<Category> categories, Context mcontext) {
        this.categories = categories;
        this.mcontext =  mcontext;
        option = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);

    }



    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view.equals(o);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {
         layoutInflater =  LayoutInflater.from(mcontext);
         final View view = layoutInflater.inflate(R.layout.item_category,container,false);


        ImageView CategoryImage;
        TextView category;

        CategoryImage = view.findViewById(R.id.CategoryImage);
        category = view.findViewById(R.id.category);


        Glide.with(mcontext).load(categories.get(position).getCategory_url()).apply(option).into(CategoryImage);
         category.setText(categories.get(position).getName());


        Button categorybox = view.findViewById(R.id.categorybox);
        categorybox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toproduct = new Intent(mcontext, ProductsActivity.class);
                toproduct.putExtra("categoryId", categories.get(position).getUid());
                toproduct.putExtra("restaurantId", categories.get(position).getRestaurantId());
                mcontext.startActivity(toproduct);
            }
        });

         container.addView(view , 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
  container.removeView((View)object);

    }
}
