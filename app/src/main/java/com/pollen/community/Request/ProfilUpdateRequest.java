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

public class ProfilUpdateRequest {
    private Context context;
    private RequestQueue queue;

    public ProfilUpdateRequest(Context context, RequestQueue queue){
        this.context = context;
        this.queue = queue;
    }


    public void profilUpdate(final String username, final String lastname, final String firstname, final String uid, final ProfilUpdateInterface callback){

        String url = AppConfig.URL_PROFIL;

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

                        if(messages.has("username")){
                            errors.put("username", messages.getString("username"));

                        }
                        if(messages.has("firstname")){
                            errors.put("firstname", messages.getString("firstname"));

                        }
                        if(messages.has("lastname")){
                            errors.put("lastname", messages.getString("lastname"));

                        }
                        if(messages.has("uid")){
                            errors.put("uid", messages.getString("uid"));

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
                map.put("username", username);
                map.put("lastname", lastname);
                map.put("firstname", firstname);
                map.put("uid", uid);

                return  map;
            }
        };

        queue.add(request);

    }

    public interface ProfilUpdateInterface {
        void onSuccess(String message);
        void inputErrors(Map<String, String> errors);
        void onError(String message);
    }

}
