package com.lawyer.controller.account;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.lawyer.BuildConfig;
import com.lawyer.MainActivity;
import com.lawyer.R;
import com.lawyer.base.BaseFragment;
import com.lawyer.controller.main.MainFm;
import com.lawyer.dialog.RxAlertDialog;
import com.lawyer.dialog.RxDownloadProgressDialog;
import com.lawyer.entity.AppVersionBean;
import com.lawyer.net.API;
import com.lawyer.net.Result;
import com.lawyer.net.ResultObserver;
import com.lawyer.util.UserCache;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;

import ink.itwo.common.util.IToast;
import ink.itwo.net.NetManager;
import ink.itwo.net.dialog.DialogUtils;
import ink.itwo.net.file.DownloadManager;
import ink.itwo.net.file.DownloadProgressListener;
import ink.itwo.net.life.RxLifecycle;
import ink.itwo.net.transformer.ResultTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 设置
 @author wang
 on 2019/2/20 */

public class SettingFm extends BaseFragment<MainActivity> {
    public static SettingFm newInstance() {

        Bundle args = new Bundle();

        SettingFm fragment = new SettingFm();
        fragment.setArguments(args);
        return fragment;
    }

    private TextView tvName;
    @Override
    protected int initLayoutId() {
        return R.layout.fm_setting;
    }

    @Override
    public void onCreateView(View viewRoot) {
        super.onCreateView(viewRoot);
        setTitle("设置");
         tvName = findViewById(R.id.tv_name);
        tvName.setText(UserCache.get().getNickName());
        TextView tvMobile = findViewById(R.id.tv_mobile);
        tvMobile.setText(UserCache.get().getMobileText());
        TextView tvVersion = findViewById(R.id.tv_version);
//        tvVersion.setText(BuildConfig.developmentPeriod+" -  "+"V" + APPUtil.getVersionCode(mActivity));
        tvVersion.setText("V" + BuildConfig.VERSION_NAME);
        findViewById(R.id.layout_nick_name)
                .setOnClickListener(v -> {
                    mActivity.startForResult(ChangeNickNamFm.newInstance(),1);
                });
        findViewById(R.id.layout_pass_word)
                .setOnClickListener(v -> {
                    mActivity.start(ChangePasswordFm.newInstance());
                });
        findViewById(R.id.tv_logout)
                .setOnClickListener(v->{
                    UserCache.clear();
                    mActivity.startWithPopTo(MainFm.newInstance(), MainFm.class, true);
                });
        findViewById(R.id.layout_version).setOnClickListener(v -> getVersion());
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        if (requestCode == 1&&resultCode==RESULT_OK) {
            String nickName = data.getString("nickName");
            if (!TextUtils.isEmpty(nickName)&&tvName!=null) {
                tvName.setText(nickName);
            }
        }
    }

    private void getVersion() {
        final DialogUtils dialogUtils = new DialogUtils();

        NetManager.http().create(API.class)
                .getVersion("android")
                .compose(RxLifecycle.bind(this))
                .compose(ResultTransformer.handleResult())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(upstream ->
                        upstream.doOnSubscribe(disposable -> {
                            dialogUtils.showProgress(SettingFm.this.mActivity);
                        }))
                .flatMap((Function<Result<AppVersionBean>, ObservableSource<AppVersionBean>>) result -> {
                    dialogUtils.dismissProgress();
                    AppVersionBean data = result.getData();
                    if (data == null || TextUtils.isEmpty(data.getVersionnum())) {
                        return Observable.empty();
                    }

                    if (data.getVersionnum().compareTo("v" + BuildConfig.VERSION_NAME) > 0) {
                        return Observable.just(data);
                    }
                    return Observable.empty();
                })
                .zipWith(observer ->
                                addDisposableLife(new RxPermissions(mActivity)
                                        .requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                        .subscribe(observer::onNext)),
                        (BiFunction<AppVersionBean, Permission, AppVersionBean>) (appVersionBean, permission) -> {
                            if (permission == null) {
                                return null;
                            } else if (permission.granted) {
                                return appVersionBean;
                            } else if (permission.shouldShowRequestPermissionRationale) {
                                // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                                IToast.show(permission.name + " is denied. More info should be provided.");
                                return null;
                            } else {// 用户拒绝了该权限，并且选中『不再询问』
                                IToast.show("更新版本需要您授予权限，请在系统设置里开启电话权限");
                                return null;
                            }
                        })
                .flatMap((Function<AppVersionBean, ObservableSource<AppVersionBean>>) appVersionBean -> {
                    if (appVersionBean == null) {
                        return Observable.empty();
                    }
                    return RxAlertDialog
                            .newBuilder()
                            .desStr(appVersionBean.getDescription())
                            //是否强制升级，强制升级不允许取消
                            .outCancel(appVersionBean.getCompulsory() == 0)
                            .build()
                            .observe(mActivity.getSupportFragmentManager(), "appVersion")
                            .compose(upstream ->
                                    upstream.flatMap((Function<Boolean, ObservableSource<AppVersionBean>>) aBoolean -> {
                                        if (aBoolean) {
                                            return Observable.just(appVersionBean);
                                        }
                                        return Observable.empty();
                                    }));
                })
                .flatMap((Function<AppVersionBean, ObservableSource<File>>) appVersionBean ->
                        DownloadManager.Builder.newBuilder()
                                .setDownLoadUrl(appVersionBean.getUpdateUrl())
                                .setDeleteOld(true)
                                .setDownloadProgressListener((DownloadProgressListener) new RxDownloadProgressDialog().show(mActivity.getSupportFragmentManager()))
                                .setSavePath(Environment.getExternalStorageDirectory().getAbsolutePath() + "/lawyer/lawyer_" + appVersionBean.getVersionnum() + ".apk")
                                .execute())
                .subscribe(new ResultObserver<File>() {
                    @Override
                    public void onNext(File file) {
                        Intent installApkIntent = new Intent();
                        installApkIntent.setAction(Intent.ACTION_VIEW);
                        installApkIntent.addCategory(Intent.CATEGORY_DEFAULT);
                        installApkIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                            installApkIntent.setDataAndType(FileProvider.getUriForFile(mActivity, "com.wanlvonline.lawfirm.appFileProvider", file), "application/vnd.android.package-archive");
                            installApkIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        } else {
                            installApkIntent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                        }
                        mActivity.startActivity(installApkIntent);

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        dialogUtils.dismissProgress();
                    }
                });
    }
}
