package com.jc.pda.tableFragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.jc.pda.R;
import com.jc.pda.activity.BillActivity;
import com.jc.pda.adapter.BillInAdapter;
import com.jc.pda.component.DaggerInFragmentComponent;
import com.jc.pda.entity.Batch;
import com.jc.pda.entity.Bill;
import com.jc.pda.entity.BillCharts;
import com.jc.pda.entity.Product;
import com.jc.pda.entity.ProductAndBatch;
import com.jc.pda.module.ContextModule;
import com.jc.pda.module.InFragmentModule;
import com.jc.pda.presenter.InFragmentPresenter;
import com.jc.pda.presenter.view.InFragmentView;
import com.jc.pda.utils.TimeUtils;
import com.jc.pda.view.HorizontalDividerItemDecoration;
import com.orhanobut.logger.Logger;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by z on 2017/12/20.
 */

public class InFragment extends Fragment implements InFragmentView, BillInAdapter.OnItemClickListener {

    @BindView(R.id.tbar_in)
    Toolbar tbarIn;
    @BindView(R.id.ctl_in)
    CollapsingToolbarLayout ctlIn;
    @BindView(R.id.abl_in)
    AppBarLayout ablIn;

    Unbinder unbinder;
    @BindView(R.id.tv_in_time)
    TextView tvTime;
    @BindView(R.id.lc_in_table)
    LineChart lcTable;
    @BindView(R.id.rv_in_bills)
    RecyclerView rvInBills;
    @BindView(R.id.tv_in_product)
    TextView tvProduct;
    @BindView(R.id.tv_in_batch)
    TextView tvBatch;

    private TimePickerView tpv;
    Calendar calendar = Calendar.getInstance();

    private List<Bill> list;
    private BillInAdapter adapter;

    @Inject
    InFragmentPresenter presenter;

    //选项
    private OptionsPickerView optionProduct;
    private List<String> options1Items = new ArrayList<>();
    private List<List<String>> options2Items = new ArrayList<>();
    private List<List<List<String>>> options3Items = new ArrayList<>();

    private List<Product> products = new ArrayList<>();
    private List<List<Batch>> batchs = new ArrayList<>();

    private String productId, batchId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerInFragmentComponent
                .builder()
                .contextModule(new ContextModule(getActivity()))
                .inFragmentModule(new InFragmentModule(this))
                .build()
                .inject(this);

