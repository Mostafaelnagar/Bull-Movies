package com.company.mostafa.yts;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
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
import com.company.mostafa.yts.Common.RequestHandler;
import com.company.mostafa.yts.Database.DataBase;
import com.company.mostafa.yts.Models.Movies;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.github.florent37.diagonallayout.DiagonalLayout;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class Tab_Details_Activity extends AppCompatActivity {

    ImageView img_movies, img_trailer;
    TextView year, movie_Rat, runTime, downTime, trailer;
    String Title = "";
    String imdb_code = "";
    public String movie_id = "";
    CollapsingToolbarLayout collapsingToolbarLayout;
    DataBase localDatabase;
    public String VIDEO_ID = "";
    KenBurnsView ken;
    String torr_Url = "";
    Toolbar toolbar;
    DiagonalLayout diagonalLayout;
    AdView adView;


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab__details_);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        //init Views
        img_movies = (ImageView) findViewById(R.id.pro);
        ken = (KenBurnsView) findViewById(R.id.ken);
        year = (TextView) findViewById(R.id.year);
        movie_Rat = (TextView) findViewById(R.id.rate);
        downTime = (TextView) findViewById(R.id.downTime);
        img_trailer = (ImageView) findViewById(R.id.trailerrr);
        runTime = (TextView) findViewById(R.id.runtime);
        diagonalLayout = (DiagonalLayout) findViewById(R.id.diagonalLayout);
        Button button_watch = (Button) findViewById(R.id.watch);
        //get Intent
        Intent intent = getIntent();
        movie_id = intent.getStringExtra("MOVIE_ID");
        final String movie_Rate = intent.getStringExtra("MOVIE_RATING");
        final String movie_image = intent.getStringExtra("MOVIE_IMAGE");
        Title = intent.getStringExtra("MOVIE_TITLE");
        imdb_code = intent.getStringExtra("MOVIE_imdb_code");
        //set Image and Movie rate
        Picasso.with(getApplicationContext()).load(movie_image).fit().into(img_movies);
        Picasso.with(getApplicationContext()).load(movie_image).into(ken);
        movie_Rat.setText(movie_Rate);
        //init collapsingToolbarLayout and setting Title
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.CollapseAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.ExpandAdapter);
        collapsingToolbarLayout.setTitle(Title);

        //rate setting icon
        movie_Rat.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        Drawable img = Tab_Details_Activity.this.getBaseContext().getResources().getDrawable(
                                R.drawable.star_rate);
                        img.setBounds(0, 0, img.getIntrinsicWidth() * movie_Rat.getMeasuredHeight() / img.getIntrinsicHeight(), movie_Rat.getMeasuredHeight());
                        movie_Rat.setCompoundDrawables(img, null, null, null);
                        movie_Rat.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });

        //settings of ToollBar
        toolbar = (Toolbar) findViewById(R.id.toolbars);
        toolbar.setTitle(Title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });
        //init SQLITE
        localDatabase = new DataBase(this);
        final FloatingActionButton actionButton = (FloatingActionButton) findViewById(R.id.btnfav);
        if (localDatabase.isFavorites(movie_id))
            actionButton.setImageResource(R.drawable.ic_favorite_black_24dp);

        actionButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                if (!localDatabase.isFavorites(movie_id)) {
                                                    localDatabase.addFavo(movie_id, Title, movie_Rate, movie_image
                                                    );
                                                    actionButton.setImageResource(R.drawable.ic_favorite_black_24dp);
                                                    Toast.makeText(Tab_Details_Activity.this, "" + Title + "IS ADD TO FAVORITE", Toast.LENGTH_LONG).show();
                                                } else {
                                                    localDatabase.removeFromFavorites(movie_id);
                                                    actionButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                                                    Toast.makeText(Tab_Details_Activity.this, "" + Title + "IS REMOVE FAVORITE", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        }
        );
        // load release year ,runTime and downTime
        loadDetailsMovies(movie_id);
// Actions
        img_trailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Tab_Details_Activity.this, MoviesDetails.class);
                intent1.putExtra("VIDEOID", VIDEO_ID);
                startActivity(intent1);
            }
        });
        button_watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getBaseContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("text", torr_Url);
                clipboard.setPrimaryClip(clip);
                showSearchDialog();
            }
        });

    }

