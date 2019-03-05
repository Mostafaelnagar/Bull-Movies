package com.company.mostafa.yts;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
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
import com.company.mostafa.yts.Adapters.TorrentsAdapter;
import com.company.mostafa.yts.Common.RequestHandler;
import com.company.mostafa.yts.Models.Movies;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Torrents_Watch_Tab extends Fragment {
    public String VIDEO_ID = "";
    private ProgressBar barTorrents;
    RecyclerView recycler_Torrents;
    private RecyclerView.Adapter torrentsAdapter;
    private List<Movies> torrentsListItems;
    RecyclerView.LayoutManager layoutManager;
    AdView adView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
        } else {
            VIDEO_ID = savedInstanceState.getString("Torrents");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.torrents_watch, container, false);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putString("Torrents", VIDEO_ID);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        barTorrents = (ProgressBar) getActivity().findViewById(R.id.barTorrents);
        TextView click_here = (TextView) getActivity().findViewById(R.id.click_here);
        click_here.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), DocsActivity.class));
            }
        });
        barTorrents.setVisibility(View.VISIBLE);
//init Torrents
        recycler_Torrents = (RecyclerView) getActivity().findViewById(R.id.recycler_torrents);
        recycler_Torrents.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recycler_Torrents.setLayoutManager(layoutManager);
        torrentsListItems = new ArrayList<>();
        loadTorrents(VIDEO_ID);
        MobileAds.initialize(getContext(), "ca-app-pub-4767740334220169~5956844350");
        adView = (AdView) getActivity().findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

    }

    private void loadTorrents(final String movie_id) {
        String ROOT_URL = "https://yts.am/api/v2/movie_details.json?movie_id=" + movie_id + "&with_images=true&with_cast=true";
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ROOT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        barTorrents.setVisibility(View.GONE);
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONObject url = obj.getJSONObject("data");
                            JSONObject movie = url.getJSONObject("movie");

                            JSONArray torrent = movie.getJSONArray("torrents");
                            torrentsListItems.clear();
                            for (int i = 0; i < torrent.length(); i++) {
                                JSONObject o = torrent.getJSONObject(i);
                                Movies item = new Movies();
                                item.setUrl_torrents(o.getString("url"));
                                item.setQuality(o.getString("quality"));
                                item.setSize(o.getString("size"));

                                torrentsListItems.add(item);

                            }
                            torrentsAdapter = new TorrentsAdapter(getContext(), torrentsListItems);
                            torrentsAdapter.notifyDataSetChanged();
                            recycler_Torrents.setAdapter(torrentsAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Low Internet Connection", Toast.LENGTH_LONG).show(); // As of f605da3 the following should work

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


        RequestHandler.getInstance(getContext()).addRequestQueue(stringRequest);


    }

    @Override
    public void onResume() {
        super.onResume();
        loadTorrents(VIDEO_ID);
    }

}
