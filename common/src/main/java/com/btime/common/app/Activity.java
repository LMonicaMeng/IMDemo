package com.btime.common.app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import butterknife.ButterKnife;

public abstract class Activity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在界面初始化之前调用的初始化窗口
        initWindow();

        if(initArgs(getIntent().getExtras())){
            getContentLayoutId();
            initWidget();
            initData();
        }else {
            finish();
        }
    }

    /**
     * 初始化窗口
     */
    protected void initWindow(){

    }

    /**
     * 初始化相关参数
     * @param bundle 参数Bundle
     * @return 如果参数正确放回True，错误返回false
     */
    protected boolean initArgs(Bundle bundle){
        return true;
    }

    /**
     * 得到当前界面的资源文件
     * @return 资源文件id
     */
    protected abstract int getContentLayoutId();

    /**
     * 初始化控件
     */
    protected void initWidget(){
        ButterKnife.bind(this);
    }

    /**
     * 初始化数据
     */
    protected void initData(){

    }

    @Override
    public boolean onSupportNavigateUp() {
        //当点击页面导航返回时，finish当前界面
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {

        @SuppressLint("RestrictedApi")
        List<Fragment> fragments =  getSupportFragmentManager().getFragments();
        //判断是否为空
        if(fragments !=null&& fragments.size()>0){
            for (Fragment fragment : fragments) {
                //判断是否为我们能处理的Fragment类型
                if(fragment instanceof com.btime.common.app.Fragment){
                    //判断是否拦截了返回按钮
                    if(((com.btime.common.app.Fragment) fragment).onBackPress()){
                        //如果有直接return
                        return;
                    }
                }
            }
        }
        super.onBackPressed();
        finish();
    }
}
