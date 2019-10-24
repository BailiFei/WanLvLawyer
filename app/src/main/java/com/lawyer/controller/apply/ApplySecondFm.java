package com.lawyer.controller.apply;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.lawyer.BuildConfig;
import com.lawyer.MainActivity;
import com.lawyer.R;
import com.lawyer.base.BaseFragment;
import com.lawyer.entity.LawyerApplyBean;
import com.lawyer.net.API;
import com.lawyer.net.Result;
import com.lawyer.net.ResultObserver;
import com.lawyer.view.DisplayImageUtil;

import java.util.ArrayList;

import ink.itwo.alioss.upIv.OSSImageView;
import ink.itwo.alioss.upIv.OSSImageViewListener;
import ink.itwo.common.img.IMGLoad;
import ink.itwo.common.util.CollectionUtil;
import ink.itwo.common.util.IToast;
import ink.itwo.media.bean.MediaBean;
import ink.itwo.media.config.Config;
import ink.itwo.media.config.MimeType;
import ink.itwo.media.rx.RxMediaLoad;
import ink.itwo.net.NetManager;
import ink.itwo.net.transformer.NetTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 Created by wangtaian on 2019-05-30. */
public class ApplySecondFm extends BaseFragment<MainActivity> {
    public static ApplySecondFm newInstance(LawyerApplyBean bean) {

        Bundle args = new Bundle();

        ApplySecondFm fragment = new ApplySecondFm();
        args.putParcelable("bean", bean);
        fragment.setArguments(args);
        return fragment;
    }

    private LawyerApplyBean bean;

    @Override
    protected int initLayoutId() {
        return R.layout.fm_apply_second;
    }

    @Override
    protected boolean isImmersionBarEnabled() {
        return false;
    }

    @Override
    public void onCreateView(View viewRoot) {
        super.onCreateView(viewRoot);
        bean = getArguments().getParcelable("bean");
        OSSImageView ivIdCardFront = viewRoot.findViewById(R.id.iv_id_card_front);
        OSSImageView ivIdCardBack = viewRoot.findViewById(R.id.iv_id_card_back);
        OSSImageView ivLicense = viewRoot.findViewById(R.id.iv_license);

        viewRoot.findViewById(R.id.tv_commit).setOnClickListener(v -> {
            if (TextUtils.isEmpty(bean.getIdCardFront())
                    || TextUtils.isEmpty(bean.getIdCardBack())
                    || TextUtils.isEmpty(bean.getCertificatePic())) {
                IToast.show("材料上传未完成");
                return;
            }
            NetManager.http().create(API.class)
                    .lawyerApply(bean.toMap())
                    .compose(NetTransformer.handle(this))
                    .subscribe(new ResultObserver<Result>() {
                        @Override
                        public void onNext(Result result) {
                            startWithPop(ApplyThreeFm.newInstance());
                        }

                    });
        });


        ivIdCardFront.setOnClickListener(v -> {
            new RxMediaLoad(this)
                    .pick(new Config()
                            .setCamera(true)
                            .setCircleCrop(false)
                            .setCrop(false)
                            .setMimeType(MimeType.PHOTO)
                            .setSingle(true)
                            .setImageLoader(new DisplayImageUtil()))
                    .execute()
                    .flatMap((Function<ArrayList<MediaBean>, ObservableSource<String>>) mediaBeans -> {
                        if (CollectionUtil.isEmpty(mediaBeans)) return Observable.empty();
                        IMGLoad.with(ApplySecondFm.this).load(mediaBeans.get(0).getPath()).into(ivIdCardFront);
                        return Observable.create(emitter -> {
                            OSSImageViewListener listener = new OSSImageViewListener.SimpleOSSImageListener() {
                                @Override
                                public void onSuccess(String fileUrl) {
                                    emitter.onNext(fileUrl);
                                    emitter.onComplete();

                                }
                            };
                            ivIdCardFront.setOSSImageViewListener(listener);
                            ivIdCardFront.upLoad(mediaBeans.get(0).getPath(), "head");
                        });
                    })
                    .flatMap((Function<String, ObservableSource<?>>) s -> {
                        bean.setIdCardFront(BuildConfig.OSS_HOST+s);
                        return Observable.just(s);
                    })
                    .doOnSubscribe(this::addDisposableLife)
                    .subscribe();

        });


        ivIdCardBack.setOnClickListener(v -> {
            new RxMediaLoad(this)
                    .pick(new Config()
                            .setCamera(true)
                            .setCircleCrop(false)
                            .setCrop(false)
                            .setMimeType(MimeType.PHOTO)
                            .setSingle(true)
                            .setImageLoader(new DisplayImageUtil()))
                    .execute()
                    .flatMap((Function<ArrayList<MediaBean>, ObservableSource<String>>) mediaBeans -> {
                        if (CollectionUtil.isEmpty(mediaBeans)) return Observable.empty();
                        IMGLoad.with(ApplySecondFm.this).load(mediaBeans.get(0).getPath()).into(ivIdCardBack);
                        return Observable.create(emitter -> {
                            OSSImageViewListener listener = new OSSImageViewListener.SimpleOSSImageListener() {
                                @Override
                                public void onSuccess(String fileUrl) {
                                    emitter.onNext(fileUrl);
                                    emitter.onComplete();

                                }
                            };
                            ivIdCardBack.setOSSImageViewListener(listener);
                            ivIdCardBack.upLoad(mediaBeans.get(0).getPath(), "head");
                        });
                    })
                    .flatMap((Function<String, ObservableSource<?>>) s -> {
                        bean.setIdCardBack(BuildConfig.OSS_HOST+s);
                        return Observable.just(s);
                    })
                    .doOnSubscribe(this::addDisposableLife)
                    .subscribe();
        });

        ivLicense.setOnClickListener(v -> {
            new RxMediaLoad(this)
                    .pick(new Config()
                            .setCamera(true)
                            .setCircleCrop(false)
                            .setCrop(false)
                            .setMimeType(MimeType.PHOTO)
                            .setSingle(true)
                            .setImageLoader(new DisplayImageUtil()))
                    .execute()
                    .flatMap((Function<ArrayList<MediaBean>, ObservableSource<String>>) mediaBeans -> {
                        if (CollectionUtil.isEmpty(mediaBeans)) return Observable.empty();
                        IMGLoad.with(ApplySecondFm.this).load(mediaBeans.get(0).getPath()).into(ivLicense);
                        return Observable.create(emitter -> {
                            OSSImageViewListener listener = new OSSImageViewListener.SimpleOSSImageListener() {
                                @Override
                                public void onSuccess(String fileUrl) {
                                    emitter.onNext(fileUrl);
                                    emitter.onComplete();

                                }
                            };
                            ivLicense.setOSSImageViewListener(listener);
                            ivLicense.upLoad(mediaBeans.get(0).getPath(), "head");
                        });
                    })
                    .flatMap((Function<String, ObservableSource<?>>) s -> {
                        bean.setCertificatePic(BuildConfig.OSS_HOST+s);
                        return Observable.just(s);
                    })
                    .doOnSubscribe(this::addDisposableLife)
                    .subscribe();

        });

    }
}
