package com.shanshengyuan.client.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.widget.ImageView;

public class SuperImageView extends ImageView {

	static final float MAX_SCALE = 2.0f;
	float imageW;
	float imageH;
	float rotatedImageW;
	float rotatedImageH;
	float viewW;
	float viewH;
	Matrix matrix = new Matrix();
	Matrix savedMatrix = new Matrix();
	static final int NONE = 0;// 初始状态
	static final int DRAG = 1;// 拖动
	static final int ZOOM = 2;// 缩放
	static final int ROTATE = 3;// 旋转
	static final int ZOOM_OR_ROTATE = 4; // 缩放或旋转
	int mode = NONE;

	static int clibWidth = 0;
	static int clibHeight = 0;

	int shadowWidth = 0; // 取景框width
	int shadowHeight = 0; // 取景框height
	Path mShadowPath = null;

	PointF pA = new PointF();
	PointF pB = new PointF();
	PointF mid = new PointF();
	PointF lastClickPos = new PointF();
	long lastClickTime = 0;
	double rotation = 0.0;
	float dist = 1f;

	boolean isZoom = false;

	public boolean getMode() {
		return isZoom;
	}

	public SuperImageView(Context context) {
		super(context);
		init();
	}

	public SuperImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SuperImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		setScaleType(ImageView.ScaleType.MATRIX);
		setPadding(ConvertUtils.dip2px(getContext(), 5), 0, ConvertUtils.dip2px(getContext(), 5), 0);
		clibHeight = clibWidth = getResources().getDisplayMetrics().widthPixels;
		// clibHeight = ConvertUtils.dip2px(getContext(), 200);
		clibHeight = 40;
		initDraw();

	}

	public void setImageBitmap(Bitmap bm) {
		super.setImageBitmap(bm);
		setImageWidthHeight();
	}

	public void setImageDrawable(Drawable drawable) {
		super.setImageDrawable(drawable);
		setImageWidthHeight();
	}

	public void setImageResource(int resId) {
		super.setImageResource(resId);
		setImageWidthHeight();
	}

	public void setShadowLayout(int width, int height) {
		this.shadowWidth = width;
		this.shadowHeight = height;
	}

	private void setImageWidthHeight() {
		Drawable d = getDrawable();
		if (d == null) {
			return;
		}
		imageW = rotatedImageW = d.getIntrinsicWidth();
		imageH = rotatedImageH = d.getIntrinsicHeight();
		initImage();
	}

	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		viewW = w;
		viewH = h;
		if (oldw == 0) {
			initImage();
		} else {
			fixScale();
			fixTranslation();
			setImageMatrix(matrix);
		}
	}

	private void initImage() {
		if (viewW <= 0 || viewH <= 0 || imageW <= 0 || imageH <= 0) {
			return;
		}
		mode = NONE;
		matrix.setScale(0, 0);
		fixScale();
		fixTranslation();
		setImageMatrix(matrix);
	}

	private void fixScale() {
		float p[] = new float[9];
		matrix.getValues(p);
		float curScale = Math.abs(p[0]) + Math.abs(p[1]);

		// 此为最适合长宽
		// float minScale = Math.min((float) viewW / (float) rotatedImageW,
		// (float) viewH / (float) rotatedImageH);

		// 此为最适合宽度
		float minScale = (float) viewW / (float) rotatedImageW;

		if (curScale < minScale) {
			if (curScale > 0) {
				double scale = minScale / curScale;
				p[0] = (float) (p[0] * scale);
				p[1] = (float) (p[1] * scale);
				p[3] = (float) (p[3] * scale);
				p[4] = (float) (p[4] * scale);
				matrix.setValues(p);
				isZoom = true;
			} else {
				matrix.setScale(minScale, minScale);
				isZoom = false;
			}
		}
	}

	private float maxPostScale() {
		float p[] = new float[9];
		matrix.getValues(p);
		float curScale = Math.abs(p[0]) + Math.abs(p[1]);

		float minScale = Math.min((float) viewW / (float) rotatedImageW, (float) viewH / (float) rotatedImageH);
		float maxScale = Math.max(minScale, MAX_SCALE);
		return maxScale / curScale;
	}

	private void fixTranslation() {
		RectF rect = new RectF(0, 0, imageW, imageH);
		matrix.mapRect(rect);

		float height = rect.height();
		float width = rect.width();

		float deltaX = 0, deltaY = 0;

		if (width < viewW) {
			deltaX = (viewW - width) / 2 - rect.left;
		} else if (rect.left > 0) {
			deltaX = -rect.left;
		} else if (rect.right < viewW) {
			deltaX = viewW - rect.right;
		}

		if (height < viewH) {
			deltaY = (viewH - height) / 2 - rect.top;
		} else if (rect.top > 0) {
			deltaY = -rect.top;
		} else if (rect.bottom < viewH) {
			deltaY = viewH - rect.bottom;
		}
		matrix.postTranslate(deltaX, deltaY);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		// 主点按下
		case MotionEvent.ACTION_DOWN:
			savedMatrix.set(matrix);
			pA.set(event.getX(), event.getY());
			pB.set(event.getX(), event.getY());
			mode = DRAG;
			break;
		// 副点按下
		case MotionEvent.ACTION_POINTER_DOWN:
			if (event.getActionIndex() > 1)
				break;
			dist = spacing(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
			// 如果连续两点距离大于10，则判定为多点模式
			if (dist > 10f) {
				savedMatrix.set(matrix);
				pA.set(event.getX(0), event.getY(0));
				pB.set(event.getX(1), event.getY(1));
				mid.set((event.getX(0) + event.getX(1)) / 2, (event.getY(0) + event.getY(1)) / 2);
				mode = ZOOM_OR_ROTATE;
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			if (mode == DRAG) {
				if (spacing(pA.x, pA.y, pB.x, pB.y) < 50) {
					long now = System.currentTimeMillis();
					if (now - lastClickTime < 500 && spacing(pA.x, pA.y, lastClickPos.x, lastClickPos.y) < 50) {
						doubleClick(pA.x, pA.y);
						now = 0;
					}
					lastClickPos.set(pA);
					lastClickTime = now;
				}
			} else if (mode == ROTATE) {
				// int level = (int) Math.floor((rotation + Math.PI / 4) /
				// (Math.PI / 2));
				// if (level == 4)
				// level = 0;
				// matrix.set(savedMatrix);
				// matrix.postRotate(90 * level, mid.x, mid.y);
				// if (level == 1 || level == 3) {
				// float tmp = rotatedImageW;
				// rotatedImageW = rotatedImageH;
				// rotatedImageH = tmp;
				// fixScale();
				// }
				// fixTranslation();
				// setImageMatrix(matrix);
			}
			mode = NONE;
			break;
		case MotionEvent.ACTION_MOVE:

			if (mode == ZOOM_OR_ROTATE) {
				PointF pC = new PointF(event.getX(1) - event.getX(0) + pA.x, event.getY(1) - event.getY(0) + pA.y);
				double a = spacing(pB.x, pB.y, pC.x, pC.y);
				double b = spacing(pA.x, pA.y, pC.x, pC.y);
				double c = spacing(pA.x, pA.y, pB.x, pB.y);
				if (a >= 10) {
					double cosB = (a * a + c * c - b * b) / (2 * a * c);
					double angleB = Math.acos(cosB);
					double PID4 = Math.PI / 4;
					if (angleB > PID4 && angleB < 3 * PID4) {
						mode = ROTATE;
						rotation = 0;
					} else {
						mode = ZOOM;
					}
				}
			}

			if (mode == DRAG) {
				matrix.set(savedMatrix);
				pB.set(event.getX(), event.getY());
				matrix.postTranslate(event.getX() - pA.x, event.getY() - pA.y);
				fixTranslation();
				setImageMatrix(matrix);
			} else if (mode == ZOOM) {
				float newDist = spacing(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
				if (newDist > 10f) {
					matrix.set(savedMatrix);
					float tScale = Math.min(newDist / dist, maxPostScale());
					matrix.postScale(tScale, tScale, mid.x, mid.y);
					isZoom = true;
					fixScale();
					fixTranslation();
					setImageMatrix(matrix);
				}
			} else if (mode == ROTATE) {
				// PointF pC = new PointF(event.getX(1) - event.getX(0) + pA.x,
				// event.getY(1) - event.getY(0) + pA.y);
				// double a = spacing(pB.x, pB.y, pC.x, pC.y);
				// double b = spacing(pA.x, pA.y, pC.x, pC.y);
				// double c = spacing(pA.x, pA.y, pB.x, pB.y);
				// if (b > 10) {
				// double cosA = (b * b + c * c - a * a) / (2 * b * c);
				// double angleA = Math.acos(cosA);
				// double ta = pB.y - pA.y;
				// double tb = pA.x - pB.x;
				// double tc = pB.x * pA.y - pA.x * pB.y;
				// double td = ta * pC.x + tb * pC.y + tc;
				// if (td > 0) {
				// angleA = 2 * Math.PI - angleA;
				// }
				// rotation = angleA;
				// matrix.set(savedMatrix);
				// matrix.postRotate((float) (rotation * 180 / Math.PI), mid.x,
				// mid.y);
				// setImageMatrix(matrix);
				// }

			}
			break;
		}

		return true;
	}

	/**
	 * 两点的距离
	 */
	private float spacing(float x1, float y1, float x2, float y2) {
		float x = x1 - x2;
		float y = y1 - y2;
		return FloatMath.sqrt(x * x + y * y);
	}

	private void doubleClick(float x, float y) {

		float p[] = new float[9];
		matrix.getValues(p);
		float curScale = Math.abs(p[0]) + Math.abs(p[1]);

		// 此为最适合长宽
		// float minScale = Math.min((float) viewW / (float) rotatedImageW,
		// (float) viewH / (float) rotatedImageH);

		// 此为最适合宽度
		float minScale = (float) viewW / (float) rotatedImageW;

		if (curScale <= minScale + 0.01) { // 放大
			float toScale = Math.max(minScale, MAX_SCALE) / curScale;
			matrix.postScale(toScale, toScale, x, y);
			isZoom = true;
		} else { // 缩小
			// float toScale = minScale / curScale;
			// matrix.postScale(toScale, toScale, x, y);
			matrix.setScale(minScale, minScale);
			isZoom = false;
			fixTranslation();
		}

		setImageMatrix(matrix);
	}

	private Paint paint = null;
	private Paint paint2 = null;
	private Rect cr = null;
	private Rect cr2 = null;

	private void initDraw() {
		paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setAntiAlias(true);
		paint.setAlpha(120);

		paint2 = new Paint();
		paint2.setColor(Color.BLACK);
		paint2.setAntiAlias(true);
		paint2.setAlpha(0);

	}

	@Override
	protected void onDraw(Canvas canvas) {

		super.onDraw(canvas);

		if (cr == null) {
			cr = new Rect();
			cr.set(0, 0, getWidth(), getHeight());
		}

		if (cr2 == null) {

			cr2 = new Rect();

			int offsetWidth = getWidth() - clibWidth;
			int offsetHeight = getHeight() - clibHeight;

			if (offsetWidth < 0) {
				offsetWidth = 0;
			}

			if (offsetHeight < 0) {
				offsetHeight = 0;
			}

			cr2.set(offsetWidth / 2, offsetHeight / 2, clibWidth + offsetWidth / 2, clibHeight + offsetHeight / 2);
			mShadowPath = new Path();
			mShadowPath.addRect(new RectF(cr2), Path.Direction.CCW);
		}
		// canvas.clipPath(mShadowPath, Region.Op.INTERSECT);
		// canvas.clipRect(cr2, Region.Op.XOR);

		canvas.drawRect(cr, paint2);
	}

	public void turnLeft() {

		if (matrix != null) {

			rotation = rotation + Math.PI / 4;

			int level = (int) Math.floor((rotation * 2 + Math.PI / 4) / (Math.PI / 2));

			if (level == 4) {
				rotation = 0;
				level = 0;
			}

			matrix.set(savedMatrix);
			matrix.postRotate(90 * level, mid.x, mid.y);
			if (level == 1 || level == 3) {
				float tmp = rotatedImageW;
				rotatedImageW = rotatedImageH;
				rotatedImageH = tmp;
				fixScale();
			}
			fixTranslation();
			setImageMatrix(matrix);
		}

		invalidate();
	}

}
