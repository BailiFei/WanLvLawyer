package com.m7.imkfsdk;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.m7.imkfsdk.chat.ChatActivity;
import com.m7.imkfsdk.constant.Constants;
import com.m7.imkfsdk.utils.PermissionUtils;
import com.m7.imkfsdk.view.ActionSheetDialog;
import com.moor.imkf.IMChatManager;
import com.moor.imkf.model.entity.CardInfo;
import com.moor.imkf.model.entity.Peer;
import com.moor.imkf.utils.LogUtils;
import com.moor.imkf.utils.MoorUtils;

import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;


public class MainActivity extends Activity {
    public static String accessId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.kf_activity_main);

        /**
         * 文件写入权限 （初始化需要写入文件，点击在线客服按钮之前需打开文件写入权限）
         * 读取设备 ID 权限 （初始化需要获取用户的设备 ID）
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PermissionUtils.hasAlwaysDeniedPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                PermissionUtils.requestPermissions(this, 0x11, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE}, new PermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                    }

                    @Override
                    public void onPermissionDenied(String[] deniedPermissions) {
                        Toast.makeText(MainActivity.this, R.string.notpermession, Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 2000);
                    }
                });
            }
        }

        /**
         * 第一步：初始化help 文件
         */
        final KfStartHelper helper = new KfStartHelper(MainActivity.this);


        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 第二步
                 * 设置参数
                 * 初始化sdk方法，必须先调用该方法进行初始化后才能使用IM相关功能
                 * @param accessId       接入id（需后台配置获取）
                 * @param userName       用户名
                 * @param userId         用户id
                 */

//                商品信息示例
                String s = "https://wap.boosoo.com.cn/bobishop/goodsdetail?id=10160&mid=36819";
                CardInfo ci = new CardInfo("http://seopic.699pic.com/photo/40023/0579.jpg_wh1200.jpg", "我是一个标题当初读书", "我是name当初读书。", "价格 1000-9999", "https://www.baidu.com");
                String icon = "http://seopic.699pic.com/photo/40023/0579.jpg_wh1200.jpg";
                String title = "！@……*（）||、/、～【】'；/。，=-";
                String content = "～【】'；/。，=-！@……*（）～【】'；/。，=-";
                String rigth3 = "价格 1000-9999";
                try {
                    ci = new CardInfo(URLEncoder.encode(icon, "utf-8"), URLEncoder.encode(title, "utf-8"), URLEncoder.encode(content, "utf-8"), URLEncoder.encode(rigth3, "utf-8"),
                            URLEncoder.encode(s, "utf-8"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                helper.setCard(ci);
//                helper.initSdkChat("fa0f5110-82ec-11e7-8ca1-8b4900b05172", "测试2", "97289000");//陈辰8002
//                helper.initSdkChat("bd696e40-bca1-11e8-8e5f-7bbf4c6bde74", "测试", "123456789");//陈辰正式 6001
//                helper.initSdkChat("7c7271e0-b664-11e8-8a50-2d2771c7b87a", "测试", "123456789");//潘娇汇测试
//                helper.initSdkChat("9c70b620-4f6e-11e9-9e9d-fffa1e2edbc4", "测试", "123456789");//腾讯云正式
//                helper.initSdkChat("ee0d87c0-60fc-11e9-bce8-dba3b36cc8b5", "测试fwff", "12fafa56789");//6008正式环境
//                helper.initSdkChat("068bdc10-6a5e-11e9-b93d-058bc760fe29", "测试fwff", "12fafa56789");//8016研发环境
//                helper.initSdkChat("8ce41210-35c4-11e9-a0e4-85286ffeb05a", "测试fwff", "12fafa56789");//亚光研发环境
//                helper.initSdkChat("79acb900-6188-11e9-8690-51c1496dadc8", "测试fwff", "12faf89");//亚光
//                helper.initSdkChat("768fe690-3b52-11e9-808a-59b9dcfe318f", "测试fwff", "12fafa56789");//zhaijiaceshi
//                helper.initSdkChat("109dbf80-827c-11e9-8d88-7152a4ee2638", "测试fwff", "12fafa56789");//潘测试
//                helper.initSdkChat("cd3a3430-82c6-11e9-8d07-015e55a6dd73", "测试fwff", "12fafa56789");//飞友
//                helper.initSdkChat("768fe690-3b52-11e9-808a-59b9dcfe318f", "测试fwff", "12fafa5689");
                helper.initSdkChat("f8a64660-8dbe-11e9-a8c9-bbd46426cdac", "测试fwff", "12fafa5689");//潘测试环境
            }
        });

        /**
         * 获取未读消息数示例
         */
        findViewById(R.id.setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (MoorUtils.isInitForUnread(MainActivity.this)) {
                    IMChatManager.getInstance().getMsgUnReadCountFromService(new IMChatManager.HttpUnReadListen() {
                        @Override
                        public void getUnRead(int acount) {
                            Toast.makeText(MainActivity.this, "未读消息数为：" + acount, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    //未初始化，消息当然为 ：0
                    Toast.makeText(MainActivity.this, "还没初始化", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    /**
     * 权限回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            PermissionUtils.onRequestPermissionsResult(this, 0x11, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, grantResults);
        }
    }

    /**
     * 语言切换
     * 中文 language：""
     * 英文 language："en"
     */
    private void initLanguage(String language) {
        Resources resources = getApplicationContext().getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = new Locale(language);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());//更新配置
    }
}
