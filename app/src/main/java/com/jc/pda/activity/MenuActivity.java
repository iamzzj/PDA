package com.jc.pda.activity;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.gyf.barlibrary.ImmersionBar;
import com.gyf.barlibrary.OnKeyboardListener;
import com.jc.pda.R;
import com.jc.pda.component.DaggerMenuActivityViewComponent;
import com.jc.pda.menuFragment.MyFragment;
import com.jc.pda.menuFragment.TableFragment;
import com.jc.pda.menuFragment.WorkFragment;
import com.jc.pda.module.ContextModule;
import com.jc.pda.module.MenuActivityModule;
import com.jc.pda.presenter.MenuActivityPresenter;
import com.jc.pda.presenter.view.MenuActivityView;
import com.jc.pda.utils.Global;
import com.jc.pda.utils.Path;
import com.jc.pda.view.DownApkDialog;

import java.lang.reflect.Field;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class MenuActivity extends AppCompatActivity implements MenuActivityView{

    @BindView(R.id.vp_menu_viewpager)
    ViewPager vpMenuViewpager;
    @BindView(R.id.bnv_menu_menu)
    BottomNavigationView bnvMenuMenu;
    @BindView(R.id.v_menu_bar)
    View vMenuBar;

    private ImmersionBar immersionBar;

    private final Fragment[] fragments = {new WorkFragment(), new TableFragment(), new MyFragment()};

    private SweetAlertDialog dialog;

    @Inject
    MenuActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            vMenuBar.setVisibility(View.VISIBLE);
        }

        DaggerMenuActivityViewComponent.builder()
                .contextModule(new ContextModule(this))
                .menuActivityModule(new MenuActivityModule(this))
                .build()
                .inject(this);

        immersionBar = ImmersionBar.with(this)
                .statusBarDarkFont(false)   //状态栏字体是深色，不写默认为亮色
                .flymeOSStatusBarFontColor(R.color.white)  //修改flyme OS状态栏字体颜色
                .keyboardEnable(true)  //解决软键盘与底部输入框冲突问题，默认为false，还有一个重载方法，可以指定软键盘mode
                .keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                .setOnKeyboardListener(new OnKeyboardListener() {    //软键盘监听回调
                    @Override
                    public void onKeyboardChange(boolean isPopup, int keyboardHeight) {
                        //isPopup为true，软键盘弹出，为false，软键盘关闭
                    }
                });
        immersionBar.init();  //必须调用方可沉浸式

        vpMenuViewpager.setAdapter(new MyViewpagerAdapter(getSupportFragmentManager()));
        vpMenuViewpager.setOffscreenPageLimit(fragments.length);
        vpMenuViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bnvMenuMenu.getMenu().size();
                for (int i = 0; i < bnvMenuMenu.getMenu().size(); i++) {
                    bnvMenuMenu.getMenu().getItem(i).setChecked(false);
                }
                bnvMenuMenu.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bnvMenuMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_navigation_work:
                        vpMenuViewpager.setCurrentItem(0);
                        break;
                    case R.id.menu_navigation_table:
                        vpMenuViewpager.setCurrentItem(1);
                        break;
                    case R.id.menu_navigation_my:
                        vpMenuViewpager.setCurrentItem(2);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void alertUpdate(String versionName,String newVersionName,String note) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("发现新版本");
        builder.setMessage("新版本："+newVersionName+
                "\n当前版本："+versionName+
                "\n升级原因："+note+
                "\n是否升级？"

        );
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DownApkDialog downApkDialog = new DownApkDialog(MenuActivity.this);
                downApkDialog.setDownLoadUrl(Global.UPDATE_APK,Path.getDownAPK(MenuActivity.this));
                downApkDialog.show();
            }
        });
        builder.create().show();
    }

    private class MyViewpagerAdapter extends FragmentPagerAdapter {

        public MyViewpagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }
    }

    /**
     * 禁用超过3个以上动画
     *
     * @param view
     */
    public void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                //noinspection RestrictedApi
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                //noinspection RestrictedApi
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (immersionBar != null)
            immersionBar.destroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            dialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
            dialog.setTitleText("确定退出？");
            dialog.setConfirmText("确定");
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sDialog) {
                    sDialog.dismiss();
                    finish();
                }
            });
            dialog.show();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(presenter!=null){
            presenter.checkUpdate();
        }
    }
}
