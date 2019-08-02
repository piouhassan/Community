package com.pollen.community.Activity;

import android.animation.ArgbEvaluator;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.pollen.community.Adapter.CategoryAdapter;
import com.pollen.community.Database.DataSource.CartRepository;
import com.pollen.community.Database.DataSource.FavoriteRepository;
import com.pollen.community.Database.Local.CartDataSource;
import com.pollen.community.Database.Local.CommunityRoomDatabase;
import com.pollen.community.Database.Local.FavoriteDatasource;
import com.pollen.community.Models.Category;
import com.pollen.community.R;
import com.pollen.community.UrlConfig.AppConfig;
import com.pollen.community.helper.Common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    ViewPager viewPager;
    CategoryAdapter adapter;
    Integer[]  color = null;
    ArgbEvaluator evaluator = new ArgbEvaluator();
    List<Category> categories;

    JsonArrayRequest request;
    RequestQueue requestQueue;
    private ProgressDialog pDialog;
    TextView cart_count;
    RelativeLayout badge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        final int uid = getIntent().getExtras().getInt("resto_id");
        final String restonames = getIntent().getExtras().getString("resto_name");

        cart_count = findViewById(R.id.cart_count);
        badge = findViewById(R.id.badge);



        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Chargement de la liste en cours");
        categories = new ArrayList<>();
        CategoryjsonRequest(uid);
        initCartDB();
        updateCartCount();
    }

    private  void CategoryjsonRequest(final int uid) {
        showDialog();

        request = new JsonArrayRequest(AppConfig.URL_CATEGORY+uid, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                    JSONObject jsonObject = null;
                if (response.length() == 0){
                    hideDialog();
                    Toast.makeText(getApplicationContext(), "Liste  vide", Toast.LENGTH_SHORT).show();
                }
                for (int i = 0 ; i < response.length(); i++){
                    hideDialog();
                    try {
                        jsonObject = response.getJSONObject(i);
                        Category category = new Category();
                        category.setName(jsonObject.getString("name"));
                        category.setCategory_url(jsonObject.getString("image"));
                        category.setUid(jsonObject.getInt("id"));
                        category.setRestaurantId(uid);
                        categories.add(category);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

         restContent();

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Toast.makeText(getApplicationContext(),"Probleme avec votre connexion Internet", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue = Volley.newRequestQueue(CategoryActivity.this);
        requestQueue.add(request);
    }

    private void restContent() {
        adapter = new CategoryAdapter(categories, this);
        viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(30, 0 ,30 , 0);

        Integer[] colors_temp = {
                getResources().getColor(R.color.transparant),
        };

        color = colors_temp;
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                if (i < (adapter.getCount() - 1) && i < (color.length - 1)){
                    viewPager.setBackgroundColor((Integer)evaluator.evaluate(v, color[i], color[i + 1]));
                }else{
                    viewPager.setBackgroundColor(color[color.length - 1]);
                }
            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
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


}
