package czzs.beilong.common.commonutils;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;

import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

/**
 * Created by LBL on 2016/10/31.
 * 处理抽屉关闭后操作的动画，解决跳转逻辑的卡顿
 */

public class RxDrawer {
    private static final float OFFSET_THRESHOLD = 0.03f;
    public static Observable<Void> close(final DrawerLayout drawerLayout){
        return Observable.create(new Observable.OnSubscribe<Void>(){
            @Override
            public void call(final Subscriber<? super Void> subscriber) {
                drawerLayout.closeDrawer(GravityCompat.START);  //抽屉关闭动画的方向
                final DrawerLayout.DrawerListener listener = new DrawerLayout.SimpleDrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        if (slideOffset < OFFSET_THRESHOLD){    //抽屉还剩0.03f的时候执行跳转
                            subscriber.onNext(null);
                            subscriber.onCompleted();
                        }
                    }
                };
                drawerLayout.addDrawerListener(listener);   //添加抽屉关闭动画监听
                subscriber.add(new MainThreadSubscription() {
                    @Override
                    protected void onUnsubscribe() {
                        Log.i("RxDrawer","onUnsubscribe");
                        drawerLayout.removeDrawerListener(listener);
                    }
                });
            }
        });
    }
}
