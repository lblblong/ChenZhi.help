package com.beilong.chenzzs.ui.select.presenter;

import com.beilong.chenzzs.ui.select.contract.SelectClassContract;

import java.util.List;

import com.beilong.chenzzs.bean.Term;

import okhttp3.ResponseBody;
import rx.Observer;

/**
 * Created by LBL on 2016/11/1.
 */

public class SelectClassPresenter extends SelectClassContract.Presenter {
    @Override
    public void getTerms(String url) {
        Observer<List<Term>> observer = new Observer<List<Term>>() {
            @Override
            public void onCompleted() {}
            @Override
            public void onError(Throwable e) {
                mView.returnTermListError();
            }

            @Override
            public void onNext(List<Term> terms) {
                mView.returnTermList(terms);
            }
        };
        mModel.getTerms(url).subscribe(observer);
    }

    @Override
    public void downloadSchoolTimeTable(String Url) {
        Observer<ResponseBody> observer = new Observer<ResponseBody>() {
            @Override
            public void onCompleted() {}
            @Override
            public void onError(Throwable e) {
                mView.returnSchoolTimeTableError();
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                mView.returnSchoolTimeTable(responseBody);
            }
        };
        mModel.downloadSchoolTimeTable(Url).subscribe(observer);
    }

    @Override
    public void saveSchoolTimeTable(ResponseBody responseBody) {
        Observer observer = new Observer() {
            @Override
            public void onCompleted() {
                mView.returnSaveSchoolTimeTable(true);
            }
            @Override
            public void onError(Throwable e) {
                mView.returnSaveSchoolTimeTable(false);
            }
            @Override
            public void onNext(Object o) {}
        };
        mModel.saveSchoolTimeTable(mContext.getFilesDir().getPath(),responseBody)
                .subscribe(observer);
    }

    @Override
    public void getClassList(String path) {

        Observer<List<String>> observer = new Observer<List<String>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                mView.returnClassList(null);
            }

            @Override
            public void onNext(List<String> strings) {
                mView.returnClassList(strings);
            }
        };
        mModel.getClassList(path).subscribe(observer);
    }

    @Override
    public void saveCurrentClass(String classname) {
        Observer observer = new Observer() {
            @Override
            public void onCompleted() {
                mView.returnSaveCurrentClass(true);
            }

            @Override
            public void onError(Throwable e) {
                mView.returnSaveCurrentClass(false);
            }

            @Override
            public void onNext(Object o) {

            }
        };
        mModel.saveCurrentClass(classname).subscribe(observer);
    }

}
