package com.xxs.leon.xxs.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xxs.leon.xxs.R;
import com.xxs.leon.xxs.rest.bean.Album;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florentchampigny on 24/04/15.
 */
@EBean
public class HomeNewAlbumRecyclerViewAdapter extends RecyclerView.Adapter<HomeNewAlbumRecyclerViewAdapter.HomeAlbumViewHolder> {

    @RootContext
    Context context;

    List<Album> contents = new ArrayList<>();

    static final int TYPE_HEADER = 0;
    static final int TYPE_CELL = 1;

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
    public void onBindViewHolder(HomeAlbumViewHolder holder, int position) {
//        switch (getItemViewType(position)) {
//            case TYPE_HEADER:
//                break;
//            case TYPE_CELL:
//                break;
//        }
        Glide.with(context).load(contents.get(position).getCover()).into(holder.cover);
        holder.title.setText(contents.get(0).getName());
    }

    class HomeAlbumViewHolder extends RecyclerView.ViewHolder {

        ImageView cover;
        TextView title;
        public HomeAlbumViewHolder(View view){
            super(view);
            cover = (ImageView) view.findViewById(R.id.cover);
            title = (TextView) view.findViewById(R.id.title);
        }
    }
}