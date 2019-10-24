package com.m7.imkfsdk.view;


import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.m7.imkfsdk.R;

/**
 * 自定义拉下刷新ListView，只使用一张进度条图片跟随下拉旋转，有回弹动画
 *
 */
public class ChatListView extends ListView implements OnScrollListener {

	/** 操作状态:下拉刚开始、回退到顶、一次刷新结束 */
	private static final int DONE = 0x1;
	/** 操作状态:松开即可刷新 */
	private final static int RELEASE_TO_REFRESH = 0x2;
	/** 操作状态:下拉可以刷新 */
	private final static int PULL_TO_REFRESH = 0x3;
	/** 操作状态:正在刷新 */
	private final static int REFRESHING = 0x4;

	/** 自定义ListView头布局 */
	private LinearLayout headView;

	/** 圆形进度条图标 */
	private ImageView imgCycle;
	/** headView宽 */
	private int headContentWidth;
	/** headView高 */
	private int headContentHeight;
	/** 圆形进度条旋转动画 */
	private Animation cycleAnim;
	/** 标识各种刷新状态 */
	private int refreshState;
	/** 首次触摸屏幕设为true,松手时设为false,控制一次触摸事件的记录状态 */
	private boolean isRecored = false;
	/** 手指首次触摸屏幕时Y位置 */
	private int startY;
	/** 手指移动的距离和headView的padding距离的比例,防止移动时headView下拉过长 */
	private final static int RATIO = 3;
	/** 表示已经下拉到可以刷新状态,可以拉回 */
	private boolean isBack = false;
	/** 刷新监听回调接口 */
	private OnRefreshListener refreshListener;
	/** 列表在屏幕顶端第一个完整可见项的position */
	private int firstItemIndex;

	/** 用于记录一次下拉过程中实时PaddingTop值 */
	private int lessDisPadding;
	private LessPaddingSetRunnable lessPaddingSetRunnable;

	/** 用于记录刷新状态又执行下拉操作后的实时PaddingTop值 */
	private int moreDisPadding;
	private MorePaddingSetRunnable morePaddingSetRunnable;

	public ChatListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	/** 初始化 */
	private void init(Context context) {
		// 获取自定义头view
		headView = (LinearLayout) LayoutInflater.from(context).inflate(
				R.layout.kf_head_private_letter, null);
		// 获取headView中控件
		imgCycle = (ImageView) headView.findViewById(R.id.imgCycle);
		// 预估headView宽高
		measureView(headView);
		// 获取headView宽高
		headContentWidth = headView.getMeasuredWidth();
		headContentHeight = headView.getMeasuredHeight();
		// 设置headView的padding值,topPadding为其本身的负值,达到在屏幕中隐藏的效果
		headView.setPadding(0, -headContentHeight, 0, 0);
		// 重绘headView
		headView.invalidate();
		// 将headView添加到自定义的ListView头部
		this.addHeaderView(headView, null, false);
		// 设置ListView的滑动监听
		this.setOnScrollListener(this);
		// 获取进度条图片旋转动画
		cycleAnim = AnimationUtils.loadAnimation(context,
				R.anim.kf_anim_chat_cycle);
		// 初始刷新状态
		refreshState = DONE;
	}

	public void dismiss() {

		this.removeHeaderView(headView);
	}

	public void visible() {

		this.addHeaderView(headView);
	}

