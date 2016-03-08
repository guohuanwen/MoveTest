package com.bcgtgjyb.autolistview;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView mListView;
    private TextView mTextView;
    private List<String> textList;
    private Button mButton;
    private LinearLayout mLinearLayout;
    private TextView head;
    private String TAG=MainActivity.class.getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.activity_main);
        setContentView(new LearnScroller(this));

    }

    private void init(){
        mListView=(ListView)findViewById(R.id.listView);
        mTextView=(TextView)findViewById(R.id.textView);
        mButton=(Button)findViewById(R.id.button);
        mLinearLayout=(LinearLayout)findViewById(R.id.linear);


        textList=new ArrayList<String>();
        for(int i=0;i<100;i++){
            textList.add("这是第"+i+"条数据");
        }



        head=new TextView(this);
        head.setSingleLine(false);
        head.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
        head.setText("this is my header /n  this is my header /n this is my header  /n " +
                "this is my header  /n this is my header /n  this is my header /n  this is my header" +
                "this is my header this is my header this is my header" +
                "this is my header this is my header this is my header this is my header");

        mListView.addHeaderView(head);
        mListView.setAdapter(new MyListAdapter(this, textList));



        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick:");
//                startActivity(new Intent(MainActivity.this,LearnValueAnimator.class));
            }
        });

        setTouch();
    }

    private float lastY;
    private float firstTouch;
    private boolean fisrtMove;
    private void setTouch(){
        mButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.i(TAG, "onTouch ACTION_DOWN");
                        lastY = (int) event.getRawY();
                        firstTouch = event.getRawY();
                        fisrtMove = true;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.i(TAG, "onTouch ACTION_MOVE");
                        int y = (int) event.getRawY();
                        float moveY = y - lastY;
                        lastY = y;
//                        if(y<firstTouch){
                        update(moveY);
//                        }
                        fisrtMove = false;
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.i(TAG, "onTouch ACTION_UP");


                        break;
                }
                return false;
            }
        });
}



    private void update(float moveY) {
//        ViewGroup.LayoutParams drawParam = drawView.paletteLay.getLayoutParams();
        //到达最顶端
//        if(drawParam.height+(int)moveY<0){
//            drawParam.height=0;
//            drawView.paletteLay.setLayoutParams(drawParam);
//            return;
//        }
        //最底端
//        else if(drawParam.height+(int)moveY>drawHeight){
//            drawParam.height=drawHeight;
//            drawView.paletteLay.setLayoutParams(drawParam);
//            return;
//        }
        //普通滑动
//        drawParam.height=drawHeight+(int)moveY;
//        drawView.paletteLay.setLayoutParams(drawParam);


        LinearLayout.LayoutParams drawParam2 = new LinearLayout.LayoutParams(mLinearLayout.getMeasuredWidth(), mLinearLayout.getMeasuredHeight());
        int topMargin2 = mLinearLayout.getMeasuredHeight() + (int) moveY;
        drawParam2.height = topMargin2;
        mLinearLayout.setLayoutParams(drawParam2);
    }



    private int headerHeight;

    private void setHeaderHeight(View view){
        headerHeight=head.getMeasuredHeight();
        int a=headerHeight-5;
        if(a>0) {
            head.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, headerHeight - 5));
        }else {
            mListView.removeHeaderView(head);
        }

//        head.scrollBy(0,10);
    }

    private int helloHeight;

    private void setHelloHeight(View view){
        helloHeight=mLinearLayout.getMeasuredHeight();
        int a=helloHeight-5;
        if(a>0) {
            mLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,a ));
        }else {
        }

    }







    private class MyListAdapter extends BaseAdapter{
        private List<String> list;
        private TextView textView;
        private Context context;

        public MyListAdapter(Context c,List l) {
            this.context=c;
            this.list=l;
        }

        @Override
        public int getCount() {
            return textList.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(view==null){
                view=new TextView(context);
            }
            if(view instanceof TextView) {
                ((TextView) view).setText((String) list.get(i));
            }
            return view;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
