package com.bcgtgjyb.autolistview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.Scroller;

/**
 * Created by bigwen on 2016/3/14.
 */
public class ImageMove extends ImageView {

    private Context mContext;
    private Scroller scroller;

    public ImageMove(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public ImageMove(Context context) {
        super(context);
        mContext = context;
        init();
    }

    private void init() {
        scroller = new Scroller(mContext);
    }

    public void smoothScrollBy(float x,float y){
        scroller.startScroll(getScrollX(),getScrollY(),(int)x,(int)y,1000);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()){
            scrollTo(scroller.getCurrX(),scroller.getCurrY());
            postInvalidate();
        }
    }
}
