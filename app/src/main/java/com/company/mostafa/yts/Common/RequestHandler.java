package com.company.mostafa.yts.Common;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by mos on 2/19/2017.
 */

public class RequestHandler {
    private static  RequestHandler mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;
    private RequestHandler(Context context){
        mCtx =context;
        mRequestQueue = getRequestQueue();

    }

    public static synchronized RequestHandler getInstance(Context context){
if (mInstance==null){
    mInstance= new RequestHandler(context);
}
        return mInstance;
    }
    //give us the requestqueue
    public RequestQueue getRequestQueue(){
        if (mRequestQueue==null){

            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());

        }
        return mRequestQueue;
    }
    // add the request object to the requestqueue
    public <T> void addRequestQueue(Request<T> req){
        getRequestQueue().add(req);
    }


}
