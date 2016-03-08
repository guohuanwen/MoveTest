package com.bcgtgjyb.autolistview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
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
        scroller = new Scroller(context);

        if (mVelocityTracker == null){
            mVelocityTracker = VelocityTracker.obtain();
        }
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
}
