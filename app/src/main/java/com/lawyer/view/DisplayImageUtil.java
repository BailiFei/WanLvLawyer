package com.lawyer.view;

import android.content.Context;
import android.widget.ImageView;

import ink.itwo.common.img.IMGLoad;
import ink.itwo.common.util.ImageLoader;

/**
 @author wang
 on 2019/3/5 */

public class DisplayImageUtil implements ImageLoader {
    @Override
    public void displayImage(Context activity, Object path, ImageView imageView, int width, int height) {
        IMGLoad.with(activity).load(path).into(imageView);
    }

    @Override
    public void displayImage(Context context, Object url, ImageView imageView) {
        IMGLoad.with(context).load(url).into(imageView);

    }

    @Override
    public void clearMemoryCache() {
    }
}
