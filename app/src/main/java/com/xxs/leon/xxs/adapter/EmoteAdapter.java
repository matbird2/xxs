package com.xxs.leon.xxs.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xxs.leon.xxs.R;
import com.xxs.leon.xxs.adapter.base.BaseArrayListAdapter;
import com.xxs.leon.xxs.bean.FaceText;

import java.util.List;

/**
 * Created by maliang on 16/1/28.
 */
public class EmoteAdapter extends BaseArrayListAdapter{

    public EmoteAdapter(Context context,List<FaceText> datas){
        super(context,datas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_face_text,null);
            holder = new ViewHolder();
            holder.mImageView = (ImageView) convertView.findViewById(R.id.v_face_text);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        FaceText faceText = (FaceText) getItem(position);
        String key = faceText.text.substring(1);
        Drawable drawable = mContext.getResources().getDrawable(mContext.getResources().getIdentifier(key,"drawable",mContext.getPackageName()));
        holder.mImageView.setImageDrawable(drawable);
        return convertView;
    }

    class ViewHolder{
        ImageView mImageView;
    }
}
