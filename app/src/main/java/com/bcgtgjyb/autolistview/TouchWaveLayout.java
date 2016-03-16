package com.bcgtgjyb.autolistview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bigwen on 2016/3/16.
 */
public class TouchWaveLayout extends LinearLayout {

    private Context mContext;
    private String TAG = TouchWaveLayout.class.getName();

    public TouchWaveLayout(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public TouchWaveLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.BLUE);
    }

    private List<Rect> rects = new ArrayList<>();
    private float minLeft = 10000;
    private float maxRight = 0;
    private float minTop = 10000;
    private float maxBottom = 0;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int count = getChildCount();
        View view = null;
        Rect rect = null;
        for (int i = 0; i < count; i++) {
            view = getChildAt(i);
            rect = new Rect((int) view.getX(), (int) view.getY(), (int) view.getX() + view.getWidth(), (int) view.getY() + view.getHeight());
            Log.i(TAG, "onLayout: " + rect.left + "  " + rect.top + "  " + rect.right + "  " + rect.bottom);
            if (minLeft > view.getLeft()) {
                minLeft = view.getLeft();
            }
            if (maxRight < view.getRight()) {
                maxRight = view.getRight();
            }
            if (minTop > view.getTop()) {
                minTop = view.getTop();
            }
            if (maxBottom < view.getBottom()) {
                maxBottom = view.getBottom();
            }
            rects.add(rect);
        }
    }

    private ValueAnimator valueAnimator;
    private boolean isLoading = false;
    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        Log.i(TAG, "drawChild: ");
        if (rect != null && !isLoading) {
            Log.i(TAG, "onDraw: ");
            View view = getChildAt(chilePosition);
            int w = view.getWidth();
            int h = view.getHeight();
            int R =  w > h ? w : h;
            valueAnimator = ValueAnimator.ofInt(0,R);
            valueAnimator.setInterpolator(new AccelerateInterpolator());
            valueAnimator.start();
            isLoading = true;
        }
        if (isLoading && valueAnimator != null){
            if (valueAnimator.isRunning()) {
                int r = (Integer) valueAnimator.getAnimatedValue();
                canvas.clipRect(rect);
                canvas.drawCircle(touchX, touchY, r, paint);
                invalidate();
            }else {
                isLoading = false;
                valueAnimator = null;
                rect = null;
                invalidate();
            }
        }

        return super.drawChild(canvas, child, drawingTime);
    }

    private Paint paint;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    private Rect rect = null;
    private int chilePosition;
    private float touchX;
    private float touchY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                float x = event.getX();
                event.getRawY();
                float y = event.getY();
                Log.i(TAG, "dispatchTouchEvent: " + minLeft + "  " + minTop + "   " + maxRight + "   " + maxBottom);
                if (x < minLeft || y < minTop || x > maxRight || y > maxBottom) {
                    break;
                }
                for (int i = 0; i < rects.size(); i++) {
                    Log.i(TAG, "dispatchTouchEvent: " + x + "  " + y);
                    Rect rect = rects.get(i);
                    if (x > rect.left && x < rect.right && y > rect.top && y < rect.bottom) {
                        Log.i(TAG, "dispatchTouchEvent: invalidate");
                        this.rect = rect;
                        this.touchX = x;
                        this.touchY = y;
                        chilePosition = i;
                        invalidate(rect);
                        break;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.dispatchTouchEvent(event);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return super.onTouchEvent(event);
    }
}
