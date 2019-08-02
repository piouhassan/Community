package com.pollen.community.fragment;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pollen.community.Database.UserDbHelper;
import com.pollen.community.Manager.SessionManager;
import com.pollen.community.R;
import com.pollen.community.Request.ProfilUpdateRequest;
import com.pollen.community.UrlConfig.AppConfig;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.facebook.accountkit.internal.AccountKitController.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    private static final String IMAGE_DIRECTORY = "/Community";
    private int GALLERY = 1, CAMERA = 2;
    private UserDbHelper db;
    ImageView add_pic;
    ImageView imageview;
    ImageView profil_change;
    SessionManager sessionManager;
    TextView profil_username;
    TextView profil_phone;
    String uid;
    ProgressDialog pdLoading;
    String phone;
    private RequestQueue queue;
    private ProfilUpdateRequest request;
    String thisusername;
    BottomSheetDialog dialog;
    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_account,
                container, false);

        db = new UserDbHelper(getApplicationContext());

         pdLoading = new ProgressDialog(getContext());


        sessionManager = new SessionManager(getApplicationContext());

        HashMap<String, String> user = db.getUserDetails();
         phone = user.get("phone");
        thisusername = user.get("username");
        String Avatar = user.get("avatar");
         uid = user.get("uid");

        profil_username = view.findViewById(R.id.profil_username);
        profil_phone = view.findViewById(R.id.profil_phone);
        profil_username.setText(thisusername);
        profil_phone.setText(phone);

        add_pic = (ImageView) view.findViewById(R.id.add_pic);
        imageview = (ImageView) view.findViewById(R.id.profil_pic);


            Picasso.get().load(Avatar).into(imageview);

        profil_change  =  (ImageView) view.findViewById(R.id.profil_change);

        profil_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.bottom_sheet, null);

                dialog = new BottomSheetDialog(getActivity());
              final EditText userEdit = (EditText) view.findViewById(R.id.username);
              final EditText nameEdit = (EditText) view.findViewById(R.id.name);
              final EditText firstnameEdit = (EditText) view.findViewById(R.id.firstname);

                userEdit.setText(thisusername);
                userEdit.setFocusable(true);
                userEdit.setEnabled(true);

                ( (Button ) view.findViewById(R.id.user_changes)).setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                         String uSernames = userEdit.getText().toString().trim();
                         String lastname = nameEdit.getText().toString().trim();
                         String firstname = firstnameEdit.getText().toString().trim();
                          if (uSernames.isEmpty() || lastname.isEmpty() || firstname.isEmpty()){

                              if (uSernames.isEmpty()){
                                  userEdit.setError("Veuillez saisir votre Identifiant");
                              }

                              else if(lastname.isEmpty()){
                                  nameEdit.setError("Veuillez saisir votre Prenom");
                              }
                              else if(firstname.isEmpty()){
                                  firstnameEdit.setError("Veuillez saisir votre Nom");
                              }

                          }
                          else{
                              updateSqlite(uSernames,lastname,firstname,uid);
                              updateServer(uSernames,lastname,firstname,phone,uid);
                              dialog.show();
                          }

                      }
                  });


                dialog.setContentView(view);
                dialog.show();
            }
        });



        add_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });

        return view;
    }

    private void updateSqlite(String identifiant, String nom, String prenom, String uid) {
        if (db.updateUser(identifiant,nom,prenom,uid)){
            Toast.makeText(getApplicationContext(), "Mise a jour effectué", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), "Un probleme est survenu", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateServer(final String user, final String last, final String first, final String uid, final String phone) {
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_PROFIL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseData(response);
                        pdLoading.dismiss();
                        dialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Une erreur s'est produite",Toast.LENGTH_LONG).show();
                        pdLoading.dismiss();
                        dialog.dismiss();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("username", user);
                params.put("lastname", last);
                params.put("firstname", first);
                params.put("uid", uid);
                params.put("phone", phone);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);


    }

    public void parseData(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString("status").equals("true")) {
                JSONArray dataArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject dataobj = dataArray.getJSONObject(i);
                    Boolean    error = Boolean.valueOf(dataobj.getString("error"));
                    String   message = dataobj.getString("message");
                    if (error){
                        Toast.makeText(getApplicationContext(),  "Une erreur est surveenue lors de la mise a jour", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "Profil mise a jour ajouter avec succes", Toast.LENGTH_SHORT).show();
                    }
                }



            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
        pictureDialog.setTitle("Choisir une Photo de Profil");
        String[] pictureDialogItems = {
                "Depuis la Galerie",
                "Depuis la Camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), contentURI);
                    String path = saveImage(bitmap);

                    String newAvatar = path.trim();

                    if (db.updateProfil("file://"+newAvatar,uid)){
                        Toast.makeText(getApplicationContext(), "Photo de Profil modifier", Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(getApplicationContext(), "erreur d'enregistrement", Toast.LENGTH_SHORT).show();

                    }

                    imageview.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Echec!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            String path = saveImage(thumbnail);
            Toast.makeText(getApplicationContext(), path, Toast.LENGTH_SHORT).show();
            imageview.setImageBitmap(thumbnail);
        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(getApplicationContext(),
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "Fichier Sauvegarder avec succes ::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }



}
