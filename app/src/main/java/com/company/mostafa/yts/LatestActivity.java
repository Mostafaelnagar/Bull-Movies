package com.company.mostafa.yts;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.company.mostafa.yts.Adapters.MoviesAdapter;
import com.company.mostafa.yts.Common.RequestHandler;
import com.company.mostafa.yts.Models.Movies;
import com.company.mostafa.yts.Models.Movies_Of_Year;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LatestActivity extends AppCompatActivity {
    RecyclerView recycler_LatestMovies;
    RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private List<Movies> listItems;
    MaterialSpinner categorySpinner, ratingsSpinner;
    String genres = "";
    String ratings = "";
    int page_Number = 1;
    AdView adView;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/restaurant_font.otf")
                .setFontAttrId(R.attr.fontPath)
                .build());
        setContentView(R.layout.activity_latest);
        //init views
        recycler_LatestMovies = (RecyclerView) findViewById(R.id.recycler_Latest);
        recycler_LatestMovies.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_LatestMovies.setLayoutManager(layoutManager);
        listItems = new ArrayList<>();
        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSearchDialog();
            }
        });

        //Load All Latest Movies
        loadLatest("", "");
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-4767740334220169~5956844350");
        adView = (AdView) findViewById(R.id.adLatestView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private void loadLatest(String genre, String minRate) {
        final SpotsDialog progressDialog = new SpotsDialog(LatestActivity.this);
        progressDialog.show();
        String ROOT_URL = "https://yts.am/api/v2/list_movies.json?sort_by=year" +
                "&genre=" + genre +
                "&minimum_rating=" + minRate +
                "&page=" + page_Number;
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ROOT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONObject url = obj.getJSONObject("data");
//                            page_Number = Integer.parseInt(url.getString("page_number"));
//                            Toast.makeText(LatestActivity.this, "page Number"+page_Number, Toast.LENGTH_SHORT).show();
                            JSONArray array = url.getJSONArray("movies");

                            // listItems.clear();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject o = array.getJSONObject(i);
                                Movies item = new Movies();
                                item.setMovie_id(o.getString("id"));
                                item.setTitle(o.getString("title"));
                                item.setRating(o.getString("rating"));
                                item.setImdb_code(o.getString("imdb_code"));
                                item.setLarge_cover_image(o.getString("medium_cover_image"));

                                listItems.add(item);

                            }
                            adapter = new MoviesAdapter(getApplicationContext(), listItems);
                            adapter.notifyDataSetChanged();
                            recycler_LatestMovies.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Low Internet Connection", Toast.LENGTH_LONG).show(); // As of f605da3 the following should work

                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null) {
                        }

                        if (error instanceof TimeoutError) {
                        } else if (error instanceof NoConnectionError) {
                        } else if (error instanceof AuthFailureError) {
                        } else if (error instanceof ServerError) {
                        } else if (error instanceof NetworkError) {
                        } else if (error instanceof ParseError) {
                        }
                    }
                });


        RequestHandler.getInstance(this).addRequestQueue(stringRequest);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.latestmovies, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.lates_Next) {
            listItems.clear();
            page_Number = page_Number + 1;
            loadLatest(genres, ratings);
            Toast.makeText(this, "" + page_Number, Toast.LENGTH_LONG).show();

            return true;
        } else if (id == R.id.latest_Prev) {
            page_Number = page_Number - 1;
            if (page_Number == 0) {
                page_Number = 1;
                Toast.makeText(this, "Sorry this is first page", Toast.LENGTH_LONG).show();

                return true;
            } else {
                listItems.clear();
                loadLatest(genres, ratings);
                Toast.makeText(this, "" + page_Number, Toast.LENGTH_LONG).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSearchDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LatestActivity.this);
        alertDialog.setMessage(getString(R.string.dialog_message));
        LayoutInflater inflater = this.getLayoutInflater();
        View updateStatus = inflater.inflate(R.layout.layout_latest_search, null);

        categorySpinner = updateStatus.findViewById(R.id.latest_category_spinner);
        ratingsSpinner = updateStatus.findViewById(R.id.latest_rating_spinner);
        //Spinners Actions
        categorySpinner.setItems("All", "Action", "Animation", "Adventure", "Comedy", "Crime", "Drama", "Fantasy", "Horror", "Mystery", "Romance", "Sci-Fi", "Thriller", "Western");
        categorySpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                genres = item;
            }
        });

        ratingsSpinner.setItems("All", "+1", "+2", "+3", "+4", "+5", "+6", "+7", "+8", "+9");
        ratingsSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                ratings = Character.toString(item.charAt(1));

            }
        });
        alertDialog.setView(updateStatus);

        alertDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, final int i) {
                dialogInterface.dismiss();
                page_Number = 1;
                listItems.clear();
                loadLatest(genres, ratings);

            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }
}
