package com.m7.imkfsdk.recordbutton;

import com.moor.imkf.mp3recorder.MP3Recorder;

import java.io.File;
import java.util.UUID;

/**
 * 录音工具类
 */
public class AudioManager {

	private String mDir;
	private String mCurrentFilePath;
	private String mPCMFilePath;

	private boolean isPrepared;
	
	private static AudioManager instance;
	
	private AudioStateListener listener;

	MP3Recorder mp3Recorder;
	
	public interface AudioStateListener{
		void wellPrepared();
	}
	
	public void setAudioStateListener(AudioStateListener listener) {
		this.listener = listener;
	}
	
	private AudioManager(){}
	
	private AudioManager(String dir){
		this.mDir = dir;
	}
	
	public static AudioManager getInstance(String dir) {
		if(instance == null) {
			synchronized (AudioManager.class) {
				instance = new AudioManager(dir);
			}
		}
		return instance;
	}
	
	public void prepareAudio() {
		try {
			isPrepared = false;
			File dir = new File(mDir);
			if(!dir.exists()) {
				dir.mkdirs();
				
			}

			String fileName = generateFileName();
			File file = new File(dir, fileName);
			mCurrentFilePath = file.getAbsolutePath();

			File pcmFile = new File(dir, generatePCMFileName());
			mPCMFilePath = pcmFile.getAbsolutePath();

			mp3Recorder = new MP3Recorder(file, pcmFile);
			mp3Recorder.start();

			if(listener != null) {
				listener.wellPrepared();
			}
			isPrepared = true;
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String generateFileName() {
		
		return UUID.randomUUID().toString() + ".mp3";
	}

	private String generatePCMFileName() {

		return UUID.randomUUID().toString() + ".pcm";
	}

	public int getVoiceLevel(int maxLevel) {
		if (isPrepared && mp3Recorder != null) {
			int volume;
			volume = maxLevel * mp3Recorder.getVolume() * mp3Recorder.getVolume() / 5000 + 1;
			if (volume > 7) {
				volume = 7;
			}
			return volume;
		}
		return 1;
	}

	public void release() {
		if(mp3Recorder != null) {
			mp3Recorder.stop();
			mp3Recorder = null;
		}
	}

	public void cancel() {
		release();
		if(mCurrentFilePath != null) {
			File file = new File(mCurrentFilePath);
			file.delete();
			mCurrentFilePath = null;
		}
		if(mPCMFilePath != null) {
			File file = new File(mPCMFilePath);
			file.delete();
			mPCMFilePath = null;
		}
	}

	public String getCurrentFilePath() {
		return mCurrentFilePath;
	}
	public String getPCMFilePath() {
		return mPCMFilePath;
	}
}
