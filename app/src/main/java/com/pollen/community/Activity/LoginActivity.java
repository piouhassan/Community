package com.pollen.community.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pollen.community.Database.UserDbHelper;
import com.pollen.community.Manager.SessionManager;
import com.pollen.community.R;
import com.pollen.community.UrlConfig.AppConfig;
import com.pollen.community.helper.DialogHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {
   EditText username;
   EditText password;
    TextView login;
    SessionManager sessionManager;
    private UserDbHelper db;
    private ProgressDialog pDialog;
    DialogHelper dialogHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login =  findViewById(R.id.login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        db = new UserDbHelper(getApplicationContext());
        sessionManager = new SessionManager(getApplicationContext());

        dialogHelper = new DialogHelper(LoginActivity.this, getApplicationContext());
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
       pDialog.setMessage("Connexion en cours");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String login_username = username.getEditableText().toString().trim();
                String login_password = password.getEditableText().toString().trim();
                if (login_username.isEmpty() || login_password.isEmpty()){

                    if(login_username.isEmpty()){
                        username.setError("Veuillez saisir votre Identifiant");
                    }

                    else if(login_password.isEmpty()){
                        password.setError("Veuillez saisir votre mot de passe");
                    }
                }


                if (!login_username.isEmpty() && !login_password.isEmpty()) {
                  Login(login_username,login_password);
                }

            }
        });


    }

    private void Login(final String username, final String password) {
       showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    boolean error = json.getBoolean("error");
                    JSONArray  jsonArray = json.getJSONArray("user");
                    if(!error){
                        sessionManager.setLogin(true);
                       for (int i = 0 ; i < jsonArray.length(); i++){
                           JSONObject jsonObject = jsonArray.getJSONObject(i);
                           String phone = jsonObject.getString("phone").trim();
                           String username = jsonObject.getString("username").trim();
                           String uid = jsonObject.getString("id").trim();
                           String address = jsonObject.getString("address").trim();
                           String avatar = jsonObject.getString("avatar").trim();
                           String created_at = jsonObject.getString("created_at").trim();

                           String profil = AppConfig.URL_PICTURE + avatar;

                           db.addUser(username,phone,uid,address,profil,created_at);
                           hideDialog();
                           dialogHelper.AccountVerify();
                       }
                    }

                } catch (JSONException e) {
                    hideDialog();
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, "Error "+e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideDialog();
                        Toast.makeText(LoginActivity.this, "Error "+error.toString(), Toast.LENGTH_SHORT).show();

                    }
                }

        ){
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("username", username);
                map.put("password", password);
                return  map;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

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
