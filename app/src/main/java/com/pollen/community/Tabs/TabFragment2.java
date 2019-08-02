package com.pollen.community.Tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.pollen.community.R;

/**
 * Created by apple on 18/03/16.
 */
public class TabFragment2 extends Fragment {

   RadioButton  exist_number,  new_number;
   RelativeLayout operateur_reseau;
   LinearLayout operateur_numero , already_num;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragmenttab2,
                container, false);

        operateur_reseau = view.findViewById(R.id.operateur_reseau);
        operateur_numero = view.findViewById(R.id.operateur_numero);
        exist_number= view.findViewById(R.id.exist_number);
        new_number= view.findViewById(R.id.new_number);
        already_num= view.findViewById(R.id.already_num);

        exist_number.setVisibility(View.INVISIBLE);
        new_number.setVisibility(View.INVISIBLE);
        operateur_numero.setVisibility(View.INVISIBLE);
        already_num.setVisibility(View.INVISIBLE);



        operateur_reseau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exist_number.setVisibility(View.VISIBLE);
                new_number.setVisibility(View.VISIBLE);
                operateur_numero.setVisibility(View.VISIBLE);
            }
        });

        exist_number.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                operateur_numero.setVisibility(View.VISIBLE);
                already_num.setVisibility(View.GONE);
            }
        });

        new_number.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                operateur_numero.setVisibility(View.GONE);
                already_num.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }
}