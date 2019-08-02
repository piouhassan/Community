package com.pollen.community.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.pollen.community.Adapter.RestaurantAdapter;
import com.pollen.community.Database.RestaurantDbHelper;
import com.pollen.community.Models.Restaurant;
import com.pollen.community.R;
import com.pollen.community.UrlConfig.AppConfig;
import com.pollen.community.helper.DialogHelper;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.accountkit.internal.AccountKitController.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class RestaurantFragment extends Fragment {

     View v;
     private RecyclerView recyclerView;
     private List<Restaurant> restaurantList ;
     RecyclerView myrecyclerview;
     JsonArrayRequest request;
     RequestQueue requestQueue;
     private RestaurantDbHelper db;
    private ProgressDialog pDialog;
      DialogHelper dialogHelper;

    public RestaurantFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
              v =   inflater.inflate(R.layout.fragment_restaurant, container, false);
                    myrecyclerview = (RecyclerView) v.findViewById(R.id.restaurant_recycler);
                    return  v;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Progress dialog
        db = new RestaurantDbHelper(getApplicationContext());

        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);
        pDialog.setMessage("Chargement de la liste en cours");
        restaurantList = new ArrayList<>();

        dialogHelper = new DialogHelper(getActivity(), getApplicationContext());

        jsonRequest();
    }


    private  void jsonRequest(){
        showDialog();

        request = new JsonArrayRequest(AppConfig.URL_RESTAURANT, new Response.Listener<JSONArray>() {
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
                        Restaurant restaurant = new Restaurant();
                        restaurant.setName(jsonObject.getString("name"));
                        restaurant.setAddress(jsonObject.getString("address"));
                        restaurant.setRating(jsonObject.getInt("stars"));
                        restaurant.setLogo(jsonObject.getString("logo"));
                        restaurant.setDescription(jsonObject.getString("description"));
                        restaurant.setPhone(jsonObject.getString("phone"));
                        restaurant.setUid(jsonObject.getInt("id"));
                        restaurantList.add(restaurant);

                    }
                    catch (JSONException e) {
                        hideDialog();
                        e.printStackTrace();
                    }
                }
            setuprecyclerview(restaurantList);
            }
        }
         , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                dialogHelper.internetWarning();
            }
        });

        requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }

    private void setuprecyclerview(List<Restaurant> restaurantList) {
        RestaurantAdapter restaurantAdapter = new RestaurantAdapter(getContext(), restaurantList);
        myrecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        myrecyclerview.setAdapter(restaurantAdapter);
    }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
