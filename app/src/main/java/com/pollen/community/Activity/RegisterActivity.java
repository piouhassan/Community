package com.pollen.community.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.pollen.community.Database.UserDbHelper;
import com.pollen.community.Manager.SessionManager;
import com.pollen.community.Manager.VolleySingleton;
import com.pollen.community.R;
import com.pollen.community.Request.RegisterRequest;

import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText register_phone, register_address;
    EditText register_fullname;
    EditText register_password;
    Button btn_register;
    private ProgressDialog pDialog;
    private SessionManager session;
    private UserDbHelper db;
    private RequestQueue queue;
    private RegisterRequest request;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btn_register = (Button) findViewById(R.id.btn_register);
        register_phone = (EditText) findViewById(R.id.register_phone);
        register_fullname = (EditText) findViewById(R.id.register_fullname);
        register_password = (EditText) findViewById(R.id.register_password);
        register_address = (EditText) findViewById(R.id.register_address);


        SharedPreferences pref = getApplicationContext().getSharedPreferences("USER_LOGIN", 0);
        String phone = pref.getString("phone_number", "");
        register_phone.setText(phone);
        register_phone.setFocusable(false);
        register_phone.setEnabled(false);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager


        queue = VolleySingleton.getInstance(this).getRequestQueue();
        request = new RegisterRequest(this, queue);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String phone = register_phone.getEditableText().toString().trim();
                String name = register_fullname.getEditableText().toString().trim();
                String address = register_address.getEditableText().toString().trim();
                String password = register_password.getEditableText().toString().trim();

                if (phone.isEmpty() || name.isEmpty() || password.isEmpty()){

                    if (phone.isEmpty()){
                        register_phone.setError("Veuillez saisir votre Numero de Telephone");
                    }

                    else if(name.isEmpty()){
                        register_fullname.setError("Veuillez saisir votre Nom complet");
                    }
                    else if(address.isEmpty()){
                        register_fullname.setError("Veuillez saisir votre Adresse");
                    }

                    else if(password.isEmpty()){
                        register_password.setError("Veuillez saisir votre mot de passe");
                    }

                }

                if (phone.length() > 0 && name.length() > 0 && password.length() > 0  ) {

                    request.register(phone, name,address, password, new RegisterRequest.RegisterCallBack() {
                        @Override
                        public void onSuccess(String message) {
                            pDialog.setMessage("Inscription en cours ...");
                            showDialog();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            intent.putExtra("REGISTER", message);
                            startActivity(intent);
                            hideDialog();
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void inputErrors(Map<String, String> errors) {
                            hideDialog();
                            btn_register.setVisibility(View.VISIBLE);
                            if(errors.get("phone") !=null){
                                register_phone.setError(errors.get("phone"));
                            }
                            if(errors.get("username") !=null){
                                register_fullname.setError(errors.get("username"));
                            }
                            if(errors.get("address") !=null){
                                register_address.setError(errors.get("address"));
                            }
                            if(errors.get("password") !=null){
                                register_password.setError(errors.get("password"));
                            }

                        }

                        @Override
                        public void onError(String message) {
                            hideDialog();
                            btn_register.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                        }


                    });
                }else {
                    Toast.makeText(getApplicationContext(), "Tous les champs sont obligatoires!", Toast.LENGTH_SHORT).show();
                    hideDialog();
                    btn_register.setVisibility(View.VISIBLE);
                }
            }
        });

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
