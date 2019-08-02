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
import com.pollen.community.Adapter.OrderAdapter;
import com.pollen.community.Database.UserDbHelper;
import com.pollen.community.Models.Order;
import com.pollen.community.R;
import com.pollen.community.UrlConfig.AppConfig;
import com.pollen.community.helper.DialogHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.facebook.accountkit.internal.AccountKitController.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransFragment extends Fragment {


    public TransFragment() {
        // Required empty public constructor
    }

    View v;
    private RecyclerView recyclerView;
    private List<Order> orderList ;
    RecyclerView myrecyclerview;
    JsonArrayRequest request;
    RequestQueue requestQueue;
    private ProgressDialog pDialog;
    DialogHelper dialogHelper;
    String userphone;
    private UserDbHelper db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v =   inflater.inflate(R.layout.fragment_trans, container, false);
        myrecyclerview = (RecyclerView) v.findViewById(R.id.transaction_recycler);
        return  v;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Progress dialog
        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);
        pDialog.setMessage("Chargement de la liste en cours");
        orderList = new ArrayList<>();

        dialogHelper = new DialogHelper(getActivity(), getApplicationContext());
        db = new UserDbHelper(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        userphone  = user.get("phone");

        jsonRequest();
    }
    private  void jsonRequest(){
        showDialog();

        String telephone = userphone.substring(1);
        request = new JsonArrayRequest(AppConfig.URL_GET_ORDER+telephone, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                if (response.length() == 0){
                    hideDialog();
                    Toast.makeText(getApplicationContext(), "Liste de transaction vide", Toast.LENGTH_SHORT).show();
                }


                for (int i = 0 ; i < response.length(); i++){
                    hideDialog();
                    try{
                        jsonObject = response.getJSONObject(i);
                        Order order = new Order();
                        order.setOrderId(jsonObject.getInt("OrderId"));
                        order.setOrderStatus(jsonObject.getInt("OrderStatus"));
                        order.setOrderprice(jsonObject.getInt("OrderPrice"));
                        order.setOrderDetail(jsonObject.getString("OrderDetail"));
                        order.setOrderComment(jsonObject.getString("OrderComment"));
                        order.setOrderAddress(jsonObject.getString("OrderAddress"));
                        orderList.add(order);

                    }
                    catch (JSONException e) {
                        hideDialog();
                        e.printStackTrace();
                    }
                }
                setuprecyclerview(orderList);
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

    private void setuprecyclerview(List<Order> orderList) {
        OrderAdapter orderAdapter = new OrderAdapter(getContext(), orderList);
        myrecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        myrecyclerview.setAdapter(orderAdapter);
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
