package com.btime.imdemo;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.widget.FloatActionButton;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.btime.common.app.Activity;
import com.btime.common.widget.PortraitView;
import com.btime.imdemo.frags.main.ActiveFragment;
import com.btime.imdemo.frags.main.ContactFragment;
import com.btime.imdemo.frags.main.GroupFragment;
import com.btime.imdemo.helper.NavHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends Activity implements BottomNavigationView.OnNavigationItemSelectedListener,
        NavHelper.OnTabChangedListener<Integer>{
    @BindView(R.id.appbar)
    View mLayAppbar;

    @BindView(R.id.im_portrait)
    PortraitView mPortrait;

    @BindView(R.id.txt_title)
    TextView mTitle;

    @BindView(R.id.lay_container)
    FrameLayout mContainer;

    @BindView(R.id.navigation)
    BottomNavigationView mNavigation;

    @BindView(R.id.btn_action)
    FloatActionButton mAction;

    private NavHelper<Integer> mNavHelper;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        //初始化底部辅助工具类
        mNavHelper = new NavHelper<>(this,R.id.lay_container,getSupportFragmentManager(),this);
        mNavHelper.add(R.id.action_home,new NavHelper.Tab<Integer>(ActiveFragment.class,R.string.title_home))
                .add(R.id.action_group,new NavHelper.Tab<Integer>(GroupFragment.class,R.string.title_group))
                .add(R.id.action_contact,new NavHelper.Tab<Integer>(ContactFragment.class,R.string.title_contact));

        //添加对底部按钮点击的监听
        mNavigation.setOnNavigationItemSelectedListener(this);

        Glide.with(this)
                .load(R.drawable.bg_src_morning)
                .centerCrop()
                .into(new ViewTarget<View, GlideDrawable>(mLayAppbar) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        this.view.setBackground(resource.getCurrent());
                    }
                });
    }

    @Override
    protected void initData() {
        super.initData();

        //从底部导中接管我们的Menu，然后进行手动的触发第一次点击
        Menu menu = mNavigation.getMenu();
        //触发首次选中Home
        menu.performIdentifierAction(R.id.action_home,0);
    }

    @OnClick(R.id.im_search)
    void onSearchMenuClick() {

    }

    @OnClick(R.id.btn_action)
    void onActionClick() {

    }

    /**
     * 当底部导航被点击时触发
     * @param item MenuItem
     * @return True代表能处理这个点击
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //转接事件流到工具类
        return mNavHelper.performClickMenu(item.getItemId());
    }

    @Override
    public void onTabChanged(NavHelper.Tab<Integer> newTab, NavHelper.Tab<Integer> oldTab) {
        //从额外字段中取出Title的资源id
        mTitle.setText(newTab.extra);

        //对浮动按钮进行隐藏与显示的动画
        float transY = 0;
        float rotation = 0;
        if(Objects.equals(newTab.extra,R.string.title_home)){
            //主界面时隐藏
            transY = Ui.dipToPx(getResources(),76);
        }else {
            //transY默认为0 则显示
            if(Objects.equals(newTab.extra,R.string.title_group)){
                //群
                mAction.setImageResource(R.drawable.ic_group_add);
                rotation = -360;
            }else {
                //联系人
                mAction.setImageResource(R.drawable.ic_contact_add);
                rotation = 360;
            }
        }

        //开始动画
        //旋转，Y轴位移，弹性差值器，时间
        mAction.animate()
                .rotation(rotation)
                .translationY(transY)
                .setInterpolator(new AnticipateOvershootInterpolator(1))
                .setDuration(480)
                .start();

    }
}
