package com.jc.pda.activity;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.gyf.barlibrary.OnKeyboardListener;
import com.jc.pda.R;
import com.jc.pda.adapter.BatchAdapter;
import com.jc.pda.component.DaggerBatchActivityComponent;
import com.jc.pda.entity.Batch;
import com.jc.pda.module.BatchActivityModule;
import com.jc.pda.module.ContextModule;
import com.jc.pda.presenter.BatchActivityPresenter;
import com.jc.pda.presenter.view.BatchActivityView;
import com.jc.pda.view.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BatchActivity extends AppCompatActivity implements BatchActivityView{
    public static final String PRODUCTID = "intent_product_id";
    public static final String PRODUCTNAME = "intent_product_name";
    @BindView(R.id.tv_batch_product_name)
    TextView tvBatchProductName;
    @BindView(R.id.rv_batch_recyclerview)
    RecyclerView rvBatch;
    @BindView(R.id.srl_batch_swiperefreshlayout)
    SwipeRefreshLayout srlBatch;

    private ImmersionBar immersionBar;

    @Inject
    BatchActivityPresenter presenter;

    private BatchAdapter adapter;
    private List<Batch> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch);
        ButterKnife.bind(this);

        tvBatchProductName.setText("产品名称："+getIntent().getStringExtra(PRODUCTNAME));

        DaggerBatchActivityComponent.builder()
                .contextModule(new ContextModule(this))
                .batchActivityModule(new BatchActivityModule(this))
                .build()
                .inject(this);

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

        srlBatch.setColorSchemeResources(R.color.app_main);
        srlBatch.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getBatchs();
            }
        });

        rvBatch.setLayoutManager(new LinearLayoutManager(this));
        Paint paint = new Paint();
        paint.setStrokeWidth(1);
        paint.setColor(ContextCompat.getColor(this, R.color.divider));
        rvBatch.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).paint(paint).build());
        //rvProductProducts.setNestedScrollingEnabled(false);

        list = new ArrayList<>();
        adapter = new BatchAdapter(this,list);
        rvBatch.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();

        presenter.getBatchs();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (immersionBar != null)
            immersionBar.destroy();
    }

    @OnClick(R.id.ll_ac_batch_back)
    public void back() {
        finish();
    }

    @Override
    public String getProductId() {
        return getIntent().getStringExtra(PRODUCTID);
    }

    @Override
    public void dialogDismiss() {
        if(srlBatch.isRefreshing()){
            srlBatch.setRefreshing(false);
        }
    }

    @Override
    public void setBatchs(List<Batch> batchs) {
        if(batchs!=null){
            list.clear();
            list.addAll(batchs);
            adapter.notifyDataSetChanged();
        }
    }
}