        tpv = new TimePickerView.Builder(getActivity(), new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                tvTime.setText(TimeUtils.formatDate(date));

                if (presenter != null) {
                    presenter.getAllBills();
                    presenter.getBillCount();
                }
            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("年", "月", "日", "", "", "")
                .setSubmitColor(ContextCompat.getColor(getActivity(), R.color.app_main))
                .setCancelColor(ContextCompat.getColor(getActivity(), R.color.app_main))
                .isCenterLabel(true)
                .isDialog(false)
                .build();

        optionProduct = new OptionsPickerView.Builder(getActivity(), new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置

                if (options1 < options1Items.size()&&options1 >=0)
                    tvProduct.setText(options1Items.get(options1));
                if (options1 < options2Items.size() && options2 < options2Items.get(options1).size()&&options1 >=0&& options2>=0)
                    tvBatch.setText(options2Items.get(options1).get(options2));

                if (options1 < products.size()&&options1 >=0)
                    productId = products.get(options1).getProductNo();
                if (options1 < batchs.size() && options2 < batchs.get(options1).size()&&options1 >=0&& options2>=0)
                    batchId = batchs.get(options1).get(options2).getBatchNo();

                Logger.i(productId + "--" + batchId);
                if(TextUtils.isEmpty(productId)){
                    tvProduct.setText("产品");
                }
                if(TextUtils.isEmpty(batchId)){
                    tvBatch.setText("批号");
                }

                if (presenter != null) {
                    presenter.getAllBills();
                }
            }
        })
                .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("")//标题
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(ContextCompat.getColor(getActivity(), R.color.app_main))//确定按钮文字颜色
                .setCancelColor(ContextCompat.getColor(getActivity(), R.color.app_main))//取消按钮文字颜色
                .setLabels("", "", "")//设置选择的三级单位
                .isCenterLabel(true) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setCyclic(false, false, false)//循环与否
                .isDialog(false)
                .setSelectOptions(0, 0, 0)  //设置默认选中项
                .setOutSideCancelable(true)//点击外部dismiss default true
                .build();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_in, container, false);
        unbinder = ButterKnife.bind(this, view);

        //下拉一瞬间颜色
        ctlIn.setContentScrimColor(ContextCompat.getColor(getActivity(), R.color.white));

        //date
        tvTime.setText(TimeUtils.formatDate(calendar.getTime()));

        rvInBills.setLayoutManager(new LinearLayoutManager(getActivity()));
        Paint paint = new Paint();
        paint.setStrokeWidth(1);
        paint.setColor(ContextCompat.getColor(getActivity(), R.color.divider));
        rvInBills.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).paint(paint).build());
        //rvProductProducts.setNestedScrollingEnabled(false);

        list = new ArrayList<>();
        adapter = new BillInAdapter(getActivity(), list);
        adapter.setOnItemClickListener(this);
        rvInBills.setAdapter(adapter);

        setTable();

        presenter.getProductAndBatch();

        return view;
    }

    private void setTable() {
        //描述信息
        Description description = new Description();
        description.setText("时间段");
        lcTable.setDescription(description);
        //设置chart是否可以触摸
        lcTable.setTouchEnabled(false);
        //设置是否可以拖拽
        lcTable.setDragEnabled(false);
        //设置是否可以缩放 x和y，默认true
        lcTable.setScaleEnabled(false);
        //设置是否可以通过双击屏幕放大图表。默认是true
        lcTable.setDoubleTapToZoomEnabled(false);
        //设置chart边界线的宽度，单位dp
        lcTable.setBorderWidth(6);
        //启用/禁用绘制图表边框（chart周围的线）
        lcTable.setDrawBorders(false);

        //=========================设置图例=========================
        // 像"□ xxx"就是图例
        Legend legend = lcTable.getLegend();
        //设置图例显示在chart那个位置 setPosition建议放弃使用了
        //设置垂直方向上还是下或中
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        //设置水平方向是左边还是右边或中
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        //设置所有图例位置排序方向
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        //设置图例的形状 有圆形、正方形、线
        legend.setForm(Legend.LegendForm.LINE);
        //是否支持自动换行 目前只支持BelowChartLeft, BelowChartRight, BelowChartCenter
        legend.setWordWrapEnabled(false);

        //=======================设置X轴显示效果==================
        XAxis xAxis = lcTable.getXAxis();
        //是否启用X轴
        xAxis.setEnabled(true);
        //是否绘制X轴线
        xAxis.setDrawAxisLine(true);
        //设置X轴上每个竖线是否显示
        xAxis.setDrawGridLines(true);
        //设置是否绘制X轴上的对应值(标签)
        xAxis.setDrawLabels(true);
        //设置X轴显示位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //设置竖线为虚线样式
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        //设置x轴标签数
        xAxis.setLabelCount(7, true);
        //图表第一个和最后一个label数据不超出左边和右边的Y轴
        // xAxis.setAvoidFirstLastClipping(true);
        /*xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int xIndex = new Float(value).intValue();

                if (xIndex < xDatas.length) {
                    return xDatas[xIndex];
                } else {
                    return "";
                }
            }
        });*/

        //====================设置右边的Y轴===============
        YAxis axisLeft = lcTable.getAxisLeft();
        //是否启用右边Y轴
        axisLeft.setEnabled(true);
        //设置横向的线为虚线
        axisLeft.enableGridDashedLine(10f, 10f, 0f);

        //====================设置右边的Y轴===============
        YAxis axisRight = lcTable.getAxisRight();
        //是否启用右边Y轴
        axisRight.setEnabled(false);
        axisRight.enableGridDashedLine(10f, 10f, 0f);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (presenter != null) {
            presenter.getAllBills();
            presenter.getBillCount();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null)
            unbinder.unbind();

        if(presenter!=null){
            presenter.unregister();
        }
    }

    @OnClick(R.id.ll_in_time)
    public void time() {
        if (tpv != null) {
            try {
                calendar.setTime(TimeUtils.parseDate(tvTime.getText().toString()));
                tpv.setDate(calendar);
                tpv.show();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick(R.id.ll_in_choose)
    public void choose() {
        if (optionProduct != null) {
            optionProduct.show();
        }
    }

    @Override
    public String getTime() {
        return tvTime.getText().toString();
    }

    @Override
    public String getProductId() {
        return productId;
    }

    @Override
    public String getBatchId() {
        return batchId;
    }

    @Override
    public void setBills(List<Bill> bills) {
        if (bills != null) {
            list.clear();
            list.addAll(bills);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setBillCharts(final BillCharts billCharts) {
        if (billCharts != null) {
            XAxis xAxis = lcTable.getXAxis();
            xAxis.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    int xIndex = new Float(value).intValue();

                    if (xIndex < billCharts.getDates().size()) {
                        return TimeUtils.changeDate(billCharts.getDates().get(xIndex));
                    } else {
                        return "";
                    }
                }
            });

            // 未上传
            List<Entry> noUpBill = new ArrayList<Entry>();
            for (int i = 0; i < billCharts.getNoUpBillCounts().size(); i++) {
                noUpBill.add(new Entry(i, billCharts.getNoUpBillCounts().get(i)));
            }
            LineDataSet linenoUp = new LineDataSet(noUpBill, "未上传");
            linenoUp.setColor(ContextCompat.getColor(getActivity(), R.color.chart_noup));
            linenoUp.setValueTextColor(ContextCompat.getColor(getActivity(), R.color.chart_noup));
            //设置线一面部分是否填充颜色
            linenoUp.setDrawFilled(false);
            //设置填充的颜色
            linenoUp.setFillColor(ContextCompat.getColor(getActivity(), R.color.chart_noup));
            //设置是否画圆
            linenoUp.setDrawCircles(false);
            // 设置平滑曲线模式
            linenoUp.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
            //设置是否显示点的坐标值
            linenoUp.setDrawValues(false);

            // 已上传
            List<Entry> upBill = new ArrayList<Entry>();
            for (int i = 0; i < billCharts.getUpBillCounts().size(); i++) {
                upBill.add(new Entry(i, billCharts.getUpBillCounts().get(i)));
            }
            LineDataSet lineup = new LineDataSet(upBill, "已上传");
            lineup.setColor(ContextCompat.getColor(getActivity(), R.color.chart_up));
            lineup.setValueTextColor(ContextCompat.getColor(getActivity(), R.color.chart_up));
            //设置线一面部分是否填充颜色
            lineup.setDrawFilled(true);
            //设置填充的颜色
            lineup.setFillColor(ContextCompat.getColor(getActivity(), R.color.chart_up));
            //设置是否画圆
            lineup.setDrawCircles(false);
            // 设置平滑曲线模式
            lineup.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
            //设置是否显示点的坐标值
            lineup.setDrawValues(false);

            // 重复上传
            List<Entry> reUpBill = new ArrayList<Entry>();
            for (int i = 0; i < billCharts.getReUpBillCounts().size(); i++) {
                reUpBill.add(new Entry(i, billCharts.getReUpBillCounts().get(i)));
            }
            LineDataSet linereup = new LineDataSet(reUpBill, "重复上传");
            linereup.setColor(ContextCompat.getColor(getActivity(), R.color.chart_reup));
            linereup.setValueTextColor(ContextCompat.getColor(getActivity(), R.color.chart_reup));
            //设置线一面部分是否填充颜色
            linereup.setDrawFilled(false);
            //设置填充的颜色
            linereup.setFillColor(ContextCompat.getColor(getActivity(), R.color.chart_reup));
            //设置是否画圆
            linereup.setDrawCircles(false);
            // 设置平滑曲线模式
            linereup.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
            //设置是否显示点的坐标值
            linereup.setDrawValues(false);

         /*all*/
            List<ILineDataSet> dataSets = new ArrayList<>();
            //dataSets.add(lineDataSet);
            dataSets.add(linenoUp);
            dataSets.add(lineup);
            dataSets.add(linereup);

            LineData lineData = new LineData(dataSets);
            lcTable.setData(lineData);
            lcTable.invalidate();

        }
    }

    @Override
    public void setProdutAndBatch(ProductAndBatch produtAndBatch) {
        if (produtAndBatch != null) {
            options1Items = produtAndBatch.getProductStrings();
            options2Items = produtAndBatch.getBatchStrings();
            if (optionProduct != null) {
                optionProduct.setPicker(produtAndBatch.getProductStrings(), produtAndBatch.getBatchStrings());
            }

            products = produtAndBatch.getProducts();
            batchs = produtAndBatch.getBatchs();
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), BillActivity.class);
        intent.putExtra(BillActivity.BILLID,list.get(position).getBillId());
        getActivity().startActivity(intent);
    }
}
