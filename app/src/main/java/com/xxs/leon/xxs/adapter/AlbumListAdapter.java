package com.xxs.leon.xxs.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
 * Created by maliang on 15/12/10.
 */
public class AlbumListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int CELL_TYPE = 0;
    private static final int EMPTY_TYPE = 1;
    private static final int FOOTER_TYPE = 2;

    Context context;
    List<Album> contents = new ArrayList<>();

    private IconicsDrawable error_icon;

    public AlbumListAdapter(Context context) {
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
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (contents.size() == 0) {
            return EMPTY_TYPE;
        } else if (position + 1 == getItemCount()) {
            return FOOTER_TYPE;
        } else {
            return CELL_TYPE;
        }
    }

    public boolean isFooter(int position){
        return position == contents.size()+1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == CELL_TYPE) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item_album, parent, false);
            return new ItemViewHolder(view);
        } else if (viewType == FOOTER_TYPE) {
            view = LayoutInflater.from(context).inflate(R.layout.footer_view, parent, false);
            return new FooterViewHolder(view);
        } else if (viewType == EMPTY_TYPE) {
            view = LayoutInflater.from(context).inflate(R.layout.empty_view, parent, false);
            return new EmptyViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            final Album album = contents.get(position);
            Glide.with(context).load(album.getCover()).error(error_icon).crossFade(500).centerCrop().into(itemViewHolder.cover);
            itemViewHolder.title.setText(album.getName());
            itemViewHolder.card_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DetailActivity_.intent(context).albumId(album.getObjectId()).albumName(album.getName()).start();
                }
            });
        } else if (holder instanceof FooterViewHolder) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
        }else if(holder instanceof EmptyViewHolder){
            EmptyViewHolder emptyViewHolder = (EmptyViewHolder) holder;
        }
    }

    @Override
    public int getItemCount() {
        return contents.size() + 1;
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

    class FooterViewHolder extends RecyclerView.ViewHolder {
        LinearLayout pullview;

        public FooterViewHolder(View view) {
            super(view);
            pullview = (LinearLayout) view.findViewById(R.id.pullview);
        }
    }

    class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(View view) {
            super(view);
        }
    }
}
