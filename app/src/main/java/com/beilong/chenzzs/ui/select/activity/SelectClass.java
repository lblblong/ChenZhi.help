package com.beilong.chenzzs.ui.select.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.beilong.chenzzs.ui.select.adapter.TermAdapter;
import com.beilong.chenzzs.ui.select.contract.SelectClassContract;

import java.io.File;
import java.util.List;

import butterknife.InjectView;
import com.beilong.chenzzs.R;
import com.beilong.chenzzs.app.AppConstant;
import com.beilong.chenzzs.bean.Term;
import com.beilong.chenzzs.ui.main.contract.SchoolTimeTableContract;
import com.beilong.chenzzs.ui.main.domain.ClassChangeEvent;
import com.beilong.chenzzs.ui.select.adapter.SelectClassAdapter;
import com.beilong.chenzzs.ui.select.model.SelectClassModel;
import com.beilong.chenzzs.ui.select.presenter.SelectClassPresenter;
import czzs.beilong.common.base.BaseActivity;
import okhttp3.ResponseBody;

/**
 * Created by LBL on 2016/10/31.
 * 选择课表
 */

public class SelectClass extends BaseActivity<SelectClassPresenter,SelectClassModel> implements SelectClassContract.View {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.progressBar)
    ProgressBar selectProgressBar;
    @InjectView(R.id.recyclerView)
    RecyclerView selectRecyclerView;

    BottomSheetDialog dialog;
    ProgressBar termProgressBar;
    RecyclerView termRecyclerView;

    /**
     * 入口
     */
    public static void startAction(Context context){
        Intent intent = new Intent(context,SelectClass.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_select_class;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this,mModel);
    }

    @Override
    public void initView() {
        toolbar.setTitle("选择课表");   //title要在setSupportActionBar前设置，不然显示的标题是appname
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitActivity();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showSchoolTimeTable();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.selectclass,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            //选择学期
            case R.id.selectTerm:
                dialog = new BottomSheetDialog(this);
                View dialogview = dialog.getLayoutInflater().inflate(R.layout.dialog_selectclass,null);
                termProgressBar = (ProgressBar) dialogview.findViewById(R.id.progressBar);
                termRecyclerView = (RecyclerView) dialogview.findViewById(R.id.recyclerView);
                termProgressBar.setVisibility(View.VISIBLE);
                termRecyclerView.setVisibility(View.GONE);
                dialog.setContentView(dialogview);
                dialog.show();
                mPresenter.getTerms("http://ocq4yyk3f.bkt.clouddn.com/");  //学期获取完成后会调用returnTermList
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void returnTermList(final List<Term> terms) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        TermAdapter termAdapter = new TermAdapter(terms);
        termProgressBar.setVisibility(View.GONE);
        termRecyclerView.setVisibility(View.VISIBLE);
        termRecyclerView.setLayoutManager(linearLayoutManager);
        termRecyclerView.setAdapter(termAdapter);
        termAdapter.setOnItemClickListener(new TermAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                dialog.dismiss();   //dialog隐藏
                dialog = null;
                selectRecyclerView.setVisibility(View.GONE);
                selectProgressBar.setVisibility(View.VISIBLE);    //progressBar显示
                mPresenter.downloadSchoolTimeTable(terms.get(postion).getUrl());//一学期课表下载完成后会调用returnSchoolTimeTable
                termProgressBar = null;
                termRecyclerView = null;
            }
        });
    }

    @Override
    public void returnTermListError() {
        showShortToast("获取学期列表失败，请检查网络。");
        dialog.dismiss();
        dialog = null;
    }

    @Override
    public void returnSchoolTimeTable(ResponseBody responseBody) {
        mPresenter.saveSchoolTimeTable(responseBody);   //保存课表到文件的回调是returnSaveSchoolTimeTable
    }

    @Override
    public void returnSchoolTimeTableError() {
        showShortToast("获取班级列表失败，请检查网络");
    }

    @Override
    public void returnSaveSchoolTimeTable(boolean b) {
        if (b){
            showShortToast("课表缓存成功~");
            selectProgressBar.setVisibility(View.GONE);
            selectRecyclerView.setVisibility(View.VISIBLE);
            showSchoolTimeTable();
        }else {
            showLongToast("课表缓存失败，请重试~");
            selectProgressBar.setVisibility(View.GONE);
        }
    }

    /**
     * -------------------------------------------------------------------------------------
     * 显示班级列表
     */
    private void showSchoolTimeTable(){
        if(selectProgressBar.getVisibility()!=View.VISIBLE){
            selectProgressBar.setVisibility(View.VISIBLE);
        }
        String path = getFilesDir()+ AppConstant.SCHOOL_TIME_TABLE;
        File file = new File(path);
        if (!file.exists()){
            selectProgressBar.setVisibility(View.GONE);
            return;
        }
        mPresenter.getClassList(path);  //获取到班级列表后会回调returnClassList
    }

    @Override
    public void returnClassList(final List<String> classNames) {
        selectProgressBar.setVisibility(View.GONE);
        if(classNames==null){
            return;
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        SelectClassAdapter adapter = new SelectClassAdapter(classNames);
        adapter.setOnItemClickListener(new SelectClassAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                mPresenter.saveCurrentClass(classNames.get(postion));
            }
        });
        selectRecyclerView.setLayoutManager(linearLayoutManager);
        selectRecyclerView.setAdapter(adapter);
    }

    @Override
    public void returnSaveCurrentClass(boolean b) {
        if (b){
            mRxManager.post(SchoolTimeTableContract.CURRENT_CLASS_NAME_TO_CHANGE,new ClassChangeEvent());   //通知所有关注课表更改的观察者
            exitActivity();
        }else {
            showLongToast("班级选择失败，请重试。");
        }
    }

    /**
     * 退出当前Activity
     */
    private void exitActivity(){
        //android5.0+有退出动画
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            finishAfterTransition();    //界面退出动画结束后再销毁
        }else{
            finish();
        }
    }
}
