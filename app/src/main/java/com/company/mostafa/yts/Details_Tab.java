package com.company.mostafa.yts;

/**
 * Created by mostafa on 6/26/2018.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.company.mostafa.yts.Common.RequestHandler;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class Details_Tab extends Fragment {
    public String id;
    ProgressBar barScreen;
    ImageView img_screen1, img_screen2, img_screen3;
    TextView movie_Description;
//    String Video_Id = "";
    AdView adView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null){
        }else {
            id=savedInstanceState.getString("Detail");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.details_tab, container, false);

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.

        savedInstanceState.putString("Detail", id);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        barScreen = (ProgressBar) getActivity().findViewById(R.id.barScreen);
        img_screen1 = (ImageView) getActivity().findViewById(R.id.movie_Image1);
        img_screen2 = (ImageView) getActivity().findViewById(R.id.movie_Image2);
        img_screen3 = (ImageView) getActivity().findViewById(R.id.movie_Image3);
        movie_Description = (TextView) getActivity().findViewById(R.id.movie_Description);


        loadScreen(id);

        MobileAds.initialize(getContext(), "ca-app-pub-4767740334220169~5956844350");
        adView = (AdView) getActivity().findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

    }
    private void loadScreen(String movie_id) {
        String ROOT_URL = "https://yts.am/api/v2/movie_details.json?movie_id=" + movie_id + "&with_images=true";
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ROOT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        barScreen.setVisibility(View.GONE);

                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONObject url = obj.getJSONObject("data");
                            JSONObject movie = url.getJSONObject("movie");
                            movie_Description.setText(movie.getString("description_full"));
                           // Video_Id = movie.getString("yt_trailer_code");
                            Picasso.with(getContext()).load(movie.getString("large_screenshot_image1")).fit().into(img_screen1);
                            Picasso.with(getContext()).load(movie.getString("large_screenshot_image2")).fit().into(img_screen2);
                            Picasso.with(getContext()).load(movie.getString("large_screenshot_image3")).fit().into(img_screen3);


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
        loadScreen(id);
    }

}
