package com.m7.imkfsdk.recordbutton;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.m7.imkfsdk.R;

import static android.R.attr.handle;
import static android.R.attr.x;
import static android.R.attr.y;


/**
 * Created by LongWei
 */
public class AudioRecorderButton extends Button implements AudioManager.AudioStateListener {

    private static final int DISTANCE_Y_CANCEL = 50;

    private static final int STATE_NORMAL = 1;
    private static final int STATE_RECORDING = 2;
    private static final int STATE_WANT_TO_CANCEL = 3;

    private int mCurrentState = STATE_NORMAL;
    private boolean isRecording = false;

    private DialogManager mDialogManager;

    private AudioManager mAudioManager;

    protected float mTime;

    protected int leftTime;
    private boolean is_send = true;

    private boolean mReady;

    private static final int MSG_RECORDER_PREPARE = 0x11;
    private static final int MSG_VOICE_CHANGE = 0x12;
    private static final int MSG_DIALOG_DISMISS = 0x13;
    private static final int MSG_TIME_LEFT_TEN = 0x14;
    private static final int MSG_TIME_LEFT_EXCEED_ALARM = 0x15;

    private RecorderFinishListener listener;

    private Runnable mGetVoiceLevelRunnable = new Runnable() {

        @Override
        public void run() {
            is_send=true;
            while (isRecording) {
                try {
                    Thread.sleep(100);
                    mTime += 0.1f;
                    if (is_send) {
                        if ((60 - Math.round(mTime + 0.5f)) == 10) {
                            leftTime = 10;
                            handler.sendEmptyMessage(MSG_TIME_LEFT_TEN);
                            is_send = false;
                        }
                    }
                    handler.sendEmptyMessage(MSG_VOICE_CHANGE);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    };

    public interface RecorderFinishListener {
        void onRecordFinished(float mTime, String filePath, String pcmFilePath);
    }

    public AudioRecorderButton(Context context) {
        this(context, null);
    }

    public AudioRecorderButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDialogManager = new DialogManager(context);

        String dir = Environment.getExternalStorageDirectory() + "/m7_chat_recorder";

        mAudioManager = AudioManager.getInstance(dir);
        mAudioManager.setAudioStateListener(this);

        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mReady = true;
                mAudioManager.prepareAudio();
                return false;
            }
        });
    }

    public void setRecordFinishListener(RecorderFinishListener listener) {
        this.listener = listener;
    }

    public void cancelListener() {
        mAudioManager.setAudioStateListener(null);
    }

    private Handler handler = new Handler() {


        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_RECORDER_PREPARE:
                    mDialogManager.showDialog();
                    isRecording = true;
                    new Thread(mGetVoiceLevelRunnable).start();
                    break;
                case MSG_VOICE_CHANGE:
                    mDialogManager.updateVoiceLevel(mAudioManager.getVoiceLevel(7));
                    break;
                case MSG_DIALOG_DISMISS:
                    mDialogManager.dismissDialog();
                    break;
                case MSG_TIME_LEFT_TEN:

                    if (leftTime >= 0) {
                        mDialogManager.secondLeft(leftTime);
                        Message message = handler.obtainMessage();
                        message.what = MSG_TIME_LEFT_TEN;
                        handler.sendMessageDelayed(message, 1000);
                        leftTime--;
                    } else {
                        mDialogManager.exceedTime();
                        handler.sendMessageDelayed(handler.obtainMessage(MSG_TIME_LEFT_EXCEED_ALARM), 1000);
                        if (listener != null) {
                            listener.onRecordFinished(mTime, mAudioManager.getCurrentFilePath(), mAudioManager.getPCMFilePath());
                        }
                        mAudioManager.release();
                        reset();
                    }
                    break;
                case MSG_TIME_LEFT_EXCEED_ALARM:
                    mDialogManager.dismissDialog();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                changeState(STATE_RECORDING);

                break;
            case MotionEvent.ACTION_MOVE:

                if (isRecording) {
                    if (wanttocancel(x, y)) {
                        changeState(STATE_WANT_TO_CANCEL);
                    } else {
                        changeState(STATE_RECORDING);
                    }

                }
                break;

            case MotionEvent.ACTION_UP:

                if (!mReady) {
                    reset();
                    return super.onTouchEvent(event);
                }
                if (!isRecording || mTime < 0.9) {
                    mDialogManager.tooShort();
                    mAudioManager.cancel();
                    handler.sendEmptyMessageDelayed(MSG_DIALOG_DISMISS, 1000);
                } else if (mCurrentState == STATE_RECORDING) {
                    mDialogManager.dismissDialog();

                    if (listener != null) {
                        listener.onRecordFinished(mTime, mAudioManager.getCurrentFilePath(), mAudioManager.getPCMFilePath());
                    }
                    mAudioManager.release();
                } else if (mCurrentState == STATE_WANT_TO_CANCEL) {
                    mDialogManager.dismissDialog();
                    mAudioManager.cancel();
                    handler.removeMessages(MSG_TIME_LEFT_TEN);
                    handler.removeMessages(MSG_TIME_LEFT_EXCEED_ALARM);
                }

                reset();
                break;
            case MotionEvent.ACTION_CANCEL:

                if (!mReady) {
                    reset();
                    return super.onTouchEvent(event);
                }
                if (!isRecording || mTime < 0.9) {
                    mDialogManager.tooShort();
                    mAudioManager.cancel();
                    handler.sendEmptyMessageDelayed(MSG_DIALOG_DISMISS, 1000);
                } else if (mCurrentState == STATE_RECORDING) {
                    mDialogManager.dismissDialog();
                    if (listener != null) {
                        listener.onRecordFinished(mTime, mAudioManager.getCurrentFilePath(),mAudioManager.getPCMFilePath());
                    }
                    mAudioManager.release();
                } else if (mCurrentState == STATE_WANT_TO_CANCEL) {
                    mDialogManager.dismissDialog();
                    mAudioManager.cancel();
                    handler.removeMessages(MSG_TIME_LEFT_TEN);
                    handler.removeMessages(MSG_TIME_LEFT_EXCEED_ALARM);
                }
                reset();
                break;
        }

        return super.onTouchEvent(event);
    }

    /**
     * 恢复标志位
     */
    private void reset() {
        isRecording = false;
        mReady = false;
        mTime = 0;
        changeState(STATE_NORMAL);
    }

    private boolean wanttocancel(int x, int y) {
        if (x < 0 || x > getWidth()) {
            return true;
        }
        if (y < -DISTANCE_Y_CANCEL || y > getHeight() + DISTANCE_Y_CANCEL) {
            return true;
        }
        return false;
    }

    private void changeState(int state) {
        if (mCurrentState != state) {
            mCurrentState = state;
            switch (state) {
                case STATE_NORMAL:
                    setBackgroundResource(R.drawable.kf_btn_recorder_normal);
                    setText(R.string.recorder_normal);
                    break;
                case STATE_RECORDING:
                    setBackgroundResource(R.drawable.kf_btn_recorder_press);
                    setText(R.string.recorder_recording);
                    if (isRecording) {
                        mDialogManager.recording();
                    }
                    break;
                case STATE_WANT_TO_CANCEL:
                    setBackgroundResource(R.drawable.kf_btn_recorder_press);
                    setText(R.string.recorder_want_cancel);
                    mDialogManager.wantToCancel();

                    break;
            }
        }
    }

    @Override
    public void wellPrepared() {
        handler.sendEmptyMessage(MSG_RECORDER_PREPARE);
    }
}
