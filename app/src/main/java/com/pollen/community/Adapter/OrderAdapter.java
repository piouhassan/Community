package com.pollen.community.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pollen.community.Activity.OrderDetailActivity;
import com.pollen.community.Models.Order;
import com.pollen.community.R;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {

private Context mContext;
        List<Order> mData;

public OrderAdapter(Context mContext, List<Order> mData) {
        this.mContext = mContext;
        this.mData = mData;
        }

@NonNull
@Override
public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.item_order,viewGroup, false);

final  MyViewHolder viewHolder =  new MyViewHolder(view);

        viewHolder.transaction_detail.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        Intent intent = new Intent(mContext, OrderDetailActivity.class);
        intent.putExtra("order_id", mData.get(viewHolder.getAdapterPosition()).getOrderId());
        intent.putExtra("order_address", mData.get(viewHolder.getAdapterPosition()).getOrderAddress());
        intent.putExtra("order_comment", mData.get(viewHolder.getAdapterPosition()).getOrderComment());
        intent.putExtra("order_price", mData.get(viewHolder.getAdapterPosition()).getOrderprice());
        intent.putExtra("order_statut", mData.get(viewHolder.getAdapterPosition()).getOrderStatus());
        intent.putExtra("order_userphone", mData.get(viewHolder.getAdapterPosition()).getUserPhone());
        intent.putExtra("order_detail", mData.get(viewHolder.getAdapterPosition()).getOrderDetail());
        mContext.startActivity(intent);
        }
        });
        return  viewHolder;
        }

@Override
public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.transaction_number.setText(new StringBuilder("#").append(mData.get(i).getOrderId()).toString());
        myViewHolder.transaction_coast.setText(new StringBuilder(String.valueOf(mData.get(i).getOrderprice())).append(" Francs").toString());
        if (mData.get(i).getOrderStatus() == 1){
            myViewHolder.transaction_statut.setText("En attente");
        }
        if (mData.get(i).getOrderStatus() == 2){
            myViewHolder.transaction_statut.setText("Payer");
        }
        myViewHolder.transaction_comment.setText(mData.get(i).getOrderComment());

        }

@Override
public int getItemCount() {
        return mData.size();
        }

public  static class MyViewHolder extends RecyclerView.ViewHolder {

    TextView  transaction_coast, transaction_statut, transaction_comment,transaction_number;
   LinearLayout transaction_detail;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        transaction_coast = itemView.findViewById(R.id.transaction_coast);
        transaction_statut = itemView.findViewById(R.id.transaction_statut);
        transaction_comment = itemView.findViewById(R.id.transaction_comment);
        transaction_detail = itemView.findViewById(R.id.transaction_detail);
        transaction_number = itemView.findViewById(R.id.transaction_number);

    }
}

}
