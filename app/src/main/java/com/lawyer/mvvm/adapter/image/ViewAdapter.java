package com.lawyer.mvvm.adapter.image;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.request.RequestOptions;

import ink.itwo.common.img.GlideRequest;
import ink.itwo.common.img.GlideRequests;
import ink.itwo.common.img.IMGLoad;

/**
 Created by wang
 on 2018/5/26 */
public class ViewAdapter {

    @BindingAdapter(value = {"lifecycle", "url", "requestOptions"}, requireAll = false)
    public static void setImageUri(ImageView imageView, Context lifecycle, String url, RequestOptions requestOptions) {
        if (TextUtils.isEmpty(url)) return;
        GlideRequests glideRequests;
        if (lifecycle == null) {
            glideRequests = IMGLoad.with(imageView.getContext());
        } else {
            glideRequests = IMGLoad.with(lifecycle);
        }
        setImageUri(glideRequests.load(url), requestOptions, imageView);
    }

  /*  @BindingAdapter(value = {"lifecycle", "url", "requestOptions"}, requireAll = false)
    public static void setImageUri(ImageView imageView, AppCompatActivity lifecycle, String url, RequestOptions requestOptions) {
        if (TextUtils.isEmpty(url)) return;
        GlideRequests glideRequests;
        if (lifecycle == null) {
            glideRequests = IMGLoad.with(imageView.getContext());
        } else {
            glideRequests = IMGLoad.with(lifecycle);
        }
        setImageUri(glideRequests.load(url), requestOptions, imageView);
    }

    @BindingAdapter(value = {"lifecycle", "url", "requestOptions"}, requireAll = false)
    public static void setImageUri(ImageView imageView, Fragment lifecycle, String url, RequestOptions requestOptions) {
        if (TextUtils.isEmpty(url)) return;
        GlideRequests glideRequests;
        if (lifecycle == null) {
            glideRequests = IMGLoad.with(imageView.getContext());
        } else {
            glideRequests = IMGLoad.with(lifecycle);
        }
        setImageUri(glideRequests.load(url), requestOptions, imageView);
    }

    @BindingAdapter(value = {"lifecycle", "url", "requestOptions"}, requireAll = false)
    public static void setImageUri(ImageView imageView, View lifecycle, String url, RequestOptions requestOptions) {
        if (TextUtils.isEmpty(url)) return;
        GlideRequests glideRequests;
        if (lifecycle == null) {
            glideRequests = IMGLoad.with(imageView.getContext());
        } else {
            glideRequests = IMGLoad.with(lifecycle);
        }
        setImageUri(glideRequests.load(url), requestOptions, imageView);
    }*/

    @BindingAdapter(value = {"lifecycle", "url", "placeholderRes", "errorPlaceholder", "circleCrop", "isRound","radiusDimenRes"}, requireAll = false)
    public static void setImageUri(ImageView imageView, Context lifecycle, Object url, int placeholderRes, int errorPlaceholder, boolean circleCrop, boolean isRound,int radiusDimenRes) {
        RequestOptions options = new RequestOptions().placeholder(placeholderRes).error(errorPlaceholder);
        if (circleCrop) options.circleCrop();
        else if (isRound) {
            RoundTransform roundTransform = radiusDimenRes == 0 ? new RoundTransform() : new RoundTransform(radiusDimenRes);
            options.transform(roundTransform);
        }
        GlideRequests glideRequests;
        if (lifecycle == null) {
            glideRequests = IMGLoad.with(imageView.getContext());
        } else {
            glideRequests = IMGLoad.with(lifecycle);
        }
        setImageUri(glideRequests.load(url), options, imageView);
    }
/*
    @BindingAdapter(value = {"lifecycle", "url", "placeholderRes", "errorPlaceholder", "circleCrop"}, requireAll = false)
    public static void setImageUri(ImageView imageView, AppCompatActivity lifecycle, Object url, int placeholderRes, int errorPlaceholder, boolean circleCrop) {
        RequestOptions options = new RequestOptions().placeholder(placeholderRes).error(errorPlaceholder);
        if (circleCrop) options.circleCrop();
        GlideRequests glideRequests;
        if (lifecycle == null) {
            glideRequests = IMGLoad.with(imageView.getContext());
        } else {
            glideRequests = IMGLoad.with(lifecycle);
        }
        setImageUri(glideRequests.load(url), options, imageView);
    }

    @BindingAdapter(value = {"lifecycle", "url", "placeholderRes", "errorPlaceholder", "circleCrop"}, requireAll = false)
    public static void setImageUri(ImageView imageView, Fragment lifecycle, Object url, int placeholderRes, int errorPlaceholder, boolean circleCrop) {
        RequestOptions options = new RequestOptions().placeholder(placeholderRes).error(errorPlaceholder);
        if (circleCrop) options.circleCrop();
        GlideRequests glideRequests;
        if (lifecycle == null) {
            glideRequests = IMGLoad.with(imageView.getContext());
        } else {
            glideRequests = IMGLoad.with(lifecycle);
        }
        setImageUri(glideRequests.load(url), options, imageView);
    }

    @BindingAdapter(value = {"lifecycle", "url", "placeholderRes", "errorPlaceholder", "circleCrop"}, requireAll = false)
    public static void setImageUri(ImageView imageView, View lifecycle, Object url, int placeholderRes, int errorPlaceholder, boolean circleCrop) {
        RequestOptions options = new RequestOptions().placeholder(placeholderRes).error(errorPlaceholder);
        if (circleCrop) options.circleCrop();
        GlideRequests glideRequests;
        if (lifecycle == null) {
            glideRequests = IMGLoad.with(imageView.getContext());
        } else {
            glideRequests = IMGLoad.with(lifecycle);
        }
        setImageUri(glideRequests.load(url), options, imageView);
    }*/

    private static void setImageUri(GlideRequest glideRequest, RequestOptions options, ImageView imageView) {
        if (options != null) glideRequest.apply(options);
        glideRequest.into(imageView);
    }

}
