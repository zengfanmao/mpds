package com.aimissu.ptt.view.widget;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.transition.Transition;

public class MsgImageTarget extends ImageViewTarget<Bitmap>
{
    // 长图，宽图比例阈值
    public static final int RATIO_OF_LARGE = 3;
    // 长图截取后的高宽比（宽图截取后的宽高比）
    public static int HW_RATIO = 3;

    public View mPrcessView;
    public MsgImageTarget(ImageView view) {
        super(view);
    }
    @Override
    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
        super.onResourceReady(resolveBitmap(resource), transition);
    }


    @Override
    protected void setResource(@Nullable Bitmap resource) {
        view.setImageBitmap(resource);
    }



    private Bitmap resolveBitmap(Bitmap resource) {
        int srcWidth = resource.getWidth();
        int srcHeight = resource.getHeight();

        if (srcWidth > srcHeight)
        {
            float srcWHRatio = (float) srcWidth / srcHeight;
            // 宽图
            if (srcWHRatio > RATIO_OF_LARGE)
            {
                return Bitmap.createBitmap(resource, 0, 0, srcHeight * HW_RATIO, srcHeight);
            }
        }
        else
        {
            float srcHWRatio = (float) srcHeight / srcWidth;
            // 长图
            if (srcHWRatio > RATIO_OF_LARGE)
            {
                return Bitmap.createBitmap(resource, 0, 0, srcWidth, srcWidth * HW_RATIO);
            }
        }
        return resource;
    }
}
