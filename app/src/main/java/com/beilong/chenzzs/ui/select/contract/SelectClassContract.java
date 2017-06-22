package com.beilong.chenzzs.ui.select.contract;

import java.util.List;

import com.beilong.chenzzs.bean.Term;
import czzs.beilong.common.base.BaseModel;
import czzs.beilong.common.base.BasePresenter;
import czzs.beilong.common.base.BaseView;
import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by LBL on 2016/11/1.
 */

public interface SelectClassContract {
    interface Model extends BaseModel{
        //获取学期
        Observable<List<Term>> getTerms(String Url);
        //下载学期课表
        Observable<ResponseBody> downloadSchoolTimeTable(String Url);
        //保存学期课表到文件
        Observable saveSchoolTimeTable(String path,ResponseBody responseBody);
        //获取班级列表
        Observable<List<String>> getClassList(String path);
        //保存当前课表标志位
        Observable saveCurrentClass(String classname);
    }

    interface View extends BaseView {
        //获取返回的学期列表
        void returnTermList(List<Term> terms);
        void returnTermListError();
        //获取返回的班级列表
        void returnSchoolTimeTable( ResponseBody responseBody);
        void returnSchoolTimeTableError();
        //保存学期课表的回调
        void returnSaveSchoolTimeTable(boolean b);
        //获取班级列表
        void returnClassList(List<String> classNames);
        //当前课表标志位保存完毕后的回调
        void returnSaveCurrentClass(boolean b);
    }

    abstract static class Presenter extends BasePresenter<View,Model>{
        //获取学期
        public abstract void getTerms(String Url);
        //下载学期课表
        public abstract void downloadSchoolTimeTable(String Url);
        //保存学期课表到文件
        public abstract void saveSchoolTimeTable(ResponseBody responseBody);
        //获取班级列表
        public abstract void getClassList(String path);
        //保存当前课表标志位
        public abstract void saveCurrentClass(String classname);
    }
}
