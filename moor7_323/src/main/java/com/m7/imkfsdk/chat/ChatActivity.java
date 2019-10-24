package com.m7.imkfsdk.chat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.m7.imkfsdk.R;
import com.m7.imkfsdk.chat.adapter.ChatAdapter;
import com.m7.imkfsdk.constant.Constants;
import com.m7.imkfsdk.recordbutton.AudioRecorderButton;
import com.m7.imkfsdk.utils.DensityUtil;
import com.m7.imkfsdk.utils.FaceConversionUtil;
import com.m7.imkfsdk.utils.FileUtils;
import com.m7.imkfsdk.utils.PermissionUtils;
import com.m7.imkfsdk.utils.PickUtils;
import com.m7.imkfsdk.utils.ToastUtils;
import com.m7.imkfsdk.view.ActionSheetDialog;
import com.m7.imkfsdk.view.ChatListView;
import com.m7.imkfsdk.view.TcpExitDiaglog;
import com.moor.imkf.AcceptOtherAgentListener;
import com.moor.imkf.ChatListener;
import com.moor.imkf.GetGlobleConfigListen;
import com.moor.imkf.GetPeersListener;
import com.moor.imkf.IMChat;
import com.moor.imkf.IMChatManager;
import com.moor.imkf.IMMessage;
import com.moor.imkf.OnConvertManualListener;
import com.moor.imkf.OnSessionBeginListener;
import com.moor.imkf.db.dao.GlobalSetDao;
import com.moor.imkf.db.dao.InfoDao;
import com.moor.imkf.db.dao.MessageDao;
import com.moor.imkf.eventbus.EventBus;
import com.moor.imkf.gson.JsonObject;
import com.moor.imkf.http.HttpManager;
import com.moor.imkf.http.HttpResponseListener;
import com.moor.imkf.model.construct.JsonBuild;
import com.moor.imkf.model.entity.CardInfo;
import com.moor.imkf.model.entity.ChatEmoji;
import com.moor.imkf.model.entity.ChatMore;
import com.moor.imkf.model.entity.FromToMessage;
import com.moor.imkf.model.entity.GlobalSet;
import com.moor.imkf.model.entity.Peer;
import com.moor.imkf.model.entity.ScheduleConfig;
import com.moor.imkf.model.parser.HttpParser;
import com.moor.imkf.tcpservice.event.ReSendMessage;
import com.moor.imkf.tcpservice.event.TcpBreakEvent;
import com.moor.imkf.tcpservice.event.UnAssignEvent;
import com.moor.imkf.tcpservice.logger.Logger;
import com.moor.imkf.tcpservice.service.IMService;
import com.moor.imkf.tcpservice.service.TcpManager;
import com.moor.imkf.utils.LogUtils;
import com.moor.imkf.utils.MoorUtils;
import com.moor.imkf.utils.NullUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 聊天界面
 *
 * @author LongWei
 */
