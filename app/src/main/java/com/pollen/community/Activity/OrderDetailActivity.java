package com.pollen.community.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pollen.community.Adapter.OrderDetailAdapter;
import com.pollen.community.Database.CartModel.Cart;
import com.pollen.community.R;

import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {

    String address, comment, phone;
    int uid, statut, price;
     RecyclerView recycler_order_detail;
    TextView detail_address, detail_comment , detail_statut;
    String detail_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        detail_address = findViewById(R.id.detail_address);
        detail_comment = findViewById(R.id.detail_comment);
        detail_statut = findViewById(R.id.detail_statut);

        uid = getIntent().getExtras().getInt("order_id");
        address = getIntent().getExtras().getString("order_address");
        comment = getIntent().getExtras().getString("order_comment");
        phone = getIntent().getExtras().getString("order_userphone");
        statut = getIntent().getExtras().getInt("order_statut");
        price = getIntent().getExtras().getInt("order_price");
        detail_detail = getIntent().getExtras().getString("order_detail");


        recycler_order_detail = findViewById(R.id.recycler_order_detail);
        recycler_order_detail.setLayoutManager(new LinearLayoutManager(this));
        recycler_order_detail.setHasFixedSize(true);

        if (statut == 1){
            detail_statut.setText("En attente");
        }
        if (statut == 2){
            detail_statut.setText("Payer");
        }

        detail_address.setText(address);
        detail_comment.setText(comment);


        displayOrderDetail();

    }

    private void displayOrderDetail() {
        List<Cart> orderDetail =  new Gson().fromJson(detail_detail, new TypeToken<List<Cart>>(){}.getType());

         recycler_order_detail.setAdapter(new OrderDetailAdapter(this,orderDetail));
    }
}
