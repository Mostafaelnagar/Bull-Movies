package com.company.mostafa.yts;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.company.mostafa.yts.Common.ConnectivityReceiver;
import com.company.mostafa.yts.Common.MyApplication;
import com.company.mostafa.yts.Common.RequestHandler;
import com.company.mostafa.yts.Models.Banner;
import com.company.mostafa.yts.Models.Movies;
import com.company.mostafa.yts.Notifications.NotificationEventReceiver;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dmax.dialog.SpotsDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Home extends AppCompatActivity
        implements ConnectivityReceiver.ConnectivityReceiverListener, NavigationView.OnNavigationItemSelectedListener {
    RecyclerView recycler_movies;
    RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private List<Movies> listItems;
    int page_Number = 1;
    String genres = "";
    TextView error_view;
    private InterstitialAd interstitialAd;
    Handler handler;
    private boolean isRunning;
    Toolbar toolbar;
    HashMap<String, String> banner_List;
    SliderLayout sliderLayout;
    NavigationView navigationView;
    BottomNavigationView bottomNavigationView;

    //36162c1a2c0d426a0089ee06883f5035 my APIKEY
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

        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Manually checking internet connection
        error_view = (TextView) findViewById(R.id.error_view);
        recycler_movies = (RecyclerView) findViewById(R.id.recycler_menu);
        recycler_movies.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
//        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        GridLayoutManager mGrid = new GridLayoutManager(this, 2);
        recycler_movies.setLayoutManager(mGrid);
        //recycler_movies.setLayoutManager(layoutManager);
        listItems = new ArrayList<>();
        checkConnection();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        //init botoom navigationView
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.recent:
                        startActivity(new Intent(Home.this, LatestActivity.class));
                        break;
                    case R.id.wishList:
                        startActivity(new Intent(Home.this, FavoActivity.class));
                        break;
                    case R.id.search:
                        startActivity(new Intent(Home.this, SearchActivity.class));
                        break;

                }
                return true;
            }
        });
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
//        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
//        layoutParams.setBehavior(new BottomNavigationBehavior());


        handler = new Handler();
        // Define the code block to be executed
        Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                // Do something here on the main thread
                interstitialAd = new InterstitialAd(getApplicationContext());
                // set the ad unit ID
                interstitialAd.setAdUnitId("ca-app-pub-4767740334220169/8136404925");

                AdRequest adRequest = new AdRequest.Builder().build();

                // Load ads into Interstitial Ads
                interstitialAd.loadAd(adRequest);

                interstitialAd.setAdListener(new AdListener() {
                    public void onAdLoaded() {
                        showInterstitial();
                    }
                });
                // Repeat this the same runnable code block again another 2 seconds
                // 'this' is referencing the Runnable object
                handler.postDelayed(this, 240000);
            }
        };
        // Start the initial runnable task by posting through the handler
        handler.post(runnableCode);
        //init Slider
        getSlider();
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
        NotificationEventReceiver.setupAlarm(getApplicationContext());
    }


    private void hideViews() {
//        appBarLayout = (AppBarLayout) findViewById(R.id.appBar);
//        appBarLayout.animate().translationY(-appBarLayout.getHeight()).setInterpolator(new AccelerateInterpolator(2));
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) sliderLayout.getLayoutParams();
        int fabBottomMargin = lp.bottomMargin;
        bottomNavigationView.animate().translationY(bottomNavigationView.getHeight() + fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();

    }

    private void showViews() {
//        appBarLayout.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        bottomNavigationView.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
    }

    //get Now Playing Movies in Slider
    private void getSlider() {
        sliderLayout = (SliderLayout) findViewById(R.id.img_Slider);
        banner_List = new HashMap<>();
        String ROOT_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=36162c1a2c0d426a0089ee06883f5035&language=en-US&page=1";
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ROOT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject obj = new JSONObject(response);

                            JSONArray array = obj.getJSONArray("results");

                            // listItems.clear();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject o = array.getJSONObject(i);
                                Banner banner = new Banner();
                                banner.setId(o.getString("id"));
                                banner.setBn_Name(o.getString("title"));
                                banner.setBn_img("http://image.tmdb.org/t/p/w185" + o.getString("poster_path"));
                                banner_List.put(banner.getBn_Name() + "_" + banner.getId(), banner.getBn_img());

                            }
                            for (String key : banner_List.keySet()) {
                                String[] keySplit = key.split("_");
                                String bn_Title = keySplit[0];
                                String bn_Id = keySplit[1];
                                TextSliderView textSliderView = new TextSliderView(getBaseContext());
                                textSliderView.description(bn_Title)
                                        .image(banner_List.get(key))
                                        .setScaleType(BaseSliderView.ScaleType.Fit)
                                        .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                            @Override
                                            public void onSliderClick(BaseSliderView slider) {
                                                sliderLayout.startAutoCycle();
                                            }
                                        });
                                sliderLayout.addSlider(textSliderView);
                            }
                            sliderLayout.setPresetTransformer(SliderLayout.Transformer.Background2Foreground);
                            sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                            sliderLayout.setCustomAnimation(new DescriptionAnimation());
                            sliderLayout.setDuration(4000);
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.Next) {
            listItems.clear();
            page_Number = page_Number + 1;
            LoadIntoRecylerView(String.valueOf(page_Number), genres, "");
            Toast.makeText(this, "" + page_Number, Toast.LENGTH_LONG).show();

            return true;
        } else if (id == R.id.Prev) {
            if (page_Number == 1) {
                page_Number = 1;
                Toast.makeText(this, "Sorry this is first page", Toast.LENGTH_LONG).show();
                return true;
            } else {
                page_Number = page_Number - 1;
                listItems.clear();
                LoadIntoRecylerView(String.valueOf(page_Number), genres, "");
                Toast.makeText(this, "" + page_Number, Toast.LENGTH_LONG).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        page_Number = 1;
        switch (id) {
            case R.id.action:
                genres = "action";
                listItems.clear();
                LoadIntoRecylerView(String.valueOf(page_Number), genres, "");
                break;
            case R.id.Animation:
                genres = "Animation";
                listItems.clear();
                LoadIntoRecylerView(String.valueOf(page_Number), genres, "");
                break;
            case R.id.Adventure:
                genres = "Adventure";
                listItems.clear();
                LoadIntoRecylerView(String.valueOf(page_Number), genres, "");
                break;
            case R.id.Crime:
                genres = "Crime";
                listItems.clear();
                LoadIntoRecylerView(String.valueOf(page_Number), genres, "");
                break;
            case R.id.Comedy:
                genres = "Comedy";
                listItems.clear();
                LoadIntoRecylerView(String.valueOf(page_Number), genres, "");
                break;
            case R.id.Drama:
                genres = "Drama";
                listItems.clear();
                LoadIntoRecylerView(String.valueOf(page_Number), genres, "");
                break;
            case R.id.Fantasy:
                genres = "Fantasy";
                listItems.clear();
                LoadIntoRecylerView(String.valueOf(page_Number), genres, "");
                break;
            case R.id.Horror:
                genres = "Horror";
                listItems.clear();
                LoadIntoRecylerView(String.valueOf(page_Number), genres, "");
                break;
            case R.id.Mystery:
                genres = "Mystery";
                listItems.clear();
                LoadIntoRecylerView(String.valueOf(page_Number), genres, "");
                break;
            case R.id.Romance:
                genres = "Romance";
                listItems.clear();
                LoadIntoRecylerView(String.valueOf(page_Number), genres, "");
                break;
            case R.id.Thriller:
                genres = "Thriller";
                listItems.clear();
                LoadIntoRecylerView(String.valueOf(page_Number), genres, "");
                break;
            case R.id.Western:
                genres = "Western";
                listItems.clear();
                LoadIntoRecylerView(String.valueOf(page_Number), genres, "");
                break;
            case R.id.Sci_Fi:
                genres = "Sci-Fi";
                listItems.clear();
                LoadIntoRecylerView(String.valueOf(page_Number), genres, "");
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void LoadIntoRecylerView(String page, String genr, String year) {
        final SpotsDialog progressDialog = new SpotsDialog(Home.this);
        progressDialog.show();
        String ROOT_URL = "https://yts.am/api/v2/list_movies.json?page=" + page + "&genre=" + genr + "&query_term=" + year;
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ROOT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONObject url = obj.getJSONObject("data");
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
                            recycler_movies.setAdapter(adapter);
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


    // Method to manually check connection status
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {


            //Toast.makeText(this,""+message,Toast.LENGTH_LONG).show();

            LoadIntoRecylerView(String.valueOf(page_Number), genres, "");
            if (listItems.size() < 0) {
                error_view.setVisibility(View.VISIBLE);

            } else {
                error_view.setVisibility(View.GONE);
            }
        } else {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.drawer_layout), message, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
        }


    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener

        MyApplication.getInstance().setConnectivityListener(this);

    }

    private void showInterstitial() {
        if (interstitialAd.isLoaded()) {
            if (isRunning && interstitialAd.isLoaded()) {
                interstitialAd.show();
            }

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        isRunning = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isRunning = false;
        sliderLayout.stopAutoCycle();
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
