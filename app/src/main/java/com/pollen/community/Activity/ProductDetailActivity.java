package com.pollen.community.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.carteasy.v1.lib.Carteasy;
import com.google.gson.Gson;
import com.pollen.community.Database.CartModel.Cart;
import com.pollen.community.Database.CartModel.Favorite;
import com.pollen.community.Database.DataSource.CartRepository;
import com.pollen.community.Database.DataSource.FavoriteRepository;
import com.pollen.community.Database.Local.CartDataSource;
import com.pollen.community.Database.Local.CommunityRoomDatabase;
import com.pollen.community.Database.Local.FavoriteDatasource;
import com.pollen.community.R;
import com.pollen.community.helper.Common;
import com.pollen.community.helper.DialogHelper;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class ProductDetailActivity extends AppCompatActivity {
CarouselView carouselView;
    LinearLayout linear_count;
    int count=1, adult=1;
    TextView number, prodcount;
    ImageView add, minus;
    Carteasy carteasy;
    int finalprice;
  ImageView add_fav;
    private int uid;
    private String name;
    private String price;
    private String cover;
    private String description;
    private String rating;
    private int categoryId;
    private int restaurantId;
    private DialogHelper dialogHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        dialogHelper = new DialogHelper(ProductDetailActivity.this, getApplicationContext());

         uid = getIntent().getExtras().getInt("prodId");
       name = getIntent().getExtras().getString("prodname");
       price = getIntent().getExtras().getString("prodprice");
        cover = getIntent().getExtras().getString("prodcover");
       description = getIntent().getExtras().getString("proddescription");
       rating = getIntent().getExtras().getString("prodrating");
       categoryId = getIntent().getExtras().getInt("categoryId");
       restaurantId = getIntent().getExtras().getInt("restaurantId");


       ImageView product_share = findViewById(R.id.product_share);

        product_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              dialogHelper.shareProducts(cover, name, description);
            }
        });


        String[] sampleImages = {cover,cover};
        initCartDB();



        add = findViewById(R.id.plus1);
        minus = findViewById(R.id.minus1);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                number.setText(String.valueOf(count));
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count!=1){
                    count--;
                    number.setText(String.valueOf(count));
                }
            }

        });

       carouselView  = findViewById(R.id.carouselview);

       carouselView.setPageCount(sampleImages.length);

       carouselView.setImageListener(loaderPic(cover));

       TextView detaildescription = findViewById(R.id.detaildescription);

       TextView detailproductname = findViewById(R.id.detailproductname);

       TextView detailrating = findViewById(R.id.detailrating);

       TextView detailprice = findViewById(R.id.detailprice);

        detaildescription.setText(description);
        detailproductname.setText(name);
        detailrating.setText(rating);
        detailprice.setText(price);


        number = findViewById(R.id.prod_amount);


        


        LinearLayout add_prod = findViewById(R.id.add_prod);
        final ImageView add_fav = findViewById(R.id.add_fav);

        if (Common.favoriteRepository.isFavorite(uid) == 1){
            add_fav.setImageResource(R.drawable.ic_heart);
        }else {
            add_fav.setImageResource(R.drawable.heart);

        }

        add_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.favoriteRepository.isFavorite(uid) != 1){
                     addOrRemoveFavorite(uid, true);
                    add_fav.setImageResource(R.drawable.ic_heart);
                    Toast.makeText(ProductDetailActivity.this, "Ajouter à la liste Favoris", Toast.LENGTH_SHORT).show();
                }else {
                    addOrRemoveFavorite(uid, false);
                    add_fav.setImageResource(R.drawable.heart);
                }
            }
        });



        add_prod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetailActivity.this);
                builder.setMessage("Ajouter au panier ???").setCancelable(false).setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        try{
                            Cart cartItem = new Cart();
                            cartItem.name = name;
                            cartItem.price = Math.round(count * Integer.parseInt(price));
                            cartItem.id = uid;
                            cartItem.link = cover;
                            cartItem.amount = count;
                            // Add to DB

                            Common.cartRepository.insertToCart(cartItem);

                            Log.d("CART_TEST_DEBUG", new Gson().toJson(cartItem));

                            Toast.makeText(ProductDetailActivity.this, "Ajouter au panier.", Toast.LENGTH_SHORT).show();

                        }catch (Exception ex){
                            Toast.makeText(ProductDetailActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                        .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });







    }

    private void addOrRemoveFavorite(int uid, boolean isAdd) {
        Favorite favorite = new Favorite();
        favorite.id =  uid;
        favorite.link = cover;
        favorite.name = name;
        favorite.price = price;
        favorite.restaurantId = restaurantId;
        favorite.categoryId = categoryId;

        if (isAdd){
            Common.favoriteRepository.insertFav(favorite);
        }else{
            Common.favoriteRepository.delete(favorite);
        }
    }



    private void initCartDB() {
        Common.CommunityRoomDatabase = CommunityRoomDatabase.getInstance(this);
        Common.cartRepository = CartRepository.getInstance(CartDataSource.getInstance(Common.CommunityRoomDatabase.cartDAO()));
        Common.favoriteRepository = FavoriteRepository.getInstance(FavoriteDatasource.getInstance(Common.CommunityRoomDatabase.favoriteDAO()));
    }



    private ImageListener loaderPic(final String cover) {
        ImageListener imageListener = new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                Picasso.get().load(cover).into(imageView);
            }
        };
        return imageListener;
    }


}
