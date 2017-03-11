package com.yyqdata.jbtest.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.LayoutDirection;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yyqdata.jbtest.R;

public class BaseActivity extends AppCompatActivity {

    private ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onStart() {
        super.onStart();
        initView();
    }

    private void initView() {
        RelativeLayout relativeLayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                dp2px(getApplicationContext(),40));
        params.setMargins(0,0,0,0);
        relativeLayout.setLayoutParams(params);
        relativeLayout.setBackgroundColor(Color.parseColor("#c2baba"));
        //添加回退按钮
        ImageButton imageButton = new ImageButton(this);
        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(dp2px(getApplicationContext(),15),
                dp2px(getApplicationContext(),20));
        params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params1.addRule(RelativeLayout.CENTER_VERTICAL);
        params1.setMargins(10,0,0,0);
        imageButton.setLayoutParams(params1);
        imageButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.lefticon));
        relativeLayout.addView(imageButton);
        //添加标题
        TextView textView = new TextView(this);
        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                dp2px(getApplicationContext(),25));
        params2.addRule(RelativeLayout.CENTER_IN_PARENT);
        textView.setText("移动作业平台");
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setLayoutParams(params2);
        relativeLayout.addView(textView);
        //添加信息
        ImageButton imageButton2 = new ImageButton(this);
        RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(dp2px(getApplicationContext(),20),
                dp2px(getApplicationContext(),20));
        params3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params3.addRule(RelativeLayout.CENTER_VERTICAL);
        params3.setMargins(0,0,10,0);
        imageButton2.setLayoutParams(params3);
        imageButton2.setBackgroundDrawable(getResources().getDrawable(R.drawable.information));
        relativeLayout.addView(imageButton2);
        //添加actionbar
        addContentView(relativeLayout,params);
        rightAnima(textView);
        rightAnima(imageButton);
        rightAnima(imageButton2);
    }

    private void rightAnima(View view){
        AnimationSet animationSet = new AnimationSet(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setDuration(300);
        alphaAnimation.setFillAfter(true);
        TranslateAnimation translateAnimation = new TranslateAnimation(30,0,0,0);
        translateAnimation.setDuration(300);
        translateAnimation.setFillAfter(true);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(translateAnimation);
        view.startAnimation(animationSet);
    }


    /**
     * dp转换成px
     * @param context
     * @param i
     * @return
     */
    private int dp2px(Context context,int i) {
        final float density = context.getResources().getDisplayMetrics().density;
        return (int) (i*density+0.5f);
    }

    /**
     * px转换dp
     * @param context
     * @param i
     * @return
     */
    private int px2dp(Context context,int i) {
        final float density = context.getResources().getDisplayMetrics().density;
        return (int) (i*density+0.5f);
    }
}
