package com.pollen.community.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pollen.community.Database.UserDbHelper;
import com.pollen.community.R;
import com.pollen.community.UrlConfig.AppConfig;
import com.pollen.community.helper.DialogHelper;

import java.util.HashMap;

public class RestaurantDetailActivity extends AppCompatActivity {
    DialogHelper dialogHelper;
View view;
    private UserDbHelper db;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        final String name = getIntent().getExtras().getString("retaurant_name");
        String description = getIntent().getExtras().getString("retaurant_description");
        String logo = getIntent().getExtras().getString("retaurant_logo");
        final String phone = getIntent().getExtras().getString("retaurant_phone");
        int rating = getIntent().getExtras().getInt("retaurant_rating");
        String address = getIntent().getExtras().getString("retaurant_address");
        final int uid = getIntent().getExtras().getInt("retaurant_id");


        ImageView resto_logo = findViewById(R.id.resto_logo);
        TextView restaurant_name = findViewById(R.id.restaurant_name);
        RatingBar resto_rating = findViewById(R.id.resto_rating);
        TextView resto_description = findViewById(R.id.resto_description);
        TextView review = findViewById(R.id.review);


        Glide.with(this).load(logo).into(resto_logo);
        restaurant_name.setText(name);
        resto_description.setText(description);
        resto_rating.setRating(rating);



        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentreview = new Intent(RestaurantDetailActivity.this, ReviewActivity.class);
                  intentreview.putExtra("uid", uid);
                  startActivity(intentreview);
            }
        });


        String url = AppConfig.URL_COUNT_COMMENT;




        TextView dial = findViewById(R.id.call_restaurant);

        dial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:"+phone));
                startActivity(intent);
            }
        });

        TextView go_to_category = findViewById(R.id.go_to_category);
        go_to_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent(RestaurantDetailActivity.this, CategoryActivity.class);
                inten.putExtra("resto_id", uid);
                inten.putExtra("resto_name", name);
                startActivity(inten);
            }
        });
        db = new UserDbHelper(getApplicationContext());
        final HashMap<String, String> user = db.getUserDetails();
        final String telephone = user.get("phone");
        final String username = user.get("username");
        final String avatar = user.get("avatar");

        dialogHelper = new DialogHelper(RestaurantDetailActivity.this, getApplicationContext());
        FloatingActionButton note = findViewById(R.id.note);

        note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogHelper.setRating(username,telephone,avatar,uid);
            }
        });



    }

    private void Comment(String comment) {
        Toast.makeText(this, comment, Toast.LENGTH_SHORT).show();
    }



    }




