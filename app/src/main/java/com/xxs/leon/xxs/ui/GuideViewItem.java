package com.xxs.leon.xxs.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xxs.leon.xxs.R;

import java.io.InputStream;

/**
 * Created by leon on 15-12-26.
 */
public class GuideViewItem extends LinearLayout {
    protected LinearLayout mLinRoot;
    protected ImageView mImg;
    private Context pContext;

    public GuideViewItem(Context pContext) {
        super(pContext);
        this.pContext = pContext;
        inflate(pContext, R.layout.item_guide, this);
        initView();
    }

    public void setData(int resId) {
        Bitmap bitMap = readBitMap(pContext, resId);
        mImg.setImageBitmap(bitMap);
        // mLinRoot.setBackgroundResource(resId);
    }

    protected void initView() {
        mLinRoot = (LinearLayout) findViewById(R.id.home_poster_item_lin);
        mImg = (ImageView) findViewById(R.id.home_poster_item_img);
    }

    /**
     * 2. * 以最省内存的方式读取本地资源的图片 3. * @param context 4. * @param resId 5. * @return
     * 6.
     */
    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }
}
