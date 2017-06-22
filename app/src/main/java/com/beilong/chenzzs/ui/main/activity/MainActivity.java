package com.beilong.chenzzs.ui.main.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.beilong.chenzzs.ui.about.activity.About;
import com.beilong.chenzzs.ui.main.contract.SchoolTimeTableContract;
import com.beilong.chenzzs.ui.main.fragment.SchoolTimeTable;
import com.beilong.chenzzs.ui.query.activity.QueryMark;
import com.beilong.chenzzs.ui.select.activity.SelectClass;
import com.jaeger.library.StatusBarUtil;

import butterknife.InjectView;
import czzs.beilong.common.base.BaseActivity;
import com.beilong.chenzzs.R;
import czzs.beilong.common.base.BaseModel;
import czzs.beilong.common.base.BasePresenter;
import czzs.beilong.common.commonutils.RxDrawer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class MainActivity extends BaseActivity<BasePresenter,BaseModel>{

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.nav_view)
    NavigationView navView;
    @InjectView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @InjectView(R.id.framelayout)
    FrameLayout framelayout;

    SchoolTimeTable schoolTimeTable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFragment(savedInstanceState);
    }

    /**
     * 初始化Fragment
     * @param savedInstanceState
     */
    private void initFragment(Bundle savedInstanceState){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(savedInstanceState == null){
            schoolTimeTable = new SchoolTimeTable();
            transaction.add(R.id.framelayout,schoolTimeTable,"schoolTimeTable");
        }else {
            schoolTimeTable = (SchoolTimeTable) getSupportFragmentManager().findFragmentByTag("schoolTimeTable");
        }
        transaction.show(schoolTimeTable);
        transaction.commit();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initPresenter() {}

    @Override
    public void setStatusBarColor() {
        StatusBarUtil.setColorForDrawerLayout(this,drawerLayout,getResources().getColor(R.color.colorPrimary),0);
    }

    /**
     * 设置Toolbar
     */
    private void setToolbar(String msg){
        getSupportActionBar().setTitle(msg);
    }

    @Override
    public void initView() {
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        initToolbar();

        initNavigation();
    }

    private void initToolbar(){
        mRxManager.on(SchoolTimeTableContract.TOOLBAR_TO_CHANGE, new Action1<String>() {
            @Override
            public void call(String string) {
                setToolbar(string);
            }
        });
    }

    private void initNavigation(){
        NavigationView.OnNavigationItemSelectedListener listener = new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                final int id = item.getItemId();
                Action1<Void> action1 = new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        switch (id){
                            //选择课表
                            case R.id.nav_select_class:
                                SelectClass.startAction(mContext);
                                break;
                            //查询成绩
                            case R.id.nav_query_mark:
                                QueryMark.startAction(mContext);
                                break;
                            //关于
                            case R.id.nav_about:
                                About.startAction(mContext);
                                break;
                        }
                    }
                };
                //解决切换时的卡顿
                RxDrawer.close(drawerLayout)
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(action1);

                item.setCheckable(false);   //抹去选择状态
                return true;
            }
        };
        navView.setNavigationItemSelectedListener(listener);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
