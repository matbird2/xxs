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

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.xxs.leon.xxs.R;
import com.xxs.leon.xxs.activity.DetailActivity_;
import com.xxs.leon.xxs.activity.PostDetailActivity_;
import com.xxs.leon.xxs.rest.bean.Post;
import com.xxs.leon.xxs.utils.L;
import com.xxs.leon.xxs.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maliang on 15/12/16.
 */
public class HomePostListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int CELL_TYPE = 0;
    //    public static final int EMPTY_TYPE = 1;
    public static final int FOOTER_TYPE = 2;

    public static final int LOADING = 3;
    public static final int NO_MORE_DATA = 4;
    public static final int LOAD_FAILED = 5;

    FooterViewHolder footerViewHolder;
    private OnGetThumbnailAndDisplayImageViewListener listener;

    Context context;
    List<Post> contents;

    private IconicsDrawable error_icon;

    public HomePostListAdapter(Context context,List<Post> contents) {
        this.context = context;
        this.contents = contents;
        error_icon = new IconicsDrawable(context)
                .icon(GoogleMaterial.Icon.gmd_broken_image)
                .color(Color.GRAY)
                .sizeDp(60);
    }

    /*public void appenList(List<Post> list) {
        if (!contents.containsAll(list) && list != null && list.size() > 0) {
            contents.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void clear() {
        contents.clear();
    }*/

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
            view = LayoutInflater.from(context).inflate(R.layout.list_item_post, parent, false);
            return new ItemViewHolder(view);
        } else if (viewType == FOOTER_TYPE) {
            view = LayoutInflater.from(context).inflate(R.layout.footer_view, parent, false);
            return new FooterViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            final Post post = contents.get(position);

            itemViewHolder.username.setText(post.getUser().getUsername());
            itemViewHolder.timetag.setText(TimeUtil.generTimeShowWord(post.getCreatedAt()));
            itemViewHolder.title.setText(post.getTitle());
            itemViewHolder.content.setText(post.getExcerpt());
            listener.getAndDisplay(post.getUser().getPhoto()+"",itemViewHolder.photo);

            itemViewHolder.card_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PostDetailActivity_.intent(context).postId(post.getObjectId()).postTitle(post.getTitle()).start();
                }
            });

        } else if (holder instanceof FooterViewHolder) {
            footerViewHolder = (FooterViewHolder) holder;
        }
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
        ImageView photo;
        TextView username;
        TextView timetag;
        TextView title;
        TextView content;

        public ItemViewHolder(View view) {
            super(view);
            card_view = (CardView) view.findViewById(R.id.card_view);
            photo = (ImageView) view.findViewById(R.id.photo);
            username = (TextView) view.findViewById(R.id.username);
            timetag = (TextView) view.findViewById(R.id.timetag);
            title = (TextView) view.findViewById(R.id.title);
            content = (TextView) view.findViewById(R.id.content);
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

    public void setOnGetThumbnailAndDisplayImageViewListener(OnGetThumbnailAndDisplayImageViewListener listener){
        this.listener = listener;
    }

    public interface OnGetThumbnailAndDisplayImageViewListener{
        void getAndDisplay(String image,ImageView iv);
    }
}