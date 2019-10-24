package com.m7.imkfsdk;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.m7.imkfsdk.chat.ChatActivity;
import com.m7.imkfsdk.chat.LoadingFragmentDialog;
import com.m7.imkfsdk.constant.Constants;
import com.m7.imkfsdk.utils.FaceConversionUtil;
import com.m7.imkfsdk.utils.ToastUtils;
import com.moor.imkf.GetGlobleConfigListen;
import com.moor.imkf.GetPeersListener;
import com.moor.imkf.IMChatManager;
import com.moor.imkf.InitListener;
import com.moor.imkf.model.entity.CardInfo;
import com.moor.imkf.model.entity.Peer;
import com.moor.imkf.model.entity.ScheduleConfig;
import com.moor.imkf.utils.LogUtils;
import com.moor.imkf.utils.MoorUtils;

import java.util.List;

/**
 * Created by pangw on 2018/7/9.
 */

public class KfStartHelper {
    private LoadingFragmentDialog loadingDialog;
    private CardInfo card;
    private Activity mActivity;
    private Context context;
    private String receiverAction = "com.m7.imkf.KEFU_NEW_MSG";
    private String accessId;
    private String userName;
    private String userId;

    public void setCard(CardInfo card) {
        this.card = card;
        IMChatManager.getInstance().cardInfo = card;
    }

    public KfStartHelper(Activity activity) {
        mActivity = activity;
        context = activity.getApplicationContext();
        loadingDialog = new LoadingFragmentDialog();
        MoorUtils.init(activity.getApplication());
        initFaceUtils();
    }

    /**
     * 初始化表情
     */
    public void initFaceUtils() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (FaceConversionUtil.getInstace().emojis == null || FaceConversionUtil.getInstace().emojis.size() == 0) {
                    com.m7.imkfsdk.utils.FaceConversionUtil.getInstace().getFileText(
                            context);
                }
            }
        }).start();
    }

    public void initSdkChat(String accessId, String userName,
                            String userId) {
        this.accessId = accessId;
        this.userName = userName;
        this.userId = userId;

        if (!MoorUtils.isNetWorkConnected(context)) {
            Toast.makeText(context, R.string.notnetwork, Toast.LENGTH_SHORT).show();
            return;
        }
        loadingDialog.show(mActivity.getFragmentManager(), "");
        if (IMChatManager.isKFSDK) {
            getIsGoSchedule();
        } else {
            startKFService();
        }
    }

    /**
     * 校验跳转日程还是技能组
     */
    private void getIsGoSchedule() {
        IMChatManager.getInstance().getWebchatScheduleConfig(new GetGlobleConfigListen() {
            @Override
            public void getSchedule(ScheduleConfig sc) {
                loadingDialog.dismiss();
                LogUtils.aTag("MainActivity", "日程");
                if (!sc.getScheduleId().equals("") && !sc.getProcessId().equals("") && sc.getEntranceNode() != null && sc.getLeavemsgNodes() != null) {
                    if (sc.getEntranceNode().getEntrances().size() > 0) {
                        if (sc.getEntranceNode().getEntrances().size() == 1) {
                            ScheduleConfig.EntranceNodeBean.EntrancesBean bean = sc.getEntranceNode().getEntrances().get(0);
                            // TODO: 2019/5/23 这里改了
                            ChatActivity.startActivity(context, Constants.TYPE_SCHEDULE, sc.getScheduleId(), sc.getProcessId(), bean.getProcessTo(), bean.getProcessType(), bean.get_id(),card);
                        } else {
                            startScheduleDialog(sc.getEntranceNode().getEntrances(), sc.getScheduleId(), sc.getProcessId());
                        }

                    } else {
                        ToastUtils.showShort(R.string.sorryconfigurationiswrong);
                    }
                } else {
                    ToastUtils.showShort(R.string.sorryconfigurationiswrong);
                }
            }

            @Override
            public void getPeers() {
                LogUtils.aTag("start", "技能组");
                startChatActivityForPeer();
            }
        });
    }

    private void startScheduleDialog(final List<ScheduleConfig.EntranceNodeBean.EntrancesBean> entrances, final String scheduleId, final String processId) {
        final String[] items = new String[entrances.size()];
        for (int i = 0; i < entrances.size(); i++) {
            items[i] = entrances.get(i).getName();
        }

        AlertDialog dialog = new AlertDialog.Builder(mActivity)
                .setTitle("选择日程")
                // 设置列表显示，注意设置了列表显示就不要设置builder.setMessage()了，否则列表不起作用。
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ScheduleConfig.EntranceNodeBean.EntrancesBean bean = entrances.get(which);
                        LogUtils.aTag("选择日程：", bean.getName());
                        ChatActivity.startActivity(context, Constants.TYPE_SCHEDULE, scheduleId, processId, bean.getProcessTo(), bean.getProcessType(), bean.get_id(),card);

                    }
                }).create();
        dialog.show();
    }

    /**
     * 跳转技能组ChatActivity
     */
    private void startChatActivityForPeer() {
        IMChatManager.getInstance().getPeers(new GetPeersListener() {
            @Override
            public void onSuccess(List<Peer> peers) {

                if (peers.size() > 1) {
                    startPeersDialog(peers, card);
                } else if (peers.size() == 1) {
                    ChatActivity.startActivity(context, Constants.TYPE_PEER, peers.get(0).getId(), card);
                } else {
                    ToastUtils.showShort(R.string.peer_no_number);
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onFailed() {
                loadingDialog.dismiss();
            }
        });
    }


    private void startKFService() {

        new Thread() {
            @Override
            public void run() {
                IMChatManager.getInstance().setOnInitListener(new InitListener() {
                    @Override
                    public void oninitSuccess() {
                        IMChatManager.isKFSDK = true;
                        getIsGoSchedule();
                        Log.d("MainActivity", "sdk初始化成功");
                    }

                    @Override
                    public void onInitFailed() {
                        IMChatManager.isKFSDK = false;
                        ToastUtils.showShort(R.string.sdkinitwrong);
                        loadingDialog.dismiss();
                        Log.d("MainActivity", "sdk初始化失败, 请填写正确的accessid");
                    }
                });
                IMChatManager.getInstance().init(context,  receiverAction, accessId, userName, userId);
            }
        }.start();
    }

    public void startPeersDialog(final List<Peer> peers, final CardInfo mCardInfo) {
        final String[] items = new String[peers.size()];
        for (int i = 0; i < peers.size(); i++) {
            items[i] = peers.get(i).getName();
        }
        AlertDialog builder = new AlertDialog.Builder(mActivity)
                .setTitle("选择技能组")
                // 设置列表显示，注意设置了列表显示就不要设置builder.setMessage()了，否则列表不起作用。
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Peer peer = peers.get(which);
                        LogUtils.aTag("选择技能组：", peer.getName());
                        ChatActivity.startActivity(context, Constants.TYPE_PEER, peer.getId(), mCardInfo);
                    }
                }).create();
        builder.show();
    }

    //关闭log
    public void closeLog() {
        LogUtils.sLogSwitch = false;
    }

    //打开log
    public void openLog() {
        LogUtils.sLogSwitch = true;
    }

    /**
     * 0: 消息以userId 分割
     * 1: 消息以peedId（技能组） 分割 不适应于日程
     * 2: 消息以accessId 分割
     */
    public void setSaveMsgType(int type) {
        IMChatManager.getInstance().setSaveMsgType(type);
    }

    /**
     * 设置 receiverAction 和AndroidManifest中注册的广播中的action一致
     */
    public void setReceiverAction(String receiverAction) {
        this.receiverAction = receiverAction;
    }
}
