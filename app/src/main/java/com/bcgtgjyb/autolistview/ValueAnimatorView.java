package com.bcgtgjyb.autolistview;

import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;

import com.nineoldandroids.animation.TypeEvaluator;
import com.nineoldandroids.animation.ValueAnimator;


/**
 * Created by bigwen on 2015/12/5.
 */
public class ValueAnimatorView extends LinearLayout {
    private Context mContext;
    private Button mButton;private Handler handler;

    public ValueAnimatorView(Context context) {
        super(context, null);
        mContext = context;
        init();
    }

    public ValueAnimatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        LayoutInflater.from(mContext).inflate(R.layout.value_animator_view, this);
        handler=new Handler(Looper.getMainLooper());
        mButton = (Button) findViewById(R.id.button);
        initMove();
    }

    private void initMove(){
        final ValueAnimator valueAnimator=setMove(100, 100);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setMove(-100,-100);
            }
        },valueAnimator.getDuration());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initMove();
            }
        }, valueAnimator.getDuration()*2);
    }

    private ValueAnimator setMove(int x, int y){
        int startX = (int) mButton.getX();
        int startY = (int) mButton.getY();
        Point startPoint = new Point((int) mButton.getX(), (int) mButton.getY());
        Point endPoint = new Point(startX + x, startY + y);
        final ValueAnimator valueAnimator = ValueAnimator.ofObject(new PointValueEvaluator(), startPoint, endPoint);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Point point = (Point) valueAnimator.getAnimatedValue();
                mButton.setX(point.x);
                mButton.setY(point.y);
                invalidate();
            }
        });
        valueAnimator.setDuration(2000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.start();
        return valueAnimator;
    }


    public class PointValueEvaluator implements TypeEvaluator {


        @Override
        public Object evaluate(float v, Object o, Object t1) {
            float startX = ((Point) o).x;
            float startY=((Point)o).y;
            float endX=((Point)t1).x;
            float endY=((Point)t1).y;
            Point point=new Point();
            point.x=(int)(startX+v*(endX-startX));
            point.y=(int)(startY+v*(endY-startY));
            return point;
        }

    }


}
