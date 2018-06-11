package com.jc.pda.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import com.gyf.barlibrary.OnKeyboardListener;
import com.jc.pda.R;
import com.jc.pda.adapter.BillCodeAdapter;
import com.jc.pda.component.DaggerBillActivityComponent;
import com.jc.pda.database.helper.DealerHelper;
import com.jc.pda.database.helper.ProductHelper;
import com.jc.pda.entity.Bill;
import com.jc.pda.entity.Code;
import com.jc.pda.module.BillActivityModule;
import com.jc.pda.module.ContextModule;
import com.jc.pda.presenter.BillActivityPresenter;
import com.jc.pda.presenter.view.BillActivityView;
import com.jc.pda.utils.Constant;
import com.jc.pda.utils.TimeUtils;
import com.jc.pda.view.HorizontalDividerItemDecoration;
import com.jc.pda.view.UpDataPopupWindow;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;

public class BillActivity extends AppCompatActivity implements BillActivityView, UpDataPopupWindow.DataChange {
    public static final String BILLID = "BILLID";

    @BindView(R.id.tbar_bill)
    Toolbar tbarBill;
    @BindView(R.id.abl_bill)
    AppBarLayout ablBill;
    @BindView(R.id.ctl_bill)
    CollapsingToolbarLayout ctlBill;
    @BindView(R.id.rv_bill_codes)
    RecyclerView rvBillCodes;

    // 单据信息
    @BindView(R.id.tv_bill_billno)
    TextView tvBillBillno;
    @BindView(R.id.tv_bill_billstyle)
    TextView tvBillBillstyle;
    @BindView(R.id.tv_bill_uptype)
    TextView tvBillUptype;
    @BindView(R.id.tv_bill_productname)
    TextView tvBillProductname;
    @BindView(R.id.tv_bill_batch)
    TextView tvBillBatch;
    @BindView(R.id.ll_bill_in)
    LinearLayout llBillIn;
    @BindView(R.id.tv_bill_dealer)
    TextView tvBillDealer;
    @BindView(R.id.ll_bill_outback)
    LinearLayout llBillOutback;
    @BindView(R.id.tv_bill_time)
    TextView tvBillTime;
    @BindView(R.id.tv_bill_oldbillno)
    TextView tvBillOldbillno;

    private ImmersionBar immersionBar;

    @Inject
    BillActivityPresenter presenter;

    private List<Code> list;
    private BillCodeAdapter adapter;

    UpDataPopupWindow pop;
    SweetAlertDialog dialog;

    private Bill bill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        ButterKnife.bind(this);

        immersionBar = ImmersionBar.with(this)
                .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
                .flymeOSStatusBarFontColor(R.color.black)  //修改flyme OS状态栏字体颜色
                .statusBarColor(R.color.white)
                .fitsSystemWindows(true)
                .keyboardEnable(true)  //解决软键盘与底部输入框冲突问题，默认为false，还有一个重载方法，可以指定软键盘mode
                .keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                .setOnKeyboardListener(new OnKeyboardListener() {    //软键盘监听回调
                    @Override
                    public void onKeyboardChange(boolean isPopup, int keyboardHeight) {
                        //isPopup为true，软键盘弹出，为false，软键盘关闭
                    }
                });
        immersionBar.init();  //必须调用方可沉浸式

        //下拉一瞬间颜色
        ctlBill.setContentScrimColor(ContextCompat.getColor(this, R.color.table_title));

        DaggerBillActivityComponent
                .builder()
                .contextModule(new ContextModule(this))
                .billActivityModule(new BillActivityModule(this))
                .build()
                .inject(this);

        rvBillCodes.setLayoutManager(new LinearLayoutManager(this));
        Paint paint = new Paint();
        paint.setStrokeWidth(1);
        paint.setColor(ContextCompat.getColor(this, R.color.divider));
        rvBillCodes.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).paint(paint).build());
        //rvProductProducts.setNestedScrollingEnabled(false);

        list = new ArrayList<>();
        adapter = new BillCodeAdapter(this, list);
        rvBillCodes.setAdapter(adapter);

        pop = new UpDataPopupWindow(this, getBillId());
        pop.setDataChange(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        presenter.getCodes();
        presenter.getBill();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (immersionBar != null) {
            immersionBar.destroy();
        }
    }

    @OnClick(R.id.ll_ac_bill_back)
    public void back() {
        finish();
    }

    @Override
    public void dialogShow(String title) {
        dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.getProgressHelper().setBarColor(ContextCompat.getColor(this, R.color.app_main));
        dialog.setTitleText(title);
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
    public void success(String success) {
        Toasty.success(this, success, Toast.LENGTH_LONG).show();
    }

    @Override
    public void err(String err) {
        Toasty.success(this, err, Toast.LENGTH_LONG).show();
    }

    @Override
    public String getBillId() {
        return getIntent().getStringExtra(BILLID);
    }

    @Override
    public void setCodes(List<Code> codes) {
        if (rvBillCodes != null) {
            list.clear();
            list.addAll(codes);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setBill(Bill bill) {
        if (bill != null) {
            this.bill = bill;
            tvBillBillno.setText(bill.getBillId());
            tvBillBatch.setText(bill.getBatchId());
            tvBillProductname.setText(ProductHelper.getSingleton(this).getProductNameById(bill.getProductId()));
            tvBillDealer.setText(DealerHelper.getSingleton(this).getDealerNameById(bill.getDealerId()));
            tvBillTime.setText(TimeUtils.formatLong(bill.getUpDateInt()));
            tvBillOldbillno.setText(bill.getOldBillNo());

            switch (bill.getBillStyle()) {
                case Constant.IN:
                    tvBillBillstyle.setText("入库");

                    llBillIn.setVisibility(View.VISIBLE);
                    llBillOutback.setVisibility(View.GONE);
                    break;
                case Constant.OUT:
                    tvBillBillstyle.setText("出库");

                    llBillIn.setVisibility(View.GONE);
                    llBillOutback.setVisibility(View.VISIBLE);
                    break;
                case Constant.BACK:
                    tvBillBillstyle.setText("退货");

                    llBillIn.setVisibility(View.GONE);
                    tvBillDealer.setText("退货没有经销商");
                    llBillOutback.setVisibility(View.VISIBLE);
                    break;
            }

            Logger.i(bill.toString());
            if (bill.isUp()) {
                if (bill.getUpType() == Constant.FISTUP) {
                    tvBillUptype.setText("已上传");
                    tvBillUptype.setTextColor(ContextCompat.getColor(this, R.color.chart_up));
                } else {
                    tvBillUptype.setText("重复上传");
                    tvBillUptype.setTextColor(ContextCompat.getColor(this, R.color.chart_reup));
                }
            } else {
                tvBillUptype.setText("未上传");
                tvBillUptype.setTextColor(ContextCompat.getColor(this, R.color.chart_noup));
            }
        }
    }

    @OnClick(R.id.iv_bill_up)
    public void up(View v) {
        pop.showAsDropDown(v);
    }

    @Override
    public void setBillChange(Bill bill) {
        setBill(bill);
    }

    @OnClick(R.id.tv_bill_oldbillno)
    public void oldBill() {
        if(bill!=null&&!TextUtils.isEmpty(bill.getOldBillNo())){
            Intent intent = new Intent();
            intent.setClass(this, BillActivity.class);
            intent.putExtra(BillActivity.BILLID,bill.getOldBillNo());
            startActivity(intent);
        }
    }
}
