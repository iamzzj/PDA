package com.jc.pda.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.jc.pda.R;
import com.jc.pda.component.DaggerUpDataPopupWindowComponent;
import com.jc.pda.entity.Bill;
import com.jc.pda.entity.Code;
import com.jc.pda.module.ContextModule;
import com.jc.pda.module.UpDataPopupWindowModule;
import com.jc.pda.presenter.UpDataPopupWindowPresenter;
import com.jc.pda.presenter.view.UpDataPopupWindowView;
import com.jc.pda.utils.PermissionUtils;
import com.orhanobut.logger.Logger;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * Created by z on 2018/1/9.
 */

public class UpDataPopupWindow extends PopupWindow implements UpDataPopupWindowView {
    @BindView(R.id.ll_pop_updata_out)
    LinearLayout llOut;
    @BindView(R.id.ll_pop_updata_up)
    LinearLayout llUp;
    @BindView(R.id.ll_pop_updata_reup)
    LinearLayout llReup;

    @Inject
    UpDataPopupWindowPresenter presenter;

    private Context context;
    private String billId;

    private Bill bill;
    private List<Code> codes;

    private DataChange dataChange;

    public UpDataPopupWindow(Context context, String billId) {
        super(context);
        this.context = context;
        this.billId = billId;

        View view = LayoutInflater.from(context).inflate(R.layout.pop_updata, null);
        ButterKnife.bind(this, view);

        DaggerUpDataPopupWindowComponent
                .builder()
                .contextModule(new ContextModule(context))
                .upDataPopupWindowModule(new UpDataPopupWindowModule(this))
                .build()
                .inject(this);

        this.setContentView(view);
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.Popup_Animation_PushDownUp);
        ColorDrawable dw = new ColorDrawable(ContextCompat.getColor(context, R.color.nullBackground));
        this.setBackgroundDrawable(dw);
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        this.setOnDismissListener(new PoponDismissListener());

        if (presenter != null) {
            presenter.getBill();
            presenter.getCodes();
        }
    }

    public void setDataChange(DataChange dataChange){
        this.dataChange = dataChange;
    }

    @OnClick(R.id.ll_pop_updata_out)
    public void out() {
        if (bill != null && codes != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!PermissionUtils.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toasty.error(context, "没有写入内存卡权限，请在应用设置开启权限后再保存到内存卡", Toast.LENGTH_LONG).show();
                    return;
                }
            }

            presenter.saveTxt(bill, codes);

        }
    }

    @Override
    public void setBill(Bill bill) {
        if (bill != null) {
            this.bill = bill;
            if (bill.isUp()) {
                llUp.setVisibility(View.GONE);
            } else {
                llReup.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void setBillChange(Bill bill) {
        if (bill != null) {
            this.bill = bill;
            if (bill.isUp()) {
                llUp.setVisibility(View.GONE);
                llReup.setVisibility(View.VISIBLE);
            } else {
                llUp.setVisibility(View.VISIBLE);
                llReup.setVisibility(View.GONE);
            }

            if(dataChange!=null){
                dataChange.setBillChange(bill);
            }
        }
    }

    @Override
    public void setCodes(List<Code> codes) {
        this.codes = codes;
        Logger.i(codes.toString());
    }

    @Override
    public String getBillId() {
        return billId;
    }


    @Override
    public void dialogShow(String title) {
        if(dataChange!=null){
            dataChange.dialogShow(title);
        }
    }

    @Override
    public void dialogDismiss() {
        if(dataChange!=null){
            dataChange.dialogDismiss();
        }
    }

    @Override
    public void success(String success) {
        if(dataChange!=null){
            dismiss();
            dataChange.success(success);
        }
    }

    @Override
    public void err(String err) {
        if(dataChange!=null){
            dismiss();
            dataChange.err(err);
        }
    }

    @OnClick(R.id.ll_pop_updata_up)
    public void up() {
        dismiss();
        if(presenter!=null){
            if(bill!=null&&codes!=null){
                presenter.upBill(bill,codes);
            }
        }
    }

    @OnClick(R.id.ll_pop_updata_reup)
    public void reup() {
        dismiss();
        if(presenter!=null){
            if(bill!=null&&codes!=null){
                presenter.reupBill(bill,codes);
            }
        }
    }

    /**
     * 添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
     *
     * @author cg
     */
    class PoponDismissListener implements OnDismissListener {

        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }

    }

    @Override
    public void showAsDropDown(View anchor) {
        super.showAsDropDown(anchor);
        backgroundAlpha(0.7f);
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) context).getWindow()
                .getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0
        ((Activity) context).getWindow().setAttributes(lp);
    }

    public interface DataChange{
        void setBillChange(Bill bill);
        void dialogShow(String title);
        void dialogDismiss();
        void success(String success);
        void err(String err);
    }
}
