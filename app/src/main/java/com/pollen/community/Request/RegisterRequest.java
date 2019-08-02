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

public class RegisterRequest {

    private Context context;
    private RequestQueue queue;

    public RegisterRequest(Context context, RequestQueue queue){

        this.context = context;
        this.queue = queue;
    }

    public  void register(final String phone, final String nom, final String adresse, final String password, final RegisterCallBack callback ){

        String url = AppConfig.URL_REGISTER;

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {

                Map<String, String> errors = new HashMap<>();


                try{
                    JSONObject json = new JSONObject(response);
                    boolean error = json.getBoolean("error");

                    if(!error){
                        //l'inscription s'est bien déroulé
                        callback.onSuccess("L'inscription s'est bien déroulé");

                    } else{
                        JSONObject messages = json.getJSONObject("message");
                        if(messages.has("phone")){
                            errors.put("phone", messages.getString("phone"));

                        }
                        if(messages.has("username")){
                            errors.put("username", messages.getString("username"));

                        }
                        if(messages.has("password")){
                            errors.put("password", messages.getString("password"));

                        }

                        callback.inputErrors(errors);

                    }

                }catch (JSONException e){
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
                map.put("phone", phone);
                map.put("username", nom);
                map.put("address", adresse);
                map.put("password", password);

                return  map;
            }
        };

        queue.add(request);

    }
    public  interface  RegisterCallBack{
        void onSuccess(String message);
        void inputErrors(Map<String, String> errors);
        void onError(String message);
    }


}

