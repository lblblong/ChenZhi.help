package com.beilong.chenzzs.ui.query.contract;

import com.beilong.chenzzs.bean.Mark;

import java.util.List;

import czzs.beilong.common.base.BaseModel;
import czzs.beilong.common.base.BasePresenter;
import czzs.beilong.common.base.BaseView;
import rx.Observable;

/**
 * Created by LBL on 2016/11/3.
 */

public interface QueryMarkContract {

    public static final String LOGIN_PATH = "http://jwc.czzy-edu.com/SSJWC/LGcheck.asp"; //登陆地址
    public static final String CXCJ_PATH = "http://jwc.czzy-edu.com/SSJWC/JWGL/SSPK8080GB/HBDATA/HBKSCJ/HBJYCJ/CXCJBrowST.asp";  //查询成绩
    public static final String ZXCJ_PATH = "http://jwc.czzy-edu.com/SSJWC/JWGL/SSPK8080GB/HBDATA/HBKSCJ/HBZXCJ/ZXCJBrowST.asp";  //最新成绩

    /** 用户名或者密码错误 */
    public static final String USERNSME_ON_PASSWORD_ERROR = "username_on_password_error";
    /** 其他错误 */
    public static final String OTHER_ERROR = "other_error";
    /** 没有网络 */
    public static final String NOT_NETWORK = "not_network";

    interface Model extends BaseModel{
        /** 登陆 */
        Observable<String> login(String username, String password);
        /** 获取成绩 */
        Observable<String> getCJ();
        /** 获取最新成绩 */
        Observable<String> getZXCJ();
        /** 封装成绩信息到bean */
        void addOnList(String info , List<Mark> marks);
    }

    interface View extends BaseView{
        /** 查询成绩的回调 */
        void returnQueryMark(List<Mark> marks);
        /** 查询成绩失败的回调 */
        void returnQueryMarkError(String errorCode);
    }

    abstract static class Presenter extends BasePresenter<View,Model>{
        public abstract void queryMark(String username,String password);
    }
}
