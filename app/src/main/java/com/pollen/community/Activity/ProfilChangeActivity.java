package com.pollen.community.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.pollen.community.Database.UserDbHelper;
import com.pollen.community.Manager.SessionManager;
import com.pollen.community.Database.UserDbHelper;
import com.pollen.community.Manager.SessionManager;
import com.pollen.community.R;

import java.util.HashMap;

import pl.bclogic.pulsator4droid.library.PulsatorLayout;

public class ProfilChangeActivity extends AppCompatActivity {
    private UserDbHelper db;
    SessionManager sessionManager;
Spinner moyens;
TextView Lusername;
ImageView back_return;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_change);

        PulsatorLayout pulsator = (PulsatorLayout) findViewById(R.id.pulsator);
        pulsator.start();
        back_return = findViewById(R.id.back_return);

        back_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(ProfilChangeActivity.this, MainActivity.class);
                startActivity(back);
                finish();
            }
        });

        db = new UserDbHelper(getApplicationContext());
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = db.getUserDetails();
        String phone = user.get("phone");
        String username = user.get("username");

         Lusername = findViewById(R.id.username);
        Lusername.setText(username);


}
}
