package com.pollen.community.helper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.pollen.community.Activity.MainActivity;
import com.pollen.community.Activity.StartActivity;
import com.pollen.community.Database.UserDbHelper;
import com.pollen.community.Manager.CustomRatingBar;
import com.pollen.community.Manager.SessionManager;
import com.pollen.community.Manager.VolleySingleton;
import com.pollen.community.R;
import com.pollen.community.Request.CommentRequest;
import com.pollen.community.UrlConfig.AppConfig;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.pollen.community.helper.Utils.urlEncode;

public class DialogHelper {

    Activity activity;
    Context mContext;
    private UserDbHelper db;
    SessionManager sessionManager;
    ShareDialog shareDialog;

    private CommentRequest  request;
    private RequestQueue queue;

    public DialogHelper(Activity activity, Context mContext) {
        this.activity = activity;
        this.mContext = mContext;
        db = new UserDbHelper(activity);
        sessionManager = new SessionManager(mContext);
    }

    public void LogoutVerifi(){
        final Dialog dialoglogout = new Dialog(activity);
        dialoglogout.setContentView(R.layout.dialog_logout_warning);
        dialoglogout.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialoglogout.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        ((TextView) dialoglogout.findViewById(R.id.logoutConf)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             logoutUser();
            }
        });

        ((TextView) dialoglogout.findViewById(R.id.logoutAnn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialoglogout.dismiss();

            }
        });

        dialoglogout.show();
        dialoglogout.getWindow().setAttributes(lp);
    }

    public void AccountVerify(){
        final Dialog dialogaccount = new Dialog(activity);
        dialogaccount.setContentView(R.layout.dialog_verify_account);
        dialogaccount.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogaccount.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ((Button) dialogaccount.findViewById(R.id.conf)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogaccount.dismiss();
                Intent inten = new Intent(mContext, MainActivity.class);
                inten.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(inten);
            }
        });

        dialogaccount.show();
        dialogaccount.getWindow().setAttributes(lp);
    }

    public void internetWarning(){
        final Dialog dialog = new Dialog(activity);
      dialog.setContentView(R.layout.dialog_no_internet);
        dialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        ((Button) dialog.findViewById(R.id.retry)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }



    public void shareProducts(final String product, final String name, final String description){
        final Dialog dialogshare = new Dialog(activity);
      dialogshare.setContentView(R.layout.dialog_share_product);
        dialogshare.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogshare.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogshare.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


                final ImageView  product_image = dialogshare.findViewById(R.id.share_product_image);

                Picasso.get().load(product).into(product_image);
            shareDialog = new ShareDialog(activity);
        ((ImageView) dialogshare.findViewById(R.id.share_to_facebook)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
                            .setContentTitle(name)
                            .setContentUrl(Uri.parse(String.valueOf(product)))
                            .setContentDescription(description)
                            .setShareHashtag(new ShareHashtag.Builder().setHashtag("#Community").build())
                            .build();
                    shareDialog.show(linkContent);
                }

            }
        });

        ((ImageView) dialogshare.findViewById(R.id.share_to_twitter)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "Twitter share", Toast.LENGTH_SHORT).show();
            }
        });

        ((ImageView) dialogshare.findViewById(R.id.share_to_instagram)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Utils.shareinstagram(mContext,product);
            }
        });

        ((ImageView) dialogshare.findViewById(R.id.share_to_envato)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ((ImageView) dialogshare.findViewById(R.id.share_to_pinterest)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shareUrl = "https://stackoverflow.com/questions/27388056/";
                String mediaUrl =product;
                String url = String.format("https://www.pinterest.com/pin/create/button/?url=%s&media=%s&description=%s",
                        urlEncode(shareUrl), urlEncode(mediaUrl), urlEncode(description));
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                Utils.filterByPackageName(mContext, intent, "com.pinterest");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });




        dialogshare.show();
        dialogshare.getWindow().setAttributes(lp);
    }


    public void logoutUser() {
        sessionManager.setLogin(false);
        db.deleteUsers();
        Intent intent = new Intent(activity, StartActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }


    public void setRating(String name, final String phone, String profil, final int uid){
        final Dialog dialograting = new Dialog(activity);
          dialograting.setContentView(R.layout.dialog_rating);
        dialograting.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialograting.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;

        CircularImageView userprofil =  dialograting.findViewById(R.id.rating_dialog_userprofil) ;
        TextView username   =  dialograting.findViewById(R.id.rating_dialog_username);
        TextView userphone =     dialograting.findViewById(R.id.rating_dialog_telephone);

        username.setText(name);
        userphone.setText(phone);
        Picasso.get().load(profil).into(userprofil);



        final AppCompatButton bt_submit = (AppCompatButton) dialograting.findViewById(R.id.bt_submit);
        ((EditText) dialograting.findViewById(R.id.commentaire)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                bt_submit.setEnabled(!s.toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        LayoutInflater layoutInflater =  activity.getLayoutInflater();

        View view = layoutInflater.inflate(R.layout.dialog_rating, null);

        queue = VolleySingleton.getInstance(mContext).getRequestQueue();
        request = new CommentRequest(mContext, queue);



        ((TextView) dialograting.findViewById(R.id.btn_closethis)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialograting.dismiss();
            }
        });

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText comment = dialograting.findViewById(R.id.commentaire);
                CustomRatingBar rating_bar = dialograting.findViewById(R.id.rating_bar);
                final String Commentaire = comment.getText().toString();
                final String rating = String.valueOf(rating_bar.getRating());
                 postnewComment(rating, Commentaire, String.valueOf(uid),phone);
                dialograting.dismiss();
            }
        });

        dialograting.show();
        dialograting.getWindow().setAttributes(lp);
    }



    private void  postnewComment(final  String rating, final  String comment, final  String restaurant, final  String phone){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_COMMENT_RESTAURANT_SEND,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseData(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mContext, "Une erreur s'est produite",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("rating",rating);
                params.put("comment",comment);
                params.put("restaurant",restaurant);
                params.put("user_phone",String.valueOf(phone));

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }

    public void parseData(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString("status").equals("true")) {
                JSONArray dataArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject dataobj = dataArray.getJSONObject(i);
                     Boolean    error = Boolean.valueOf(dataobj.getString("error"));
                      String   message = dataobj.getString("message");
                     if (error){
                         Toast.makeText(mContext,  "Une erreur est surveenue lors de l'ajout du commentaire", Toast.LENGTH_SHORT).show();
                     }else{
                         Toast.makeText(mContext, "Commentaire ajouter avec succes", Toast.LENGTH_SHORT).show();
                     }
                }



            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