public class ChatActivity extends MyBaseActivity implements OnClickListener,
        OnItemClickListener, ChatListView.OnRefreshListener, AudioRecorderButton.RecorderFinishListener {
    private ChatListView mChatList;
    private Button mChatSend, mChatMore, mChatSetModeVoice,
            mChatSetModeKeyboard;
    TextView chat_tv_back, chat_tv_convert;
    private EditText mChatInput;
    private ChatAdapter chatAdapter;
    private RelativeLayout mChatEdittextLayout,
            mChatMoreContainer;
    private LinearLayout mMore;
    private AudioRecorderButton mRecorderButton;
    //	private SpeechRecorderButton mRecorderButton;
    private RelativeLayout mChatFaceContainer;
    private ImageView mChatEmojiNormal, mChatEmojiChecked;
    private InputMethodManager manager;
    private TextView mOtherName;
    private OnCorpusSelectedListener mListener;
    private ViewPager mChatEmojiVPager, mChatMoreVPager;
    private ArrayList<View> facePageViews;
    private ArrayList<View> morePageViews;
    private LinearLayout mChatIvImageMore, mChatIvImageFace;
    private ArrayList<ImageView> pointViewsFace, pointViewsMore;
    private List<List<ChatEmoji>> emojis;
    private List<FaceAdapter> faceAdapters;
    private List<MoreAdapter> moreAdapters;
    private int current = 0;
    private ArrayList<ChatMore> moreList;
    private String codeDescription = "由于您长时间未接入，会话已被系统关闭~"; //更新内容描述信息
    private String codeDescription2 = "系统监测到网络异常，你可能收不到客服消息，请退出重试~"; //更新内容描述信息
    private TcpExitDiaglog singleButtonDialog;
    private TcpExitDiaglog.Builder builder;
    // 表情分页的结果集合
    public List<List<ChatMore>> moreLists = new ArrayList<List<ChatMore>>();
    private List<FromToMessage> fromToMessage;
    private Boolean JZflag = true;
    private boolean isZXResply = false;//坐席是否回复过访客，默认没有回复过（tcp推送变为true，接口校验也变为true ）
    private View header;
    private int i = 2;
    private int height;
    private List<FromToMessage> descFromToMessage = new ArrayList<FromToMessage>();
    private static final String tag = "ChatActivity";
    private static final int PICK_IMAGE_ACTIVITY_REQUEST_CODE = 200;
    private static final int PICK_FILE_ACTIVITY_REQUEST_CODE = 300;
    private String picFileFullName;
    MsgReceiver msgReceiver;
    KeFuStatusReceiver keFuStatusReceiver;
    private String peerId = "";
    LinearLayout chat_queue_ll;
    TextView chat_queue_tv;
    LinearLayout bar_bottom;
    private LoadingFragmentDialog loadingDialog;
    private static final int HANDLER_MSG = 1;
    private static final int HANDLER_MSG_MORE = 2;
    private static final int HANDLER_ROBOT = 0x111;
    private static final int HANDLER_ONLINE = 0x222;
    private static final int HANDLER_OFFNLINE = 0x333;
    private static final int HANDLER_INVESTIGATE = 0x444;
    private static final int HANDLER_QUEUENUM = 0x555;
    private static final int HANDLER_CLIAM = 0x666;
    private static final int HANDLER_FINISH = 0x777;
    private static final int HANDLER_BREAK = 0x888;
    private static final int HANDLER_BREAK_TIP = 0x999;
    private static final int HANDLER_VIPASSIGNFAIL = 0x1000;
    private static final int HANDLER_LEAVEMSG = 0x1100;
    private static final int HANDLER_WRITING = 0x1200;
    private static final int HANDLER_NO_WRITING = 0x1300;
    private boolean isRobot = false;
    private String type = "";
    private String scheduleId = "";
    private String processId = "";
    private String currentNodeId = "";
    private String schedule_id = "";
    private String schedule_topeer = "";
    private String processType = "";
    private String titleName = "等待接入";
    private String entranceId = "";
    private Handler handler = new MyHandle();
    private boolean isShowExitDialog = true;//标记 如果pc端率先结束了会话，这个时候app隐藏对话框，不展示强制退出的弹框

    //小陌机器人评价是否完成
    private Boolean robotEvaluationFinish = false;

    //是否和机器人发送过消息
    private boolean hasSendRobotMsg = false;

    ChatMore chatMore2, chatMore3, chatMore4;


    private boolean isQueue = false;

    private boolean isServiceConnected = false;
    private InvestigateDialog dialog;
    private String id;
    private boolean isInvestigate = true;//是否已经评价过了 true代表没有评价过，false代表评价过了
    private boolean convesationIsLive = true;// 会话是有存在或者有效。 true代表存在，false代表不存在
    private boolean hasSet = true;//是否配置了满意度列表
    private boolean conversationOver = false;//pc是否已经关闭会话 true关闭了
    private LinearLayout ll_hintView;

    class MyHandle extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == HANDLER_MSG) {
                updateMessage();
            } else if (msg.what == HANDLER_MSG_MORE) {
                // 加载更多的时候
                JZMoreMessage();
            } else if (msg.what == HANDLER_ROBOT) {
                //当前是机器人
                ToastUtils.showShort(R.string.now_robit);
                if (IMChatManager.getInstance().isShowTransferBtn()) {
                    chat_tv_convert.setVisibility(View.VISIBLE);
                } else {
                    chat_tv_convert.setVisibility(View.GONE);
                }
                bar_bottom.setVisibility(View.VISIBLE);
                isRobot = true;
                // 机器人
                setChatMoreList();
            } else if (msg.what == HANDLER_ONLINE) {
                //当前是客服
                chat_tv_convert.setVisibility(View.GONE);
            } else if (msg.what == HANDLER_WRITING) {
                //对方当前正在输入
                mOtherName.setText(R.string.other_writing);
            } else if (msg.what == HANDLER_NO_WRITING) {
                mOtherName.setText(titleName);
            } else if (msg.what == HANDLER_OFFNLINE) {
                ToastUtils.showShort(R.string.people_not_online);
                if (IMChatManager.getInstance().isShowTransferBtn()) {
                    chat_tv_convert.setVisibility(View.VISIBLE);
                } else {
                    chat_tv_convert.setVisibility(View.GONE);
                }
                if (isRobot) {
                    bar_bottom.setVisibility(View.VISIBLE);
                } else {
                    bar_bottom.setVisibility(View.VISIBLE);
                }
                showOffLineDialog();
            } else if (msg.what == HANDLER_INVESTIGATE) {
                sendInvestigate();
            } else if (msg.what == HANDLER_QUEUENUM) {
                String queueNem = (String) msg.obj;
                showQueueNumLabel(queueNem);
                isQueue = true;
                //排队
                setChatMoreList();
            } else if (msg.what == HANDLER_CLIAM) {
                chat_queue_ll.setVisibility(View.GONE);
                chat_tv_convert.setVisibility(View.GONE);
                bar_bottom.setVisibility(View.VISIBLE);
                isRobot = false;
                isQueue = false;
                //校验是否有效会话
                checkConverstaion();
//                setChatMoreList();
                Toast.makeText(getApplicationContext(), R.string.people_now, Toast.LENGTH_SHORT).show();
            } else if (msg.what == HANDLER_FINISH) {
                //  showSessionFinishDialog();
                // 如果会话已经不存在了
                if (IMChatManager.getInstance().isFinishWhenReConnect) {
                } else {
                    bar_bottom.setVisibility(View.GONE);
                }
                mOtherName.setText(R.string.people_isleave);
                titleName = getString(R.string.people_isleave);
                chat_tv_convert.setVisibility(View.GONE);
                conversationOver = true;
            } else if (msg.what == HANDLER_LEAVEMSG) {
                //跳留言
                Intent intent = new Intent(ChatActivity.this, ScheduleOfflineMessageActivity.class);
                intent.putExtra("LeavemsgNodeId", schedule_id);
                intent.putExtra("ToPeer", schedule_topeer);
                startActivity(intent);
                finish();
            } else if (msg.what == HANDLER_BREAK) {
                //断开会话
                IMChatManager.getInstance().quitSDk();
                finish();
            } else if (msg.what == HANDLER_BREAK_TIP) {
                //断开会话前提示
                IMChat.getInstance().createBreakTipMsg(break_tips);
                updateMessage();
            } else if (msg.what == HANDLER_VIPASSIGNFAIL) {
                //专属座席不在线
                showVipAssignFailDialog();
            } else if (getString(R.string.chat_img).equals(msg.obj)) {
                //校验权限
                openAlbum();
            } else if (getString(R.string.chat_evaluate).equals(msg.obj)) {
                openInvestigateDialog();
            } else if (getString(R.string.chat_file).equals(msg.obj)) {
                //校验权限
                openFile();
            }
        }
    }

    /**
     * 打开文件选择
     */
    private void openFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");//设置类型
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, PICK_FILE_ACTIVITY_REQUEST_CODE);
    }


    private void showVipAssignFailDialog() {
        new AlertDialog.Builder(this).setTitle(R.string.warm_prompt)
                .setMessage(R.string.doyouneedother)
                .setPositiveButton(R.string.need, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        IMChatManager.getInstance().acceptOtherAgent(peerId, new AcceptOtherAgentListener() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(ChatActivity.this, "已通知其它座席为你服务", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailed() {
                                Toast.makeText(ChatActivity.this, "通知其它座席失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton(R.string.noneed, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        IMChatManager.getInstance().quitSDk();
                        finish();
                    }
                })
                .setCancelable(false)
                .create()
                .show();

    }

    /**
     * 显示排队数
     */
    private void showQueueNumLabel(String queueNum) {
        if (Integer.parseInt(queueNum) > 0) {
            chat_queue_ll.setVisibility(View.VISIBLE);
            try {
                String queueNumText = GlobalSetDao.getInstance().getGlobalSet().queueNumText;
                int beginIndex = queueNumText.indexOf("{");
                int endIndex = queueNumText.indexOf("}");
                String newString = queueNumText.replace(queueNumText.substring(beginIndex, endIndex + 1), queueNum);
                SpannableString ss = new SpannableString(newString);
                ss.setSpan(new ForegroundColorSpan(Color.parseColor("#21b38a")), beginIndex,
                        beginIndex + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                chat_queue_tv.setText(ss);
            } catch (Exception e) {

                chat_queue_tv.setText(getResources().getText(R.string.numbers01) + queueNum + getResources().getText(R.string.number02));
            }
        } else {
            chat_queue_ll.setVisibility(View.GONE);
        }
    }

    public static void startActivity(Context mContext, String type, String scheduleId, String processId, String processTo, String processType, String id, CardInfo card) {
        // TODO: 2019/5/23 新增加，日程也可以有商品信息
        //卡片不为空 则传入数据库
        if (card != null) {
            FromToMessage cardMsg = new FromToMessage();
            cardMsg.msgType = FromToMessage.MSG_TYPE_CARD;
            cardMsg.cardInfo = JsonBuild.getCardInfo(card);
            cardMsg.userType = "0";
            cardMsg.when = System.currentTimeMillis();
            LogUtils.aTag("cardinfo", JsonBuild.getCardInfo(card));
            MessageDao.getInstance().insertSendMsgsToDao(cardMsg);
        }
        Intent chatIntent = new Intent(mContext, ChatActivity.class);
        chatIntent.putExtra("type", type);
        chatIntent.putExtra("scheduleId", scheduleId);
        chatIntent.putExtra("processId", processId);
        chatIntent.putExtra("currentNodeId", processTo);
        chatIntent.putExtra("processType", processType);
        chatIntent.putExtra("entranceId", id);
        chatIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(chatIntent);
    }

    public static void startActivity(Context mContext, String type, String scheduleId, String processId, String processTo, String processType, String id) {
        startActivity(mContext, type, scheduleId, processId, processTo, processType, id, null);
    }

    public static void startActivity(Context mContext, String type, String peerId, CardInfo card) {
        InfoDao.getInstance().updataPeedID(peerId);
        //卡片不为空 则传入数据库
        if (card != null) {
            FromToMessage cardMsg = new FromToMessage();
            cardMsg.msgType = FromToMessage.MSG_TYPE_CARD;
            cardMsg.cardInfo = JsonBuild.getCardInfo(card);
            cardMsg.userType = "0";
            cardMsg.when = System.currentTimeMillis();
            LogUtils.aTag("cardinfo", JsonBuild.getCardInfo(card));
            MessageDao.getInstance().insertSendMsgsToDao(cardMsg);
        }
        Intent chatIntent = new Intent(mContext, ChatActivity.class);
        chatIntent.putExtra("type", type);
        chatIntent.putExtra("PeerId", peerId);
        chatIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(chatIntent);
    }

    public static void startActivity(Context mContext, String type, String peerId) {
        startActivity(mContext, type, peerId, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.kf_activity_chat);
        getIntentData(getIntent());

        IntentFilter intentFilter = new IntentFilter("com.m7.imkfsdk.msgreceiver");
        msgReceiver = new MsgReceiver();
        registerReceiver(msgReceiver, intentFilter);

        EventBus.getDefault().register(this);

        IntentFilter kefuIntentFilter = new IntentFilter();
        kefuIntentFilter.addAction(IMChatManager.ROBOT_ACTION);
        kefuIntentFilter.addAction(IMChatManager.ONLINE_ACTION);
        kefuIntentFilter.addAction(IMChatManager.OFFLINE_ACTION);
        kefuIntentFilter.addAction(IMChatManager.CLIAM_ACTION);
        kefuIntentFilter.addAction(IMChatManager.INVESTIGATE_ACTION);
        kefuIntentFilter.addAction(IMChatManager.QUEUENUM_ACTION);
        kefuIntentFilter.addAction(IMChatManager.LEAVEMSG_ACTION);
        kefuIntentFilter.addAction(IMChatManager.FINISH_ACTION);
        kefuIntentFilter.addAction(IMChatManager.USERINFO_ACTION);
        kefuIntentFilter.addAction(IMChatManager.VIPASSIGNFAIL_ACTION);
        kefuIntentFilter.addAction(IMChatManager.CANCEL_ROBOT_ACCESS_ACTION);
        kefuIntentFilter.addAction(IMChatManager.WITHDRAW_ACTION);
        kefuIntentFilter.addAction(IMChatManager.WRITING_ACTION);
        kefuIntentFilter.addAction(IMChatManager.ROBOT_SWITCH_ACTION);
        kefuIntentFilter.addAction(IMChatManager.TCP_ACTION);
        kefuIntentFilter.addAction(IMChatManager.ZXMSG_ACTION);
        keFuStatusReceiver = new KeFuStatusReceiver();
        registerReceiver(keFuStatusReceiver, kefuIntentFilter);
        init();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            initPermission();
        }
        registerListener();
        initEmojiViewPager();
        initEmojiPoint();
        initEmojiData();
        initMoreViewPager();
        initMorePoint();
        initMoreData();
        updateMessage();
//        //是否配置了满意度
        if (IMChatManager.getInstance().getInvestigate().size() > 0) {
            hasSet = true;
        } else {
            hasSet = false;
        }
        loadingDialog = new LoadingFragmentDialog();
        loadingDialog.show(this.getFragmentManager(), "");
        if (type.equals("peedId")) {
            LogUtils.aTag("beginSession", "ChatActivity482行代码");
            beginSession(peerId);
        }
        if (type.equals("schedule")) {
            beginScheduleSession(scheduleId, processId, currentNodeId, entranceId);
        }
        builder = new TcpExitDiaglog.Builder(this);
        //设置全局配置
        setGlobalConfig();

    }


    private void initPermission() {
        if (PermissionUtils.hasAlwaysDeniedPermission(this, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            PermissionUtils.requestPermissions(this, 0x11, new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionUtils.OnPermissionListener() {
                @Override
                public void onPermissionGranted() {
                }

                @Override
                public void onPermissionDenied(String[] deniedPermissions) {
                    Toast.makeText(ChatActivity.this, R.string.notpermession, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    Timer break_timer;
    Timer break_tip_timer;
    long breakTime = 0;
    long breakTipTime = 0;
    String break_tips;
    BreakTimerTask breakTimerTask;
    BreakTipTimerTask breakTipTimerTask;

    /**
     * 设置断开定时器
     */
    private void setGlobalConfig() {
        GlobalSet globalSet = GlobalSetDao.getInstance().getGlobalSet();
        if (globalSet != null) {
            String break_len = globalSet.break_len;
            String break_tips_len = globalSet.break_tips_len;
            break_tips = globalSet.break_tips;
            try {
                breakTime = Integer.parseInt(break_len) * 60 * 1000;
            } catch (Exception e) {
            }
            try {
                breakTipTime = breakTime - Integer.parseInt(break_tips_len) * 60 * 1000;
            } catch (Exception e) {
            }
            if (breakTime > 0) {
                break_timer = new Timer();
                breakTimerTask = new BreakTimerTask();
                break_timer.schedule(breakTimerTask, breakTime);
            }
            if (breakTipTime > 0) {
                break_tip_timer = new Timer();
                breakTipTimerTask = new BreakTipTimerTask();
                break_tip_timer.schedule(breakTipTimerTask, breakTipTime);
            }
        }
    }


    /**
     * 发送文本消息
     *
     * @param msgStr
     */
    public void sendTextMsg(String msgStr) {
        //访客没发过消息，显示评论按。 2 访客发过消息后，就不再校验了。
        if (!hasSendRobotMsg) {
            hasSendRobotMsg = true;
            setChatMoreList();
        }

        LogUtils.aTag("send", msgStr);
        FromToMessage fromToMessage = IMMessage.createTxtMessage(msgStr);

        //界面显示
        descFromToMessage.add(fromToMessage);
        chatAdapter.notifyDataSetChanged();
        mChatList.setSelection(descFromToMessage.size());
        mChatInput.setText("");

        resetBreakTimer();

        //发送消息
        IMChat.getInstance().sendMessage(fromToMessage, new ChatListener() {
            @Override
            public void onSuccess() {
                //消息发送成功
                updateMessage();
            }

            @Override
            public void onFailed() {
                //消息发送失败
                updateMessage();
            }

            @Override
            public void onProgress(int progress) {

            }
        });
    }

    /**
     * 断开定时器任务
     */
    class BreakTimerTask extends TimerTask {
        @Override
        public void run() {
            handler.sendEmptyMessage(HANDLER_BREAK);
            break_timer.cancel();
        }
    }

    /**
     * 断开前提示定时器任务
     */
    class BreakTipTimerTask extends TimerTask {
        @Override
        public void run() {
            handler.sendEmptyMessage(HANDLER_BREAK_TIP);
            break_tip_timer.cancel();
        }
    }

    /**
     * 重置断开提示定时器
     */
    private void resetBreakTimer() {
        if (break_timer != null) {
            break_timer.cancel();
            break_timer = null;
        }
        if (break_tip_timer != null) {
            break_tip_timer.cancel();
            break_tip_timer = null;
        }

        if (breakTimerTask != null) {
            breakTimerTask.cancel();
        }
        if (breakTipTimerTask != null) {
            breakTipTimerTask.cancel();
        }

        if (breakTime > 0) {
            break_timer = new Timer();
            breakTimerTask = new BreakTimerTask();
            break_timer.schedule(breakTimerTask, breakTime);
        }
        if (breakTipTime > 0) {
            break_tip_timer = new Timer();
            breakTipTimerTask = new BreakTipTimerTask();
            break_tip_timer.schedule(breakTipTimerTask, breakTipTime);
        }
    }

    /**
     * 查询数据库更新页面
     */
    public void updateMessage() {
        fromToMessage = IMChatManager.getInstance().getMessages(1);
        descFromToMessage.clear();
        for (int i = fromToMessage.size() - 1; i >= 0; i--) {
            descFromToMessage.add(fromToMessage.get(i));
        }
        // 是否有数据
        if (IMChatManager.getInstance().isReachEndMessage(
                descFromToMessage.size())) {
            mChatList.dismiss();
        }
        chatAdapter = new ChatAdapter(ChatActivity.this, descFromToMessage);
        mChatList.setAdapter(chatAdapter);
        chatAdapter.notifyDataSetChanged();
        mChatList.setSelection(fromToMessage.size() + 1);

        //重置未读消息数量
        IMChatManager.getInstance().resetMsgUnReadCount();
        //刷新标题
        mOtherName.setText(titleName);
        if (handler.hasMessages(HANDLER_NO_WRITING)) {
            handler.removeMessages(HANDLER_NO_WRITING);
        }
    }

    // 会话被结束掉了，点击输入框，重新开始会话，
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        updateMessage();
        //小陌机器人评价是否完成
        robotEvaluationFinish = false;

        //是否和机器人发送过消息
        hasSendRobotMsg = false;

        isQueue = false;
        JZflag = true;
        resetBreakTimer();

        getIntentData(intent);
        /*临时注释掉*/
        if (type.equals("peedId")) {
            LogUtils.aTag("beginSession", "ChatActivity690行代码");
            beginSession(peerId);
        }
        if (type.equals("schedule")) {
            beginScheduleSession(scheduleId, processId, currentNodeId, entranceId);
        }

    }

    public void getIntentData(Intent intent) {
        //获取技能组id
        if (intent.getStringExtra("PeerId") != null) {
            peerId = intent.getStringExtra("PeerId");
        }
        if (intent.getStringExtra("type") != null) {
            type = intent.getStringExtra("type");
        }
        if (intent.getStringExtra("scheduleId") != null) {
            scheduleId = intent.getStringExtra("scheduleId");
        }
        if (intent.getStringExtra("processId") != null) {
            processId = intent.getStringExtra("processId");
        }
        if (intent.getStringExtra("currentNodeId") != null) {
            currentNodeId = intent.getStringExtra("currentNodeId");
        }
        if (intent.getStringExtra("entranceId") != null) {
            entranceId = intent.getStringExtra("entranceId");
        }
        if (intent.getStringExtra("processType") != null) {
            processType = intent.getStringExtra("processType");
        }
        //初始化成功打标记（引用于调用服务未读消息数）
        MoorUtils.initForUnread(ChatActivity.this);
        IMChatManager.getInstance().isFinishWhenReConnect = false;
    }

    // 分页加载更多
    public void JZMoreMessage() {
        fromToMessage = IMChatManager.getInstance().getMessages(i);
        descFromToMessage.clear();
        for (int i = fromToMessage.size() - 1; i >= 0; i--) {
            descFromToMessage.add(fromToMessage.get(i));
        }

        chatAdapter.notifyDataSetChanged();

        if (mChatList.getHeaderViewsCount() > 0) {
            mChatList.removeHeaderView(header);
        }

        // 是否有数据
        if (IMChatManager.getInstance().isReachEndMessage(
                descFromToMessage.size())) {
            mChatList.setSelectionFromTop(fromToMessage.size() - (i - 1) * 15,
                    height);
            mChatList.dismiss();
        } else {
            mChatList.setSelectionFromTop(fromToMessage.size() - (i - 1) * 15
                    + 1, height);
        }

        mChatList.onRefreshFinished();
        JZflag = true;
        i++;

    }

    // 初始化方法
    public void init() {
        // 设置进来时间软键盘不弹出的默认状态
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mChatSend = (Button) this.findViewById(R.id.chat_send);
        chat_tv_back = (TextView) this.findViewById(R.id.chat_tv_back);
        mRecorderButton = (AudioRecorderButton) findViewById(R.id.chat_press_to_speak);
        mRecorderButton.setRecordFinishListener(this);
        mChatInput = (EditText) this.findViewById(R.id.chat_input);
        ll_hintView = (LinearLayout) this.findViewById(R.id.ll_hintView);
        mChatEdittextLayout = (RelativeLayout) this
                .findViewById(R.id.chat_edittext_layout);
        mMore = (LinearLayout) this.findViewById(R.id.more);
        mChatEmojiNormal = (ImageView) this
                .findViewById(R.id.chat_emoji_normal);
        mChatEmojiChecked = (ImageView) this
                .findViewById(R.id.chat_emoji_checked);
        mChatFaceContainer = (RelativeLayout) this
                .findViewById(R.id.chat_face_container);
        mChatMoreContainer = (RelativeLayout) this
                .findViewById(R.id.chat_more_container);
        mChatMore = (Button) this.findViewById(R.id.chat_more);

        mChatSetModeVoice = (Button) this
                .findViewById(R.id.chat_set_mode_voice);
        mChatSetModeKeyboard = (Button) this
                .findViewById(R.id.chat_set_mode_keyboard);

        //转人工服务按钮，判断是否需要显示
        chat_tv_convert = (TextView) this.findViewById(R.id.chat_tv_convert);

        if (type.equals("schedule") && !processType.equals("robot")) {
            chat_tv_convert.setVisibility(View.GONE);
        }
        if (!IMChatManager.getInstance().isShowTransferBtn()) {
            chat_tv_convert.setVisibility(View.GONE);
        }

        mOtherName = (TextView) this.findViewById(R.id.other_name);
        mChatInput.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mChatEdittextLayout
                            .setBackgroundResource(R.drawable.kf_input_bar_bg_active);
                } else {
                    mChatEdittextLayout
                            .setBackgroundResource(R.drawable.kf_input_bar_bg_normal);
                }

            }
        });

        mChatInput.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!MoorUtils.isNetWorkConnected(IMChatManager.getInstance().getAppContext()) &&
                        TcpManager.TcpStatus.BREAK.equals(TcpManager.getInstance(IMChatManager.getInstance().getAppContext()).getTcpStatus())) {
//                    Toast.makeText(getApplicationContext(), "检测到您网络异常啦~", Toast.LENGTH_SHORT).show();
                    LogUtils.aTag("第五个地方break");
                    TcpManager.getInstance(IMChatManager.getInstance().getAppContext()).setTcpStatus(TcpManager.TcpStatus.NONET);
                    startReStartDialog3();
                    return;
                }
                if (IMChatManager.getInstance().isFinishWhenReConnect) {
                    // beginSession();
                    startReStartDialog();
                } else {
                    mChatEdittextLayout
                            .setBackgroundResource(R.drawable.kf_input_bar_bg_active);
                    mChatEmojiNormal.setVisibility(View.VISIBLE);
                    mChatEmojiChecked.setVisibility(View.GONE);

                    mMore.setVisibility(View.GONE);
                    mChatFaceContainer.setVisibility(View.GONE);
                    mChatMoreContainer.setVisibility(View.GONE);
                }


            }
        });

        // 监听文字框
        mChatInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (!TextUtils.isEmpty(s)) {
                    mChatMore.setVisibility(View.GONE);
                    mChatSend.setVisibility(View.VISIBLE);
                } else {
                    mChatMore.setVisibility(View.VISIBLE);
                    mChatSend.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                //如果开启联想功能,xbot机器人
                if (!TextUtils.isEmpty(s)) {
                    if (isRobot && "xbot".equals(IMChat.getInstance().getRobotType()) && IMChat.getInstance().getLianXiangOn())
                        HttpManager.queryLianXiangData(InfoDao.getInstance().getConnectionId(), "xbot", s.toString(), new GetLianXiangDataResponeHandler());
                }
            }
        });

        mChatList = (ChatListView) this.findViewById(R.id.chat_list);
        header = View.inflate(this, R.layout.kf_chatlist_header, null);
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        header.measure(w, h);
        height = header.getMeasuredHeight();

        mChatList.setOnTouchListener(new OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                mMore.setVisibility(View.GONE);
                mChatEmojiNormal.setVisibility(View.VISIBLE);
                mChatEmojiChecked.setVisibility(View.GONE);
                mChatFaceContainer.setVisibility(View.GONE);
                mChatMoreContainer.setVisibility(View.GONE);
                return false;
            }
        });

        emojis = FaceConversionUtil.getInstace().emojiLists;
        moreList = new ArrayList<ChatMore>();
        chatMore2 = new ChatMore(2, R.drawable.kf_icon_chat_pic + "",
                getString(R.string.chat_img));
        chatMore3 = new ChatMore(3, R.drawable.kf_icon_chat_file + "",
                getString(R.string.chat_file));
        chatMore4 = new ChatMore(4, R.drawable.kf_icon_chat_investigate + "",
                getString(R.string.chat_evaluate));
        moreList.add(chatMore2);
        moreList.add(chatMore3);
