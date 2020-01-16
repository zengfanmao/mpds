package com.aimissu.ptt.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.aimissu.ptt.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

/**
 * 图片工具类
 */
public class ImageUtils {
    private static volatile RequestManager requestManager;
    private final static int MAX_WIDTH = 200;
    private final static int MAX_HEIGHT = 200;

    private static void initRequest(Context context) {
        if (requestManager == null) {
            synchronized (ImageUtils.class) {
                if (requestManager == null) {
                    requestManager = Glide.with(context.getApplicationContext());
                }
            }
        }
    }

    public static void loadImage(Context context, String url, ImageView imageView, View pocessView) {
        initRequest(context);
        requestManager
                .load(url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        if (pocessView != null)
                            pocessView.setVisibility(View.INVISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (pocessView != null)
                            pocessView.setVisibility(View.INVISIBLE);
                        return false;
                    }
                })
                .apply(new RequestOptions()
                        .centerCrop()
                        .autoClone()
                        .error(R.drawable.msg_default)
                        .placeholder(R.drawable.msg_default)
                        .override(300, 300))
                .into(imageView);
    }


    public static void loadImage(Context context, Integer urlRes, ImageView imageView, View pocessView) {
        initRequest(context);
        int width = context.getResources().getDimensionPixelSize(R.dimen.im_image_width);
        requestManager
                .load(urlRes)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        if (pocessView != null)
                            pocessView.setVisibility(View.INVISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (pocessView != null)
                            pocessView.setVisibility(View.INVISIBLE);
                        return false;
                    }
                })
                .apply(new RequestOptions()
                        .centerCrop()
                        .autoClone()
                        .override(width, width))
                .into(imageView);

    }
}
