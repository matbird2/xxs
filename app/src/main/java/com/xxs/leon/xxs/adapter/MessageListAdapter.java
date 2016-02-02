package com.xxs.leon.xxs.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xxs.leon.xxs.R;
import com.xxs.leon.xxs.activity.ChatActivity_;
import com.xxs.leon.xxs.activity.DetailActivity_;
import com.xxs.leon.xxs.bean.XSBmobChatUser;
import com.xxs.leon.xxs.rest.bean.Album;
import com.xxs.leon.xxs.ui.DialogTips;
import com.xxs.leon.xxs.utils.FaceTextUtils;
import com.xxs.leon.xxs.utils.TimeTool;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.bean.BmobRecent;
import cn.bmob.im.config.BmobConfig;
import cn.bmob.im.db.BmobDB;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by maliang on 16/2/2.
 */
public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.ItemViewHolder> {

    Context context;
    List<BmobRecent> contents = new ArrayList<>();

    public MessageListAdapter(Context context) {
        this.context = context;
    }

    public void appenList(List<BmobRecent> list) {
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
        view = LayoutInflater.from(context).inflate(R.layout.item_conversation, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        final BmobRecent item = contents.get(position);
        String avatar = item.getAvatar();
        if(avatar!=null&& !avatar.equals("")){
            Glide.with(context).load(avatar).placeholder(R.drawable.default_head_photo).error(R.drawable.default_head_photo).bitmapTransform(new CropCircleTransformation(context)).crossFade(500).centerCrop().into(holder.iv_recent_avatar);
        }else{
            holder.iv_recent_avatar.setImageResource(R.drawable.default_head_photo);
        }

        holder.tv_recent_name.setText(item.getUserName());
        holder.tv_recent_time.setText(TimeTool.getChatTime(item.getTime()));
        //显示内容
        if(item.getType()== BmobConfig.TYPE_TEXT){
            SpannableString spannableString = FaceTextUtils.toSpannableString(context, item.getMessage());
            holder.tv_recent_msg.setText(spannableString);
        }else if(item.getType()==BmobConfig.TYPE_IMAGE){
            holder.tv_recent_msg.setText("[图片]");
        }else if(item.getType()==BmobConfig.TYPE_LOCATION){
            String all =item.getMessage();
            if(all!=null &&!all.equals("")){//位置类型的信息组装格式：地理位置&维度&经度
                String address = all.split("&")[0];
                holder.tv_recent_msg.setText("[位置]"+address);
            }
        }else if(item.getType()==BmobConfig.TYPE_VOICE){
            holder.tv_recent_msg.setText("[语音]");
        }

        int num = BmobDB.create(context).getUnreadCount(item.getTargetid());
        if (num > 0) {
            holder.tv_recent_unread.setVisibility(View.VISIBLE);
            holder.tv_recent_unread.setText(num + "");
        } else {
            holder.tv_recent_unread.setVisibility(View.GONE);
        }

        holder.rl_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //组装聊天对象
                XSBmobChatUser user = new XSBmobChatUser();
                user.setAvatar(item.getAvatar());
                user.setNick(item.getNick());
                user.setUsername(item.getUserName());
                user.setObjectId(item.getTargetid());
                ChatActivity_.intent(context).targetUser(user).start();
            }
        });

        holder.rl_content.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDeleteDialog(item);
                return true;
            }
        });
    }


    @Override
    public int getItemCount() {
        return contents.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tv_recent_name;
        TextView tv_recent_msg;
        TextView tv_recent_time;
        TextView tv_recent_unread;
        ImageView iv_recent_avatar;
        RelativeLayout rl_content;

        public ItemViewHolder(View view) {
            super(view);
            tv_recent_name = (TextView) view.findViewById(R.id.tv_recent_name);
            tv_recent_msg = (TextView) view.findViewById(R.id.tv_recent_msg);
            tv_recent_time = (TextView) view.findViewById(R.id.tv_recent_time);
            tv_recent_unread = (TextView) view.findViewById(R.id.tv_recent_unread);
            iv_recent_avatar = (ImageView) view.findViewById(R.id.iv_recent_avatar);
            rl_content = (RelativeLayout) view.findViewById(R.id.rl_content);
        }
    }

    public void showDeleteDialog(final BmobRecent recent) {
        DialogTips dialog = new DialogTips(context,recent.getUserName(),"删除会话", "确定",true,true);
        // 设置成功事件
        dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int userId) {
                deleteRecent(recent);
            }
        });
        // 显示确认对话框
        dialog.show();
        dialog = null;
    }
    /** 删除会话
     * deleteRecent
     * @param @param recent
     * @return void
     * @throws
     */
    private void deleteRecent(BmobRecent recent){
        remove(recent);
        BmobDB.create(context).deleteRecent(recent.getTargetid());
        BmobDB.create(context).deleteMessages(recent.getTargetid());
    }

    public void remove(BmobRecent recent){
        contents.remove(recent);
        notifyDataSetChanged();
    }
}
