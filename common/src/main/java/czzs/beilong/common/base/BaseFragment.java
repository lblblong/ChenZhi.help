package czzs.beilong.common.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import czzs.beilong.common.baserx.RxManager;
import czzs.beilong.common.commonutils.TUtil;
import czzs.beilong.common.commonutils.ToastUtil;

/**
 * Created by LBL on 2016/10/31.
 */

public abstract class BaseFragment<T extends BasePresenter, E extends BaseModel> extends Fragment {
    protected View rootView;
    public T mPresenter;
    public E mModel;
    public RxManager mRxManager;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutResoure(), container, false);
        }
        mRxManager = new RxManager();
        ButterKnife.inject(this, rootView);
        mPresenter = TUtil.getT(this, 0);
        mModel= TUtil.getT(this,1);
        if(mPresenter!=null){
            mPresenter.mContext=this.getActivity();
        }
        initPresenter();
        initView();
        return rootView;
    }

    /********************************* 子类实现 *********************************/
    //获取布局文件
    protected abstract int getLayoutResoure();
    //初始化Presenter（无需mvp则不用管此方法）
    public abstract void initPresenter();
    //初始化View
    public abstract void initView();

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
        intent.setClass(getActivity(), cls);
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
        intent.setClass(getActivity(), cls);
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
        ToastUtil.showLong(text);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        if (mPresenter != null){
            mPresenter.onDestroy();
        }
        mRxManager.clear();
    }
}
