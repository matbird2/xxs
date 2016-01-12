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
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
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

    public static final int CELL_TYPE = 0;
//    public static final int EMPTY_TYPE = 1;
    public static final int FOOTER_TYPE = 2;

    public static final int LOADING = 3;
    public static final int NO_MORE_DATA = 4;
    public static final int LOAD_FAILED = 5;

    FooterViewHolder footerViewHolder;

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
//        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return FOOTER_TYPE;
        } else {
            return CELL_TYPE;
        }
    }

    public boolean isFooter(int position){
        return position == getItemCount();
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
        }/* else if (viewType == EMPTY_TYPE) {
            view = LayoutInflater.from(context).inflate(R.layout.empty_view, parent, false);
            return new EmptyViewHolder(view);
        }*/
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            final Album album = contents.get(position);
            Glide.with(context).load(album.getCover()).placeholder(R.drawable.default_image_loading).error(R.drawable.default_loading_error).crossFade(500).centerCrop().into(itemViewHolder.cover);
            itemViewHolder.title.setText(album.getName());
            itemViewHolder.card_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DetailActivity_.intent(context).albumId(album.getObjectId()).albumName(album.getName()).start();
                }
            });
        } else if (holder instanceof FooterViewHolder) {
            footerViewHolder = (FooterViewHolder) holder;
//            setFooterViewState(LOADING);
        }/*else if(holder instanceof EmptyViewHolder){
            EmptyViewHolder emptyViewHolder = (EmptyViewHolder) holder;
        }*/
    }

    public void setFooterViewState(int state){
        if(footerViewHolder == null)
            return ;
        switch (state){
            case LOADING:
                footerViewHolder.pb.setVisibility(View.VISIBLE);
                footerViewHolder.showword.setText("正在加载...");
                break;
            case NO_MORE_DATA:
                footerViewHolder.pb.setVisibility(View.GONE);
                footerViewHolder.showword.setText("没有更多数据");
                break;
            case LOAD_FAILED:
                footerViewHolder.pb.setVisibility(View.GONE);
                footerViewHolder.showword.setText("加载失败");
                break;
            default:
                footerViewHolder.pb.setVisibility(View.VISIBLE);
                footerViewHolder.showword.setText("正在加载...");
                break;
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
        ProgressBarCircularIndeterminate pb;
        TextView showword;

        public FooterViewHolder(View view) {
            super(view);
            pullview = (LinearLayout) view.findViewById(R.id.pullview);
            pb = (ProgressBarCircularIndeterminate) view.findViewById(R.id.pb);
            showword = (TextView) view.findViewById(R.id.showword);
        }
    }

    class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(View view) {
            super(view);
        }
    }

    interface OnLoadMoreListener{

    }
}
