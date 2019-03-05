package com.company.mostafa.yts.Adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.company.mostafa.yts.Models.Movies;
import com.company.mostafa.yts.R;
import com.company.mostafa.yts.Tab_Details_Activity;

import java.util.List;

/**
 * Created by mostafa on 2/4/2018.
 */

public class TorrentsAdapter extends RecyclerView.Adapter<TorrentsAdapter.ViewHolder> {
    private List<Movies> listItems;
    private Context context;
    private int lastPosition = -1;

    public TorrentsAdapter(Context context, List<Movies> listItems) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public TorrentsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.torrents, parent, false);
        return new TorrentsAdapter.ViewHolder(view);
    }

    //Movies movie=null;
     Movies movie;
    @Override
    public void onBindViewHolder(final TorrentsAdapter.ViewHolder holder, int position) {
         movie = listItems.get(position);

        holder.quality.setText(movie.getQuality());
        holder.size.setText(movie.getSize());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSearchDialog();
            }
        });

//        holder.size.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Uri webpage = Uri.parse(item.getUrl_torrents());
//                Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
//                webIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.getApplicationContext().startActivity(webIntent);
//            }
//        });
//        holder.buttonViewOption.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                PopupMenu popup = new PopupMenu(context, holder.buttonViewOption);
//                //inflating menu from xml resource
//                popup.inflate(R.menu.torrents_menu);
//                //adding click listener
//                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//                        switch (item.getItemId()) {
//                            case R.id.CopyURL:
//                                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
//                                ClipData clip = ClipData.newPlainText("text", movie.getUrl_torrents());
//                                clipboard.setPrimaryClip(clip);
//                                Toast.makeText(context, "Movie Torrent URL Copied", Toast.LENGTH_SHORT).show();
//                                break;
//                            case R.id.download_torrents:
//                                Uri webpage = Uri.parse(movie.getUrl_torrents());
//                                Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
//                                webIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                context.getApplicationContext().startActivity(webIntent);
//                                break;
//
//                        }
//                        return false;
//                    }
//                });
//                //displaying the popup
//                popup.show();
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView quality, size;
        private LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            quality = (TextView) itemView.findViewById(R.id.quality);
            size = (TextView) itemView.findViewById(R.id.size);
//            buttonViewOption = (TextView) itemView.findViewById(R.id.textViewOptions);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.torrents_item);

        }
    }

    private void showSearchDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View updateStatus = inflater.inflate(R.layout.layout_download_ways, null);

        TextView txt_Copy = updateStatus.findViewById(R.id.txt_Copy);
        TextView txt_Down_File = updateStatus.findViewById(R.id.txt_Down_File);

        txt_Copy.setPaintFlags(txt_Copy.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txt_Down_File.setPaintFlags(txt_Down_File.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        txt_Copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("text", movie.getUrl_torrents());
                                clipboard.setPrimaryClip(clip);
                                Toast.makeText(context, "Movie Torrent URL Copied", Toast.LENGTH_SHORT).show();
            }
        });
        txt_Down_File.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri webpage = Uri.parse(movie.getUrl_torrents());
                                Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
                                webIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.getApplicationContext().startActivity(webIntent);
            }
        });
        alertDialog.setView(updateStatus);
        alertDialog.show();
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }
}
