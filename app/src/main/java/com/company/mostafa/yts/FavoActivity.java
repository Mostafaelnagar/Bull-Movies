package com.company.mostafa.yts;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.company.mostafa.yts.Adapters.FavoritesAdapter;
import com.company.mostafa.yts.Database.DataBase;
import com.company.mostafa.yts.Models.Movies;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FavoActivity extends AppCompatActivity {
    RecyclerView recycler_movies;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    List<Movies> listItems = new ArrayList<>();
    TextView error_view;
    AdView adView;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favo);
        error_view = (TextView) findViewById(R.id.error_view_Favo);

        recycler_movies = (RecyclerView) findViewById(R.id.recycler_Favorites);
        recycler_movies.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_movies.setLayoutManager(layoutManager);
        loadFavorites();
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-4767740334220169~5956844350");
        adView = (AdView) findViewById(R.id.adFavoView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private void loadFavorites() {
        listItems = new DataBase(this).getMovies();
        if (listItems.size() < 0) {
            error_view.setVisibility(View.VISIBLE);

        } else {
            error_view.setVisibility(View.GONE);
            adapter = new FavoritesAdapter(this, listItems);
            recycler_movies.setAdapter(adapter);
        }


    }
}
