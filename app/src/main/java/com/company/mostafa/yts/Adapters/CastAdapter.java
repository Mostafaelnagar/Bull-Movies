package com.company.mostafa.yts.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.company.mostafa.yts.Models.Movies;
import com.company.mostafa.yts.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mostafa on 2/4/2018.
 */

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.ViewHolder> {
    private List<Movies> listItems;
    private Context context;
    private int lastPosition = -1;

    public CastAdapter(Context context, List<Movies> listItems) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public CastAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cast, parent, false);
        return new CastAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CastAdapter.ViewHolder holder, int position) {
        final Movies item = listItems.get(position);
        Picasso.with(context).load(item.getUrl_cast_image()).into(holder.movie_Image);

        holder.person_Name.setText(item.getCast_name());
        holder.char_name.setText(item.getChar_name());
        setAnimation(holder.itemView, position);


    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView movie_Image;
        private TextView person_Name, char_name;

        public ViewHolder(View itemView) {
            super(itemView);

            movie_Image = (CircleImageView) itemView.findViewById(R.id.movie_Image);
            person_Name = (TextView) itemView.findViewById(R.id.person_name);
            char_name = (TextView) itemView.findViewById(R.id.char_name);

        }
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
