package com.beilong.chenzzs.ui.query.presenter;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.beilong.chenzzs.bean.Mark;
import com.beilong.chenzzs.ui.query.contract.QueryMarkContract;
import rx.Observer;

/**
 * Created by LBL on 2016/11/4.
 */

public class QueryMarkPresenter extends QueryMarkContract.Presenter {

    List<Mark> marks = null;

    @Override
    public void queryMark(String username, String password) {

        final Observer<String> zxcjObserver = new Observer<String>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                mView.returnQueryMarkError(QueryMarkContract.OTHER_ERROR);
            }

            @Override
            public void onNext(String s) {
                mModel.addOnList(s,marks);
                mView.returnQueryMark(marks);
                marks = null;
            }
        };

        final Observer<String> cjObserver = new Observer<String>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Log.e("QueryP",e.getMessage());
                mView.returnQueryMarkError(QueryMarkContract.OTHER_ERROR);
            }

            @Override
            public void onNext(String s) {
                marks = new ArrayList<>();
                mModel.addOnList(s,marks);
                mModel.getZXCJ().subscribe(zxcjObserver);
            }
        };

        //登陆（成功则获取成绩，失败则通知UI）
        Observer<String> loginObserver = new Observer<String>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
                switch (e.getMessage()){
                    case QueryMarkContract.USERNSME_ON_PASSWORD_ERROR:
                        mView.returnQueryMarkError(QueryMarkContract.USERNSME_ON_PASSWORD_ERROR);
                        break;
                    case QueryMarkContract.NOT_NETWORK:
                        mView.returnQueryMarkError(QueryMarkContract.NOT_NETWORK);
                        break;
                }
            }

            @Override
            public void onNext(String responseBody) {
                //登陆成功
                mModel.getCJ().subscribe(cjObserver);   //获取成绩
            }
        };
        mModel.login(username, password).subscribe(loginObserver);
    }
}
