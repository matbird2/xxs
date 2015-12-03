package com.xxs.leon.xxs.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kogitune.activity_transition.ActivityTransitionLauncher;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.xxs.leon.xxs.R;
import com.xxs.leon.xxs.activity.CommentActivity_;
import com.xxs.leon.xxs.activity.DetailActivity_;
import com.xxs.leon.xxs.rest.bean.Album;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florentchampigny on 24/04/15.
 */
public class HomeNewAlbumRecyclerViewAdapter extends RecyclerView.Adapter<HomeNewAlbumRecyclerViewAdapter.HomeAlbumViewHolder> {

    Context context;

    List<Album> contents = new ArrayList<>();

    static final int TYPE_HEADER = 0;
    static final int TYPE_CELL = 1;
    private IconicsDrawable error_icon;

    public HomeNewAlbumRecyclerViewAdapter(Context context,List<Album> contents){
        this.context = context;
        this.contents = contents;
        error_icon = new IconicsDrawable(context)
                .icon(GoogleMaterial.Icon.gmd_broken_image)
                .color(Color.GRAY)
                .sizeDp(60);
    }

    public void appenList(List<Album> contents){
        contents.addAll(contents);
        notifyDataSetChanged();
    }

    public void clear(){
        contents.clear();
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TYPE_HEADER;
            default:
                return TYPE_CELL;
        }
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    @Override
    public HomeAlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        switch (viewType) {
            case TYPE_HEADER: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_home_album_big, parent, false);
                return new HomeAlbumViewHolder(view);
            }
            case TYPE_CELL: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_home_album_small, parent, false);
                return new HomeAlbumViewHolder(view);
            }
        }
        return null;
    }


    @Override
    public void onBindViewHolder(final HomeAlbumViewHolder holder, int position) {
//        switch (getItemViewType(position)) {
//            case TYPE_HEADER:
//                break;
//            case TYPE_CELL:
//                break;
//        }
        Glide.with(context).load(contents.get(position).getCover()).error(error_icon).crossFade(500).centerCrop().into(holder.cover);
        holder.title.setText(contents.get(position).getName());

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(context, DetailActivity_.class);
                ActivityTransitionLauncher.with((AppCompatActivity)context).from(holder.cover).launch(intent);
            }
        });

    }

    class HomeAlbumViewHolder extends RecyclerView.ViewHolder {

        CardView card_view;
        ImageView cover;
        TextView title;
        public HomeAlbumViewHolder(View view){
            super(view);
            cover = (ImageView) view.findViewById(R.id.cover);
            title = (TextView) view.findViewById(R.id.title);
            card_view = (CardView) view.findViewById(R.id.card_view);
        }
    }
}