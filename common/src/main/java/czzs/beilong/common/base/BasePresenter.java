package czzs.beilong.common.base;

import android.content.Context;

import czzs.beilong.common.baserx.RxManager;

/**
 * Created by LBL on 2016/10/29.
 */

public abstract class BasePresenter<T,E> {
    public Context mContext;
    public E mModel;
    public T mView;
    public RxManager mRxManage = new RxManager();

    public BasePresenter(){}

    public void setV(T v){
        this.mView = v;
    }

    public void setM(E m){
        this.mModel = m;
    }

    public void setVM(T v, E m) {
        this.mView = v;
        this.mModel = m;
        this.onStart();
    }

    public void onStart(){}
    public void onDestroy(){mRxManage.clear();}
}
