package com.pollen.community.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pollen.community.Manager.SessionManager;
import com.pollen.community.R;
import com.pollen.community.helper.Utils;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;

import java.util.HashMap;


public class StartActivity extends AppCompatActivity {

    LinearLayout tologin;
     LinearLayout facebook;
    LinearLayout google;
    TextView phone;
     ImageView community;
    TextView welcome;
    SessionManager sessionManager;
    private static final int REQUEST_CODE = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    Utils.requestMultiplePermissions(StartActivity.this);

         sessionManager = new SessionManager(this);
        if (sessionManager.isLoggedIn()) {
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        tologin = findViewById(R.id.tologin);
        phone = findViewById(R.id.phone);
        facebook = findViewById(R.id.facebooklog);
        google = findViewById(R.id.gmaillog);
        community = findViewById(R.id.img);
        welcome = findViewById(R.id.txt);
        tologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });



        community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StartActivity.this, AboutActivity.class);
                startActivity(i);
            }
        });

        welcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StartActivity.this, MainActivity.class);
                startActivity(i);
            }
        });




        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLoginPage(LoginType.PHONE);
            }
        });

    }



    private void startLoginPage(LoginType loginType) {
        Intent intent = new Intent(this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder builder = new AccountKitConfiguration.AccountKitConfigurationBuilder(loginType,AccountKitActivity.ResponseType.TOKEN);
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,builder.build());
        startActivityForResult(intent,REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE){
            AccountKitLoginResult result = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            if (result.getError() != null){
                Toast.makeText(this,""+result.getError().getErrorType().getMessage(), Toast.LENGTH_SHORT).show();
            }else if (result.wasCancelled()){
                Toast.makeText(this, "Annulé", Toast.LENGTH_SHORT).show();
            }
            else{
                if (result.getAccessToken() !=null){


                    AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                        @Override
                        public void onSuccess(final Account account) {
                            String phoneNumber = account.getPhoneNumber().toString();
                            SharedPreferences pref = getApplicationContext().getSharedPreferences("USER_LOGIN", 0);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("phone_number", phoneNumber);
                            editor.commit();
                             Intent intenter = new Intent(StartActivity.this, RegisterActivity.class);
                             startActivity(intenter);
                            overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
                        }

                        @Override
                        public void onError(AccountKitError accountKitError) {
                            Log.d("ERROR",accountKitError.getErrorType().getMessage());
                        }
                    });
                    /*
                    final SpotsDialog alertDialog = new SpotsDialog(StartActivity.this);
                    alertDialog.show();
                    alertDialog.setMessage("Veuillez Patienter...");

                    // Get user phone AND Check exists on server



*/
                }
            }
        }
    }

    private void showregisterActivity() {
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }


}
