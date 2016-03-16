package com.bcgtgjyb.autolistview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;

/**
 * Created by bigwen on 2016/3/14.
 */
public class WaveView extends View {

    private String TAG = WaveView.class.getName();
    private Context mContext;
    private Scroller scroller;

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public WaveView(Context context) {
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

    private Path clipPath = new Path();
    private ValueAnimator clipVA;
    private void initPath(){
        Log.i(TAG, "initPath: ");
        clipVA = ValueAnimator.ofInt(0,200);
        clipVA.setInterpolator(new LinearInterpolator());
        clipVA.setDuration(2000);
        clipVA.start();
        invalidate();
    }

    private Bitmap bitmap;
    private void initBitmap(){
        bitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.ic_launcher);
    }

    private boolean finshed = false;
    private boolean init = false;
    private Paint p;
    @Override
    protected void onDraw(Canvas canvas) {
        Log.i(TAG, "onDraw: ");
        if (!init){
            Log.i(TAG, "onDraw: init");
            initPath();
            p = new Paint();
            init = true;
            initBitmap();
        }
        if (clipVA != null && clipVA.isRunning()){
            clipPath.reset();
            int param = (Integer)clipVA.getAnimatedValue();
            clipPath.addCircle(getWidth() / 2, getHeight() / 2, param, Path.Direction.CW);
            Log.i(TAG, "onDraw: run=" + param + "   getWidth=" + getWidth());
            canvas.clipPath(clipPath);
            canvas.drawBitmap(bitmap,0,0,p);
//            super.onDraw(canvas);
            invalidate();
        }else {
            Log.i(TAG, "onDraw: finish");
//            super.onDraw(canvas);
            canvas.drawBitmap(bitmap,0,0,p);
            if (!finshed) {
                invalidate();
                finshed = true;
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        Log.i(TAG, "draw: ");
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        Log.i(TAG, "dispatchDraw: ");
    }


}
