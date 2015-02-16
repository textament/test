/**
 * 
 */
package com.shanshengyuan.client.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.widget.ProgressBar;

/**
 * 带进度条的WebView
 * @see http://www.cnblogs.com/over140/archive/2013/03/07/2947721.html
 * 
 */
@SuppressWarnings("deprecation")
public class ProgressWebView extends WebView {

    private ProgressBar progressbar;
    
    ScrollInterface mt;    

    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
     
    }
    
    public void setProgress(ProgressBar progressbar){
    	this.progressbar = progressbar;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
    	mt.onSChanged(l, t, oldl, oldt);    
        super.onScrollChanged(l, t, oldl, oldt);
        
    }
    
    public void setOnCustomScroolChangeListener(ScrollInterface t){       
    	this.mt=t;
    	}
    
    /**     

    * 定义滑动接口     

    * @param t     

    */    

    public interface ScrollInterface {    

        

    public void onSChanged(int l, int t, int oldl, int oldt) ;    

    }

}
