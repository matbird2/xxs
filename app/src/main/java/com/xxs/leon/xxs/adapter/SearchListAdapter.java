package com.xxs.leon.xxs.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xxs.leon.xxs.R;
import com.xxs.leon.xxs.activity.DetailActivity_;
import com.xxs.leon.xxs.rest.bean.Album;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maliang on 16/1/8.
 */
public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ItemViewHolder> {

    Context context;
    List<Album> contents = new ArrayList<>();

    public SearchListAdapter(Context context) {
        this.context = context;
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
            view = LayoutInflater.from(context).inflate(R.layout.list_item_search, parent, false);
            return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        final Album album = contents.get(position);
        holder.tv_word.setText(album.getName());
        holder.content.setOnClickListener(new View.OnClickListener() {
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
        TextView tv_word;
        View content;

        public ItemViewHolder(View view) {
            super(view);
            content = view.findViewById(R.id.content);
            tv_word = (TextView) view.findViewById(R.id.tv_word);
        }
    }
}