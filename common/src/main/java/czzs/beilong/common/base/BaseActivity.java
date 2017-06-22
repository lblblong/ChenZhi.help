package czzs.beilong.common.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.jaeger.library.StatusBarUtil;

import butterknife.ButterKnife;
import czzs.beilong.common.R;
import czzs.beilong.common.baseapp.AppManager;
import czzs.beilong.common.baserx.RxManager;
import czzs.beilong.common.commonutils.TUtil;
import czzs.beilong.common.commonutils.ToastUtil;

/**
 * Created by LBL on 2016/10/29.
 */

public abstract class BaseActivity<T extends BasePresenter, E extends BaseModel> extends AppCompatActivity {

    public T mPresenter;
    public E mModel;
    public Context mContext;
    public RxManager mRxManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRxManager=new RxManager();
        doBeforeSetcontentView();
        setContentView(getLayoutId());
        ButterKnife.inject(this);
        setStatusBarColor();
        mContext = this;
        mPresenter = TUtil.getT(this, 0);
        mModel=TUtil.getT(this,1);
        if(mPresenter!=null){
            mPresenter.mContext=this;
        }
        initPresenter();
        initView();
    }

    /**
     * 设置layout前的配置
     */
    private void doBeforeSetcontentView() {
        //把Activity放到application栈中管理
        AppManager.getInstance().addActivity(this);
        //去除系统标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //锁定竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /********************* 子类实现********************************/
    //获取布局文件
    public abstract int getLayoutId();
    //初始化Presenter,简单页面无需mvp则不用管这个方法
    public abstract void initPresenter();
    //初始化view
    public abstract void initView();

    /**
     * 设置状态栏
     */
    protected void setStatusBarColor(){
        StatusBarUtil.setColor(this,getResources().getColor(R.color.colorPrimary));
    }

    /**
     * 通过Class跳转界面
     **/
    public void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    /**
     * 通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, int requestCode) {
        startActivityForResult(cls, null, requestCode);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, Bundle bundle,
                                       int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 短暂显示Toast提示(来自String)
     **/
    public void showShortToast(String text) {
        ToastUtil.showShort(text);
    }

    /**
     * 短暂显示Toast提示(id)
     **/
    public void showShortToast(int resId) {
        ToastUtil.showShort(resId);
    }

    /**
     * 长时间显示Toast提示(来自res)
     **/
    public void showLongToast(int resId) {
        ToastUtil.showLong(resId);
    }

    /**
     * 长时间显示Toast提示(来自String)
     **/
    public void showLongToast(String text) {
        ToastUtil .showLong(text);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null){
            mPresenter.onDestroy();
        }
        mRxManager.clear();
        ButterKnife.reset(this);
        AppManager.getInstance().finishActivity(this);
    }
}
