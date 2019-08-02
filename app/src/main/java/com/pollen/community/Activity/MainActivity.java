package com.pollen.community.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.pollen.community.Database.DataSource.CartRepository;
import com.pollen.community.Database.DataSource.FavoriteRepository;
import com.pollen.community.Database.Local.CartDataSource;
import com.pollen.community.Database.Local.CommunityRoomDatabase;
import com.pollen.community.Database.Local.FavoriteDatasource;
import com.pollen.community.Database.UserDbHelper;
import com.pollen.community.Manager.BottomNavigationBehavior;
import com.pollen.community.Manager.DarkModePrefManager;
import com.pollen.community.Manager.SessionManager;
import com.pollen.community.R;
import com.pollen.community.fragment.AcceuilFragment;
import com.pollen.community.fragment.AccountFragment;
import com.pollen.community.fragment.ClassementFragment;
import com.pollen.community.fragment.FavorisFragment;
import com.pollen.community.fragment.ParameterFragment;
import com.pollen.community.fragment.RestaurantFragment;
import com.pollen.community.fragment.SearchFragment;
import com.pollen.community.fragment.TransFragment;
import com.pollen.community.helper.Common;
import com.pollen.community.helper.DialogHelper;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName() ;
    private BottomNavigationView bottomNavigationView;
    public TextView night;
    private UserDbHelper db;
    private TransFragment transFragment;
    private AcceuilFragment acceuilFragment;
    private ClassementFragment classementFragment;
    private FavorisFragment favorisFragment;
    private ParameterFragment parameterFragment;
  private RestaurantFragment restaurantFragment;
  private AccountFragment accountFragment;
  private SearchFragment searchFragment;
    private AppCompatButton show_dialog;
    private ProgressBar progress_bar;
private  TextView usernamesidebar;
    NavigationView navigationView;
    SessionManager sessionManager;
    RequestOptions option;
    TextView cart_count;
    RelativeLayout badge;

    DialogHelper dialogHelper;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item)
        {
            restaurantFragment = new RestaurantFragment();
            accountFragment = new AccountFragment();
            searchFragment = new SearchFragment();
              Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigationMyProfile:
                    setFragment(accountFragment);
                    return true;
                case R.id.navigationMyCourses:
                    setFragment(restaurantFragment);
                    return true;
                case R.id.navigationHome:
                    setFragment(acceuilFragment);
                    return true;
                case  R.id.navigationSearch:
                    setFragment(searchFragment);
                    return true;
                case  R.id.navigationMenu:
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.openDrawer(GravityCompat.START);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        show_dialog = findViewById(R.id.show_dialog);

        acceuilFragment = new AcceuilFragment();
        transFragment = new TransFragment();
        parameterFragment = new ParameterFragment();
        classementFragment = new ClassementFragment();
        favorisFragment = new FavorisFragment();
        if(new DarkModePrefManager(this).isNightMode()){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        dialogHelper = new DialogHelper(MainActivity.this, getApplicationContext());

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        cart_count = findViewById(R.id.cart_count);
        badge = findViewById(R.id.badge);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

        bottomNavigationView.setSelectedItemId(R.id.navigationHome);

        show_dialog = (AppCompatButton) findViewById(R.id.show_dialog);

        show_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent inte =  new Intent(MainActivity.this, CartActivity.class);
              startActivity(inte);
            }
        });
        db = new UserDbHelper(getApplicationContext());

        sessionManager = new SessionManager(this);
        HashMap<String, String> user = db.getUserDetails();
        String phone = user.get("phone");
        String username = user.get("username");
        String avatar = user.get("avatar");


        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.usernamesidebar);
        TextView navPhone = (TextView) headerView.findViewById(R.id.phoneSidebar);
        CircularImageView navavatar =  headerView.findViewById(R.id.userprofil);
        CircleImageView firstavatar =  findViewById(R.id.profile_image_acc);
        navUsername.setText(username);
        navPhone.setText(phone);

        Picasso.get().load(avatar).into(navavatar);
        Picasso.get().load(avatar).into(firstavatar);

        if (!sessionManager.isLoggedIn()) {
           dialogHelper.logoutUser();
        }

        initCartDB();

       updateCartCount();

    }

    private void updateCartCount() {
         if (cart_count == null) return;
         runOnUiThread(new Runnable() {
             @Override
             public void run() {
                 if (Common.cartRepository.countCartItems() == 0)
                      badge.setVisibility(View.INVISIBLE);
                 else
                 {
                     badge.setVisibility(View.VISIBLE);
                     cart_count.setText(String.valueOf(Common.cartRepository.countCartItems()));
                 }
             }
         });
    }

    private void initCartDB() {
        Common.CommunityRoomDatabase = CommunityRoomDatabase.getInstance(this);
        Common.cartRepository = CartRepository.getInstance(CartDataSource.getInstance(Common.CommunityRoomDatabase.cartDAO()));
         Common.favoriteRepository = FavoriteRepository.getInstance(FavoriteDatasource.getInstance(Common.CommunityRoomDatabase.favoriteDAO()));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Voulez vous quitter Community ???").setCancelable(false).setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MainActivity.super.onBackPressed();
                }
            })
                    .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.transaction) {
          setFragment(transFragment);
        } else if (id == R.id.classement) {
            setFragment(classementFragment);
        } else if (id == R.id.favoris) {
          setFragment(favorisFragment);
        } else if (id == R.id.parameter) {
       setFragment(parameterFragment);
        }
        else if (id == R.id.nav_share) {
         shareTo();
        }
        else if (id == R.id.nav_logout) {

            dialogHelper.LogoutVerifi();
        }

        else if (id == R.id.nav_dark_mode) {
            DarkModePrefManager darkModePrefManager = new DarkModePrefManager(this);
            darkModePrefManager.setDarkMode(!darkModePrefManager.isNightMode());
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            recreate();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @SuppressLint("ResourceType")
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
        shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        shareIntent.putExtra(Intent.EXTRA_TEXT,text + "https://play.google.com/store/apps/details?id=" +getPackageName());
        shareIntent.setType("image/png");
        startActivity(Intent.createChooser(shareIntent,"Partager avec"));
    }

    public  void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,fragment);
        fragmentTransaction.commit();
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        updateCartCount();
    }
}
