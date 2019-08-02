package com.pollen.community.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.pollen.community.R;
import com.pollen.community.R.id;


 public class SplashActivity extends AppCompatActivity {


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Animation leftToNormal = AnimationUtils.loadAnimation(this, R.anim.left_to_normal);
        Animation rightToNormal = AnimationUtils.loadAnimation(this, R.anim.right_to_normal);
        Animation rotate = AnimationUtils.loadAnimation(this, R.anim.rotate);

        ((ImageView)findViewById(id.iv_logo_splash)).startAnimation(leftToNormal);
        ((ImageView)findViewById(id.iv_text_app)).startAnimation(rightToNormal);
        ((TextView)findViewById(id.tv_sub_text_app)).startAnimation(rightToNormal);
        ((ImageView)findViewById(id.iv_splash_progress)).startAnimation(rotate);

        final Intent i = new Intent(this, StartActivity






























































                .class);
        Thread timer = new Thread(){
            @Override
            public void run() {
                try{
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                }
            }
        };
        timer.start();

    }

     @Override
     public void finish() {
         super.finish();
         overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
     }

    }

