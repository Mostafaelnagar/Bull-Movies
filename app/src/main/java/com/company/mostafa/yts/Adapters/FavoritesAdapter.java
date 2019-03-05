package com.company.mostafa.yts.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.company.mostafa.yts.Models.Movies;
import com.company.mostafa.yts.Tab_Details_Activity;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by mostafa on 2/18/2018.
 */

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {
    private List<Movies> listItems;
    private Context context;
    private int lastPosition = -1;

    public FavoritesAdapter(Context context, List<Movies> listItems) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(com.company.mostafa.yts.R.layout.movie_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Movies item = listItems.get(position);
        holder.movie_Rate.setText(item.getRating());
        holder.movie_Title.setText(item.getTitle());
       final String images=item.getLarge_cover_image().toLowerCase();
        Picasso.with(context).load(images).into(holder.movie_Image);
        setAnimation(holder.itemView, position);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String movie_Id = item.getMovie_id();
                String movie_rate = item.getRating();
                String movie_image = images;
                String movie_Title = item.getTitle();
                Intent intent = new Intent(context, Tab_Details_Activity.class);
                intent.putExtra("MOVIE_ID", movie_Id);
                intent.putExtra("MOVIE_RATING", movie_rate);
                intent.putExtra("MOVIE_IMAGE", movie_image);
                intent.putExtra("MOVIE_TITLE", movie_Title);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.getApplicationContext().startActivity(intent);


            }
        });

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView movie_Rate, movie_Title;
        private ImageView movie_Image;
        private RelativeLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            movie_Rate = (TextView) itemView.findViewById(com.company.mostafa.yts.R.id.movie_Rate);
            movie_Title = (TextView) itemView.findViewById(com.company.mostafa.yts.R.id.movie_Title);
            movie_Image = (ImageView) itemView.findViewById(com.company.mostafa.yts.R.id.movie_Images);
            linearLayout = (RelativeLayout) itemView.findViewById(com.company.mostafa.yts.R.id.rel);


        }
    }
    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }


}
