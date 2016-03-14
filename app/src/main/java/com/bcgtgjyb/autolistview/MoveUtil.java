package com.bcgtgjyb.autolistview;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

/**
 * Created by bigwen on 2016/3/14.
 */
public class MoveUtil {

    public static AnimatorSet move(View view,float x,float y,int duration){
        AnimatorSet animatorSet = new AnimatorSet();
        PropertyValuesHolder p1=PropertyValuesHolder.ofFloat("translationX", (float) x);
        PropertyValuesHolder p2= PropertyValuesHolder.ofFloat("translationY", (float) y);
        animatorSet.play(ObjectAnimator.ofPropertyValuesHolder(view, p1, p2));
        //setDuration在play后面设置
        animatorSet.setDuration(duration);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        return animatorSet;
    }

    public static AnimatorSet setRotationX(View view,float x,int duration,Interpolator interpolator){
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.play(
                ObjectAnimator.ofFloat(view, "rotationX", x)
        );
        animatorSet.setDuration(duration);
        animatorSet.setInterpolator(interpolator);
        return  animatorSet;
    }

    public static AnimatorSet setRotationY(View view,float x,int duration){
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(
                ObjectAnimator.ofFloat(view, "rotationY", x)
        );
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.setDuration(duration);
        return  animatorSet;
    }

    public static Rotate3dAnimation rotation(View view,float startAngle,float endAngle, int duration){
        Rotate3dAnimation rotate3dAnimation = new Rotate3dAnimation(startAngle,endAngle,(view.getX()+view.getWidth())/2,(view.getY()+view.getHeight())/2,0,true);
        rotate3dAnimation.setDuration(duration);
        rotate3dAnimation.setFillAfter(true);
        rotate3dAnimation.setInterpolator(new LinearInterpolator());
        return rotate3dAnimation;
    }


    public static class Rotate3dAnimation extends Animation {
        private final float mFromDegrees;
        private final float mToDegrees;
        private final float mCenterX;
        private final float mCenterY;
        private final float mDepthZ;
        private final boolean mReverse;
        private Camera mCamera;

        /**
         * Creates a new 3D rotation on the Y axis. The rotation is defined by its
         * start angle and its end angle. Both angles are in degrees. The rotation
         * is performed around a center point on the 2D space, definied by a pair
         * of X and Y coordinates, called centerX and centerY. When the animation
         * starts, a translation on the Z axis (depth) is performed. The length
         * of the translation can be specified, as well as whether the translation
         * should be reversed in time.
         *
         * @param fromDegrees the start angle of the 3D rotation
         * @param toDegrees the end angle of the 3D rotation
         * @param centerX the X center of the 3D rotation
         * @param centerY the Y center of the 3D rotation
         * @param reverse true if the translation should be reversed, false otherwise
         */
        public Rotate3dAnimation(float fromDegrees, float toDegrees,
                                 float centerX, float centerY, float depthZ, boolean reverse) {
            mFromDegrees = fromDegrees;
            mToDegrees = toDegrees;
            mCenterX = centerX;
            mCenterY = centerY;
            mDepthZ = depthZ;
            mReverse = reverse;
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            mCamera = new Camera();
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            final float fromDegrees = mFromDegrees;
            float degrees = fromDegrees + ((mToDegrees - fromDegrees) * interpolatedTime);

            final float centerX = mCenterX;
            final float centerY = mCenterY;
            final Camera camera = mCamera;

            final Matrix matrix = t.getMatrix();

            camera.save();
            if (mReverse) {
                camera.translate(0.0f, 0.0f, mDepthZ * interpolatedTime);
            } else {
                camera.translate(0.0f, 0.0f, mDepthZ * (1.0f - interpolatedTime));
            }
            camera.rotateY(degrees);
            camera.getMatrix(matrix);
            camera.restore();

            matrix.preTranslate(-centerX, -centerY);
            matrix.postTranslate(centerX, centerY);
        }
    }
}