	/**
	 * 预估headView的宽高
	 * 
	 * @param child
	 */
	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
						 int visibleItemCount, int totalItemCount) {
		// 记录滚动时列表第一个完整可见项的position
		firstItemIndex = firstVisibleItem;

	}

	/** 监听触摸事件 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:// 第一次触摸时
			if (firstItemIndex == 0) {
				// 开始记录
				isRecored = true;
				// 获取首次Y位置
				startY = (int) ev.getY();
			} else {
			}
			break;
		case MotionEvent.ACTION_UP:// 松开屏幕时
			// 移除记录
			isRecored = false;
			if (refreshState == PULL_TO_REFRESH) {
				refreshState = DONE;
				changeHeadView();
			} else if (refreshState == RELEASE_TO_REFRESH) {
				refreshState = REFRESHING;
				changeHeadView();

				// morePadding回弹
				if (morePaddingSetRunnable != null) {
					morePaddingSetRunnable.stop();
				}
				morePaddingSetRunnable = new MorePaddingSetRunnable(
						moreDisPadding);
				post(morePaddingSetRunnable);

				// 执行刷新方法
				onRefreshing();
			} else if (refreshState == REFRESHING) {
				if (firstItemIndex == 0) {

					// morePadding回弹
					if (morePaddingSetRunnable != null) {
						morePaddingSetRunnable.stop();
					}
					if (lessPaddingSetRunnable != null) {
						lessPaddingSetRunnable.stop();
					}
					morePaddingSetRunnable = new MorePaddingSetRunnable(
							moreDisPadding);
					post(morePaddingSetRunnable);
				} else {
					// NO-OP
				}
			}
			break;
		case MotionEvent.ACTION_MOVE:// 手势移动时
			// 记录实时的手指移动时在屏幕的Y位置,用于和startY比较
			int curY = (int) ev.getY();

			if (!isRecored && firstItemIndex == 0) {
				isRecored = true;
				startY = curY;
			}

			if (isRecored) {
				// 开始或结束状态
				if (refreshState == DONE) {
					if (curY - startY > 0) {// 表示向下拉了
						// 状态改为下拉刷新
						refreshState = PULL_TO_REFRESH;
						changeHeadView();
					}
				}

				// 下拉刷新状态
				if (refreshState == PULL_TO_REFRESH) {
					// setSelection(0);

					// 不断改变headView的高度
					headView.setPadding(0, (curY - startY) / RATIO
							- headContentHeight, 0, 0);

					// 旋转圆形进度条
					rotateCycle(curY - startY);

					// 实时记录lessDisPadding偏移量，做最小矫正
					lessDisPadding = (curY - startY) / RATIO
							- headContentHeight;
					if (lessDisPadding <= -headContentHeight) {
						lessDisPadding = -headContentHeight;
					}

					// 下拉到RELEASE_TO_REFRESH状态
					if ((curY - startY) / RATIO >= headContentHeight) {
						refreshState = RELEASE_TO_REFRESH;
						isBack = true;
						changeHeadView();
					} else if ((curY - startY) <= 0) {
						// 上推到顶
						refreshState = DONE;
						changeHeadView();
					}
				}

				// 松手可以刷新状态
				if (refreshState == RELEASE_TO_REFRESH) {
					// setSelection(0);

					// 不断改变headView的高度
					headView.setPadding(0, (curY - startY) / RATIO
							- headContentHeight, 0, 0);

					// 实时记录lessDisPadding偏移量，做最小矫正
					lessDisPadding = (curY - startY) / RATIO
							- headContentHeight;
					if (lessDisPadding <= -headContentHeight) {
						lessDisPadding = -headContentHeight;
					}

					// 实时记录moreDisPadding偏移量，做最小矫正
					moreDisPadding = (curY - startY) / RATIO
							- headContentHeight;
					if (moreDisPadding <= 0) {
						moreDisPadding = 0;
					}

					// 又往上推
					if ((curY - startY) / RATIO < headContentHeight) {
						refreshState = PULL_TO_REFRESH;
						changeHeadView();
					}
				}

				// 正在刷新状态
				if (refreshState == REFRESHING) {
					if (curY - startY > 0) {
						// 只改变padding值,不做其余处理
						headView.setPadding(0, (curY - startY) / RATIO, 0, 0);

						// 实时记录lessDisPadding偏移量，做最小矫正
						lessDisPadding = (curY - startY) / RATIO;
						if (lessDisPadding <= -headContentHeight) {
							lessDisPadding = -headContentHeight;
						}

						// 实时记录moreDisPadding偏移量，做最小矫正
						moreDisPadding = (curY - startY) / RATIO;
						if (moreDisPadding <= 0) {
							moreDisPadding = 0;
						}
					}
				}
			}
			break;
		}
		return super.onTouchEvent(ev);
	}

	/** 进入刷新的方法 */
	private void onRefreshing() {
		// 调用回调接口中的刷新方法
		if (refreshListener != null) {
			refreshListener.toRefresh();
		}
	}

	/** 使用界面传递给此ListView的回调接口,用于两者间通信 */
	public interface OnRefreshListener {
		public void toRefresh();
	}

	/**
	 * 注册一个用于刷新的回调接口
	 * 
	 * @param refreshListener
	 */
	public void setOnRefreshListener(OnRefreshListener refreshListener) {
		// 获取传递过来的回调接口
		this.refreshListener = refreshListener;
	}

	/** 使用界面执行完刷新操作时调用此方法 */
	public void onRefreshFinished() {
		refreshState = DONE;
		changeHeadView();

	}

	/** 下拉过程中旋转进度条 */
	private void rotateCycle(int del) {
		float angle = (float) (del * 2);
		// 获取进度条图片属性
		ViewGroup.LayoutParams lp = imgCycle.getLayoutParams();
		imgCycle.setScaleType(ScaleType.MATRIX);
		Matrix matrix = new Matrix();
		// 绕(高度/2)和(宽度/2)的中心点进行旋转
		matrix.postRotate(angle, lp.width / 2f, lp.height / 2f);

		imgCycle.setImageMatrix(matrix);
	}

	/** 根据下拉的状态改变headView */
	private void changeHeadView() {
		switch (refreshState) {
		case DONE:// 开始或结束状态

			if (isRecored) {// 正在刷新状态下又下拉并且直至刷新完还未松手
				// 标记不再记录
				isRecored = false;
			} else {// 松手状态下进入刷新完成
				// 回弹距离做最大矫正
				if (lessDisPadding >= 0) {
					lessDisPadding = 0;
				}
			}

			// 回退状态清除
			isBack = false;
			// 清除进度条旋转动画
			imgCycle.clearAnimation();

			// lessPadding回弹
			if (lessPaddingSetRunnable != null) {
				lessPaddingSetRunnable.stop();
			}
			if (morePaddingSetRunnable != null) {
				morePaddingSetRunnable.stop();
			}
			lessPaddingSetRunnable = new LessPaddingSetRunnable(lessDisPadding);
			post(lessPaddingSetRunnable);
			break;
		case PULL_TO_REFRESH:// 下拉刷新状态
			// 从RELEASE_TO_REFRESH回到PULL_TO_REFRESH状态
			if (isBack) {
				// NO-OP
			}
			break;
		case RELEASE_TO_REFRESH:
			break;
		case REFRESHING:
			// 开启进度条旋转动画
			imgCycle.startAnimation(cycleAnim);
			break;
		}
	}

	/**
	 * 头部回弹动画，采用分时间段设置Padding的方式
	 * <ul>
	 * <strong>两个过程包含此效果：</strong>
	 * <li>单次下拉未达到刷新位置后松手</li>
	 * <li>单次下拉超过headview高度后松手</li>
	 * </ul>
	 * 
	 * <p>
	 * 代码逻辑参照<em>Chris Banes</em>的<code>PullToRefreshListView</code>
	 * <p>
	 */
	private class LessPaddingSetRunnable implements Runnable {
		private Interpolator interpolator;
		private int disP;
		private long startTime = -1;
		private int currentPadding = -1;
		private boolean canRunning = true;

		public LessPaddingSetRunnable(int disPadding) {
			interpolator = new DecelerateInterpolator();
			this.disP = disPadding;
		}

		@Override
		public void run() {
			if (startTime == -1) {
				startTime = System.currentTimeMillis();
			} else {
				long normalizedTime = (1000 * (System.currentTimeMillis() - startTime)) / 200;
				normalizedTime = Math.max(Math.min(normalizedTime, 1000), 0);
				int deltaP = Math
						.round((disP + headContentHeight)
								* interpolator
										.getInterpolation(normalizedTime / 1000f));
				currentPadding = disP - deltaP;
				headView.setPadding(0, currentPadding, 0, 0);
			}

			if (canRunning && (currentPadding > -headContentHeight)) {
				ChatListView.this.postDelayed(this, 16);
			} else {
				stop();
			}
		}

		public void stop() {
			canRunning = false;
			removeCallbacks(this);
		}
	}

	/**
	 * 头部回弹动画，采用分时间段设置Padding的方式
	 * <ul>
	 * <strong>一个过程包含此效果：</strong>
	 * <li>正在刷新状态下又执行下拉后松手</li>
	 * </ul>
	 * 
	 * <p>
	 * 代码逻辑参照<em>Chris Banes</em>的<code>PullToRefreshListView</code>
	 * <p>
	 */
	private class MorePaddingSetRunnable implements Runnable {
		private Interpolator interpolator;
		private int disP;
		private long startTime = -1;
		private int currentPadding = 1;
		private boolean canRunning = true;

		public MorePaddingSetRunnable(int disPadding) {
			interpolator = new DecelerateInterpolator();
			this.disP = disPadding;
		}

		@Override
		public void run() {
			if (startTime == -1) {
				startTime = System.currentTimeMillis();
			} else {
				long normalizedTime = (1000 * (System.currentTimeMillis() - startTime)) / 200;
				normalizedTime = Math.max(Math.min(normalizedTime, 1000), 0);
				int deltaP = Math
						.round((disP)
								* interpolator
										.getInterpolation(normalizedTime / 1000f));
				currentPadding = disP - deltaP;
				headView.setPadding(0, currentPadding, 0, 0);
			}

			if (canRunning && (currentPadding > 0)) {
				ChatListView.this.postDelayed(this, 16);
			} else {
				stop();
			}
		}

		public void stop() {
			canRunning = false;
			removeCallbacks(this);
		}
	}
}
