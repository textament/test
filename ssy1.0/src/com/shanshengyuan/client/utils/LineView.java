/**
 * LineView.java
 * com.zhubajie.client.views
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2013-4-16 		lihao
 *
 *  Copyright (c) 2013 zhubajie, TNT All Rights Reserved.
 */

package com.shanshengyuan.client.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * ClassName:LineView Function: TODO ADD FUNCTION Reason: TODO ADD REASON
 * 
 * @author lihao
 * @version
 * @since Ver 2.0.0
 * @Date 2013-4-16
 * 
 * @see
 * 
 */
public class LineView extends View {

	private int lineWidth = 0;

	private Paint mLinePaint = null;
	private Paint mLineShdowPaint = null;

	public LineView(Context context) {
		super(context);
		initValue();
	}

	public LineView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initValue();
	}

	public LineView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initValue();
	}

	private void initValue() {
		lineWidth = ConvertUtils.dip2px(getContext(), 1f);
		mLinePaint = new Paint();
		mLinePaint.setARGB(200, 80, 80, 80);

		mLineShdowPaint = new Paint();
		mLineShdowPaint.setARGB(180, 50, 50, 50);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.drawLine(0, 0, lineWidth, getHeight(), mLinePaint);
		canvas.drawLine(lineWidth, 0, lineWidth * 2, getHeight(), mLineShdowPaint);

	}

}
