package com.pollen.community.Tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.pollen.community.R;

/**
 * Created by apple on 18/03/16.
 */
public class TabFragment1 extends Fragment {


Button nexttabs;






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         View view =  inflater.inflate(R.layout.fragmenttab1,
                container, false);


        return view;

    }
}