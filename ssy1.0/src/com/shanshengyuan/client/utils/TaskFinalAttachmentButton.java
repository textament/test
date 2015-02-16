/**
 * TaskFinalAttachmentButton.java
 * com.zhubajie.client.views
 *
 * Function： TODO 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2013-7-12 		Administrator
 *
 *  Copyright (c) 2013 zhubajie, TNT All Rights Reserved.
 */

package com.shanshengyuan.client.utils;

import android.content.Context;
import android.text.TextPaint;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * ClassName:TaskFinalAttachmentButton Function: TODO ADD FUNCTION Reason: TODO
 * ADD REASON
 * 
 * @author Administrator
 * @version
 * @since Ver 2.0.1
 * @Date 2013-7-12
 * 
 * @see
 * 
 */
public class TaskFinalAttachmentButton extends LinearLayout {

    private TextView mText = null;
    private ImageView mImage = null;
    private TextView mKBText = null;

    public TaskFinalAttachmentButton(Context context) {

        super(context);

        init();
    }

    public TaskFinalAttachmentButton(Context context, AttributeSet attrs) {

        super(context, attrs);
        init();
    }

    private void init() {
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER);
        mText = new TextView(getContext());
        mText.setLayoutParams(new LayoutParams(150, LayoutParams.WRAP_CONTENT));
        mText.setSingleLine();
        mText.setTag("tfile");
        mText.setEllipsize(TruncateAt.valueOf("MIDDLE"));
        mText.setGravity(Gravity.CENTER);

        mImage = new ImageView(getContext());
        mImage.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        mImage.setScaleType(ScaleType.CENTER);

        mKBText = new TextView(getContext());
        mKBText.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        mKBText.setTag("tKB");
        mKBText.setGravity(Gravity.CENTER);

        addView(mImage);
        addView(mText);
        addView(mKBText);
    }

    public void setImageAText(int imageId, String textId, int textKB) {
        mImage.setImageResource(imageId);
        mText.setText(textId);
        mKBText.setText(textKB + "kb");
        mKBText.setVisibility(View.GONE);
    }

    /**
     * 
     * getTextViewWidth:获取控件显示之前的宽度
     * 
     * @param
     * @return void
     * @throws
     * @author
     * @since zhubajie　Ver 2.0.1
     */
    public int getTextViewWidth(String name) {
        TextPaint paint = mText.getPaint();
        float len = paint.measureText(name);
        int le = (int) len;
        le += mText.getCompoundPaddingLeft() + mText.getCompoundPaddingRight();
        return le;
    }

}
