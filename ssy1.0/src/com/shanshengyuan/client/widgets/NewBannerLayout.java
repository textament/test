package com.shanshengyuan.client.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.shanshengyuan.client.R;
import com.shanshengyuan.client.cache.SSYImageCache;
import com.shanshengyuan.client.cache.SSYImageCache.ImageCallback;
import com.shanshengyuan.client.model.ad.Adver;
import com.shanshengyuan.client.utils.ConvertUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NewBannerLayout extends RelativeLayout implements OnPageChangeListener{

    Context context;

    private AdPagerAdapter mAdapter = null;

    private List<Adver> mAdList = null;
    private List<ImageView> vList = null;

    // 图片缓存
    private HashMap<String, ImageView> mImageMap = null;

    private Timer mAdPagerTimer;

    ViewPager mBanner;

    LinearLayout ly;

    // 用来表示每个小圆点的imageView
    private ImageView imageView;
    // 小圆点ImageViews的数组
    private ImageView[] image;

    private boolean mIsChanged = false;
    private int mCurrentPagePosition = 0;
    private int mCurrentIndex;
    private  int POINT_LENGTH = 0;
    private int FIRST_ITEM_INDEX = 1;

    public NewBannerLayout(Context context) {
        super(context);
        this.context = context;
    }

    public NewBannerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void close() {
        if (mAdPagerTimer != null) {
            mAdPagerTimer.cancel();
            mAdPagerTimer=null;
        }
    }

    public void init() {
        this.setId(11);
        mAdPagerTimer = new Timer();
        ly = new LinearLayout(context);
        ly.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        ly.setPadding(0, 0, 0, 10);
        ly.setGravity(Gravity.CENTER | Gravity.BOTTOM);

        mBanner = new ViewPager(context);

        mAdList = new ArrayList<Adver>();
        vList = new ArrayList<ImageView>();

        mAdapter = new AdPagerAdapter();
        mBanner.setAdapter(mAdapter);
        mImageMap = new HashMap<String, ImageView>();

    }

    public int getViewPaperCurrentItem() {

        if (mBanner != null) {
            return mBanner.getCurrentItem();
        } else {
            return -1;
        }
    }

    public void setInit(List<Adver> list, OnClickListener click) {

        if (list == null) {
            this.mAdList = new ArrayList<Adver>();
        } else {
            this.mAdList = list;
            image = new ImageView[list.size()];
            POINT_LENGTH = list.size();
        }

        //添加首页和未页
        ImageView mImageFrist = new ImageView(getContext());
        RelativeLayout.LayoutParams paramsFrist = new RelativeLayout.LayoutParams(300, 80);
        mImageFrist.setLayoutParams(paramsFrist);

        String urlfrist = mAdList.get(mAdList.size()-1).getImgUrl();
        SSYImageCache.getInstance().downloadImage(mImageMap, mImageFrist, urlfrist,
                false, false, new ImageCallback() {

                    @Override
                    public void onImageLoaded(Bitmap bitmap, String imageUrl) {
                        if (bitmap != null
                                && mImageMap.get(imageUrl) != null)
                            mImageMap.get(imageUrl).setImageBitmap(bitmap);

                    }
                });
        mImageFrist.setScaleType(ImageView.ScaleType.FIT_XY);

        vList.add(mImageFrist);

        for (int i = 0; i < mAdList.size(); i++) {

            ImageView mImage = new ImageView(getContext());
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(300, 80);
            mImage.setLayoutParams(params);

            String url = mAdList.get(i).getImgUrl();
            SSYImageCache.getInstance().downloadImage(mImageMap, mImage, url,
                    false, false, new ImageCallback() {

                        @Override
                        public void onImageLoaded(Bitmap bitmap, String imageUrl) {
                            if (bitmap != null
                                    && mImageMap.get(imageUrl) != null)
                                mImageMap.get(imageUrl).setImageBitmap(bitmap);

                        }
                    });
            mImage.setScaleType(ImageView.ScaleType.FIT_XY);
            mImage.setOnClickListener(click);
            vList.add(mImage);

        }

        ImageView mImageEnd = new ImageView(getContext());
        RelativeLayout.LayoutParams paramsEnd = new RelativeLayout.LayoutParams(300, 80);
        mImageFrist.setLayoutParams(paramsEnd);

        String urlend = mAdList.get(0).getImgUrl();
        SSYImageCache.getInstance().downloadImage(mImageMap, mImageEnd, urlend,
                false, false, new ImageCallback() {

                    @Override
                    public void onImageLoaded(Bitmap bitmap, String imageUrl) {
                        if (bitmap != null
                                && mImageMap.get(imageUrl) != null)
                            mImageMap.get(imageUrl).setImageBitmap(bitmap);

                    }
                });
        mImageEnd.setScaleType(ImageView.ScaleType.FIT_XY);

        vList.add(mImageEnd);

        mAdapter.notifyDataSetChanged();

        if (list != null && list.size() > 0&&mAdPagerTimer!=null) {
            mAdPagerTimer.cancel();
            mAdPagerTimer = new Timer();
            mAdPagerTimer.schedule(mAdtask, 5000, 5000);
        }
        addView(mBanner);
        mBanner.setAdapter(mAdapter);
        mBanner.setOnPageChangeListener(this);
        mBanner.setCurrentItem(FIRST_ITEM_INDEX, false);
        initPoint();
    }

    /**
     * 初始化底部小点
     */
    private void initPoint() {

        // 循环取得小点图片
        if(image!=null){
            for (int i = 0; i < image.length; i++) {
                imageView = new ImageView(context);
                imageView.setLayoutParams(new LayoutParams(ConvertUtils.dip2px(context, 10), ConvertUtils.dip2px(context, 10)));
                imageView.setPadding(ConvertUtils.dip2px(context, 10), 0, ConvertUtils.dip2px(context, 10), 0);
                image[i] = imageView;
                if (i == 0) {
                    // 默认是第一张图片 小圆点是
                    image[i].setBackgroundResource(R.drawable.home_point);
                } else {
                    image[i].setBackgroundResource(R.drawable.home_point_s);
                }
                // 这里循环向LinearLayout中添加了view
                ly.addView(image[i]);
            }

            addView(ly);
        }

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                Log.i("1", "ACTION_MOVE");
                close();
                mAdPagerTimer = new Timer();
                mAdPagerTimer.schedule( new TimerTask() {
                    @Override
                    public void run() {
                        mAdPagerHander.sendEmptyMessage(0);
                    }
                }, 5000, 5000);
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
            case MotionEvent.ACTION_UP:
                Log.i("1", "ACTION_UP");

                break;
            case MotionEvent.ACTION_CANCEL:
                Log.i("1", "ACTION_CANCEL");

                break;
        }
        return super.onInterceptTouchEvent(event);
    }

    class AdPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return vList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView(vList.get(position));
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }

        @Override
        public Object instantiateItem(View container, int position) {
            ((ViewPager) container).addView(vList.get(position));
            return vList.get(position);
        }

    }

    /**
     * 运营区广告栏切换任务
     */
    private TimerTask mAdtask = new TimerTask() {
        @Override
        public void run() {
            mAdPagerHander.sendEmptyMessage(0);
        }
    };

    /**
     * 运营区广告栏切换UI更新
     */
    private Handler mAdPagerHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int size = mAdList.size();
            int currentItem = mBanner.getCurrentItem();
            if (currentItem < size) {
                mBanner.setCurrentItem(mBanner.getCurrentItem() + 1, true);
            } else {
                mBanner.setCurrentItem(1, true);
            }
        }
    };

    @Override
    public void onPageScrollStateChanged(int arg0) {
        if (ViewPager.SCROLL_STATE_IDLE == arg0) {
            if (mIsChanged) {
                mIsChanged = false;
                mBanner.setCurrentItem(mCurrentPagePosition, false);
            }
        }
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    private void setCurrentDot(int positon) {
        // 界面实际显示的序号是第1, 2, 3。而点的序号应该是0, 1, 2.所以减1.
        positon = positon - 1;
        if (positon < 0 || positon > vList.size() - 1 || mCurrentIndex == positon) {
            return;
        }

        for (int i = 0; i < image.length; i++) {
            image[positon].setBackgroundResource(R.drawable.home_point);

            if (positon != i) {
                image[i].setBackgroundResource(R.drawable.home_point_s);
            }
        }
        mCurrentIndex = positon;
    }

    @Override
    public void onPageSelected(int arg0) {
        mIsChanged = true;
        if (arg0 > POINT_LENGTH) {
            mCurrentPagePosition = FIRST_ITEM_INDEX;
        } else if (arg0 < FIRST_ITEM_INDEX) {
            mCurrentPagePosition = POINT_LENGTH;
        } else {
            mCurrentPagePosition = arg0;
        }

        setCurrentDot(mCurrentPagePosition);
    }
}