//    @Override
//    public void onSaveInstanceState(Bundle savedInstanceState) {
//        super.onSaveInstanceState(savedInstanceState);
//        // Save UI state changes to the savedInstanceState.
//        // This bundle will be passed to onCreate if the process is
//        // killed and restarted.
//
//        savedInstanceState.putString("MyString", movie_id);
//        Toast.makeText(this, "Saved"+movie_id, Toast.LENGTH_SHORT).show();
//        // etc.
//    }
//
//    @Override
//    public void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        // Restore UI state from the savedInstanceState.
//        // This bundle has also been passed to onCreate.
//
//         movie_id = savedInstanceState.getString("MyString");
//        Toast.makeText(this, "Restored"+movie_id, Toast.LENGTH_SHORT).show();
//
//    }

    // load release year ,runTime and downTime
    private void loadDetailsMovies(final String movie_id) {
        String ROOT_URL = "https://yts.am/api/v2/movie_details.json?movie_id=" + movie_id + "&with_images=true&with_cast=true";
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ROOT_URL,
                new Response.Listener<String>() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onResponse(String response) {

                        // barTorrents.setVisibility(View.GONE);
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONObject url = obj.getJSONObject("data");
                            JSONObject movie = url.getJSONObject("movie");
                            JSONArray torrent = movie.getJSONArray("torrents");
                            String download = movie.getString("download_count");
                            String time = movie.getString("runtime");
                            String release = movie.getString("year");
                            VIDEO_ID = movie.getString("yt_trailer_code");
                            if (Locale.getDefault().getDisplayLanguage().equals("العربية")) {
                                toolbar.setNavigationIcon(R.drawable.right_back);
                                diagonalLayout.setDirection(View.LAYOUT_DIRECTION_RTL);
                                downTime.setText("تنزيلات" + download + " .");
                                year.setText("عام" + release);
                                runTime.setText(time + "دقيقة .");


                            } else {
                                toolbar.setNavigationIcon(R.drawable.left_back);
                                downTime.setText(". " + download + " downloaded");
                                year.setText(". release in " + release);
                                runTime.setText(time + "min");

                            }
                            if (torrent.length() > 0) {
                                JSONObject jsonObject = torrent.getJSONObject(1);
                                torr_Url = jsonObject.getString("url");
                            }
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
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    Details_Tab details_tab = new Details_Tab();
                    details_tab.id = movie_id;
                    return details_tab;
                case 1:
                    Torrents_Watch_Tab torrents_watch_tab = new Torrents_Watch_Tab();

                    torrents_watch_tab.VIDEO_ID = movie_id;

                    return torrents_watch_tab;
                case 2:
                    Recommended_Tab watch_tab = new Recommended_Tab();
                    watch_tab.id = movie_id;
                    return watch_tab;
                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String info = "";
            String torrents = "";
            String recomend = "";
            if (Locale.getDefault().getDisplayLanguage().equals("العربية")) {
                info = "معلومات";
                torrents = "مشاهدة/تحميل";
                recomend = "مقترح لك";
            } else {
                info = "Info";
                torrents = "Torrents/Watch";
                recomend = "Recommended";
            }
            switch (position) {
                case 0:
                    return info;
                case 1:
                    return torrents;
                case 2:
                    return recomend;
            }
            return null;
        }
    }

    private void showSearchDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Tab_Details_Activity.this);
        alertDialog.setMessage(getString(R.string.watch_message_ways));
        LayoutInflater inflater = this.getLayoutInflater();
        View updateStatus = inflater.inflate(R.layout.layout_watch_ways, null);

        TextView linkTextViewInstall = updateStatus.findViewById(R.id.linkTextViewInstall);
        TextView linkTextViewWatchBrowser = updateStatus.findViewById(R.id.linkTextViewWatchBrowser);
        TextView linkTextViewWatch = updateStatus.findViewById(R.id.linkTextViewWatchSeedr);
        linkTextViewInstall.setPaintFlags(linkTextViewInstall.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        linkTextViewWatchBrowser.setPaintFlags(linkTextViewWatchBrowser.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        linkTextViewWatch.setPaintFlags(linkTextViewWatch.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        // Use package name which we want to check
        boolean isAppInstalled = appInstalledOrNot("com.techbii.seedr");

        if (isAppInstalled) {
            //This intent will help you to launch if the package is already installed
            linkTextViewInstall.setVisibility(View.GONE);
            linkTextViewWatch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent LaunchIntent = getPackageManager()
                            .getLaunchIntentForPackage("com.techbii.seedr");
                    startActivity(LaunchIntent);
                }
            });
        } else {
            // Do whatever we want to do if application not installed
            // For example, Redirect to play store
            linkTextViewInstall.setVisibility(View.VISIBLE);
            linkTextViewWatch.setVisibility(View.GONE);
            linkTextViewInstall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=com.techbii.seedr")));
                }
            });


        }
        linkTextViewWatchBrowser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setToolbarColor(ContextCompat.getColor(Tab_Details_Activity.this, R.color.colorAccent));
                builder.addDefaultShareMenuItem();
                builder.setShowTitle(true);
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(Tab_Details_Activity.this, Uri.parse("https://www.seedr.cc/"));
            }
        });
        alertDialog.setView(updateStatus);
        alertDialog.show();
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }
}
