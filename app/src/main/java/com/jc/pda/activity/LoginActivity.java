package com.jc.pda.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import com.jc.pda.R;
import com.jc.pda.component.DaggerLoginActivityComponent;
import com.jc.pda.module.ContextModule;
import com.jc.pda.module.LoginActivityModule;
import com.jc.pda.presenter.LoginActivityPresenter;
import com.jc.pda.presenter.view.LoginActivityView;
import com.jc.pda.utils.PermissionUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity implements LoginActivityView {
    @BindView(R.id.et_login_user)
    EditText etLoginUser;
    @BindView(R.id.et_login_pwd)
    EditText etLoginPwd;
    @BindView(R.id.iv_login_pwd_setting)
    ImageView ivLoginPwdSetting;
    @BindView(R.id.cb_login_remenber_pwd)
    CheckBox cbLoginRemenberPwd;
    @BindView(R.id.cb_login_auto_login)
    CheckBox cbLoginAutoLogin;

    @Inject
    LoginActivityPresenter presenter;

    private ImmersionBar immersionBar;

    private boolean isPwdPassaword = true;

    private SweetAlertDialog dialog;

    private static final int PERMISION_DATABASE = 10001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        //大于6.0申请动态权限
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            PermissionUtils.requestDataBasePermission(this,PERMISION_DATABASE);

        immersionBar = ImmersionBar.with(this)
                .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
                .flymeOSStatusBarFontColor(R.color.black)  //修改flyme OS状态栏字体颜色
                .fitsSystemWindows(true)
                .keyboardEnable(true)  //解决软键盘与底部输入框冲突问题，默认为false，还有一个重载方法，可以指定软键盘mode
                .keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        immersionBar.init();  //必须调用方可沉浸式

        DaggerLoginActivityComponent.builder()
                .loginActivityModule(new LoginActivityModule(this))
                .contextModule(new ContextModule(this))
                .build()
                .inject(this);

        cbLoginAutoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    cbLoginRemenberPwd.setChecked(true);
                }
            }
        });

        if(presenter !=null){
            presenter.getLoginInfo();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (immersionBar != null)
            immersionBar.destroy();
    }

    @OnClick(R.id.iv_login_pwd_setting)
    public void pwdSetting() {
        if (isPwdPassaword) {
            isPwdPassaword = false;
            ivLoginPwdSetting.setImageResource(R.mipmap.login_pwd_eye_open);
            etLoginPwd.setInputType(InputType.TYPE_CLASS_TEXT);
        } else {
            isPwdPassaword = true;
            ivLoginPwdSetting.setImageResource(R.mipmap.login_pwd_eye_close);
            etLoginPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

    @OnClick(R.id.btn_login_submit)
    public void login() {
        presenter.checkLogin();
    }

    @Override
    public void dialogShow() {
        dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.getProgressHelper().setBarColor(ContextCompat.getColor(this, R.color.app_main));
        dialog.setTitleText("正在登录");
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    @Override
    public void dialogDismiss() {
        if(dialog!=null)
            dialog.dismiss();
    }

    @Override
    public String getUser() {
        return etLoginUser.getText().toString();
    }

    @Override
    public String getPwd() {
        return etLoginPwd.getText().toString();
    }

    @Override
    public boolean isSave() {
        return cbLoginRemenberPwd.isChecked();
    }

    @Override
    public boolean isAuto() {
        return cbLoginAutoLogin.isChecked();
    }

    @Override
    public void loginInfo(String user, String pwd, boolean isSave, boolean isAuto) {
        if (etLoginUser!=null)
            etLoginUser.setText(user);
        if(etLoginPwd!=null) {
            if(isSave){
                etLoginPwd.setText(pwd);
            }
        }
        if(cbLoginRemenberPwd!=null)
            cbLoginRemenberPwd.setChecked(isSave);
        if(cbLoginAutoLogin!=null)
            cbLoginAutoLogin.setChecked(isAuto);


        if(isAuto){
            if (presenter!=null)
            presenter.checkLogin();
        }
    }

    @Override
    public void err(String err) {
        Toasty.error(this,err, Toast.LENGTH_LONG).show();
    }

    @Override
    public void loginSuccess() {
        startActivity(new Intent(this,MenuActivity.class));
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == PERMISION_DATABASE){
            if(PermissionUtils.getPermissionResult(grantResults)){

            }
        }

    }
}
