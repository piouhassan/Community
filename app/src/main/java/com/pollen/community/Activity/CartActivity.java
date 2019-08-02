package com.pollen.community.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.gson.Gson;
import com.pollen.community.Adapter.CartAdapter;
import com.pollen.community.Database.CartModel.Cart;
import com.pollen.community.Database.UserDbHelper;
import com.pollen.community.Manager.VolleySingleton;
import com.pollen.community.R;
import com.pollen.community.Request.OrderRequest;
import com.pollen.community.helper.Common;
import com.pollen.community.helper.RecyclerItemTouchHelper;
import com.pollen.community.helper.RecyclerItemTouchHelperListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class CartActivity extends AppCompatActivity  implements RecyclerItemTouchHelperListener {

    RecyclerView recyclerViewCart;
    TextView place_order;
    CompositeDisposable compositeDisposable;
    private RequestQueue queue;
    private OrderRequest request;
    List<Cart>  cartList = new ArrayList<>();
    private UserDbHelper db;
    CartAdapter cartAdapter;
   RelativeLayout rootLayouter;
    String userphone;
    String Address;
   TextView sub_price;
  RelativeLayout bottompanel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        db = new UserDbHelper(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        userphone  = user.get("phone");
        Address  = user.get("address");

        compositeDisposable = new CompositeDisposable();
        recyclerViewCart = findViewById(R.id.cart_content);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCart.setHasFixedSize(true);
           TextView empty_cart = findViewById(R.id.empty_cart);
             bottompanel = findViewById(R.id.bottompanel);



        rootLayouter  = findViewById(R.id.rootLayouter);
        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerViewCart);

        queue = VolleySingleton.getInstance(this).getRequestQueue();
        request = new OrderRequest(this, queue);


        sub_price = findViewById(R.id.sub_price);
        sub_price.setText(new StringBuilder(String.valueOf(Common.cartRepository.sumPrice())).append(" Frcs").toString());



        place_order = findViewById(R.id.place_order);


        if (Common.cartRepository.sumPrice() == 0){
            empty_cart.setVisibility(View.VISIBLE);
            bottompanel.setVisibility(View.GONE);
            place_order.setVisibility(View.GONE);

        }

        place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent =  new Intent(CartActivity.this , CheckoutActivity.class);
               // startActivity(intent);
                placeOrder();

            }
        });

       loadCartItems();

    }

    private void placeOrder() {
        // Creation de la boite de dialogue
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Validation du Panier");
         View sumit_order_layout = LayoutInflater.from(this).inflate(R.layout.order_dialog, null);

        final EditText edt_comment = (EditText) sumit_order_layout.findViewById(R.id.edt_comment);
        final EditText  edt_other_address = (EditText) sumit_order_layout.findViewById(R.id.other_address_edit);

        final RadioButton rdi_user_address = (RadioButton) sumit_order_layout.findViewById(R.id.user_address);
        final RadioButton other_address = (RadioButton) sumit_order_layout.findViewById(R.id.other_address);


        // Event

        rdi_user_address.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    edt_other_address.setEnabled(false);
                }
            }
        });

        other_address.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    edt_other_address.setEnabled(true);
                }
            }
        });

        builder.setView(sumit_order_layout);
        builder.setNegativeButton("ANNULER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                 dialog.dismiss();
            }
        }).setPositiveButton("VALIDER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                 final String orderComment = edt_comment.getText().toString();
                 final String  orderAddress;
                 if (rdi_user_address.isChecked()){
                     orderAddress = Address;
                 }
                 else  if (other_address.isChecked()) {
                     orderAddress = edt_other_address.getText().toString();

                 }
                 else {
                     orderAddress = "";
                 }

                compositeDisposable.add(
                        Common.cartRepository.getCartItems()
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe(new Consumer<List<Cart>>() {
                                    @Override
                                    public void accept(List<Cart> carts) throws Exception {
                                        if (!TextUtils.isEmpty(orderAddress)){
                                            sendOrderToServer(Common.cartRepository.sumPrice(), carts, orderComment, orderAddress);
                                        }else{
                                            Toast.makeText(CartActivity.this, "Veuillez renseigner votre address", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                })
                );

            }
        });
   builder.show();
    }

    private void sendOrderToServer(int sumPrice, List<Cart> carts, String orderComment, String orderAddress) {
     if (carts.size() > 0){
           String orderDetail = new Gson().toJson(carts);

           request.OrderSubmit(sumPrice,orderDetail,orderComment,orderAddress,userphone,new OrderRequest.OrderSubmitInterface() {
               @Override
               public void onSuccess(String message) {
                   bottompanel.setVisibility(View.GONE);
                   place_order.setVisibility(View.GONE);
                   Common.cartRepository.emptyCart();
                   android.app.AlertDialog.Builder build = new android.app.AlertDialog.Builder(CartActivity.this);
                   build.setMessage("Panier valider avec succes. Proceder au paiement ?").setCancelable(false).setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                         Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
                         startActivity(intent);
                    }
                   })
                           .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   dialog.cancel();
                               }
                           });

                   android.app.AlertDialog alertDi = build.create();
                   alertDi.show();

               }

               @Override
               public void inputErrors(Map<String, String> errors) {
                   if(errors.get("phone") !=null){
                       Toast.makeText(CartActivity.this, errors.get("phone"), Toast.LENGTH_SHORT).show();
                   }
                   if(errors.get("address") !=null){
                       Toast.makeText(CartActivity.this, errors.get("address"), Toast.LENGTH_SHORT).show();
                   }
                   if(errors.get("status") !=null){
                       Toast.makeText(CartActivity.this, errors.get("status"), Toast.LENGTH_SHORT).show();
                   }
                   if(errors.get("comment") !=null){
                       Toast.makeText(CartActivity.this, errors.get("comment"), Toast.LENGTH_SHORT).show();
                   }
                   if(errors.get("detail") !=null){
                       Toast.makeText(CartActivity.this, errors.get("detail"), Toast.LENGTH_SHORT).show();
                   }
                   if(errors.get("price") !=null){
                       Toast.makeText(CartActivity.this, errors.get("price"), Toast.LENGTH_SHORT).show();
                   }


               }

               @Override
               public void onError(String message) {
                   Log.e("ERROR" , message);
               }
           });

     }


    }


    private void loadCartItems() {
          compositeDisposable.add(
                  Common.cartRepository.getCartItems()
                .subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(new Consumer<List<Cart>>() {
                      @Override
                      public void accept(List<Cart> carts) throws Exception {
                           displayCartitem(carts);
                      }


                  })
          );
    }

    private void displayCartitem(List<Cart> carts) {
        cartList = carts;
        cartAdapter = new CartAdapter(this, carts);
        recyclerViewCart.setAdapter(cartAdapter);
    }

    @Override
    protected void onDestroy() {
         compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    protected void OnResume(){
        super.onResume();
        loadCartItems();
    }

    @Override
    public void onSwipe(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof CartAdapter.CardViewHolder){
            String name = cartList.get(viewHolder.getAdapterPosition()).name;
            final Cart deleteItem = cartList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // Suppression from Adapter

            cartAdapter.removeItem(deletedIndex);

            // Suppression de la RoomDatabase
            Common.cartRepository.deleteCartItem(deleteItem);

            Snackbar snackbar = Snackbar.make(rootLayouter, new StringBuilder(name).append(" Supprimer du panier").toString(), Snackbar.LENGTH_LONG);
            snackbar.setAction("ANNULER", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cartAdapter.restoreItem(deleteItem,deletedIndex);
                    Common.cartRepository.insertToCart(deleteItem);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }

    }
}
