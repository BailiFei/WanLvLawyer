/*
 *  Copyright (c) 2013 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.yuntongxun.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.m7.imkfsdk.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import com.m7.imkfsdk.R;


/**
 * 语音播放动画
 */
public class VoiceAnimImageView extends TextView {
	
	/**
	 * type for voice downloading.
	 */
	private static final int TYPE_VOICE_DOWNLOADING = 0;
	
	/**
	 * type for voice playing.
	 */
	private static final int TYPE_VOICE_PLAYING = 1;

	private Context mActivity;
	
	private AlphaAnimation mAlphaAnimation;
	
	/**
	 * chatting from animation
	 */
	private AnimationDrawable mChattingFromAnimationDrawable;
	
	/**
	 * chatting to animation
	 */
	private AnimationDrawable mChattingToAnimationDrawable;
	
	/**
	 * 
	 */
	private int mDuration = 300;
	
	private int mVoiceType = TYPE_VOICE_PLAYING;
	
	/**
	 * 
	 */
	private boolean isFrom = false;
	
	private boolean isRunning = false;
	
	/**
	 * @param context
	 */
	public VoiceAnimImageView(Context context) {
		super(context);
		mActivity = context;
		initAnimImageView();
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public VoiceAnimImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mActivity = context;
		initAnimImageView();
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public VoiceAnimImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mActivity = context;
		initAnimImageView();
	}
	
	
	/**
	 * 
	 */
	public void initAnimImageView() {
		
		mAlphaAnimation = new AlphaAnimation(0.1F, 0.1F);
		mAlphaAnimation.setDuration(1000L);
		mAlphaAnimation.setRepeatCount(AlphaAnimation.INFINITE);
		mAlphaAnimation.setRepeatMode(AlphaAnimation.REVERSE);
		
		// chatting from animation
		mChattingFromAnimationDrawable = new AnimationDrawable();
		Drawable chattingFDrawale1 = getResources().getDrawable(R.drawable.kf_chatfrom_voice_playing_f1);
		chattingFDrawale1.setBounds(0, 0, chattingFDrawale1.getIntrinsicWidth(), chattingFDrawale1.getIntrinsicHeight());
		mChattingFromAnimationDrawable.addFrame(chattingFDrawale1, mDuration);
		
		Drawable chattingFDrawale2 = getResources().getDrawable(R.drawable.kf_chatfrom_voice_playing_f2);
		chattingFDrawale2.setBounds(0, 0, chattingFDrawale2.getIntrinsicWidth(), chattingFDrawale2.getIntrinsicHeight());
		mChattingFromAnimationDrawable.addFrame(chattingFDrawale2, mDuration);
		
		Drawable chattingFDrawale3 = getResources().getDrawable(R.drawable.kf_chatfrom_voice_playing_f3);
		chattingFDrawale3.setBounds(0, 0, chattingFDrawale3.getIntrinsicWidth(), chattingFDrawale3.getIntrinsicHeight());
		mChattingFromAnimationDrawable.addFrame(chattingFDrawale3, mDuration);
		mChattingFromAnimationDrawable.setOneShot(false);
		mChattingFromAnimationDrawable.setVisible(true, true);
		
		
		// chatting to animation
		mChattingToAnimationDrawable = new AnimationDrawable();
		Drawable chattingTDrawable_1 = getResources().getDrawable(R.drawable.kf_chatto_voice_playing_f1);
		chattingTDrawable_1.setBounds(0, 0, chattingTDrawable_1.getIntrinsicWidth(), chattingTDrawable_1.getIntrinsicHeight());
		mChattingToAnimationDrawable.addFrame(chattingTDrawable_1, mDuration);
		
		Drawable chattingTDrawable_2 = getResources().getDrawable(R.drawable.kf_chatto_voice_playing_f2);
		chattingTDrawable_2.setBounds(0, 0, chattingTDrawable_2.getIntrinsicWidth(), chattingTDrawable_2.getIntrinsicHeight());
		mChattingToAnimationDrawable.addFrame(chattingTDrawable_2, mDuration);
		
		Drawable chattingTDrawable_3 = getResources().getDrawable(R.drawable.kf_chatto_voice_playing_f3);
		chattingTDrawable_3.setBounds(0, 0, chattingTDrawable_3.getIntrinsicWidth(), chattingTDrawable_3.getIntrinsicHeight());
		mChattingToAnimationDrawable.addFrame(chattingTDrawable_3, mDuration);
		mChattingToAnimationDrawable.setOneShot(false);
		mChattingToAnimationDrawable.setVisible(true, true);
	}

	/**
	 * from or to
	 * @param from
	 */
	public final void setVoiceFrom(boolean from) {
		isFrom = from;
	}
	
	/**
	 * the type for voice that playing or downloading.
	 * @see #TYPE_VOICE_DOWNLOADING
	 * @see #TYPE_VOICE_PLAYING
	 * @param type
	 */
	public final void setVoiceType(int type) {
		mVoiceType = type;
	}
	
	public final void startVoiceAnimation() {
		switch (mVoiceType) {
		case TYPE_VOICE_DOWNLOADING:
			
			if(isFrom) {
				setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.kf_chatfrom_bg_normal));
			} else {
				setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.kf_chatto_bg_normal));
			}
			
			setAnimation(mAlphaAnimation);
			mAlphaAnimation.startNow();
			break;
		case TYPE_VOICE_PLAYING:
			
			if(!isRunning) {
				isRunning = true;
				if(isFrom) {
					// start chatting from animation
					setCompoundDrawablesWithIntrinsicBounds(mChattingFromAnimationDrawable, null, null, null);
					mChattingFromAnimationDrawable.stop();
					mChattingFromAnimationDrawable.start();
					return;
				}
				
				// start chatting to animation
				setCompoundDrawablesWithIntrinsicBounds(null, null, mChattingToAnimationDrawable, null);
				mChattingToAnimationDrawable.stop();
				mChattingToAnimationDrawable.start();
			}
			
			break;
		default:
			break;
		}
	}
	
	/**
	 * 
	 */
	public final void restBackground() {
		if(isFrom) {
			setBackgroundDrawable(getResources().getDrawable(R.drawable.kf_chatfrom_bg_normal));
			return;
		}
		setBackgroundDrawable(getResources().getDrawable(R.drawable.kf_chatto_bg_normal));
	}
	
	/**
	 * 
	 */
	public final void stopVoiceAnimation() {
		if(mAlphaAnimation != null && mAlphaAnimation.isInitialized()) {
			setAnimation(null);
		}
		
		if(mVoiceType != 1) {
			return;
		}
		
		isRunning = false;
		setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
		setBackgroundDrawable(null);
		this.mChattingFromAnimationDrawable.stop();
		this.mChattingToAnimationDrawable.stop();
	}
	
}
