package com.bcgtgjyb.autolistview;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Created by bigwen on 2016/3/8.
 */
public class LearnScroller extends LinearLayout{

    private Context context;
    private Scroller scroller;
    private Button button;
    private String TAG = LearnScroller.class.getName();
    private VelocityTracker mVelocityTracker;
    private WaveView waveView;
    private WaveView scaleView;

    public LearnScroller(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public LearnScroller(Context context) {
        super(context);
        this.context = context;
        init();
    }

    private void init() {
        LinearLayout.inflate(context, R.layout.learn_scroller, this);
        button = (Button)findViewById(R.id.button);
        waveView = (WaveView) findViewById(R.id.imagemove);
        scaleView = (WaveView) findViewById(R.id.imagescale);
        scroller = new Scroller(context);

        if (mVelocityTracker == null){
            mVelocityTracker = VelocityTracker.obtain();
        }
        post(new Runnable() {
            @Override
            public void run() {
//                MoveUtil.Rotate3dAnimation rotate3dAnimation = MoveUtil.rotation(imageMove, 0, 180, 1000);
//                imageMove.setAnimation(rotate3dAnimation);
                AnimatorSet animatorSet = MoveUtil.setRotationY(waveView, 90, 500);
                animatorSet.start();
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        waveView.setBackgroundResource(R.color.ddd);
                        MoveUtil.setRotationY(waveView, 180, 500).start();
                    }
                }, animatorSet.getDuration());
            }
        });

        postDelayed(new Runnable() {
            @Override
            public void run() {
//                imageMove.smoothScrollBy(-100,-100);
                AnimatorSet animatorSet = MoveUtil.move(waveView, 100, 100, 1000);
                animatorSet.start();
            }
        }, 2000);

        initPath();
    }


    private void smoothScrollBy(float dx,float dy){
        //参数解释：1、x轴起始位置；2、y轴起始位置；3、x轴的偏移量；4、y轴的偏移量；5、完成这个滑动需要的时间
        scroller.startScroll(getScrollX(), getScrollY(), (int)dx, (int)dy, 500);
        invalidate();
    }
    private void smoothScrollBy(float dx,float dy,int time){
        //参数解释：1、x轴起始位置；2、y轴起始位置；3、x轴的偏移量；4、y轴的偏移量；5、完成这个滑动需要的时间
        scroller.startScroll(getScrollX(), getScrollY(), (int)dx, (int)dy, time);
        invalidate();
    }


    @Override
    public void computeScroll() {
        //scroller是否结束
        if (scroller.computeScrollOffset()){
            scrollTo(scroller.getCurrX(),scroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    private float downX;
    private float downY;
    private float lastX = 0;
    private float lastY = 0;

    private float xV;
    private float yV;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mVelocityTracker.addMovement(event);
        int action = event.getAction();
        float x = event.getX();
        float y= event.getY();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "onTouchEvent: ACTION_DOWN x= "+x+";  y="+y);
                downX = x;
                downY = y;
                lastY = downY;
                lastX = downX;
                return true;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "onTouchEvent: ACTION_MOVE  x= " + (x - downX) + ";  y= " + (y - downY));
//                smoothScrollBy(-x+downX,-y+downY);
                scrollBy((int)(-x+lastX),(int)(-y+lastY));
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "onTouchEvent: ACTION_UP");
                //计算100毫秒运动像素
                mVelocityTracker.computeCurrentVelocity(200);
                //获取速度
                xV = mVelocityTracker.getXVelocity();
                yV = mVelocityTracker.getYVelocity();
                smoothScrollBy(-xV,-yV,1000);
                Log.i(TAG, "onTouchEvent: vv  "+xV+"  "+yV );
                break;
        }
        return super.onTouchEvent(event);
    }

    private Path clipPath = new Path();
    private ValueAnimator clipVA;
    private void initPath(){
        Log.i(TAG, "initPath: ");
        clipVA = ValueAnimator.ofInt(0,1500);
        clipVA.setInterpolator(new LinearInterpolator());
        clipVA.setDuration(1200);
        clipVA.start();
        invalidate();
    }



    private boolean finshed = false;
    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        boolean b;
        if (clipVA != null && clipVA.isRunning()){
            int param = (Integer)clipVA.getAnimatedValue();
            clipPath.reset();
            clipPath.addCircle(getWidth()/2, getHeight()/2, param, Path.Direction.CW);
            Log.i(TAG, "onDraw: run=" + param + "   getWidth=" + getWidth());
            canvas.clipPath(clipPath);
            b = super.drawChild(canvas, child, drawingTime);
            invalidate();
        }else {
            Log.i(TAG, "onDraw: finish");
            b = super.drawChild(canvas, child, drawingTime);
            if (!finshed) {
                invalidate();
                finshed = true;
            }
        }
        return b;
    }
}
