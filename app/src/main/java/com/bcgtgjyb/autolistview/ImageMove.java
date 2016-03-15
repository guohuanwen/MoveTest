package com.bcgtgjyb.autolistview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.Scroller;

/**
 * Created by bigwen on 2016/3/14.
 */
public class ImageMove extends ImageView {

    private String TAG = ImageMove.class.getName();
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
        initPath();
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

    private Path clipPath = new Path();
    private ValueAnimator clipVA;
    private void initPath(){
        Log.i(TAG, "initPath: ");
        clipVA = ValueAnimator.ofInt(0,1000);
        clipVA.setInterpolator(new LinearInterpolator());
        clipVA.setDuration(2000);
        clipVA.start();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (clipVA != null && clipVA.isRunning()){
            clipPath.reset();
            int param = (Integer)clipVA.getAnimatedValue();
            clipPath.addCircle(getWidth() / 2, getHeight() / 2, param, Path.Direction.CW);
            Log.i(TAG, "onDraw: run=" + param + "   getWidth=" + getWidth());
            canvas.clipPath(clipPath);
            super.onDraw(canvas);
            invalidate();
        }else {
            Log.i(TAG, "onDraw: finish");
            super.onDraw(canvas);
        }
    }
}
