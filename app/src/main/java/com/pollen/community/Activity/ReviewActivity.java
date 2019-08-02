package com.pollen.community.Activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.pollen.community.Adapter.CommentRestaurantAdapter;
import com.pollen.community.Adapter.RestaurantAdapter;
import com.pollen.community.Models.Comment;
import com.pollen.community.Models.Products;
import com.pollen.community.Models.Restaurant;
import com.pollen.community.R;
import com.pollen.community.UrlConfig.AppConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.accountkit.internal.AccountKitController.getApplicationContext;

public class ReviewActivity extends AppCompatActivity {
    private ProgressDialog pDialog;
    private List<Comment> commentList ;
    RecyclerView myrecyclerview;
    JsonArrayRequest request;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        final int uid = getIntent().getExtras().getInt("uid");
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Chargement de la liste en cours");

        myrecyclerview = (RecyclerView) findViewById(R.id.review_recycler);

        commentList = new ArrayList<>();

        jsonRequest(uid);

    }

    private void jsonRequest(int uid) {
        showDialog();
        request = new JsonArrayRequest(AppConfig.URL_COMMENT_RESTAURANT+uid, new Response.Listener<JSONArray>() {
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
                        Comment comment = new Comment();
                        comment.setFirstname(jsonObject.getString("firstname"));
                        comment.setLastname(jsonObject.getString("lastname"));
                        comment.setCommentaire(jsonObject.getString("commentaire"));
                        comment.setRating(jsonObject.getString("rating"));
                        comment.setUserCover(jsonObject.getString("cover"));
                        commentList.add(comment);

                    }
                    catch (JSONException e) {
                        hideDialog();
                        e.printStackTrace();
                        Toast.makeText(ReviewActivity.this, String.valueOf(e), Toast.LENGTH_SHORT).show();
                    }
                }
                setuprecyclerview(commentList);
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Toast.makeText(ReviewActivity.this, String.valueOf(error), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void setuprecyclerview(List<Comment> commentList) {
        CommentRestaurantAdapter commentRestaurantAdapter = new CommentRestaurantAdapter(getApplicationContext(), commentList);
        myrecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        myrecyclerview.setAdapter(commentRestaurantAdapter);
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
