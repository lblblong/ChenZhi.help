package com.beilong.chenzzs.ui.query.model;

import android.util.Log;

import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;
import com.yolanda.nohttp.rest.SimpleResponseListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.beilong.chenzzs.app.AppApplication;
import com.beilong.chenzzs.bean.Mark;
import com.beilong.chenzzs.ui.query.contract.QueryMarkContract;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by LBL on 2016/11/4.
 */

public class QueryMarkModel implements QueryMarkContract.Model {
    @Override
    public Observable<String> login(final String username, final String password) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                Request<String> loginRequest = NoHttp.createStringRequest(QueryMarkContract.LOGIN_PATH, RequestMethod.POST);
                loginRequest.add("UserN",username);
                loginRequest.add("PassW",password);
                AppApplication.getRequestQueue().add(1, loginRequest, new SimpleResponseListener<String>() {
                    @Override
                    public void onSucceed(int what, Response<String> response) {
                        //登陆失败，返回了一个弹窗，所以NoHttp认定请求成功
                        subscriber.onError(new Throwable(QueryMarkContract.USERNSME_ON_PASSWORD_ERROR));
                    }

                    @Override
                    public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
                        String errorInfo = "The network is not available, please check the network. The requested url is: http://jwc.czzy-edu.com/SSJWC/LGcheck.asp";
                        if(errorInfo.equals(exception.getMessage())){
                            subscriber.onError(new Throwable(QueryMarkContract.NOT_NETWORK));
                            return;
                        }
                        //登陆成功，返回了一个asp重定向，NoHttp无法处理，认定请求失败
                        subscriber.onNext("登陆成功");
                    }
                });
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<String> getCJ() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                Request<String> cjRequest = NoHttp.createStringRequest(QueryMarkContract.CXCJ_PATH,RequestMethod.GET);
                AppApplication.getRequestQueue().add(1, cjRequest, new SimpleResponseListener<String>() {
                    @Override
                    public void onSucceed(int what, Response<String> response) {
                        try {
                            String info = new String(response.getByteArray(),"GBK");
                            subscriber.onNext(info);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
                        //subscriber.onError(new Throwable());
                    }
                });
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<String> getZXCJ() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                Request<String> zxcjRequest = NoHttp.createStringRequest(QueryMarkContract.ZXCJ_PATH,RequestMethod.GET);
                AppApplication.getRequestQueue().add(1, zxcjRequest, new SimpleResponseListener<String>() {
                    @Override
                    public void onSucceed(int what, Response<String> response) {
                        try {
                            String info = new String(response.getByteArray(),"GBK");
                            subscriber.onNext(info);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
                        Log.e("QueryM",exception.getMessage());
                        subscriber.onError(new Throwable());
                    }
                });
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 把成绩信息装进List
     *
     * @param info
     */
    public void addOnList(String info,List<Mark> marks) {
        Document doc = Jsoup.parse(info);
        Elements tables = doc.select("table");
        //用户信息：班级学号，姓名
        Element userinfo = tables.get(3);
        Elements userinfo_tds = userinfo.select("td");
        Element userinfo_td = userinfo_tds.get(0);
        Elements userinfo_fonts = userinfo_td.select("font");
        Element userinfo_xuehao = userinfo_fonts.get(0);
        Element userinfo_xingming = userinfo_fonts.get(1);

        String name = userinfo_xingming.text();
        //成绩信息
        Element e_cj = tables.get(4);
        Elements cj_trs = e_cj.select("tr");
        for (int i = 1; i < cj_trs.size(); i++) {
            Element cj_tr = cj_trs.get(i);
            Elements cj_tr_tds = cj_tr.select("td");

            String term = cj_tr_tds.get(0).text();
            String subject = cj_tr_tds.get(1).text();
            String fraction = cj_tr_tds.get(3).text();

            Mark mark = new Mark();
            mark.setFraction(fraction);
            mark.setName(name);
            mark.setSubject(subject);
            mark.setTerm(term);
            marks.add(mark);
        }
    }

}
