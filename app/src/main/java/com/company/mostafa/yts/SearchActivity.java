package com.company.mostafa.yts;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SearchActivity extends AppCompatActivity {
    EditText edtSearch;
    RecyclerView recycler_movies;
    RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private List<Movies> listItems;
    ProgressBar progressBar;
    MaterialSpinner qualitySpinner, orderSpinner, ratingsSpinner;
    String quality = "";
    String genres = "";
    String ratings = "";
    AdView adView;
    Button btnSearch, btnQuality, btnOrderBy, btnRatings, btn_Next, btn_Prev;
    int page_Number = 1;
    CardView cardView;

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
        setContentView(R.layout.activity_search);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnQuality = (Button) findViewById(R.id.btnQuality);
        btnOrderBy = (Button) findViewById(R.id.btnOrderBY);
        btnRatings = (Button) findViewById(R.id.btnRate);
        btn_Next = (Button) findViewById(R.id.btn_Next);
        btn_Prev = (Button) findViewById(R.id.btn_Prev);
        edtSearch = (EditText) findViewById(R.id.edtSearch);
        cardView= (CardView) findViewById(R.id.card);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        recycler_movies = (RecyclerView) findViewById(R.id.recycler_Search);
        recycler_movies.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_movies.setLayoutManager(layoutManager);
        listItems = new ArrayList<>();
        btn_Prev.setVisibility(View.GONE);
        btn_Next.setVisibility(View.GONE);
        btnQuality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showQualityDialog();
            }
        });
        btnOrderBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCategoriesDialog();

            }
        });
        btnRatings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRatingsDialog();

            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page_Number=1;
                listItems.clear();
                LoadIntoRecylerView(edtSearch.getText().toString().toLowerCase(), ratings, quality, genres, page_Number);

            }
        });
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equals("")) {
                    listItems.clear();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        btn_Prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(SearchActivity.this, ""+page_Number, Toast.LENGTH_LONG).show();
                if (page_Number == 1) {
                    //LoadIntoRecylerView(edtSearch.getText().toString().toLowerCase(), ratings, quality, genres,page_Number);
                    Toast.makeText(SearchActivity.this, "Sorry this is first page", Toast.LENGTH_LONG).show();

                } else {
                    page_Number = page_Number - 1;
                    listItems.clear();
                    LoadIntoRecylerView(edtSearch.getText().toString().toLowerCase(), ratings, quality, genres, page_Number);
                    Toast.makeText(SearchActivity.this, "" + page_Number, Toast.LENGTH_SHORT).show();
                }

            }
        });
        btn_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listItems.clear();
                page_Number = page_Number + 1;
                LoadIntoRecylerView(edtSearch.getText().toString().toLowerCase(), ratings, quality, genres, page_Number);
                Toast.makeText(SearchActivity.this, "" + page_Number, Toast.LENGTH_SHORT).show();
            }
        });
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-4767740334220169~5956844350");
        adView = (AdView) findViewById(R.id.adSearchView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        recycler_movies.setOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                hideViews();
            }

            @Override
            public void onShow() {
                showViews();
            }
        });


    }


    private void hideViews() {
//        appBarLayout = (AppBarLayout) findViewById(R.id.appBar);
//        appBarLayout.animate().translationY(-appBarLayout.getHeight()).setInterpolator(new AccelerateInterpolator(2));
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) cardView.getLayoutParams();
        int fabBottomMargin = lp.bottomMargin;
        btn_Next.animate().translationY(btn_Next.getHeight() + fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();
        btn_Prev.animate().translationY(btn_Prev.getHeight() + fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();

    }

    private void showViews() {
//        appBarLayout.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        btn_Next.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        btn_Prev.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
    }

    public void LoadIntoRecylerView(String search, String rate, String quality, String genre, int page) {

        progressBar.setVisibility(View.VISIBLE);
        String ROOT_URL = "https://yts.am/api/v2/list_movies.json?query_term=" + search
                + "&minimum_rating=" + rate +
                "&quality=" + quality +
                "&genre=" + genre +
                "&page=" + page;
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ROOT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressBar.setVisibility(View.GONE);
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONObject url = obj.getJSONObject("data");
                            JSONArray array = url.getJSONArray("movies");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject o = array.getJSONObject(i);
                                Movies item = new Movies();
                                item.setMovie_id(o.getString("id"));
                                item.setTitle(o.getString("title"));
                                item.setRating(o.getString("rating"));
                                item.setLarge_cover_image(o.getString("large_cover_image"));
                                listItems.add(item);

                            }
                            adapter = new MoviesAdapter(getApplicationContext(), listItems);
                            adapter.notifyDataSetChanged();
                            recycler_movies.setAdapter(adapter);
                            btn_Prev.setVisibility(View.VISIBLE);
                            btn_Next.setVisibility(View.VISIBLE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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

    private void showQualityDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SearchActivity.this);
        alertDialog.setTitle(getString(R.string.Quality_button));
        alertDialog.setMessage(getString(R.string.Quality_Header));
        LayoutInflater inflater = this.getLayoutInflater();
        View updateStatus = inflater.inflate(R.layout.quality_search, null);
        qualitySpinner = updateStatus.findViewById(R.id.qualitySpinner);
        qualitySpinner.setItems("All", "All", "3D", "720p", "1080p");
        qualitySpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                if (item.equals("All")) {
                    quality = "";
                } else {
                    quality = item;
                }
            }
        });
        alertDialog.setView(updateStatus);

        alertDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                //update Info
                quality = String.valueOf(qualitySpinner.getSelectedIndex());


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

    String gen = "";

    private void showCategoriesDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SearchActivity.this);
        alertDialog.setTitle(getString(R.string.Cat_button));
        alertDialog.setMessage(getString(R.string.Cat_Header));
        LayoutInflater inflater = this.getLayoutInflater();
        View updateStatus = inflater.inflate(R.layout.order_by, null);
        orderSpinner = updateStatus.findViewById(R.id.orderSpinner);
        orderSpinner.setItems("Action", "Action", "Animation", "Adventure", "Comedy", "Crime", "Drama", "Fantasy", "Horror", "Mystery", "Romance", "Sci-Fi", "Thriller", "Western");
        orderSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                genres = item;

            }
        });
        alertDialog.setView(updateStatus);

        alertDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // genres = gen;
                dialogInterface.dismiss();
                //update Info


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

    private void showRatingsDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SearchActivity.this);
        alertDialog.setTitle(getString(R.string.Ratings_button));
        alertDialog.setMessage(getString(R.string.Ratings_Header));
        LayoutInflater inflater = this.getLayoutInflater();
        View updateStatus = inflater.inflate(R.layout.ratings, null);
        ratingsSpinner = updateStatus.findViewById(R.id.ratingsSpinner);
        ratingsSpinner.setItems("+1", "+1", "+2", "+3", "+4", "+5", "+6", "+7", "+8", "+9");
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

                //
                //

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

    public abstract class HidingScrollListener extends RecyclerView.OnScrollListener {
        private static final int HIDE_THRESHOLD = 20;
        private int scrolledDistance = 0;
        private boolean controlsVisible = true;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            int firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
            //show views if first item is first visible position and views are hidden
            if (firstVisibleItem == 0) {
                if (!controlsVisible) {
                    onShow();
                    controlsVisible = true;
                }
            } else {
                if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
                    onHide();
                    controlsVisible = false;
                    scrolledDistance = 0;
                } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
                    onShow();
                    controlsVisible = true;
                    scrolledDistance = 0;
                }
            }

            if ((controlsVisible && dy > 0) || (!controlsVisible && dy < 0)) {
                scrolledDistance += dy;
            }
        }

        public abstract void onHide();

        public abstract void onShow();
    }

}
