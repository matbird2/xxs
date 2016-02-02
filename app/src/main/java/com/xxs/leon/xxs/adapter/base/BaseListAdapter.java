package com.xxs.leon.xxs.adapter.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by maliang on 16/1/28.
 */
public abstract class BaseListAdapter<E> extends BaseAdapter{

    public List<E> list;

    public Context mContext;
    public LayoutInflater mInflater;

    public List<E> getList(){
        return list;
    }

    public void setList(List<E> list){
        this.list = list;
        notifyDataSetChanged();
    }

    public void add(E e){
        this.list.add(e);
        notifyDataSetChanged();
    }

    public void addAll(List<E> list){
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public BaseListAdapter(Context context,List<E> list){
        super();
        this.mContext = context;
        this.list = list;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = bindView(position,convertView,parent);
        //绑定内部点击监听
        addInternalClickListener(convertView,position,list.get(position));
        return convertView;
    }

    public abstract View bindView(int position,View convertView,ViewGroup parent);

    public Map<Integer,OnInternalClickListener> canClickItem;

    private void addInternalClickListener(final View view,final Integer position,final Object valueMap){
        if(canClickItem != null){
            for(Integer key : canClickItem.keySet()){
                View inView = view.findViewById(key);
                final OnInternalClickListener onInternalClickListener = canClickItem.get(key);
                if(inView != null && onInternalClickListener != null){
                    inView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onInternalClickListener.onClickListener(view,v,position,valueMap);
                        }
                    });
                }
            }
        }
    }

    public void setOnInViewClickListener(Integer key ,OnInternalClickListener onInternalClickListener){
        if(canClickItem == null)
            canClickItem = new HashMap<>();
        canClickItem.put(key,onInternalClickListener);
    }

    public interface OnInternalClickListener{
        void onClickListener(View parent,View v ,Integer position,Object values);
    }

}
