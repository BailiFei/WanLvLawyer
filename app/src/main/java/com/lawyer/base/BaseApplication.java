package com.lawyer.base;

import android.content.Context;
import android.text.TextUtils;

import com.lawyer.BuildConfig;
import com.lawyer.net.Url;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import ink.itwo.alioss.AliOSSHelper;
import ink.itwo.common.ctrl.CommonApplication;
import ink.itwo.common.img.DisplayImageLoader;
import ink.itwo.common.util.ILog;
import ink.itwo.common.util.Utils;
import ink.itwo.common.widget.vp.BannerViewPager;
import ink.itwo.net.NetManager;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;
import me.yokeyword.fragmentation.Fragmentation;

/**
 @author wang
 on 2019/1/30 */

public class BaseApplication extends CommonApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this, BuildConfig.DEBUG, BuildConfig.APPLICATION_ID);
        // 建议在Application里初始化
        Fragmentation.builder()
                // 显示悬浮球 ; 其他Mode:SHAKE: 摇一摇唤出   NONE：隐藏
                .stackViewMode(Fragmentation.NONE)
                .debug(BuildConfig.DEBUG)
                .install();
        BannerViewPager.setImageLoader(new DisplayImageLoader());
        NetManager.init(this, BuildConfig.SERVICE_HOST, BuildConfig.DEBUG, null);
//        oss_end_point=oss-cn-hangzhou.aliyuncs.com
//        oss_bucket_img_name=lawfirm2
        AliOSSHelper.getInstance().setBucketName("lawfirm2").initOSS(this,BuildConfig.SERVICE_HOST+Url.OSS_DISTRIBUTE_TOKEN);

        UMConfigure.init(this, BuildConfig.UMENG_APPKEY, BuildConfig.UMENG_CHANNEL_VALUE, UMConfigure.DEVICE_TYPE_PHONE,  null);
        UMConfigure.setLogEnabled(BuildConfig.DEBUG);
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);

        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                ILog.d(throwable);
                //异常处理
            }
        });
//        Stetho.initializeWithDefaults(this);

    }
    private void initCrashReport() {
        Context context = getApplicationContext();
        // 获取当前包名
        String packageName = context.getPackageName();
        // 获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        // 初始化Bugly
        CrashReport.initCrashReport(context, "3ece10bf88", BuildConfig.DEBUG, strategy);
    }
    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }
}
