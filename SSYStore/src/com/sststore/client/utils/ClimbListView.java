package com.sststore.client.utils;

/**
 * DataListView.java
 * com.zhubajie.client.views
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2012-9-4 		lihao
 *
 *  Copyright (c) 2012 zhubajie, TNT All Rights Reserved.
 */

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.HeaderViewListAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;


import static com.sststore.client.R.*;
import static com.sststore.client.utils.ConvertUtils.*;

/**
 * 数据列表的父类
 * 
 * @author lihao
 * @version
 * @since Ver 2.0.0
 * @Date 2012-9-4
 * 
 * @see
 */
public class ClimbListView extends PullToRefreshListView implements OnScrollListener {

	public static final String tag = "ClimbListView";

	private boolean isFirstItem = true;
	private boolean isLastItem = false;
	public boolean isFirst = false;

	protected View headView = null;
	protected View footerView = null;
	protected UpLoadListener listener = null;

	private boolean isRecored = false;

	private boolean isScrollUp = true; // 是否可以滚出顶部
	private boolean isScrollDown = true; // 是否可以滚出底部
	private boolean isScrollBack = true; // 是否回滚
	private boolean doTopAction = true; // 滚出顶部后是否执行操作
	private int scrollStatus = 0; // 滚动状态
	protected boolean doBottomAction = true; // 滚出底部后是否执行操作

	private int offsetTopDip = 0; // 顶部偏移量
	private int maxOffsetTopDip = 0; // 顶部最大偏移量
	private int offsetBottomDip = 0; // 底部偏移量
	private int maxOffsetBottomDip = 0; // 底部最大偏移量

	public boolean isScroll = true; // 当前list是否上下滚动
	private boolean tureScroll = false; // 确定滚动方向

	private OnClickListener mErrorListener = null; // 获取列表失败时，轻触刷新事件

	private View mFootShowView = null;
	private View mFootLoadView = null;
	private View mFootNoDataView = null;
	private View mFootErrorDataView = null;

	private int scrollType = 0; // 表示当前列表所处状态，0：普通， 1：获取数据中， 2：没有获取到数据，
								// 3：获取数据失败， -1：无状态

	/**
	 * 解决ScrollView嵌套ViewPager出现的滑动冲突问题
	 */
	private boolean canScroll;
	private GestureDetector mGestureDetector;
	View.OnTouchListener mGestureListener;
	
	public int getScrollType() {
		return scrollType;
	}

	public ClimbListView(Context context) {
		super(context);
		init();
	}

	public ClimbListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	@Override
	public ListAdapter getAdapter() {

		ListAdapter adapter = super.getAdapter();

		if (adapter instanceof HeaderViewListAdapter) {
			return ((HeaderViewListAdapter) adapter).getWrappedAdapter();
		} else {
			return adapter;
		}
	}

