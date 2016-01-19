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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.xxs.leon.xxs.R;
import com.xxs.leon.xxs.activity.CommentDialogActivity_;
import com.xxs.leon.xxs.activity.ShowAllCommentActivity;
import com.xxs.leon.xxs.rest.bean.Comment;
import com.xxs.leon.xxs.utils.TimeUtil;

import java.util.List;

/**
 * Created by maliang on 16/1/19.
 */
public class CommentListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int CELL_TYPE = 0;
    //    public static final int EMPTY_TYPE = 1;
    public static final int FOOTER_TYPE = 2;

    public static final int LOADING = 3;
    public static final int NO_MORE_DATA = 4;
    public static final int LOAD_FAILED = 5;

    FooterViewHolder footerViewHolder;
    private OnGetThumbnailAndDisplayImageViewListener listener;

    Context context;
    String postId;
    String albumId;
    List<Comment> contents;

    private IconicsDrawable error_icon;

    public CommentListAdapter(Context context,List<Comment> contents,String postId,String albumId) {
        this.context = context;
        this.contents = contents;
        this.postId = postId;
        this.albumId = albumId;
        error_icon = new IconicsDrawable(context)
                .icon(GoogleMaterial.Icon.gmd_broken_image)
                .color(Color.GRAY)
                .sizeDp(60);
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
            view = LayoutInflater.from(context).inflate(R.layout.list_item_comment, parent, false);
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
            final Comment comment = contents.get(position);

            itemViewHolder.username.setText(comment.getUser().getUsername());
            itemViewHolder.timetag.setText(TimeUtil.generTimeShowWord(comment.getCreatedAt()));
            itemViewHolder.content.setText(comment.getContent());
            listener.getAndDisplay(comment.getUser().getPhoto()+"",itemViewHolder.photo);

            itemViewHolder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CommentDialogActivity_.intent(context).commentType(0).parentId(comment.getObjectId()).postId(postId).albumId(albumId).startForResult(ShowAllCommentActivity.REQUEST_REPLY_COMMENT);
                }
            });

            //----parent---
            if(comment.getParent() != null && comment.getParent().getContent() != null && comment.getParent().getUser() != null){
                itemViewHolder.ll_parent.setVisibility(View.VISIBLE);
                itemViewHolder.parent_content.setText(comment.getParent().getContent());
                itemViewHolder.parent_username.setText(comment.getParent().getUser().getUsername());
            }else{
                itemViewHolder.ll_parent.setVisibility(View.GONE);
            }

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

        RelativeLayout container;
        ImageView photo;
        TextView username;
        TextView parent_username;
        TextView timetag;
        TextView content;
        TextView parent_content;
        LinearLayout ll_parent;

        public ItemViewHolder(View view) {
            super(view);
            container = (RelativeLayout) view.findViewById(R.id.container);
            photo = (ImageView) view.findViewById(R.id.photo);
            username = (TextView) view.findViewById(R.id.username);
            parent_username = (TextView) view.findViewById(R.id.parent_username);
            timetag = (TextView) view.findViewById(R.id.timetag);
            content = (TextView) view.findViewById(R.id.content);
            parent_content = (TextView) view.findViewById(R.id.parent_content);
            ll_parent = (LinearLayout) view.findViewById(R.id.ll_parent);
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