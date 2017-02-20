package com.example.bgfvg.mydanmutest;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    public LinearLayout mDanmuView;
    public TextView mDanmuText;
    public ImageView mDammuImg;
    public RelativeLayout mTanmu_container;
    private int validHeightSpace;
    public int mFromX;
    public int mToX;
    public int mLeft;
    public int mRight;
    public int mPaddingLeft;
    public int mMMDanmuViewHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initView() {
        mTanmu_container = (RelativeLayout) findViewById(R.id.tanmu_container);
    }

    private void initData() {
        initContentList();
    }

    private void initContentList() {
        contentList = new ArrayList<>();
        if (contentList != null)
            for (int i = 0; i < 10; i++) {
                contentList.add("弹幕消息item " + i);
            }
    }


    public void startAnimator(View view) {
        getX();
        getX();
        mTanmu_container.removeAllViews();
        //startAnimator();
        startForAnimators();
    }

    private void startForAnimators() {
        new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    Log.e("qw", "MainActivity.run.i= "+i);
                    Message message = mHandler.obtainMessage();
                    message.what = i;
                    boolean b = mHandler.sendMessage(message);
                    Log.e("qw", "MainActivity.run发送消息结果= "+b);
                    SystemClock.sleep(5000);
                }
            }
        }.start();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.e("qw", "MainActivity.handleMessage.接收到的信息= " + msg.what);
            startAnimator(msg.what);
        }
    };

    public void startAnimator(int pos) {
        beginAnimator(pos);
    }

    private void beginAnimator(int pos) {
        initDanMuView(pos);
        setDanMuViewContent(pos);
        getDanMuViewHeight(pos);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ObjectAnimator animator = ObjectAnimator.ofFloat(mDanmuView, "translationX", mFromX, mToX);
                //自动计算时间
                long duration = (long) (Math.abs(mToX - mFromX) * 1.0f / ScreenUtils.getScreenW(getApplicationContext()) * 2000);
                animator.setDuration(duration);
                animator.start();
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                        Log.e("qw", "MainActivity.onAnimationStart.");

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        Log.e("qw", "MainActivity.onAnimationEnd.");
                        mTanmu_container.removeView(mDanmuView);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {
                        Log.e("qw", "MainActivity.onAnimationCancel.");

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {
                        Log.e("qw", "MainActivity.onAnimationRepeat.");

                    }
                });
                mTanmu_container.addView(mDanmuView);
            }
        });


    }

    private void initDanMuView(int pos) {
        Log.e("qw", "MainActivity.initDanMuView.pos= "+pos);
        mDanmuView = (LinearLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.danmu, null);
        mDanmuText = (TextView) mDanmuView.findViewById(R.id.textView);
        mDammuImg = (ImageView) mDanmuView.findViewById(R.id.imageView);
    }

    List<String> contentList;

    private void setDanMuViewContent(int pos) {
        Log.e("qw", "MainActivity.setDanMuViewContent.pos= "+pos);
        mDanmuText.setText(contentList.get(pos));
    }

    private void getDanMuViewHeight(int pos) {
        Log.e("qw", "MainActivity.getDanMuViewHeight.pos= "+pos);
        mDanmuView.measure(0, 0);
        mMMDanmuViewHeight = mDanmuView.getMeasuredHeight();
        getRandomTopMargin();
    }

    private void getX() {
        mTanmu_container.measure(0, 0);
        mLeft = mTanmu_container.getLeft();
        mRight = mTanmu_container.getRight();
        mPaddingLeft = mTanmu_container.getPaddingLeft();
        mFromX = mRight - mLeft - mPaddingLeft;
        mToX = -ScreenUtils.getScreenW(getApplicationContext());
    }

    private int getRandomTopMargin() {
        //计算用于弹幕显示的空间高度
        if (validHeightSpace == 0) {
            int top = mTanmu_container.getTop();
            int bottom = mTanmu_container.getBottom();
            int paddingTop = mTanmu_container.getPaddingTop();
            int paddingBottom = mTanmu_container.getPaddingBottom();
            validHeightSpace = bottom - top - paddingTop - paddingBottom;
        }

        //计算可用的行数
        if (linesCount == 0) {
            linesCount = validHeightSpace / mMMDanmuViewHeight;
            if (linesCount == 0) {
                throw new RuntimeException("Not enough space to show text.");
            }
        }

        //检查重叠
        while (true) {
            int randomIndex = (int) (Math.random() * linesCount);
            int marginValue = randomIndex * (validHeightSpace / linesCount);

            if (!existMarginValues.contains(marginValue)) {
                existMarginValues.add(marginValue);
                return marginValue;
            }
        }
    }

    //记录当前仍在显示状态的弹幕的位置（避免重复）
    private Set<Integer> existMarginValues = new HashSet<>();
    private int linesCount;
    private int validWidthSpace;

    private int getRandomTLeftMargin() {
        int marginValue = 0;
        //计算可用于弹幕显示的控件宽度
        if (validWidthSpace == 0) {
            validWidthSpace = mTanmu_container.getRight() - mTanmu_container.getLeft() - mTanmu_container.getPaddingLeft() - mTanmu_container.getPaddingRight();
        }

        //检查重叠
        while (true) {
            marginValue = (int) (Math.random() * validWidthSpace / 5);
            if (!existMarginValues.contains(marginValue)) {
                existMarginValues.add(marginValue);
                return marginValue;
            }
        }
    }


}
