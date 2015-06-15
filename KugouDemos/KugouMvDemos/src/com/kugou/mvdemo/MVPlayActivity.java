package com.kugou.mvdemo;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class MVPlayActivity extends Activity implements OnClickListener {
	private static final String TAG = "MVPlayActivity";
	public static final String EXTRA_PATH = "extra_path";

	private static final int MSG_UPDATE_POSITION = 012;
	private SurfaceView mSurfaceView;

	private MediaPlayer mMdiaPlayer;

	private boolean isAutoStart = true;

	private boolean isPrepared = false;

	private boolean isComplection = false;
	/** 是否全屏 */
	private static boolean isFullscreen;
	private long mDuration = 1;
	/** 小屏视频布局参数 */
	private LinearLayout.LayoutParams mDefaulVideoLayoutParams;

	/** 全屏视频布局参数 */
	private LinearLayout.LayoutParams mFullScreenVideoLayoutParams;

	/** 视频播放区域 */
	private View mVideoLayout = null;
	private Button btnAllScreen;
	private Handler handler;
	// All the stuff we need for playing and showing a video
	public static SurfaceHolder mSurfaceHolder = null;
	private String mediaPath;
	private SeekBar sbPosition;

	private TextView tvPosition;
	private boolean isShow = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activty_mv_play);
		mSurfaceView = (SurfaceView) findViewById(R.id.mSurfaceView);
		mSurfaceView.getHolder().addCallback(mSHCallback);
		mSurfaceView.getHolder().setKeepScreenOn(true);
		mSurfaceView.getHolder().setType(
				SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		mSurfaceView.setVisibility(View.VISIBLE);
		mVideoLayout = findViewById(R.id.layoutVideo);
		sbPosition = (SeekBar) findViewById(R.id.sbPosition);
		mMdiaPlayer = new MediaPlayer();
		mMdiaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mediaPath = getIntent().getStringExtra(EXTRA_PATH);
		if (!TextUtils.isEmpty(mediaPath)) {
			setTitle(new File(mediaPath).getName());
		}
		btnAllScreen = (Button) findViewById(R.id.btnAllScreen);
		tvPosition = (TextView) findViewById(R.id.tvPosition);
		findViewById(R.id.btnPlay).setOnClickListener(this);
		findViewById(R.id.btnPause).setOnClickListener(this);
		findViewById(R.id.btnReplay).setOnClickListener(this);
		findViewById(R.id.btnAllScreen).setOnClickListener(this);
		isFullscreen=this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
		initScreenMode();
		mMdiaPlayer.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				isPrepared = true;
				mDuration = mp.getDuration();
				if (isAutoStart) {
					start();
				}
			}
		});

		mMdiaPlayer.setOnInfoListener(new OnInfoListener() {

			@Override
			public boolean onInfo(MediaPlayer mp, int what, int extra) {
				TBLog.d(TAG, "onInfo what=" + what + "extra=" + extra);
				return false;
			}
		});
		mMdiaPlayer.setOnErrorListener(new OnErrorListener() {

			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				TBLog.d(TAG, "onError what=" + what + "extra=" + extra);
				isPrepared = false;
				return false;
			}
		});
		mMdiaPlayer.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				isComplection = true;
			}
		});
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case MSG_UPDATE_POSITION:
					if (isPrepared && mDuration != 0&&isShow) {
						sbPosition.setProgress((int) (mMdiaPlayer
								.getCurrentPosition() * 100 / mDuration));
						tvPosition.setText(String.format("%d:%d/%d:%d",
								mMdiaPlayer.getCurrentPosition() / 1000 / 60,
								mMdiaPlayer.getCurrentPosition() / 1000 % 60,
								mDuration / 1000 / 60, mDuration / 1000 % 60));
					}
					sendEmptyMessageDelayed(MSG_UPDATE_POSITION, 1000);
					break;
				default:
					break;
				}
			}
		};
		sbPosition.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				TBLog.d(TAG, "onStopTrackingTouch:");
				if (isPrepared) {
					mMdiaPlayer.seekTo(mMdiaPlayer.getDuration()
							* seekBar.getProgress() / 100);
				}

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				TBLog.d(TAG, "onStartTrackingTouch:");
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {

			}
		});
		mSurfaceView.setOnClickListener(this);
		handler.sendEmptyMessageDelayed(MSG_UPDATE_POSITION, 100);
	}

	private void start() {
		mMdiaPlayer.start();
	}

	/**
	 * 切换为全屏模式
	 */
	private void switchFullScreenMode() {
		try {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			isFullscreen = true;
			setVideoLayoutParam();
		} catch (Exception e) {
			// empty
		}
	}

	private void initScreenMode() {
		if (isFullscreen) {
			switchFullScreenMode();
		} else {
			setVideoLayoutParam();
		}}

	private void setVideoLayoutParam() {
		if (isFullscreen) {
			if (mFullScreenVideoLayoutParams == null) {
				mFullScreenVideoLayoutParams = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.MATCH_PARENT);
			}
			mVideoLayout.setLayoutParams(mFullScreenVideoLayoutParams);
			btnAllScreen.setText(R.string.button_small_screen);
		} else {
			if (mDefaulVideoLayoutParams == null) {
				int screenWidth = getResources().getDisplayMetrics().widthPixels;
				mDefaulVideoLayoutParams = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						screenWidth * 9 / 16);
			}
			mVideoLayout.setLayoutParams(mDefaulVideoLayoutParams);
			btnAllScreen.setText(R.string.button_all_screen);
		}
	}

	private void play() {
		isComplection = false;
		if (!TextUtils.isEmpty(mediaPath)) {
			isAutoStart = true;
			try {
				mMdiaPlayer.reset();
				mMdiaPlayer.setDataSource(mediaPath);
				mMdiaPlayer.prepareAsync();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			Toast.makeText(this, "找不到路径", Toast.LENGTH_LONG).show();

			isAutoStart = false;
		}

	}

	private void pause() {
		mMdiaPlayer.pause();
	}

	@Override
	protected void onDestroy() {
		isFullscreen=false;
		super.onDestroy();
		if (mMdiaPlayer.isPlaying()) {
			mMdiaPlayer.stop();
		}
		mMdiaPlayer.release();
		handler.removeMessages(MSG_UPDATE_POSITION);
	}

	@Override
	protected void onResume() {
		super.onResume();
		isShow=true;
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		mMdiaPlayer.pause();
		isShow=false;
	}

	private SurfaceHolder.Callback mSHCallback = new SurfaceHolder.Callback() {

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			TBLog.d(TAG, "surfaceChanged:width" + width + "height=" + height);
			mSurfaceHolder = holder;
			mMdiaPlayer.setDisplay(holder);
		}

		@Override
		public void surfaceCreated(final SurfaceHolder holder) {
			TBLog.d(TAG, "surfaceCreated");
			mSurfaceHolder = holder;
			mMdiaPlayer.setDisplay(holder);
			play();
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			TBLog.d(TAG, "surfaceDestroyed");
			mSurfaceHolder = null;
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnPlay:
			if (isComplection) {
				play();
			} else {
				start();
			}
			break;
		case R.id.btnPause: {
			pause();
		}
			break;
		case R.id.btnReplay: {
			play();
		}
			break;
		case R.id.btnAllScreen: {
			if (isFullscreen) {
				getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				isFullscreen = false;
				initScreenMode();
			} else {
				switchFullScreenMode();
			}
		}
			break;
		case R.id.mSurfaceView: {
			if (sbPosition.getVisibility() != View.VISIBLE) {
				sbPosition.setVisibility(View.VISIBLE);
			} else {
				sbPosition.setVisibility(View.GONE);
			}
		}
		default:
			break;
		}
	}
}
