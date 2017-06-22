package com.beilong.chenzzs.ui.query.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import com.beilong.chenzzs.R;
import com.beilong.chenzzs.bean.Mark;
import com.beilong.chenzzs.ui.query.adapter.QueryMarkAdapter;
import com.beilong.chenzzs.ui.query.contract.QueryMarkContract;
import com.beilong.chenzzs.ui.query.model.QueryMarkModel;
import com.beilong.chenzzs.ui.query.presenter.QueryMarkPresenter;
import com.beilong.chenzzs.utils.SharedPreferencesUtil;

import czzs.beilong.common.base.BaseActivity;
import czzs.beilong.common.commonutils.SystemUtil;

/**
 * Created by LBL on 2016/10/31.
 * 查询成绩
 */

public class QueryMark extends BaseActivity<QueryMarkPresenter, QueryMarkModel> implements QueryMarkContract.View {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.username)
    TextInputLayout username;
    @InjectView(R.id.password)
    TextInputLayout password;
    @InjectView(R.id.submit)
    Button submit;
    @InjectView(R.id.name)
    TextView name;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    @InjectView(R.id.progressBar)
    ProgressBar progressBar;

    /**
     * 入口
     *
     * @param context
     */
    public static void startAction(Context context) {
        Intent intent = new Intent(context, QueryMark.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_query_mark;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this,mModel);
    }

    @Override
    public void initView() {
        toolbar.setTitle("成绩查询");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else {
                    finish();
                }
            }
        });
    }

    @OnClick(R.id.submit)
    public void onClick() {
        SystemUtil.shutKeyboard(this);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        name.setVisibility(View.GONE);
        String strUsername = username.getEditText().getText().toString();
        String strPassword = password.getEditText().getText().toString();
        mPresenter.queryMark(strUsername,strPassword);  //查询结果会回调returnQuetyMark
    }

    @Override
    public void returnQueryMark(List<Mark> marks) {
        String username = marks.get(0).getName();
        name.setText(username);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        QueryMarkAdapter adapter = new QueryMarkAdapter(marks);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        name.setVisibility(View.VISIBLE);
    }

    @Override
    public void returnQueryMarkError(String errorCode) {
        progressBar.setVisibility(View.GONE);
        switch (errorCode){
            //其他错误
            case QueryMarkContract.OTHER_ERROR:
                showLongToast("网络或其他问题导致成绩获取失败");
                break;
            //用户名或密码错误
            case QueryMarkContract.USERNSME_ON_PASSWORD_ERROR:
                showLongToast("账号或密码错误");
                break;
            //没有网络
            case QueryMarkContract.NOT_NETWORK:
                showLongToast("当前处在没有网络的异次元");
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.querymark,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            //帮助
            case R.id.help:
                showHelp();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 显示帮助
     */
    private void showHelp(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_queryhelp,null,false);
        new AlertDialog.Builder(this)
                .setView(view)
                .setNegativeButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }
}
