package com.jc.pda.menuFragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jc.pda.R;
import com.jc.pda.activity.AboutActivity;
import com.jc.pda.activity.LoginActivity;
import com.jc.pda.activity.StatisticalActivity;
import com.jc.pda.component.DaggerMyFragmentComponent;
import com.jc.pda.module.ContextModule;
import com.jc.pda.module.MyFragmentModule;
import com.jc.pda.presenter.MyFragmentPresenter;
import com.jc.pda.presenter.view.MyFragmentView;
import com.jc.pda.utils.SharedPreUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;

/**
 * Created by z on 2017/12/20.
 */

public class MyFragment extends Fragment implements MyFragmentView {
    @BindView(R.id.tbar_my)
    Toolbar tbarMy;
    @BindView(R.id.ctl_my)
    CollapsingToolbarLayout ctlMy;
    @BindView(R.id.abl_my)
    AppBarLayout ablMy;
    @BindView(R.id.fab_my_login_out)
    FloatingActionButton fabMyLoginOut;

    private Unbinder unBinder;

    @Inject
    MyFragmentPresenter presenter;

    private SweetAlertDialog dialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerMyFragmentComponent.builder()
                .myFragmentModule(new MyFragmentModule(this))
                .contextModule(new ContextModule(getActivity()))
                .build()
                .inject(this);

        presenter.getCount();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        unBinder = ButterKnife.bind(this, view);

        ctlMy.setTitle(SharedPreUtil.getLoginUser(getActivity()));
        ctlMy.setExpandedTitleColor(ContextCompat.getColor(getActivity(), R.color.black));
        ctlMy.setCollapsedTitleTextColor(ContextCompat.getColor(getActivity(), R.color.white));

        fabMyLoginOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreUtil.clearLogin(getActivity());

                Intent logoutIntent = new Intent(getActivity(),
                        LoginActivity.class);
                logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(logoutIntent);
                getActivity().finish();
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unBinder != null)
            unBinder.unbind();
    }

    @Override
    public String getCodeValue() {
        return SharedPreUtil.getUserCID(getActivity());
    }

    @Override
    public void success(String success) {
        String str = "成功下载了：\n"
                + "经销商" + presenter.getDealerCount() + "条\n"
                + "产品" + presenter.getProductCount() + "条\n"
                + "批号" + presenter.getBatchCount() + "条";
        if(getActivity()!=null)
            Toasty.success(getActivity(), str, Toast.LENGTH_LONG).show();
    }

    @Override
    public void err(String err) {
        if(getActivity()!=null)
            Toasty.error(getActivity(), err, Toast.LENGTH_LONG).show();
    }

    @Override
    public void info(String info) {
        if(getActivity()!=null)
            Toasty.info(getActivity(), info, Toast.LENGTH_LONG).show();
    }

    @Override
    public void dialogShow() {
        dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        dialog.getProgressHelper().setBarColor(ContextCompat.getColor(getActivity(), R.color.app_main));
        dialog.setTitleText("正在下载资料");
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    @Override
    public void dialogDismiss() {
        if (dialog != null)
            dialog.dismiss();
    }

    @Override
    public void setCount(int productCount, int batchCount, int dealerCount) {
        if (productCount == 0 || batchCount == 0 || dealerCount == 0) {
            presenter.startDownLoad();
        }
    }

    @OnClick(R.id.ll_my_download_data)
    public void download() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("下载提示")
                .setMessage("数据量较大，下载时间较长。请在网络好的环境下下载，" +
                        "下载的数据保存在本地。数据资料变动才需下载。")
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.startDownLoad();
                    }
                })
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.create().show();

    }

    @OnClick(R.id.ll_my_about)
    public void about() {
        startActivity(new Intent(getActivity(), AboutActivity.class));
    }

    @OnClick(R.id.ll_my_statistical)
    public void statistical() {
        startActivity(new Intent(getActivity(), StatisticalActivity.class));
    }
}
