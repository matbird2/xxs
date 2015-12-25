package com.xxs.leon.xxs.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import com.xxs.leon.xxs.activity.AlbumListActivity_;
import com.xxs.leon.xxs.activity.CommentActivity_;
import com.xxs.leon.xxs.activity.DetailActivity_;
import com.xxs.leon.xxs.activity.PostDetailActivity_;
import com.xxs.leon.xxs.activity.WebViewActivity;
import com.xxs.leon.xxs.activity.WebViewActivity_;
import com.xxs.leon.xxs.constant.AlbumType;
import com.xxs.leon.xxs.rest.bean.Album;
import com.xxs.leon.xxs.rest.bean.Post;
import com.xxs.leon.xxs.utils.TimeUtil;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florentchampigny on 24/04/15.
 */
public class HomeNewAlbumRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;

    List<Object> contents = new ArrayList<>();

    static final int TYPE_HEADER = 0;
    static final int TYPE_CELL = 1;
    private IconicsDrawable error_icon;

    public HomeNewAlbumRecyclerViewAdapter(Context context,List<Object> contents){
        this.context = context;
        this.contents = contents;
        error_icon = new IconicsDrawable(context)
                .icon(GoogleMaterial.Icon.gmd_broken_image)
                .color(Color.GRAY)
                .sizeDp(60);
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        switch (viewType) {
            case TYPE_HEADER: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_home_album_big, parent, false);
                return new NoticeViewHolder(view);
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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                NoticeViewHolder noticeViewHolder = (NoticeViewHolder) holder;
                if(contents.get(0) instanceof Post){
                    final Post post = (Post) contents.get(0);
                    noticeViewHolder.username.setText(post.getUser().getUsername());
                    noticeViewHolder.timetag.setText(TimeUtil.generTimeShowWord(post.getCreatedAt()));
                    noticeViewHolder.title.setText(post.getTitle());
                    noticeViewHolder.content.setText(post.getExcerpt());
                    Glide.with(context).load(post.getUser().getPhoto()).crossFade(500).placeholder(R.drawable.glide_placeholder_bg).centerCrop().into(noticeViewHolder.photo);
                    noticeViewHolder.card_view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            PostDetailActivity_.intent(context).postId(post.getObjectId()).postTitle(post.getTitle()).start();
                        }
                    });
                }
                break;
            case TYPE_CELL:
                HomeAlbumViewHolder homeAlbumViewHolder = (HomeAlbumViewHolder) holder;
                if(contents.get(position) instanceof Album){
                    final Album album = (Album) contents.get(position);
                    Glide.with(context).load(album.getCover()).error(error_icon).crossFade(500).centerCrop().into(homeAlbumViewHolder.cover);
                    homeAlbumViewHolder.title.setText(album.getName());
                    homeAlbumViewHolder.desc.setText(album.getDescri());
                    homeAlbumViewHolder.type.setText(AlbumType.getType(album.getType()));
                    homeAlbumViewHolder.card_view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DetailActivity_.intent(context).albumId(album.getObjectId()).albumName(album.getName()).start();
                        }
                    });
                    if(album.getPrice() == 0){
                        homeAlbumViewHolder.free.setVisibility(View.VISIBLE);
                    }else{
                        homeAlbumViewHolder.free.setVisibility(View.GONE);
                    }
                }
                break;
        }
    }

    class HomeAlbumViewHolder extends RecyclerView.ViewHolder {

        CardView card_view;
        ImageView cover;
        TextView title;
        TextView type;
        TextView desc;
        TextView free;
        public HomeAlbumViewHolder(View view){
            super(view);
            cover = (ImageView) view.findViewById(R.id.cover);
            title = (TextView) view.findViewById(R.id.title);
            type = (TextView) view.findViewById(R.id.type);
            desc = (TextView) view.findViewById(R.id.desc);
            free = (TextView) view.findViewById(R.id.free);
            card_view = (CardView) view.findViewById(R.id.card_view);
        }
    }

    class NoticeViewHolder extends RecyclerView.ViewHolder {

        CardView card_view;
        ImageView photo;
        TextView username;
        TextView timetag;
        TextView title;
        TextView content;

        public NoticeViewHolder(View view) {
            super(view);
            card_view = (CardView) view.findViewById(R.id.card_view);
            photo = (ImageView) view.findViewById(R.id.photo);
            username = (TextView) view.findViewById(R.id.username);
            timetag = (TextView) view.findViewById(R.id.timetag);
            title = (TextView) view.findViewById(R.id.title);
            content = (TextView) view.findViewById(R.id.content);
        }
    }
}