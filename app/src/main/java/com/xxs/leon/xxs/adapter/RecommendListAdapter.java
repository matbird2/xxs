package com.xxs.leon.xxs.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.xxs.leon.xxs.R;
import com.xxs.leon.xxs.activity.DetailActivity_;
import com.xxs.leon.xxs.rest.bean.Album;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maliang on 16/1/11.
 */
public class RecommendListAdapter extends RecyclerView.Adapter<RecommendListAdapter.ItemViewHolder> {

    Context context;
    List<Album> contents = new ArrayList<>();

    private IconicsDrawable error_icon;

    public RecommendListAdapter(Context context) {
        this.context = context;
        error_icon = new IconicsDrawable(context)
                .icon(GoogleMaterial.Icon.gmd_broken_image)
                .color(Color.GRAY)
                .sizeDp(60);
    }

    public void appenList(List<Album> list) {
        if (!contents.containsAll(list) && list != null && list.size() > 0) {
            contents.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void clear() {
        contents.clear();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(context).inflate(R.layout.list_item_album, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder itemViewHolder, int position) {
        final Album album = contents.get(position);
        Glide.with(context).load(album.getCover()).error(error_icon).crossFade(500).centerCrop().into(itemViewHolder.cover);
        itemViewHolder.title.setText(album.getName());
        itemViewHolder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailActivity_.intent(context).albumId(album.getObjectId()).albumName(album.getName()).start();
            }
        });
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        CardView card_view;
        ImageView cover;
        TextView title;

        public ItemViewHolder(View view) {
            super(view);
            cover = (ImageView) view.findViewById(R.id.cover);
            title = (TextView) view.findViewById(R.id.title);
            card_view = (CardView) view.findViewById(R.id.card_view);
        }
    }
}