//        moreList.add(chatMore4);//默认不展示评价按钮
        int pageCount = (int) Math.ceil(moreList.size() / 8 + 0.1);
        for (int i = 0; i < pageCount; i++) {
            moreLists.add(getData(i));
        }

        mChatEmojiVPager = (ViewPager) findViewById(R.id.chat_emoji_vPager);
        mChatMoreVPager = (ViewPager) findViewById(R.id.chat_more_vPager);
        mChatInput = (EditText) findViewById(R.id.chat_input);
        mChatIvImageFace = (LinearLayout) findViewById(R.id.chat_iv_image_face);
        mChatIvImageMore = (LinearLayout) findViewById(R.id.chat_iv_image_more);
        chat_queue_ll = (LinearLayout) findViewById(R.id.chat_queue_ll);
        chat_queue_tv = (TextView) findViewById(R.id.chat_queue_tv);
        bar_bottom = (LinearLayout) findViewById(R.id.bar_bottom);
    }

    // 注册监听方法
    public void registerListener() {
        mChatSend.setOnClickListener(this);
        chat_tv_back.setOnClickListener(this);
        mChatSetModeVoice.setOnClickListener(this);
        mChatSetModeKeyboard.setOnClickListener(this);
        mChatEmojiNormal.setOnClickListener(this);
        mChatEmojiChecked.setOnClickListener(this);
        mChatMore.setOnClickListener(this);
        mChatList.setOnRefreshListener(this);
        chat_tv_convert.setOnClickListener(this);
    }

    // 获取分页数据
    private List<ChatMore> getData(int page) {
        int startIndex = page * 8;
        int endIndex = startIndex + 8;

        if (endIndex > moreList.size()) {
            endIndex = moreList.size();
        }
        List<ChatMore> list = new ArrayList<ChatMore>();
        list.addAll(moreList.subList(startIndex, endIndex));
        if (list.size() < 8) {
            for (int i = list.size(); i < 8; i++) {
                ChatMore object = new ChatMore();
                list.add(object);
            }
        }
        return list;
    }

    // 隐藏软键盘
    private void hideKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onClick(View v) {
        int i1 = v.getId();
        if (i1 == R.id.chat_tv_back) {//断开长连接
            //人工，开启了评价,坐席发过消息、会话存在，访客发过消息，没有评价过,配置了满意度列表,pc没有结束会话
            if (!isRobot && IMChatManager.getInstance().isInvestigateOn() && convesationIsLive && isZXResply && isInvestigate && hasSet && !conversationOver) {
                if (dialog != null && dialog.getDialog() != null && dialog.getDialog().isShowing()) {
                    return;
                }
                dialog = new InvestigateDialog(new InvestigateDialog.SubmitPingjiaListener() {
                    //无论评价成功与否，都要退出
                    @Override
                    public void OnSubmitSuccess() {
                        //去掉开关
                        IMChat.getInstance().setLianXiangOn(false);
                        IMChat.getInstance().setBotsatisfaOn(false);
                        //评价成功
                        IMChatManager.getInstance().quitSDk();
                        finish();
                    }

                    @Override
                    public void OnSubmitFailed() {
                        IMChatManager.getInstance().quitSDk();
                        finish();
                    }
                });
                dialog.show(getFragmentManager(), "InvestigateDialog");
            } else {
                //去掉开关
                IMChat.getInstance().setLianXiangOn(false);
                IMChat.getInstance().setBotsatisfaOn(false);
                IMChatManager.getInstance().quitSDk();
                finish();
            }
        } else if (i1 == R.id.chat_tv_convert) {//转人工服务
            IMChatManager.getInstance().convertManual(new OnConvertManualListener() {
                @Override
                public void onLine() {
                    if (!type.equals("schedule")) {
                        //有客服在线,隐藏转人工按钮
                        chat_tv_convert.setVisibility(View.GONE);
                        bar_bottom.setVisibility(View.VISIBLE);
                        mOtherName.setText(R.string.wait_link);
                        titleName = getString(R.string.wait_link);
                        Toast.makeText(getApplicationContext(), R.string.topeoplesucceed, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void offLine() {
                    //当前没有客服在线
                    if (!type.equals("schedule")) {

                        showOffLineDialog();
                    }

                }
            });

        } else if (i1 == R.id.chat_send) {
            String txt = mChatInput.getText().toString();
            //如果是tcp断开并且网络ok的情况，执行重连操作
            if (!MoorUtils.isNetWorkConnected(IMChatManager.getInstance().getAppContext()) &&
                    TcpManager.TcpStatus.BREAK.equals(TcpManager.getInstance(IMChatManager.getInstance().getAppContext()).getTcpStatus())) {
                Toast.makeText(getApplicationContext(), "检测到您网络异常啦~", Toast.LENGTH_SHORT).show();
                LogUtils.aTag("第四个地方break");
//                IMService.isRelogin = true;
                TcpManager.getInstance(IMChatManager.getInstance().getAppContext()).setTcpStatus(TcpManager.TcpStatus.NONET);
                startReStartDialog3();
                return;
            }
            if (IMChatManager.getInstance().isFinishWhenReConnect) {
                startReStartDialog();//并且弹框提示开始新会话
//            }
            } else {
                ll_hintView.setVisibility(View.GONE);
                sendTextMsg(txt);
            }
        } else if (i1 == R.id.chat_set_mode_voice) {
            if (Build.VERSION.SDK_INT < 23) {
                showVoice();
            } else {
                //6.0
                if (ContextCompat.checkSelfPermission(ChatActivity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                    //该权限已经有了
                    showVoice();
                } else {
                    //申请该权限
                    ActivityCompat.requestPermissions(ChatActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, 0x4444);
                }
            }


        } else if (i1 == R.id.chat_set_mode_keyboard) {
            mChatEdittextLayout.setVisibility(View.VISIBLE);
            mChatSetModeKeyboard.setVisibility(View.GONE);
            mChatSetModeVoice.setVisibility(View.VISIBLE);
            mChatInput.requestFocus();
            mRecorderButton.setVisibility(View.GONE);
            mChatFaceContainer.setVisibility(View.GONE);

            if (TextUtils.isEmpty(mChatInput.getText())) {
                mChatMore.setVisibility(View.VISIBLE);
                mChatSend.setVisibility(View.GONE);
            } else {
                mChatMore.setVisibility(View.GONE);
                mChatSend.setVisibility(View.VISIBLE);
            }


        } else if (i1 == R.id.chat_emoji_normal) {
            hideKeyboard();
            mMore.setVisibility(View.VISIBLE);
            mChatEmojiNormal.setVisibility(View.GONE);
            mChatEmojiChecked.setVisibility(View.VISIBLE);
            mChatMoreContainer.setVisibility(View.GONE);
            mChatFaceContainer.setVisibility(View.VISIBLE);
            mChatMoreVPager.setVisibility(View.GONE);
            mChatEmojiVPager.setVisibility(View.VISIBLE);

        } else if (i1 == R.id.chat_emoji_checked) {
            mChatEmojiNormal.setVisibility(View.VISIBLE);
            mChatEmojiChecked.setVisibility(View.GONE);
            mChatMoreContainer.setVisibility(View.GONE);
            mChatFaceContainer.setVisibility(View.GONE);
            mMore.setVisibility(View.GONE);

        } else if (i1 == R.id.chat_more) {
            if (mChatMoreVPager.getVisibility() == View.VISIBLE) {
                mChatMoreVPager.setVisibility(View.GONE);
                mMore.setVisibility(View.GONE);
            } else {
                mChatMoreVPager.setVisibility(View.VISIBLE);
                mMore.setVisibility(View.VISIBLE);
                mChatEmojiNormal.setVisibility(View.VISIBLE);
                mChatEmojiChecked.setVisibility(View.GONE);
                mChatFaceContainer.setVisibility(View.GONE);
                mChatMoreContainer.setVisibility(View.VISIBLE);
                mChatEmojiVPager.setVisibility(View.GONE);
                hideKeyboard();
            }

        } else {
        }
    }

    private void showVoice() {
        hideKeyboard();
        mChatEdittextLayout.setVisibility(View.GONE);
        mMore.setVisibility(View.GONE);
        mChatSetModeVoice.setVisibility(View.GONE);
        mChatSetModeKeyboard.setVisibility(View.VISIBLE);
        mChatSend.setVisibility(View.GONE);
        mChatMore.setVisibility(View.VISIBLE);
        mRecorderButton.setVisibility(View.VISIBLE);
        mChatEmojiNormal.setVisibility(View.VISIBLE);
        mChatEmojiChecked.setVisibility(View.GONE);
        mChatMoreContainer.setVisibility(View.VISIBLE);
        mChatFaceContainer.setVisibility(View.GONE);
    }

    // 表情选择监听器
    public interface OnCorpusSelectedListener {

        void onCorpusSelected(ChatEmoji emoji);

        void onCorpusDeleted();
    }

    // 初始化文件的viewpager
    private void initMoreViewPager() {

        morePageViews = new ArrayList<View>();
        // 左侧添加空页
        View nullView1 = new View(this);
        // 设置透明背景
        nullView1.setBackgroundColor(Color.TRANSPARENT);
        morePageViews.add(nullView1);

        // 中间添加表情页
        moreAdapters = new ArrayList<MoreAdapter>();
        for (int i = 0; i < moreLists.size(); i++) {
            GridView view = new GridView(this);
            MoreAdapter adapter = new MoreAdapter(this, moreLists.get(i),
                    handler);
            view.setAdapter(adapter);
            moreAdapters.add(adapter);
            view.setOnItemClickListener(this);
            view.setNumColumns(4);
            view.setBackgroundColor(Color.TRANSPARENT);
            view.setHorizontalSpacing(1);
            view.setVerticalSpacing(1);
            view.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
            view.setCacheColorHint(0);
            view.setPadding(5, 0, 5, 0);
            view.setSelector(new ColorDrawable(Color.TRANSPARENT));
            view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
                    LayoutParams.WRAP_CONTENT));
            view.setGravity(Gravity.CENTER);
            morePageViews.add(view);

        }

        // 右侧添加空页面
        View nullView2 = new View(this);
        // 设置透明背景
        nullView2.setBackgroundColor(Color.TRANSPARENT);
        morePageViews.add(nullView2);

    }

    // 初始化游标
    private void initMorePoint() {

        pointViewsMore = new ArrayList<ImageView>();
        ImageView imageView;
        mChatIvImageMore.removeAllViews();
        for (int i = 0; i < morePageViews.size(); i++) {
            imageView = new ImageView(this);
            imageView.setBackgroundResource(R.drawable.kf_d1);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT));
            layoutParams.leftMargin = 10;
            layoutParams.rightMargin = 10;
            layoutParams.width = 8;
            layoutParams.height = 8;
            mChatIvImageMore.addView(imageView, layoutParams);
            if (i == 0 || i == morePageViews.size() - 1) {
                imageView.setVisibility(View.GONE);
            }
            if (i == 1) {
                imageView.setBackgroundResource(R.drawable.kf_d2);
            }
            pointViewsMore.add(imageView);

        }
    }

    // 填充数据
    private void initMoreData() {
        mChatMoreVPager.setAdapter(new ViewPagerAdapter(morePageViews));

        mChatMoreVPager.setCurrentItem(1);
        current = 0;
        mChatMoreVPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                current = arg0 - 1;
                // 描绘分页点
                drawMorePoint(arg0);
                // 如果是第一屏或者是最后一屏禁止滑动，其实这里实现的是如果滑动的是第一屏则跳转至第二屏，如果是最后一屏则跳转到倒数第二屏.
                if (arg0 == pointViewsMore.size() - 1 || arg0 == 0) {
                    if (arg0 == 0) {
                        mChatMoreVPager.setCurrentItem(arg0 + 1);// 第二屏
                        // 会再次实现该回调方法实现跳转.
                        pointViewsMore.get(1).setBackgroundResource(
                                R.drawable.kf_d2);
                    } else {
                        mChatMoreVPager.setCurrentItem(arg0 - 1);// 倒数第二屏
                        pointViewsMore.get(arg0 - 1).setBackgroundResource(
                                R.drawable.kf_d2);
                    }
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

    }

    // 绘制游标背景
    public void drawMorePoint(int index) {
        for (int i = 1; i < pointViewsMore.size(); i++) {
            if (index == i) {
                pointViewsMore.get(i).setBackgroundResource(R.drawable.kf_d2);
            } else {
                pointViewsMore.get(i).setBackgroundResource(R.drawable.kf_d1);
            }
        }
    }

    // 初始化显示表情的viewpager
    private void initEmojiViewPager() {
        facePageViews = new ArrayList<View>();
        // 左侧添加空页
        View nullView1 = new View(this);
        // 设置透明背景
        nullView1.setBackgroundColor(Color.TRANSPARENT);
        facePageViews.add(nullView1);
        // 中间添加表情页
        faceAdapters = new ArrayList<FaceAdapter>();
        for (int i = 0; i < emojis.size(); i++) {
            GridView view = new GridView(this);
            FaceAdapter adapter = new FaceAdapter(this, emojis.get(i));
            view.setAdapter(adapter);
            faceAdapters.add(adapter);
            view.setOnItemClickListener(this);
            view.setNumColumns(7);
            view.setBackgroundColor(Color.TRANSPARENT);
            view.setHorizontalSpacing(1);
            view.setVerticalSpacing(1);
            view.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
            view.setCacheColorHint(0);
            view.setPadding(5, 0, 5, 0);
            view.setSelector(new ColorDrawable(Color.TRANSPARENT));
            view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
                    LayoutParams.WRAP_CONTENT));
            view.setGravity(Gravity.CENTER);
            facePageViews.add(view);
        }

        // 右侧添加空页面
        View nullView2 = new View(this);
        // 设置透明背景
        nullView2.setBackgroundColor(Color.TRANSPARENT);
        facePageViews.add(nullView2);

    }

    // 初始化游标
    private void initEmojiPoint() {
        pointViewsFace = new ArrayList<ImageView>();
        ImageView imageView;
        for (int i = 0; i < facePageViews.size(); i++) {
            imageView = new ImageView(this);
            imageView.setBackgroundResource(R.drawable.kf_d1);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT));
            layoutParams.leftMargin = 10;
            layoutParams.rightMargin = 10;
            layoutParams.width = 8;
            layoutParams.height = 8;
            mChatIvImageFace.addView(imageView, layoutParams);
            if (i == 0 || i == facePageViews.size() - 1) {
                imageView.setVisibility(View.GONE);
            }
            if (i == 1) {
                imageView.setBackgroundResource(R.drawable.kf_d2);
            }
            pointViewsFace.add(imageView);
        }
    }

    // 填充数据
    private void initEmojiData() {
        mChatEmojiVPager.setAdapter(new ViewPagerAdapter(facePageViews));

        mChatEmojiVPager.setCurrentItem(1);
        current = 0;
        mChatEmojiVPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                current = arg0 - 1;
                // 描绘分页点
                drawFacePoint(arg0);
                // 如果是第一屏或者是最后一屏禁止滑动，其实这里实现的是如果滑动的是第一屏则跳转至第二屏，如果是最后一屏则跳转到倒数第二屏.
                if (arg0 == pointViewsFace.size() - 1 || arg0 == 0) {
                    if (arg0 == 0) {
                        mChatEmojiVPager.setCurrentItem(arg0 + 1);// 第二屏
                        // 会再次实现该回调方法实现跳转.
                        pointViewsFace.get(1).setBackgroundResource(
                                R.drawable.kf_d2);
                    } else {
                        mChatEmojiVPager.setCurrentItem(arg0 - 1);// 倒数第二屏
                        pointViewsFace.get(arg0 - 1).setBackgroundResource(
                                R.drawable.kf_d2);
                    }
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

    }

    // 绘制游标背景
    public void drawFacePoint(int index) {
        for (int i = 1; i < pointViewsFace.size(); i++) {
            if (index == i) {
                pointViewsFace.get(i).setBackgroundResource(R.drawable.kf_d2);
            } else {
                pointViewsFace.get(i).setBackgroundResource(R.drawable.kf_d1);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        if (mChatFaceContainer.getVisibility() == View.VISIBLE
                && mChatMoreContainer.getVisibility() == View.GONE) {

            ChatEmoji emoji = (ChatEmoji) faceAdapters.get(current).getItem(
                    arg2);
            if (emoji.getId() == R.drawable.kf_face_del_icon) {
                int selection = mChatInput.getSelectionStart();
                String text = mChatInput.getText().toString();
                if (selection > 0) {
                    String text2 = text.substring(selection - 1);
                    if (":".equals(text2)) {
                        String str = text.substring(0, selection - 1);
                        int start = str.lastIndexOf(":");
                        int end = selection;
                        mChatInput.getText().delete(start, end);
                        return;
                    }
                    mChatInput.getText().delete(selection - 1, selection);
                }

            }
            if (!TextUtils.isEmpty(emoji.getCharacter())) {
                if (mListener != null)
                    mListener.onCorpusSelected(emoji);
                SpannableString spannableString = FaceConversionUtil
                        .getInstace().addFace(this, emoji.getId(),
                                emoji.getCharacter(), mChatInput);
                mChatInput.append(spannableString);
            }
        }
    }

    // 打开本地相册
    public void openAlbum() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        this.startActivityForResult(intent, PICK_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    /**
     * 打开评价对话框
     */
    private void openInvestigateDialog() {

        //判断是否是机器人
        if (isRobot) {
            openRobotInvestigateDialog();
        } else {//人工
            if (IMChatManager.getInstance().getInvestigate().size() > 0) {
                if (dialog != null && dialog.getDialog() != null && dialog.getDialog().isShowing()) {
                    return;
                }
                dialog = new InvestigateDialog(new InvestigateDialog.SubmitPingjiaListener() {
                    @Override
                    public void OnSubmitSuccess() {
                        //评价成功需要标记成已评价，按钮隐藏
                        isInvestigate = false;//已经评价过了
                        setChatMoreList();
                    }

                    @Override
                    public void OnSubmitFailed() {
                        isInvestigate = true;
                        //评价失败不用管
                    }
                });
                dialog.show(getFragmentManager(), "InvestigateDialog");
            } else {
//                ToastUtils.showShort(R.string.nothing_evaluate);

            }
        }

    }

    // 打开小莫或者xbot机器人评价
    private void openRobotInvestigateDialog() {
        final String[] items = new String[]{"已解决", "未解决", "取消"};

        android.support.v7.app.AlertDialog builder = new android.support.v7.app.AlertDialog.Builder(ChatActivity.this)
                .setTitle("评价机器人服务")

                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 2) {
                            return;
                        } else {
                            String solve = which == 0 ? "true" : "false";
                            GlobalSet globalSet = GlobalSetDao.getInstance().getGlobalSet();
                            if ("xbot".equals(globalSet.robotType)) {//xobot机器人评价接口
                                HttpManager.getRobotCsrInfo(solve, new HttpResponseListener() {
                                    @Override
                                    public void onSuccess(String responseStr) {
                                        robotEvaluationFinish = true;
                                        ToastUtils.showLong("机器人评价成功");
                                        setChatMoreList();

                                    }

                                    @Override
                                    public void onFailed() {
                                        robotEvaluationFinish = false;
                                        ToastUtils.showLong("机器人评价失败");
                                    }
                                });
                            } else {
                                HttpManager.getRobotCsrInfo(IMChat.getInstance().getBotId(), solve, new HttpResponseListener() {
                                    @Override
                                    public void onSuccess(String responseStr) {
                                        robotEvaluationFinish = true;
                                        ToastUtils.showLong("机器人评价成功");
                                        //机器人不执行是否评价过接口
                                        setChatMoreList();

                                    }

                                    @Override
                                    public void onFailed() {
                                        robotEvaluationFinish = false;
                                        ToastUtils.showLong("机器人评价失败");
                                    }
                                });
                            }
                        }
                    }
                }).create();
        builder.show();
    }

    // 拍照回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                if (uri != null) {
                    String realPath = PickUtils.getPath(ChatActivity.this, uri);
                    picFileFullName = realPath;
                    Log.d("发送图片消息了", "图片的本地路径是：" + picFileFullName);
                    //准备发送图片消息
                    FromToMessage fromToMessage = IMMessage.createImageMessage(picFileFullName);
                    ArrayList fromTomsgs = new ArrayList<FromToMessage>();
                    fromTomsgs.add(fromToMessage);
                    descFromToMessage.addAll(fromTomsgs);
                    chatAdapter.notifyDataSetChanged();
                    mChatList.setSelection(descFromToMessage.size());
                    resetBreakTimer();
                    IMChat.getInstance().sendMessage(fromToMessage, new ChatListener() {
                        @Override
                        public void onSuccess() {
                            updateMessage();
                        }

                        @Override
                        public void onFailed() {
                            updateMessage();
                        }

                        @Override
                        public void onProgress(int progress) {

                        }
                    });
                } else {
                    Log.e(tag, "从相册获取图片失败");
                }
            }
        } else if (requestCode == PICK_FILE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();//得到uri，后面就是将uri转化成file的过程。
            String path = PickUtils.getPath(ChatActivity.this, uri);
            if (!NullUtil.checkNULL(path)) {
                Toast.makeText(ChatActivity.this, "暂不支持此类文件", Toast.LENGTH_SHORT).show();
                return;
            }
            File file = new File(path);
            String fileSizeStr = "";
            if (file.exists()) {
                long fileSize = file.length();
                if ((fileSize / 1024 / 1024) > 20.0) {
                    //大于20M不能上传
                    Toast.makeText(ChatActivity.this, R.string.sendfiletoobig, Toast.LENGTH_SHORT).show();
                } else {
                    fileSizeStr = FileUtils.formatFileLength(fileSize);
                    String fileName = path.substring(path.lastIndexOf("/") + 1);
                    //发送文件
                    FromToMessage fromToMessage = IMMessage.createFileMessage(path, fileName, fileSizeStr);
                    ArrayList fromTomsgs = new ArrayList<FromToMessage>();
                    fromTomsgs.add(fromToMessage);
                    descFromToMessage.addAll(fromTomsgs);
                    chatAdapter.notifyDataSetChanged();
                    mChatList.setSelection(descFromToMessage.size());
                    resetBreakTimer();
                    IMChat.getInstance().sendMessage(fromToMessage, new ChatListener() {
                        @Override
                        public void onSuccess() {
                            updateMessage();
                        }

                        @Override
                        public void onFailed() {
                            updateMessage();
                        }

                        @Override
                        public void onProgress(int progress) {
                            updateMessage();
                        }
                    });
                }
            }
        }

    }

    // 获取字符
    public String getRealPathFromURI(Uri contentUri) {
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            // Do not call Cursor.close() on a cursor obtained using this
            // method,
            // because the activity will do that for you at the appropriate time
            Cursor cursor = this.managedQuery(contentUri, proj, null, null,
                    null);
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            return contentUri.getPath();
        }
    }

    // 覆盖手机返回键
    @Override
    public void onBackPressed() {
        if (mMore.getVisibility() == View.VISIBLE) {
            mMore.setVisibility(View.GONE);
            mChatEmojiNormal.setVisibility(View.VISIBLE);
            mChatEmojiChecked.setVisibility(View.INVISIBLE);
        } else {
            IMChatManager.getInstance().quitSDk();
            finish();
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(msgReceiver);
        unregisterReceiver(keFuStatusReceiver);
        //解绑服务
//        if (isServiceConnected) {
//            unbindService(conn);
//            isServiceConnected = false;
//        }
        mRecorderButton.cancelListener();
        if (break_timer != null) {
            break_timer.cancel();
            break_timer = null;
        }
        if (break_tip_timer != null) {
            break_tip_timer.cancel();
            break_tip_timer = null;
        }
        if (breakTimerTask != null) {
            breakTimerTask.cancel();
        }
        if (breakTipTimerTask != null) {
            breakTipTimerTask.cancel();
        }
        //删除卡片信息
        MessageDao.getInstance().delecteCardMsgs();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        chatAdapter.onPause();
    }

    @Override
    public void toRefresh() {
        if (JZflag) {
            JZflag = false;
            new Thread() {
                public void run() {
                    try {
                        sleep(300);
                        handler.sendEmptyMessage(HANDLER_MSG_MORE);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

                ;
            }.start();
        }
    }

    /**
     * 新消息接收器,用来通知界面进行更新
     */
    class MsgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            handler.sendEmptyMessage(HANDLER_MSG);
        }
    }

    /**
     * 客服状态接收器
     */
    class KeFuStatusReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (IMChatManager.ROBOT_ACTION.equals(action)) {
                //当前是机器人
                handler.sendEmptyMessage(HANDLER_ROBOT);
            } else if (IMChatManager.ONLINE_ACTION.equals(action)) {
                //当前是客服在线
                handler.sendEmptyMessage(HANDLER_ONLINE);
            } else if (IMChatManager.OFFLINE_ACTION.equals(action)) {
                //当前是客服离线
                handler.sendEmptyMessage(HANDLER_OFFNLINE);
            } else if (IMChatManager.INVESTIGATE_ACTION.equals(action)) {
                //客服发起了评价
                handler.sendEmptyMessage(HANDLER_INVESTIGATE);
            } else if (IMChatManager.QUEUENUM_ACTION.equals(action)) {
                //技能组排队数
                if (intent.getStringExtra(IMChatManager.QUEUENUM_ACTION) != null) {
                    String queueNum = intent.getStringExtra(IMChatManager.QUEUENUM_ACTION);
                    Message queueMsg = Message.obtain();
                    queueMsg.what = HANDLER_QUEUENUM;
                    queueMsg.obj = queueNum;
                    handler.sendMessage(queueMsg);
                }
            } else if (IMChatManager.CLIAM_ACTION.equals(action)) {
                //客服领取了会话或者转人工
                handler.sendEmptyMessage(HANDLER_CLIAM);
            } else if (IMChatManager.LEAVEMSG_ACTION.equals(action)) {
                //schedule 跳留言
                schedule_id = intent.getStringExtra(IMChatManager.CONSTANT_ID);
                schedule_topeer = intent.getStringExtra(IMChatManager.CONSTANT_TOPEER);
                handler.sendEmptyMessage(HANDLER_LEAVEMSG);
            } else if (IMChatManager.FINISH_ACTION.equals(action)) {
                //客服关闭了会话
                handler.sendEmptyMessage(HANDLER_FINISH);
            } else if (IMChatManager.USERINFO_ACTION.equals(action)) {
                //客服信息
                String type = intent.getStringExtra(IMChatManager.CONSTANT_TYPE);
                String exten = intent.getStringExtra(IMChatManager.CONSTANT_EXTEN);
                String userName = intent.getStringExtra(IMChatManager.CONSTANT_USERNAME);
                String userIcon = intent.getStringExtra(IMChatManager.CONSTANT_USERICON);

                // 转人工
                if ("claim".equals(type)) {
                    mOtherName.setText(NullUtil.checkNull(userName) + getString(R.string.seiveceforyou));
                    titleName = NullUtil.checkNull(userName) + getString(R.string.seiveceforyou);
                }
                // 坐席领取了会话
                if ("activeClaim".equals(type)) {
                    mOtherName.setText(NullUtil.checkNull(userName) + getString(R.string.seiveceforyou));
                    titleName = NullUtil.checkNull(userName) + getString(R.string.seiveceforyou);
                }
                if ("redirect".equals(type)) {
                    mOtherName.setText(NullUtil.checkNull(userName) + getString(R.string.seiveceforyou));
                    titleName = NullUtil.checkNull(userName) + getString(R.string.seiveceforyou);
                }
                if ("robot".equals(type)) {
                    mOtherName.setText(NullUtil.checkNull(userName) + getString(R.string.seiveceforyou));
                    titleName = NullUtil.checkNull(userName) + getString(R.string.seiveceforyou);
                }
            } else if (IMChatManager.VIPASSIGNFAIL_ACTION.equals(action)) {
                //专属座席不在线
                handler.sendEmptyMessage(HANDLER_VIPASSIGNFAIL);
            } else if (IMChatManager.CANCEL_ROBOT_ACCESS_ACTION.equals(action)) {
                //人工干预
                Toast.makeText(ChatActivity.this, R.string.receivepeopleaction, Toast.LENGTH_SHORT).show();
            } else if (IMChatManager.WITHDRAW_ACTION.equals(action)) {
                //消息撤回
                String id = intent.getStringExtra(IMChatManager.WITHDEAW_ID);
                MessageDao.getInstance().updateMsgWithDrawStatus(id);
                handler.sendEmptyMessage(HANDLER_MSG);
            } else if (IMChatManager.WRITING_ACTION.equals(action)) {
                //对方正在输入
                handler.sendEmptyMessage(HANDLER_WRITING);
                handler.sendEmptyMessageDelayed(HANDLER_NO_WRITING, 5000);
            } else if (IMChatManager.ROBOT_SWITCH_ACTION.equals(action)) {
                //robotSwitch
                String status = intent.getStringExtra(IMChatManager.CONSTANT_ROBOT_SWITCH);
                String sessionId = intent.getStringExtra(IMChatManager.CONSTANT_SESSIONID);
            } else if (IMChatManager.TCP_ACTION.equals(action)) {
                //tcp状态
                String tcpstatus = intent.getStringExtra(IMChatManager.TCPSTATUS);
//            } else if (IMChatManager.UNASSIGN_ACTION.equals(action)){
//                //开启 访客说话后再接入会话
//                chat_tv_convert.setVisibility(View.GONE);
            } else if (IMChatManager.ZXMSG_ACTION.equals(action)) { //坐席发了消息
                //如果没有回复过，执行接口校验是否回复过
                if (!isZXResply) {
                    OnlycheckConverstaion();//这个时候校验是否回复过，刷新UI
                }
            }
        }
    }


    /**
     * 人工客服设置更多功能专用
     */
    private void setChatMoreList() {
        moreList.clear();
        moreList.add(chatMore2);
        moreList.add(chatMore3);
        // TODO: 2019/5/22 访客发过消息存在sp是否更安全，因为用户退出再重新进入会话的时候，可能之前已经发送过了。
        //人工，开启了评价,坐席发过消息、会话存在，访客发过消息，没有评价过,配置了满意度列表
        if (!isRobot && IMChatManager.getInstance().isInvestigateOn() && convesationIsLive && isZXResply && isInvestigate && hasSet) {
            //展示评价按钮
            if (!moreList.contains(chatMore4)) {
                moreList.add(chatMore4);
            }
        }
        //判断是小莫还是xbot
        GlobalSet globalSet = GlobalSetDao.getInstance().getGlobalSet();
        // 如果是机器人，并且还未评价过,并且已经发送过消息
        if (isRobot && !robotEvaluationFinish && hasSendRobotMsg) {
            if ("xbot".equals(globalSet.robotType)) {
                if (isRobot && !robotEvaluationFinish && hasSendRobotMsg && IMChat.getInstance().getBotsatisfaOn()) {
                    if (!moreList.contains(chatMore4)) {
                        moreList.add(chatMore4);
                    }
                }
            } else {
                // 并且小陌机器人开启了评价
                if (IMChat.getInstance().getBotsatisfaOn()) {
                    if (!moreList.contains(chatMore4)) {
                        moreList.add(chatMore4);
                    }
                }
            }
        }

        //如果在排队
        if (isQueue) {
            if (moreList.contains(chatMore4)) {
                moreList.remove(chatMore4);
            }
        }
        moreLists.clear();
        int pageCount = (int) Math.ceil(moreList.size() / 8 + 0.1);
        for (int i = 0; i < pageCount; i++) {
            moreLists.add(getData(i));
        }
        initMoreViewPager();
        initMorePoint();
        initMoreData();
    }

    /**
     * 只校验会话是否有效（收到坐席发消息推送后调用，刷新评论按钮UI）
     */
    private void OnlycheckConverstaion() {
        //人工会话 校验会话是否有效
        HttpManager.getChatSession(new HttpResponseListener() {

            @Override
            public void onSuccess(String responseStr) {
                LogUtils.eTag("huihua", responseStr);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(responseStr);
                    JSONObject data = jsonObject.getJSONObject("data");
                    if (data != null) {//会话存在
                        convesationIsLive = true;
                        id = data.getString("_id");//会话id
                        if (data.has("replyMsgCount")) {
                            if (data.getInt("replyMsgCount") > 0) {
                                //代表坐席回复过
                                isZXResply = true;
                            } else {
                                isZXResply = false;
                            }
                        } else {
                            isZXResply = false;
                        }

                    } else {
                        //会话不存在
                        convesationIsLive = false;
                    }
                    setChatMoreList();//接口都走完了初始化UI
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setChatMoreList();
            }

            @Override
            public void onFailed() {
                //判断是否存在会话要是返回失败了，先按照存在该会话,并且可以评论处理。 当我们评论的时候，后台会提示一个评论过了的提示语
//                convesationIsLive = true;
//                isInvestigate = false;
//                setChatMoreList();
            }
        });
    }


    /**
     * 判断是否是有效会话，并且是否评价过
     *
     * @param
     */
    private void checkConverstaion() {
        //人工会话 校验会话是否有效
        HttpManager.getChatSession(new HttpResponseListener() {

            @Override
            public void onSuccess(String responseStr) {
                LogUtils.eTag("huihua", responseStr);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(responseStr);
                    JSONObject data = jsonObject.getJSONObject("data");
                    if (data != null) {//会话存在
                        convesationIsLive = true;
                        id = data.getString("_id");//会话id
                        if (data.has("replyMsgCount")) {
                            if (data.getInt("replyMsgCount") > 0) {
                                //代表坐席回复过
                                isZXResply = true;
                            } else {
                                isZXResply = false;
                            }
                        } else {
                            isZXResply = false;
                        }
                        //校验是否已经评价过
                        HttpManager.checkIsAppraised(id, new HttpResponseListener() {
                            @Override
                            public void onSuccess(String responseStr) {
                                try {
                                    JSONObject jsonObject1 = new JSONObject(responseStr);
                                    isInvestigate = jsonObject1.getBoolean("isInvestigate");
                                    setChatMoreList();//接口都走完了初始化UI
                                } catch (JSONException e) {

                                }
                                LogUtils.eTag("piangjia", responseStr);
                            }

                            @Override
                            public void onFailed() {
                                //请求失败的话默认 先按没有评价过处理
//                                isInvestigate = false;
                            }
                        });
                    } else {
                        //会话不存在
                        convesationIsLive = false;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailed() {
                //判断是否存在会话要是返回失败了，先按照存在该会话,并且可以评论处理。 当我们评论的时候，后台会提示一个评论过了的提示语
//                convesationIsLive = true;
//                isInvestigate = false;
            }
        });
    }

    /**
     * 首次进入开启会话
     *
     * @param peerId
     */
    private void beginSession(String peerId) {
        IMChatManager.getInstance().beginSession(peerId, new OnSessionBeginListener() {
            @Override
            public void onSuccess() {
                if (loadingDialog != null) {
                    loadingDialog.dismiss();
                }
                //判断会话是否是存在或者有效，然后校验是否评价过了
//                if (!isRobot)
//                checkConverstaion();
            }

            @Override
            public void onFailed() {
                if (loadingDialog != null) {
                    loadingDialog.dismiss();
                }
                chat_tv_convert.setVisibility(View.GONE);
                showOffLineDialog();
            }
        });
    }

    private void beginScheduleSession(String scheduleId, String processId, String currentNodeId, String entranceId) {
        IMChatManager.getInstance().beginScheduleSession(scheduleId, processId, currentNodeId, entranceId, new OnSessionBeginListener() {

            @Override
            public void onSuccess() {
                if (loadingDialog != null) {
                    loadingDialog.dismiss();
                }
                //非机器人，判断会话是否是存在或者有效，然后校验是否评价过了
//                if (!isRobot)
//                checkConverstaion();

            }

            @Override
            public void onFailed() {
                if (loadingDialog != null) {
                    loadingDialog.dismiss();
                }
                chat_tv_convert.setVisibility(View.GONE);
                showOffLineDialog();
            }
        });
    }

    private void showOffLineDialog() {

        if (type.equals("schedule")) {
            return;
        }
        GlobalSet globalSet = GlobalSetDao.getInstance().getGlobalSet();

        if (null != globalSet) {
            if (isRobot) {
                bar_bottom.setVisibility(View.VISIBLE);
            } else {
                bar_bottom.setVisibility(View.GONE);
            }

            if ("1".equals(NullUtil.checkNull(globalSet.isLeaveMsg))) {
                Intent intent = new Intent(ChatActivity.this, OfflineMessageActicity.class);
                intent.putExtra("PeerId", peerId);
                intent.putExtra("leavemsgTip", NullUtil.checkNull(globalSet.leavemsgTip));
                intent.putExtra("inviteLeavemsgTip", NullUtil.checkNull(globalSet.inviteLeavemsgTip));
                startActivity(intent);
//                finish();
            } else {
                new AlertDialog.Builder(this).setTitle(R.string.warm_prompt)
                        .setMessage(NullUtil.checkNull(globalSet.msg))
                        .setPositiveButton(R.string.iknow, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            }
        }
    }

    public ChatAdapter getChatAdapter() {
        return chatAdapter;
    }

    public void resendMsg(FromToMessage msg, int position) {
        if (IMChatManager.getInstance().isFinishWhenReConnect) {
            startReStartDialog();
        } else {
            resetBreakTimer();
            IMChat.getInstance().reSendMessage(msg, new ChatListener() {
                @Override
                public void onSuccess() {
                    updateMessage();
                }

                @Override
                public void onFailed() {
                    updateMessage();
                }

                @Override
                public void onProgress(int progress) {
                    updateMessage();
                }
            });
        }

    }

    public void sendMsg(FromToMessage msg) {
        FromToMessage fromToMessage = new FromToMessage();
        fromToMessage.userType = "0";
        fromToMessage.message = "";
        fromToMessage.msgType = FromToMessage.MSG_TYPE_CARDINFO;
        fromToMessage.when = System.currentTimeMillis();
        fromToMessage.sessionId = IMChat.getInstance().getSessionId();
        fromToMessage.tonotify = IMChat.getInstance().get_id();
        fromToMessage.type = "User";
        fromToMessage.from = IMChat.getInstance().get_id();
        fromToMessage.cardInfo = msg.cardInfo;

        //界面显示
        descFromToMessage.add(fromToMessage);
        chatAdapter.notifyDataSetChanged();
        mChatList.setSelection(descFromToMessage.size());
        mChatInput.setText("");

        resetBreakTimer();

        //发送消息
        IMChat.getInstance().sendMessage(fromToMessage, new ChatListener() {
            @Override
            public void onSuccess() {
                //消息发送成功
                updateMessage();
            }

            @Override
            public void onFailed() {
                //消息发送失败
                updateMessage();
            }

            @Override
            public void onProgress(int progress) {

            }
        });


    }


    private void getIsGoSchedule() {
        IMChatManager.getInstance().getWebchatScheduleConfig(new GetGlobleConfigListen() {
            @Override
            public void getSchedule(ScheduleConfig sc) {
                // loadingDialog.dismiss();
                LogUtils.aTag("MainActivity", "日程");
                if (!sc.getScheduleId().equals("") && !sc.getProcessId().equals("") && sc.getEntranceNode() != null && sc.getLeavemsgNodes() != null) {
                    if (sc.getEntranceNode().getEntrances().size() > 0) {
                        if (sc.getEntranceNode().getEntrances().size() == 1) {
                            ScheduleConfig.EntranceNodeBean.EntrancesBean bean = sc.getEntranceNode().getEntrances().get(0);
                            ChatActivity.startActivity(ChatActivity.this, Constants.TYPE_SCHEDULE, sc.getScheduleId(), sc.getProcessId(), bean.getProcessTo(), bean.getProcessType(), bean.get_id());
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

        android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(ChatActivity.this)
                .setTitle("选择日程")
                // 设置列表显示，注意设置了列表显示就不要设置builder.setMessage()了，否则列表不起作用。
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ScheduleConfig.EntranceNodeBean.EntrancesBean bean = entrances.get(which);
                        LogUtils.aTag("选择日程：", bean.getName());
                        ChatActivity.startActivity(ChatActivity.this, Constants.TYPE_SCHEDULE, scheduleId, processId, bean.getProcessTo(), bean.getProcessType(), bean.get_id());

                    }
                }).create();
        dialog.show();
    }

    private void startChatActivityForPeer() {
        IMChatManager.getInstance().getPeers(new GetPeersListener() {
            @Override
            public void onSuccess(List<Peer> peers) {

                if (peers.size() > 1) {
                    startPeersDialog(peers, IMChatManager.getInstance().cardInfo);
                } else if (peers.size() == 1) {
                    ChatActivity.startActivity(ChatActivity.this, Constants.TYPE_PEER, peers.get(0).getId(), IMChatManager.getInstance().cardInfo);
                } else {
                    ToastUtils.showShort(R.string.peer_no_number);
                }
                // loadingDialog.dismiss();
            }

            @Override
            public void onFailed() {
                // loadingDialog.dismiss();
            }
        });
    }

    public void startPeersDialog(final List<Peer> peers, final CardInfo mCardInfo) {
        final String[] items = new String[peers.size()];
        for (int i = 0; i < peers.size(); i++) {
            items[i] = peers.get(i).getName();
        }
        android.support.v7.app.AlertDialog builder = new android.support.v7.app.AlertDialog.Builder(ChatActivity.this)
                .setTitle("选择技能组")
                // 设置列表显示，注意设置了列表显示就不要设置builder.setMessage()了，否则列表不起作用。
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Peer peer = peers.get(which);
                        LogUtils.aTag("选择技能组：", peer.getName());
                        ChatActivity.startActivity(ChatActivity.this, Constants.TYPE_PEER, peer.getId(), mCardInfo);
                    }
                }).create();
        builder.show();
    }

    public void startReStartDialog() {
        //设置聊天背景
        new ActionSheetDialog(this)
                .builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .setTitle("当前会话已经关闭,是否重新开启会话")
                .addSheetItem(
//                        this.getResources().getString(R.string.restartchat),
                        "开始会话",
                        ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                getIsGoSchedule();
                            }
                        })
                .addSheetItem(
//                        this.getResources().getString(R.string.exitchat),
                        "退出",
                        ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                IMChatManager.getInstance().quitSDk();
                                finish();
                            }
                        }

                ).show();
    }

    public void startReStartDialog2() {
        singleButtonDialog = builder.setMessage("")
                .setMessageDetail(codeDescription)
                .setSingleButton("重新接入", 0xFF459ae9, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        singleButtonDialog.dismiss();
                        IMChatManager.getInstance().quitSDk();
                        finish();
                    }
                }).createSingleButtonDialog();
        if (!isFinishing())
            singleButtonDialog.show();
    }

    public void startReStartDialog3() {
        singleButtonDialog = builder.setMessage("")
                .setMessageDetail(codeDescription2)
                .setSingleButton("退出", 0xFF459ae9, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        singleButtonDialog.dismiss();
                        IMChatManager.getInstance().quitSDk();
                        finish();
                    }
                }).createSingleButtonDialog();
        singleButtonDialog.show();
    }


    /**
     * 坐席邀请访客评价
     */
    private void sendInvestigate() {
//        ArrayList<Investigate> investigates = (ArrayList<Investigate>) IMChatManager.getInstance().getInvestigate();
//        IMMessage.createInvestigateMessage(investigates);
//        updateMessage();

        openInvestigateDialog();

    }

    public ChatListView getChatListView() {
        return mChatList;
    }


    @Override
    public void onRecordFinished(float mTime, String filePath, String pcmFilePath) {
        if (!FileUtils.isExists(filePath)) {
            ToastUtils.showShort("录音失败，请再次重试");
            return;
        }
        //先在界面上显示出来
        FromToMessage fromToMessage = IMMessage.createAudioMessage(mTime, filePath, "");
        descFromToMessage.add(fromToMessage);
        chatAdapter.notifyDataSetChanged();
        mChatList.setSelection(descFromToMessage.size());
        sendVoiceMsg("", fromToMessage);
    }

    /**
     * 发送录音消息
     *
     * @param voiceText
     */
    private void sendVoiceMsg(String voiceText, FromToMessage fromToMessage) {
        fromToMessage.voiceText = voiceText;
        resetBreakTimer();
        IMChat.getInstance().sendMessage(fromToMessage, new ChatListener() {
            @Override
            public void onSuccess() {
                updateMessage();
            }

            @Override
            public void onFailed() {
                updateMessage();
            }

            @Override
            public void onProgress(int progress) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.onRequestPermissionsResult(this, 0x11, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, grantResults);
    }

    public void onEventMainThread(UnAssignEvent unAssignEvent) {
        chat_tv_convert.setVisibility(View.GONE);
    }

    public void onEventMainThread(ReSendMessage reSendMessage) {
        updateMessage();
    }

    /**
     * 每次进入聊天页面重新校验tcp是否已经连接，未连接就重新连接一次
     */
    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.aTag("chatActivity", "走到OnResume了" + TcpManager.getInstance(IMChatManager.getInstance().getAppContext()).getTcpStatus());
        if (MoorUtils.isNetWorkConnected(IMChatManager.getInstance().getAppContext())) {
            LogUtils.aTag("onresume", "监测到网络ok");
            if (isServiceRunning(this, "com.moor.imkf.tcpservice.service.IMService")) {
                if (TcpManager.TcpStatus.BREAK.equals(TcpManager.getInstance(IMChatManager.getInstance().getAppContext()).getTcpStatus())) {
                    EventBus.getDefault().post(new TcpBreakEvent());//重连
                }
            } else {
                //服务挂了
                startReStartDialog2();
            }
        } else {
            startReStartDialog3();
            LogUtils.aTag("onresume", "监测到网络not ok");
        }
    }

    /**
     * 判断服务是否还活着
     */
    public static boolean isServiceRunning(Context context, String ServiceName) {
        boolean isWork = false;
        if (TextUtils.isEmpty(ServiceName)) {
            isWork = false;
            LogUtils.aTag("runService", "服务名字是空的");
        }
        ActivityManager myManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningService = myManager
                .getRunningServices(Integer.MAX_VALUE);
        if (runningService.size() <= 0) {
//            isWork = false;
            LogUtils.aTag("runService", "服务数是0");
        }
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().equals(ServiceName)) {
                isWork = true;
                LogUtils.aTag("runService", "服务还活着" + isWork);
                break;
            }
        }
        return isWork;
    }


    /**
     * 联想输入
     */
    private class GetLianXiangDataResponeHandler implements HttpResponseListener {

        @Override
        public void onFailed() {
            ll_hintView.setVisibility(View.GONE);
        }

        @Override
        public void onSuccess(String responseString) {
            System.out.println(responseString);
            String succeed = HttpParser.getSucceed(responseString);
            if ("true".equals(succeed)) {
                ll_hintView.removeAllViews();
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    final JSONArray questions = jsonObject.getJSONArray("questions");
                    if (questions.length() > 0) {
                        //如果含有提示文字
                        ll_hintView.setVisibility(View.VISIBLE);
                        for (int j = 0; j < questions.length(); j++) {
                            //首先引入要添加的View
                            View view = View.inflate(ChatActivity.this, R.layout.item_hint_view, null);
                            TextView textView = (TextView) view.findViewById(R.id.tv_hintView);
                            textView.setText(questions.getString(j));
                            final int position = j;
                            textView.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    try {
                                        // TODO: 2019/6/4 直接发出去，然后清空所有内容。并且隐藏
                                        if (!MoorUtils.isNetWorkConnected(IMChatManager.getInstance().getAppContext()) &&
                                                TcpManager.TcpStatus.BREAK.equals(TcpManager.getInstance(IMChatManager.getInstance().getAppContext()).getTcpStatus())) {
                                            Toast.makeText(getApplicationContext(), "检测到您网络异常啦~", Toast.LENGTH_SHORT).show();
                                            LogUtils.aTag("第四个地方break");
                                            TcpManager.getInstance(IMChatManager.getInstance().getAppContext()).setTcpStatus(TcpManager.TcpStatus.NONET);
                                            startReStartDialog3();
                                            return;
                                        }
                                        if (IMChatManager.getInstance().isFinishWhenReConnect) {
                                            startReStartDialog();//并且弹框提示开始新会话
//            }
                                        } else {
                                            sendTextMsg(questions.getString(position));
                                            mChatInput.setText("");
                                            ll_hintView.setVisibility(View.GONE);
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            ll_hintView.addView(view);
                        }


                    } else {
                        ll_hintView.setVisibility(View.GONE);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                ToastUtils.showShort(responseString);
            } else {
                //请求数据不对，隐藏
                ll_hintView.setVisibility(View.GONE);
            }
        }
    }

}
