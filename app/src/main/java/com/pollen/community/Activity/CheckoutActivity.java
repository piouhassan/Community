package com.pollen.community.Activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.pollen.community.Adapter.CheckoutAdapter;
import com.pollen.community.R;

public class CheckoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        //         *******Tabview*********

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("PAIEMENT"));
        tabLayout.addTab(tabLayout.newTab().setText("STATUT"));
        tabLayout.addTab(tabLayout.newTab().setText("VALIDATION"));
        tabLayout.addTab(tabLayout.newTab().setText("RECAPITULATION"));



        final ViewPager viewPager = (ViewPager) findViewById(R.id.pagercheck);

        CheckoutAdapter adapter = new CheckoutAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
