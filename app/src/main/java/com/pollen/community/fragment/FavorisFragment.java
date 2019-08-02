package com.pollen.community.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.pollen.community.Adapter.FavoriteAdapter;
import com.pollen.community.Database.CartModel.Favorite;
import com.pollen.community.R;
import com.pollen.community.helper.Common;
import com.pollen.community.helper.RecyclerItemTouchHelper;
import com.pollen.community.helper.RecyclerItemTouchHelperListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavorisFragment extends Fragment implements RecyclerItemTouchHelperListener {

     RecyclerView recycler_fav;

     RelativeLayout rootLayout;
     CompositeDisposable compositeDisposable;

    FavoriteAdapter favoriteAdapter;

    List<Favorite>  localFavorite = new ArrayList<>();

    public FavorisFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_favoris,
                container, false);

        compositeDisposable = new CompositeDisposable();

        rootLayout = (RelativeLayout) view.findViewById(R.id.rootLayout);

        recycler_fav = view.findViewById(R.id.recycler_fav );
        recycler_fav.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler_fav.setHasFixedSize(true);

        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recycler_fav);
        loadFAvoriteItems();
        return  view;
    }


    @Override
    public void onResume() {
        super.onResume();
        loadFAvoriteItems();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    private void loadFAvoriteItems() {
        compositeDisposable.add(
                Common.favoriteRepository.getFavItems()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<Favorite>>() {
                            @Override
                            public void accept(List<Favorite> favorite) throws Exception {
                                displayFavoriteItems(favorite);
                            }


                        })
        );
    }

    private void displayFavoriteItems(List<Favorite> favorite) {
        localFavorite = favorite;
         favoriteAdapter = new FavoriteAdapter(getContext(), favorite);
        recycler_fav.setAdapter(favoriteAdapter);
    }

    @Override
    public void onSwipe(RecyclerView.ViewHolder viewHolder, int direction, int position) {
       if (viewHolder instanceof  FavoriteAdapter.FavoriteViewHolder){
           String name = localFavorite.get(viewHolder.getAdapterPosition()).name;
           final Favorite deleteItem = localFavorite.get(viewHolder.getAdapterPosition());
           final int deletedIndex = viewHolder.getAdapterPosition();

           // Suppression from Adapter

           favoriteAdapter.removeItem(deletedIndex);

           // Suppression de la RoomDatabase
           Common.favoriteRepository.delete(deleteItem);

           Snackbar snackbar = Snackbar.make(rootLayout, new StringBuilder(name).append(" Supprimer de la liste des favoris").toString(), Snackbar.LENGTH_LONG);
            snackbar.setAction("ANNULER", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    favoriteAdapter.restoreItem(deleteItem,deletedIndex);
                    Common.favoriteRepository.insertFav(deleteItem);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
       }
    }
}
