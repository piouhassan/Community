package com.pollen.community.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.pollen.community.Adapter.ProductAdapter;
import com.pollen.community.Database.DataSource.CartRepository;
import com.pollen.community.Database.DataSource.FavoriteRepository;
import com.pollen.community.Database.Local.CartDataSource;
import com.pollen.community.Database.Local.CommunityRoomDatabase;
import com.pollen.community.Database.Local.FavoriteDatasource;
import com.pollen.community.Models.Products;
import com.pollen.community.R;
import com.pollen.community.UrlConfig.AppConfig;
import com.pollen.community.helper.Common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ProductsActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private List<Products> productsList ;
    RecyclerView myrecyclerview;
    JsonArrayRequest request;
    RequestQueue requestQueue;
    TextView cart_count;
    RelativeLayout badge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        final  int uid = getIntent().getExtras().getInt("categoryId");
        final  int restaurantId = getIntent().getExtras().getInt("restaurantId");
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Chargement de la liste en cours");

        myrecyclerview = (RecyclerView) findViewById(R.id.products_recycler);

        productsList = new ArrayList<>();

        jsonRequest(uid, restaurantId);

        cart_count = findViewById(R.id.cart_count);
        badge = findViewById(R.id.badge);
        initCartDB();
        updateCartCount();

    }

    private void initCartDB() {
        Common.CommunityRoomDatabase = CommunityRoomDatabase.getInstance(this);
        Common.cartRepository = CartRepository.getInstance(CartDataSource.getInstance(Common.CommunityRoomDatabase.cartDAO()));
        Common.favoriteRepository = FavoriteRepository.getInstance(FavoriteDatasource.getInstance(Common.CommunityRoomDatabase.favoriteDAO()));
    }

    private void updateCartCount() {
        if (cart_count == null) return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Common.cartRepository.countCartItems() == 0)
                    badge.setVisibility(View.INVISIBLE);
                else
                {
                    badge.setVisibility(View.VISIBLE);
                    cart_count.setText(String.valueOf(Common.cartRepository.countCartItems()));
                }
            }
        });
    }

    private  void jsonRequest(final int uid, final int restaurantId){
        showDialog();

        request = new JsonArrayRequest(AppConfig.URL_PRODUCTS+uid, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                if (response.length() == 0){
                    hideDialog();
                    Toast.makeText(getApplicationContext(), "Liste  vide", Toast.LENGTH_SHORT).show();
                }
                for (int i = 0 ; i < response.length(); i++){
                    hideDialog();
                    try{
                        jsonObject = response.getJSONObject(i);
                        Products products = new Products();
                        products.setName(jsonObject.getString("name"));
                        products.setPrice(jsonObject.getString("price"));
                        products.setCover(jsonObject.getString("cover"));
                        products.setDescription(jsonObject.getString("description"));
                        products.setRating(jsonObject.getString("rating"));
                        products.setUid(jsonObject.getInt("id"));
                        products.setCategoryId(uid);
                        products.setRestaurantId(restaurantId);
                        productsList.add(products);
                    }
                    catch (JSONException e) {
                        hideDialog();
                        e.printStackTrace();
                    }
                }
                setuprecyclerview(productsList);
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }

    private void setuprecyclerview(List<Products> productsList) {
        ProductAdapter productAdapter = new ProductAdapter(getApplicationContext(), productsList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        myrecyclerview.setLayoutManager(gridLayoutManager);
        myrecyclerview.setAdapter(productAdapter);

    }



    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartCount();
    }
}
