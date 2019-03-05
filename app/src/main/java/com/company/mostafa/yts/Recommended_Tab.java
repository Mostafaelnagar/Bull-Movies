package com.company.mostafa.yts;

/**
 * Created by mostafa on 6/26/2018.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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
import com.company.mostafa.yts.Adapters.Recommended;
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

public class Recommended_Tab extends Fragment {
    public String id = "";
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recycler_Recommended;
    private RecyclerView.Adapter recommendedAdapter;
    private List<Movies> recommendedListItems;

    private ProgressBar barReco;
    AdView adView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
        } else {
            id = savedInstanceState.getString("Recommended");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recommended_tab, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        barReco = (ProgressBar) getActivity().findViewById(R.id.barRecomended);
        barReco.setVisibility(View.VISIBLE);
        //init Recommended
        recycler_Recommended = (RecyclerView) getActivity().findViewById(R.id.recycler_Recomended);
        recycler_Recommended.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recycler_Recommended.setLayoutManager(layoutManager);
        recommendedListItems = new ArrayList<>();
        LoadRecommended(id);
        MobileAds.initialize(getContext(), "ca-app-pub-4767740334220169~5956844350");
        adView = (AdView) getActivity().findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    public void LoadRecommended(String movie_Id) {

        String ROOT_URL = "https://yts.am/api/v2/movie_suggestions.json?movie_id=" + movie_Id;
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ROOT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        barReco.setVisibility(View.GONE);
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
                                item.setMedium_cover_image(o.getString("medium_cover_image"));

                                recommendedListItems.add(item);

                            }
                            recommendedAdapter = new Recommended(getContext(), recommendedListItems);
                            recommendedAdapter.notifyDataSetChanged();
                            recycler_Recommended.setAdapter(recommendedAdapter);
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


        RequestHandler.getInstance(getContext()).addRequestQueue(stringRequest);


    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.

        savedInstanceState.putString("Recommended", id);
    }

    @Override
    public void onResume() {
        super.onResume();
        recommendedListItems.clear();
        LoadRecommended(id);
    }
}
