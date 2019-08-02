package com.pollen.community.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pollen.community.Activity.AboutActivity;
import com.pollen.community.Activity.RatingActivity;
import com.pollen.community.Manager.DarkModePrefManager;
import com.pollen.community.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * A simple {@link Fragment} subclass.
 */
public class ParameterFragment extends Fragment {

    RelativeLayout themechange;
    RelativeLayout setting6;
    RelativeLayout setting4;
    RelativeLayout setting5;
    RelativeLayout setting7;
    public ParameterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_parameter,
                container, false);

        themechange = view.findViewById(R.id.theme);
        setting6 = (RelativeLayout)  view.findViewById(R.id.setting6);
        setting4 = (RelativeLayout)  view.findViewById(R.id.setting4);
        setting5 = (RelativeLayout)  view.findViewById(R.id.setting5);
        setting7 = (RelativeLayout)  view.findViewById(R.id.setting7);

        setting4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting4 = new Intent(Intent.ACTION_CALL);
                setting4.setData(Uri.parse("tel:+22892363533"));
                startActivity(setting4);
            }
        });

        setting5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareTo();
            }
        });

        setting6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting5 = new Intent(getActivity(), RatingActivity.class);
                startActivity(setting5);
            }
        });

        setting7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting7 = new Intent(getActivity(), AboutActivity.class);
                startActivity(setting7);
            }
        });

        themechange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DarkModePrefManager darkModePrefManager = new DarkModePrefManager(getActivity());
                darkModePrefManager.setDarkMode(!darkModePrefManager.isNightMode());
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                getActivity().recreate();
            }
        });


        return view;
    }
    private void shareTo() {
        String text = "La chose la plus  difficile de nos jours est d'attendre la cuisson d'un plat au restaurant. Il est encore plus penible de devoir attendre que tous les plats soient prêt avant de pouvoir savourer. Cres est l'application qui vient resoudre ce problême et vous assure une livraison express en plus de la reservation de table et la commande. ";
        Intent shareIntent;
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.filetoshare);
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/Share.png";
        OutputStream out = null;
        File file=new File(path);
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        path=file.getPath();
        Uri bmpUri = Uri.parse("file://"+path);
        shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        shareIntent.putExtra(Intent.EXTRA_TEXT,text + "https://play.google.com/store/apps/details?id=" +getActivity().getPackageName());
        shareIntent.setType("image/png");
        startActivity(Intent.createChooser(shareIntent,"Partager avec"));
    }
}