	private boolean canUpdate = true;

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		canUpdate = true;
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	}

	@Override
	protected void layoutChildren() {

		Log.i(tag, "call me");

		if (!canUpdate) {
			return;
		}
		canUpdate = false;
		super.layoutChildren();

	}

	protected void init() {

		// 
		mGestureDetector = new GestureDetector(getContext(), new YScrollDetector());
		canScroll = true;
		
		offsetTopDip = ConvertUtils.px2dip(getContext(),offsetTopDip);

		offsetBottomDip = ConvertUtils.px2dip(getContext(),offsetBottomDip);

		// initHeadView();
		// initFooterView();

		listener = new UpLoadListener() {

			public void scrollTopAction() {

				Log.i(tag, "do top action");
			}

			public void scrollBottomAction() {

				Log.i(tag, "do bottom action");
			}

		};
		this.setOnScrollListener(this);
	}

	private int tempScrollY = 0;

	public void initFooterView() {
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		footerView = inflater.inflate(layout.demand_list_footer, null);

		mFootErrorDataView = footerView.findViewById(id.demand_footer_error);
		mFootLoadView = footerView.findViewById(id.demand_footer_load);
		mFootNoDataView = footerView.findViewById(id.demand_footer_nomore);
		mFootShowView = footerView.findViewById(id.demand_footer_show);

		if (mErrorListener != null) {
			mFootErrorDataView.setOnClickListener(mErrorListener);
		}

		this.addFooterView(footerView, null, false);
	}

	protected void initHeadView() {
		headView = new LinearLayout(getContext()); 
		headView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		headView.setBackgroundColor(Color.RED);
		this.addHeaderView(headView, null, false);
		headView.setPadding(0, offsetTopDip, 0, 0);
	}

	public void updateFinish() {
		// headView.setPadding(0, -1 * offsetDip, 0, 0);
		// Log.i(tag, "reflash finish");
		// TextView dateText = (TextView)
		// headView.findViewById(R.id.demand_head_date);
		// dateText.setText(getContext().getString(R.string.last_flash_date, new
		// Date().toLocaleString()));
	}

	public void loadingFinish() {
		mFootLoadView.setVisibility(View.VISIBLE);
		mFootShowView.setVisibility(View.GONE);
		mFootErrorDataView.setVisibility(View.GONE);
		mFootNoDataView.setVisibility(View.GONE);
		scrollType = 1;
	}

	public void loadedFinish() {
		mFootShowView.setVisibility(View.GONE);
		mFootLoadView.setVisibility(View.VISIBLE);
		mFootErrorDataView.setVisibility(View.GONE);
		mFootNoDataView.setVisibility(View.GONE);
		scrollType = 0;
	}

	public void noDataFinish() {
		mFootShowView.setVisibility(View.GONE);
		mFootLoadView.setVisibility(View.GONE);
		mFootErrorDataView.setVisibility(View.GONE);
		mFootNoDataView.setVisibility(View.VISIBLE);
		scrollType = 2;
	}

	public void noDataFinishNoScroll() {
		doBottomAction = false;
		mFootShowView.setVisibility(View.GONE);
		mFootLoadView.setVisibility(View.GONE);
		mFootErrorDataView.setVisibility(View.GONE);
		mFootNoDataView.setVisibility(View.VISIBLE);
		scrollType = 2;
	}

	public void errorDataFinish() {
		mFootShowView.setVisibility(View.GONE);
		mFootLoadView.setVisibility(View.GONE);
		mFootErrorDataView.setVisibility(View.VISIBLE);
		mFootNoDataView.setVisibility(View.GONE);
		scrollType = 3;
	}

	public void setHiddenFooterView() {
		doBottomAction = false;
		mFootShowView.setPadding(0, 0, 0, 0);
		mFootShowView.setVisibility(View.GONE);
		mFootLoadView.setPadding(0, 0, 0, 0);
		mFootLoadView.setVisibility(View.GONE);
		mFootErrorDataView.setPadding(0, 0, 0, 0);
		mFootErrorDataView.setVisibility(View.GONE);
		mFootNoDataView.setPadding(0, 0, 0, 0);
		mFootNoDataView.setVisibility(View.GONE);

		scrollType = -1;
	}

	public void setHiddenFooterViewForScroll() {
		doBottomAction = true;
		if (mFootShowView == null) {
			return;
		}
		mFootShowView.setPadding(0, 0, 0, 0);
		mFootShowView.setVisibility(View.GONE);
		if (mFootLoadView == null) {
			return;
		}
		mFootLoadView.setPadding(0, 0, 0, 0);
		mFootLoadView.setVisibility(View.GONE);
		if (mFootErrorDataView == null) {
			return;
		}
		mFootErrorDataView.setPadding(0, 0, 0, 0);
		mFootErrorDataView.setVisibility(View.GONE);
		if (mFootNoDataView == null) {
			return;
		}
		mFootNoDataView.setPadding(0, 0, 0, 0);
		mFootNoDataView.setVisibility(View.GONE);

		scrollType = -1;
	}

	public void setUpLoadListener(UpLoadListener upLoadListener) {
		listener = upLoadListener;
	}

	int startX = 0;
	int startY = 0;
	int offsetX = 0;
	int offsetY = 0;

	// @Override
	// public boolean onTouchEvent(MotionEvent ev) {
	//
	// int type = ev.getAction();
	//
	// switch (type) {
	// case MotionEvent.ACTION_DOWN:
	//
	// startX = (int) ev.getRawX();
	// startY = (int) ev.getRawY();
	// break;
	// case MotionEvent.ACTION_UP:
	//
	// tureScroll = false;
	// isRecored = false;
	//
	// if (isFirstItem) {
	//
	// } else if (isLastItem) {
	//
	// // if (footerView.getPaddingTop() > offsetTopDip) {
	// // Log.i(tag, "bottom scroll back");
	// // footerView.setPadding(0, offsetBottomDip, 0, 0);
	// // }
	//
	// }
	//
	// // if (doTopAction && isFirstItem) {
	// // if (headView.getPaddingTop() == 0) {
	// // listener.scrollTopAction();
	// // }
	// // }
	// //
	// // if (doBottomAction && isLastItem) {
	// // if (footerView.getPaddingBottom() == 0) {
	// // listener.scrollBottomAction();
	// // }
	// // }
	// break;
	// case MotionEvent.ACTION_MOVE:
	//
	// // if (!isFirstItem && !isLastItem) {
	// // return super.onTouchEvent(ev);
	// // }
	// //
	// // if (!isRecored && isFirstItem && headView.getTop() == 0) {
	// // Log.v(tag, "在move时候记录下位置");
	// // isRecored = true;
	// // startY = (int) ev.getRawY();
	// // offsetY = (int) ev.getY();
	// // break;
	// // }
	//
	// break;
	// }
	// return super.onTouchEvent(ev);
	// }

	
	public int getOffsetTopDip() {
		return offsetTopDip;
	}

	public void setOffsetTopDip(int offsetTopDip) {
		this.offsetTopDip = offsetTopDip;
	}

	public int getMaxOffsetTopDip() {
		return maxOffsetTopDip;
	}

	public void setMaxOffsetTopDip(int maxOffsetTopDip) {
		this.maxOffsetTopDip = maxOffsetTopDip;
	}

	public int getOffsetBottomDip() {
		return offsetBottomDip;
	}

	public void setOffsetBottomDip(int offsetBottomDip) {
		this.offsetBottomDip = offsetBottomDip;
	}

	public int getMaxOffsetBottomDip() {
		return maxOffsetBottomDip;
	}

	public void setMaxOffsetBottomDip(int maxOffsetBottomDip) {
		this.maxOffsetBottomDip = maxOffsetBottomDip;
	}

	public int getScrollStatus() {
		return scrollStatus;
	}

	public boolean isScrollUp() {
		return isScrollUp;
	}

	public void setScrollUp(boolean isScrollUp) {
		this.isScrollUp = isScrollUp;
	}

	public boolean isScrollDown() {
		return isScrollDown;
	}

	public void setScrollDown(boolean isScrollDown) {
		this.isScrollDown = isScrollDown;
	}

	public boolean isScrollBack() {
		return isScrollBack;
	}

	public void setScrollBack(boolean isScrollBack) {
		this.isScrollBack = isScrollBack;
		if (mFootErrorDataView != null) {
			mFootErrorDataView.setOnClickListener(mErrorListener);
		}
	}

	public void setOnErrorListener(OnClickListener click) {
		this.mErrorListener = click;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//		 Log.i("firstVisibleItem", firstVisibleItem+"");
//		Log.i("visibleItemCount", visibleItemCount+"");
//		 Log.i("totalItemCount", totalItemCount+"");
		isFirstItem = (firstVisibleItem == 0);
		isLastItem = ((firstVisibleItem + visibleItemCount) == totalItemCount);
		// Log.i("isLastItem", isLastItem+"");
		if (isLastItem && isFirst) {
			Log.i("执行分页", "执行了");
			isFirst = false;
			listener.scrollBottomAction();
		}

		super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
	}

//	@Override
//	public boolean onInterceptTouchEvent(MotionEvent ev) {
//		if (ev.getAction() == MotionEvent.ACTION_UP)
//			canScroll = true;
//		return super.onInterceptTouchEvent(ev) && mGestureDetector.onTouchEvent(ev);
//	}

	class YScrollDetector extends SimpleOnGestureListener {
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//			if (canScroll)
//				if (Math.abs(distanceY) >= Math.abs(distanceX))
//					canScroll = true;
//				else
//					canScroll = false;
			return canScroll;
		}
	}
	
	public interface UpLoadListener {
		public void scrollTopAction();

		public void scrollBottomAction();
	}

}
