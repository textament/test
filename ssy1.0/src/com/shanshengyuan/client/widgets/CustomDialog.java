package com.shanshengyuan.client.widgets;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.LinearLayout.LayoutParams;

import com.shanshengyuan.client.R;

/**
 * @author Action Tan
 * @date Jul 24, 2013
 */
public class CustomDialog extends Dialog {
    public CustomDialog(Context context) {
        super(context);
    }

    public CustomDialog(Context ctx, int theme) {
        super(ctx, theme);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.progress_bar);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(params);

    }

}
