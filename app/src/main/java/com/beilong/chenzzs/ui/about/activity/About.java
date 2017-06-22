package com.beilong.chenzzs.ui.about.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.beilong.chenzzs.R;
import com.beilong.chenzzs.bean.VersionAPI;
import com.beilong.chenzzs.ui.about.contract.AboutContract;
import com.beilong.chenzzs.ui.about.model.AboutModel;
import com.beilong.chenzzs.ui.about.presenter.AboutPresenter;
import com.beilong.chenzzs.utils.SharedPreferencesUtil;
import com.jaeger.library.StatusBarUtil;

import butterknife.InjectView;
import butterknife.OnClick;
import czzs.beilong.common.base.BaseActivity;

/**
 * Created by LBL on 2016/11/5.
 */

public class About extends BaseActivity<AboutPresenter,AboutModel> implements AboutContract.View{

    @InjectView(R.id.Toolbar)
    android.support.v7.widget.Toolbar toolbar;
    @InjectView(R.id.btShare)
    Button btShare;
    @InjectView(R.id.btUpData)
    Button btUpData;
    @InjectView(R.id.btFeedback)
    Button btFeedback;

    /**
     * 入口
     *
     * @param context
     */
    public static void startAction(Context context) {
        Intent intent = new Intent(context, About.class);
        context.startActivity(intent);
    }

    @Override
    protected void setStatusBarColor() {
        StatusBarUtil.setTranslucent(this, 0);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this,mModel);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        toolbar.setTitle("郴职助手");
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

    @OnClick({R.id.btShare, R.id.btUpData, R.id.btFeedback})
    public void onClick(View view) {
        switch (view.getId()) {
            //分享
            case R.id.btShare:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT,"Subject Here");
                shareIntent.putExtra(Intent.EXTRA_TEXT,getString(R.string.share_txt));
                startActivity(Intent.createChooser(shareIntent,"分享-郴职助手"));
                break;
            //检测更新
            case R.id.btUpData:
                mPresenter.checkVersion();  //结果会回调returnCheckVersion
                break;
            //意见反馈
            case R.id.btFeedback:
                goToHtml(getString(R.string.feedback_url));
                break;
        }
    }

    //打开链接
    private void goToHtml(String url){
        Uri uri = Uri.parse(url);   //指定网址
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);           //指定Action
        intent.setData(uri);                            //设置Uri
        startActivity(intent);        //启动Activity
    }

    @Override
    public void returnCheckVersion(final VersionAPI versionAPI) {
        if(versionAPI == null){
            showLongToast("已经是最新版本，无需更新");
            return;
        }
        //显示更新dialog
        String title = versionAPI.getName()+" "+versionAPI.getVersionShort();
        View view = getLayoutInflater().inflate(R.layout.dialog_updata,null,false);
        ((TextView)view.findViewById(R.id.title)).setText(title);
        ((TextView)view.findViewById(R.id.content)).setText(versionAPI.getChangelog());
        new AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton("前往下载", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Uri uri = Uri.parse(versionAPI.getUpdate_url());    //下载地址
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(uri);
                        mContext.startActivity(intent);
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
