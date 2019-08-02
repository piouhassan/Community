package com.pollen.community.Request;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.pollen.community.UrlConfig.AppConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CommentRequest {
    private Context context;
    private RequestQueue queue;

    public CommentRequest(Context context, RequestQueue queue){
        this.context = context;
        this.queue = queue;
    }


    public void commentadd(final String rating, final String comment, final String restaurant_id, final String user_phone, final commentInterface callback){

        String url = AppConfig.URL_COMMENT_RESTAURANT_SEND;

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                Map<String, String> errors = new HashMap<>();

                try {
                    JSONObject json = new JSONObject(response);
                    boolean error = json.getBoolean("error");
                    if(!error){
                        JSONObject messages = json.getJSONObject("message");
                        callback.onSuccess(String.valueOf(messages));
                    }else if (error){
                        callback.inputErrors(errors);
                    }

                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error instanceof NetworkError){
                    callback.onError("Impossible de se connecter");
                } else if(error != null){
                    callback.onError("Une erreur s'est produite");
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("rating", rating);
                map.put("comment", comment);
                map.put("restaurant", restaurant_id);
                map.put("user_phone", user_phone);

                return  map;
            }
        };

        queue.add(request);

    }
    public interface commentInterface {
        void onSuccess(String message);
        void inputErrors(Map<String, String> errors);
        void onError(String message);
    }




}
