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

public class OrderRequest {
    private Context context;
    private RequestQueue queue;

    public OrderRequest(Context context, RequestQueue queue){

        this.context = context;
        this.queue = queue;
    }


    public void OrderSubmit(final int Price,final String Detail, final String Comment, final String Address, final String phone, final OrderSubmitInterface callback){

        String url = AppConfig.URL_ORDER;

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                Map<String, String> errors = new HashMap<>();

                try {
                    JSONObject json = new JSONObject(response);
                    boolean error = json.getBoolean("error");
                    if(!error){
                        callback.onSuccess("Succes");
                    }else {
                        JSONObject messages = json.getJSONObject("message");
                        if(messages.has("phone")){
                            errors.put("phone", messages.getString("phone"));

                        }
                        if(messages.has("status")){
                            errors.put("status", messages.getString("status"));

                        }
                        if(messages.has("price")){
                            errors.put("price", messages.getString("price"));

                        }
                        if(messages.has("comment")){
                            errors.put("comment", messages.getString("comment"));

                        }
                        if(messages.has("detail")){
                            errors.put("detail", messages.getString("detail"));

                        }
                        if(messages.has("address")){
                            errors.put("address", messages.getString("address"));

                        }

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
                map.put("price", String.valueOf(Price));
                map.put("detail", Detail);
                map.put("comment", Comment);
                map.put("address", Address);
                map.put("phone", phone);

                return  map;
            }
        };

        queue.add(request);

    }

    public interface OrderSubmitInterface {
        void onSuccess(String message);
        void inputErrors(Map<String, String> errors);
        void onError(String message);
    }

}
