package com.yyqdata.myyouku;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.LoginFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.yyqdata.myyouku.fragment.ChannelFragment;
import com.yyqdata.myyouku.fragment.MineFragment;
import com.yyqdata.myyouku.fragment.SubscribeFragment;
import com.yyqdata.myyouku.fragment.VIPFragment;
import com.yyqdata.myyouku.fragment.WelcomeFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LinearLayout fragLinear;
    private RadioGroup mainRg;
    private RadioButton[] radioButtons;
    private List<Fragment> fragmentList = new ArrayList<>();
    private int curIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化fragment
        initData();
        //初始化视图
        initView();
        //设置监听
        initListener();
    }

    private void initListener() {
        mainRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switchFragment(checkedId);
                }
        });
    }

    private void switchFragment(int position) {
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        Fragment fragment=fragmentList.get(position);
        if(!fragment.isAdded()){
            transaction.hide(fragmentList.get(curIndex)).add(R.id.fragment_ll,fragment);
        }else{
            transaction.hide(fragmentList.get(curIndex)).show(fragment);
        }
        transaction.commit();
        curIndex=position;
    }

    private void initData() {
        Fragment fragment1=new WelcomeFragment();
        Fragment fragment2=new ChannelFragment();
        Fragment fragment3=new SubscribeFragment();
        Fragment fragment4=new VIPFragment();
        Fragment fragment5=new MineFragment();
        fragmentList.add(fragment1);
        fragmentList.add(fragment2);
        fragmentList.add(fragment3);
        fragmentList.add(fragment4);
        fragmentList.add(fragment5);
    }

    private void initView() {
        fragLinear = (LinearLayout) findViewById(R.id.fragment_ll);
        mainRg = (RadioGroup) findViewById(R.id.main_bottom_rg);
        radioButtons=new RadioButton[mainRg.getChildCount()];
        for(int i=0;i<radioButtons.length;i++){
            radioButtons[i]= (RadioButton) mainRg.getChildAt(i);
            radioButtons[i].setId(i);
        }

        //设置默认button
        selectRadioButton(0);
        //设置默认Fragment
        setdefaultFragment();
    }

    private void setdefaultFragment() {
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        transaction.add(R.id.fragment_ll,fragmentList.get(0));
        transaction.commit();
        curIndex=0;
    }

    private void selectRadioButton(int positoin) {
        for(int i = 0;i<radioButtons.length;i++){
            if(i== positoin){
                radioButtons[i].setChecked(true);
            }else{
                radioButtons[i].setChecked(false);
            }
        }
    }

    //    设置返回键 退出的效果
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            dialog();
        }
        return super.onKeyDown(keyCode, event);
    }

    //创建对话框方法dialog()
    private void dialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("真的要退出吗?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }
}
