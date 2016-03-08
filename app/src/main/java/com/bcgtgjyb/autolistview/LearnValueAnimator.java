package com.bcgtgjyb.autolistview;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by bigwen on 2015/12/5.
 */
public class LearnValueAnimator extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new ValueAnimatorView(this));
    }
}
