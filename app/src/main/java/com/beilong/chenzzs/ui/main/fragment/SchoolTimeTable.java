package com.beilong.chenzzs.ui.main.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.beilong.chenzzs.app.AppConstant;
import com.beilong.chenzzs.bean.VersionAPI;
import com.beilong.chenzzs.ui.main.contract.SchoolTimeTableContract;
import com.beilong.chenzzs.ui.main.domain.ClassChangeEvent;
import com.beilong.chenzzs.ui.main.model.SchoolTimeTableModel;

import java.io.File;
import java.util.List;
import java.util.Random;

import butterknife.InjectView;
import com.beilong.chenzzs.app.AppApplication;
import com.beilong.chenzzs.ui.main.presenter.SchoolTimeTablePresenter;
import czzs.beilong.common.base.BaseFragment;
import com.beilong.chenzzs.R;
import com.beilong.chenzzs.utils.SharedPreferencesUtil;

import rx.functions.Action1;

/**
 * Created by LBL on 2016/10/31.
 */

public class SchoolTimeTable extends BaseFragment<SchoolTimeTablePresenter,SchoolTimeTableModel> implements SchoolTimeTableContract.View {
    @InjectView(R.id.tableLayout)
    TableLayout tableLayout;
    @InjectView(R.id.empty)
    LinearLayout empty;

    int[] tv_ids = {R.id.tv_grid_1, R.id.tv_grid_2, R.id.tv_grid_3, R.id.tv_grid_4,
            R.id.tv_grid_5, R.id.tv_grid_6, R.id.tv_grid_7, R.id.tv_grid_8,
            R.id.tv_grid_9, R.id.tv_grid_10, R.id.tv_grid_11, R.id.tv_grid_12,
            R.id.tv_grid_13, R.id.tv_grid_14, R.id.tv_grid_15, R.id.tv_grid_16,
            R.id.tv_grid_17, R.id.tv_grid_18, R.id.tv_grid_19, R.id.tv_grid_20,};
    int[] cv_ids = {R.id.cardview_grid_1, R.id.cardview_grid_2, R.id.cardview_grid_3, R.id.cardview_grid_4,
            R.id.cardview_grid_5, R.id.cardview_grid_6, R.id.cardview_grid_7, R.id.cardview_grid_8,
            R.id.cardview_grid_9, R.id.cardview_grid_10, R.id.cardview_grid_11, R.id.cardview_grid_12,
            R.id.cardview_grid_13, R.id.cardview_grid_14, R.id.cardview_grid_15, R.id.cardview_grid_16,
            R.id.cardview_grid_17, R.id.cardview_grid_18, R.id.cardview_grid_19, R.id.cardview_grid_20,};
    String[] colors = {"#b2DA4453", "#b237BC98", "#b24A89DC", "#b2D770AD", "#b2967ADC"};

    @Override
    protected int getLayoutResoure() {
        return R.layout.fragment_main_school_timetable;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this,mModel);
    }

    @Override
    public void initView() {
        mRxManager.on(SchoolTimeTableContract.CURRENT_CLASS_NAME_TO_CHANGE, new Action1<ClassChangeEvent>() {
            @Override
            public void call(ClassChangeEvent event) {
                OnChangeClass(event);
            }
        });
        showSchoolTimeTable();
        checkUpData();
    }

    /**
     * 课表是否被更改
     *
     * @param event
     */
    private void OnChangeClass(ClassChangeEvent event) {
        Log.e("MainActivity", "课表被更改");
        showSchoolTimeTable();
    }

    /**
     * 显示课表
     */
    private void showSchoolTimeTable() {
        File file = new File(AppApplication.getAppContext().getFilesDir() + AppConstant.SCHOOL_TIME_TABLE);
        if (!file.exists()) {
            //没有课表文件
            empty.setVisibility(View.VISIBLE);
            tableLayout.setVisibility(View.GONE);
            return;
        }
        String classname = SharedPreferencesUtil.getInstance().getCurrentClass();
        if ("".equals(classname) && classname==null) {
            //当前未选择课表
            empty.setVisibility(View.VISIBLE);
            tableLayout.setVisibility(View.GONE);
            return;
        }
        mPresenter.getClassTable(classname);    //解析成功课表后会回调returnClassTable
    }

    @Override
    public void returnClassTable(List<String> courses) { //课表解析成功的回调
        for(int i = 0;i<courses.size();i++){
            String courseName =  courses.get(i);
            if(!("".equals(courseName)) && courseName!=null){
                TextView tv = (TextView) tableLayout.findViewById(tv_ids[i]);
                CardView cv = (CardView) tableLayout.findViewById(cv_ids[i]);
                tv.setText(courseName);
                cv.setCardBackgroundColor(Color.parseColor(colors[new Random().nextInt(5)]));
                cv.setVisibility(View.VISIBLE);
            }else{
                CardView cv = (CardView) tableLayout.findViewById(cv_ids[i]);
                cv.setVisibility(View.INVISIBLE);
            }
        }
        empty.setVisibility(View.GONE);
        tableLayout.setVisibility(View.VISIBLE);
        String nowClassName = SharedPreferencesUtil.getInstance().getCurrentClass();
        Log.e("School","当前班级："+nowClassName);
        mRxManager.post(SchoolTimeTableContract.TOOLBAR_TO_CHANGE,nowClassName);
    }

    @Override
    public void returnClassTableOnError() { //课表解析失败的回调
        showLongToast("课表解析失败，重新下载试试呗！");
    }

    /**
     * 检查是否有新版本
     */
    private void checkUpData() {
        mPresenter.checkVersion();  //检查完毕回调returnCheckVersion
    }

    @Override
    public void returnCheckVersion(final VersionAPI versionAPI) {
        if(versionAPI == null){
            Log.e("SchoolTimeTable","已是最新版本，无需下载");
            return;
        }
        //显示更新dialog
        String title = versionAPI.getName()+" "+versionAPI.getVersionShort();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.dialog_updata,null,false);
        ((TextView)view.findViewById(R.id.title)).setText(title);
        ((TextView)view.findViewById(R.id.content)).setText(versionAPI.getChangelog());
        new AlertDialog.Builder(getContext())
                .setView(view)
                .setPositiveButton("前往下载", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Uri uri = Uri.parse(versionAPI.getUpdate_url());    //下载地址
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(uri);
                        getContext().startActivity(intent);
                    }
                })
                .setNegativeButton("忽略此版本", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferencesUtil.getInstance().changeCurrentVersion(versionAPI.getVersionShort());
                    }
                })
                .show();
    }
}
