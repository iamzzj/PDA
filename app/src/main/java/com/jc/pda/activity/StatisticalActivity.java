package com.jc.pda.activity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.gyf.barlibrary.ImmersionBar;
import com.gyf.barlibrary.OnKeyboardListener;
import com.jc.pda.R;
import com.jc.pda.component.DaggerStatisticalActivityComponent;
import com.jc.pda.entity.BillNearYearCharts;
import com.jc.pda.entity.BillYearCharts;
import com.jc.pda.entity.BillYearMonthCharts;
import com.jc.pda.module.ContextModule;
import com.jc.pda.module.StatisticalActivityModule;
import com.jc.pda.presenter.StatisticalActivityPresenter;
import com.jc.pda.presenter.view.StatisticalActivityView;
import com.jc.pda.utils.Constant;
import com.jc.pda.utils.Global;
import com.jc.pda.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StatisticalActivity extends AppCompatActivity implements StatisticalActivityView {
    @BindView(R.id.tv_statistical_time)
    TextView tvTime;
    @BindView(R.id.v_statistical_bar)
    View vStatisticalBar;
    @BindView(R.id.tv_statistical_year_table_time)
    TextView tvYearTableTime;
    @BindView(R.id.lc_statistical_table)
    LineChart lcTable;
    @BindView(R.id.tv_statistical_choose)
    TextView tvChoose;
    @BindView(R.id.srl_statistical_refresh)
    SwipeRefreshLayout srlStatisticalRefresh;
    @BindView(R.id.pc_statistical_allyear_table)
    PieChart pcTable;
    @BindView(R.id.bc_statistical_nearyear_table)
    BarChart bcTable;

    private ImmersionBar immersionBar;

    @Inject
    StatisticalActivityPresenter presenter;

    private TimePickerView tpv;
    Calendar calendar = Calendar.getInstance();
    private OptionsPickerView optionChoose;
    private String[] types = {"全部", "未上传", "已上传", "重复上传"};
    private String[] names = {"入库", "出库", "退货"};
    private List<String> options1Items = new ArrayList<>();

    private int upType = -1;
    private boolean isUp = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistical);
        ButterKnife.bind(this);

        DaggerStatisticalActivityComponent.builder()
                .contextModule(new ContextModule(this))
                .statisticalActivityModule(new StatisticalActivityModule(this))
                .build()
                .inject(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            vStatisticalBar.setVisibility(View.VISIBLE);
        }

        immersionBar = ImmersionBar.with(this)
                .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
                .flymeOSStatusBarFontColor(R.color.black)  //修改flyme OS状态栏字体颜色
                .keyboardEnable(true)  //解决软键盘与底部输入框冲突问题，默认为false，还有一个重载方法，可以指定软键盘mode
                .keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                .setOnKeyboardListener(new OnKeyboardListener() {    //软键盘监听回调
                    @Override
                    public void onKeyboardChange(boolean isPopup, int keyboardHeight) {
                        //isPopup为true，软键盘弹出，为false，软键盘关闭
                    }
                });
        immersionBar.init();  //必须调用方可沉浸式

        srlStatisticalRefresh.setColorSchemeResources(R.color.app_main);
        srlStatisticalRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (presenter != null) {
                    presenter.getYearMonthBillCount();
                    presenter.getYearBillPcCount();
                }
            }
        });

        tpv = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                tvTime.setText(TimeUtils.formatDateYear(date));

                tvYearTableTime.setText(TimeUtils.formatDateYear(date) + "年每月订单数量");

                if (presenter != null) {
                    presenter.getYearMonthBillCount();
                    presenter.getYearBillPcCount();
                }

            }
        })
                .setType(new boolean[]{true, false, false, false, false, false})
                .setLabel("年", "月", "日", "", "", "")
                .setSubmitColor(ContextCompat.getColor(this, R.color.app_main))
                .setCancelColor(ContextCompat.getColor(this, R.color.app_main))
                .isCenterLabel(false)
                .isDialog(false)
                .build();

        tvTime.setText(TimeUtils.formatDateYear(calendar.getTime()));
        tvYearTableTime.setText(TimeUtils.formatDateYear(calendar.getTime()) + "年每月订单数量");

        for (String s : types) {
            options1Items.add(s);
        }
        optionChoose = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置

                if (options1 < options1Items.size() && options1 >= 0) {

                    tvChoose.setText(options1Items.get(options1));
                    if (types[1].equals(options1Items.get(options1))) {
                        //未
                        upType = Constant.FISTUP;
                        isUp = false;
                    } else if (types[2].equals(options1Items.get(options1))) {
                        //已
                        upType = Constant.FISTUP;
                        isUp = true;
                    } else if (types[3].equals(options1Items.get(options1))) {
                        //重复
                        upType = Constant.REUP;
                        isUp = true;
                    } else {
                        tvChoose.setText("类型");

                        upType = -1;
                        isUp = false;
                    }

                    if (presenter != null) {
                        presenter.getYearMonthBillCount();
                        presenter.getYearBillPcCount();
                    }

                }

            }
        })
                .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("")//标题
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(ContextCompat.getColor(this, R.color.app_main))//确定按钮文字颜色
                .setCancelColor(ContextCompat.getColor(this, R.color.app_main))//取消按钮文字颜色
                .setLabels("", "", "")//设置选择的三级单位
                .isCenterLabel(true) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setCyclic(false, false, false)//循环与否
                .isDialog(false)
                .setSelectOptions(0, 0, 0)  //设置默认选中项
                .setOutSideCancelable(true)//点击外部dismiss default true
                .build();
        optionChoose.setPicker(options1Items);

        setYearLcTable();
        setAllYearPcTable();

        //setNearYearBcTable();
        //setNearYearBillBcCount(new BillNearYearCharts());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (presenter != null) {
            presenter.getYearMonthBillCount();
            presenter.getYearBillPcCount();
        }
    }

    /**
     * 设置第一个折线图
     */
    private void setYearLcTable() {
        //描述信息
        Description description = new Description();
        description.setText("月份");
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
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //设置图例的形状 有圆形、正方形、线
        legend.setForm(Legend.LegendForm.CIRCLE);
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
        xAxis.setLabelCount(12, true);
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

        //====================设置左边的Y轴===============
        YAxis axisLeft = lcTable.getAxisLeft();
        //是否启用右边Y轴
        axisLeft.setEnabled(true);
        axisLeft.setAxisMinimum(0);
        //设置横向的线为虚线
        axisLeft.enableGridDashedLine(10f, 10f, 0f);

        //====================设置右边的Y轴===============
        YAxis axisRight = lcTable.getAxisRight();
        //是否启用右边Y轴
        axisRight.setEnabled(false);
        axisRight.enableGridDashedLine(10f, 10f, 0f);
    }

    /**
     * 设置第二个饼状图
     */
    private void setAllYearPcTable() {
        // 设置饼图是否接收点击事件，默认为true
        pcTable.setTouchEnabled(true);
        //设置饼图是否使用百分比
        pcTable.setUsePercentValues(true);

        //是否显示圆盘中间文字，默认显示
        pcTable.setDrawCenterText(true);
        //设置圆盘中间文字
        pcTable.setCenterText(Global.NAME);
        //设置圆盘中间文字的大小
        pcTable.setCenterTextSize(20);
        //设置圆盘中间文字的颜色
        pcTable.setCenterTextColor(ContextCompat.getColor(this, R.color.app_main));
        //设置圆盘中间文字的字体
        pcTable.setCenterTextTypeface(Typeface.DEFAULT);

        //设置中间圆盘的颜色
        pcTable.setHoleColor(ContextCompat.getColor(this, R.color.white));
        //设置中间圆盘的半径,值为所占饼图的百分比
        pcTable.setHoleRadius(40);

        //设置中间透明圈的半径,值为所占饼图的百分比
        pcTable.setTransparentCircleRadius(50);
        pcTable.setTransparentCircleAlpha(200);

        //是否显示饼图中间空白区域，默认显示
        pcTable.setDrawHoleEnabled(true);
        //设置圆盘是否转动，默认转动
        pcTable.setRotationEnabled(true);
        //设置初始旋转角度
        pcTable.setRotationAngle(0);

        //设置比例图
        Legend mLegend = pcTable.getLegend();
        //设置比例图显示在饼图的哪个位置
        //mLegend.setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);
        mLegend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        mLegend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        //设置比例图的形状，默认是方形,可为方形、圆形、线性
        mLegend.setForm(Legend.LegendForm.CIRCLE);
//        mLegend.setXEntrySpace(7f);
//        mLegend.setYEntrySpace(5f);

        //设置X轴动画
        pcTable.animateX(1800);
//        //设置y轴动画
//        pieChart.animateY(1800);
//        //设置xy轴一起的动画
//        pieChart.animateXY(1800, 1800);

        // 设置一个选中区域监听
        pcTable.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, Highlight h) {

            }

            @Override
            public void onNothingSelected() {
            }
        });
    }

    private void setNearYearBcTable() {
        //描述信息
        Description description = new Description();
        description.setText("年份");
        bcTable.setDescription(description);
        //设置chart是否可以触摸
        bcTable.setTouchEnabled(false);
        //设置是否可以拖拽
        bcTable.setDragEnabled(true);
        //设置是否可以缩放 x和y，默认true
        bcTable.setScaleEnabled(true);
        //设置是否可以通过双击屏幕放大图表。默认是true
        bcTable.setDoubleTapToZoomEnabled(false);
        //设置chart边界线的宽度，单位dp
        bcTable.setBorderWidth(6);
        //启用禁用绘制图表边框（chart周围的线）
        bcTable.setDrawBorders(false);

        bcTable.setDrawBarShadow(false);//true绘画的Bar有阴影。
        bcTable.setDrawValueAboveBar(true);//true文字绘画在bar上

        //=========================设置图例=========================
        // 像"□ xxx"就是图例
        Legend legend = bcTable.getLegend();
        //设置图例显示在chart那个位置 setPosition建议放弃使用了
        //设置垂直方向上还是下或中
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        //设置水平方向是左边还是右边或中
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        //设置所有图例位置排序方向
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //设置图例的形状 有圆形、正方形、线
        legend.setForm(Legend.LegendForm.CIRCLE);
        //是否支持自动换行 目前只支持BelowChartLeft, BelowChartRight, BelowChartCenter
        legend.setWordWrapEnabled(false);

        //=======================设置X轴显示效果==================
        XAxis xAxis = bcTable.getXAxis();
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
        xAxis.setLabelCount(5, true);
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

        //====================设置左边的Y轴===============
        YAxis axisLeft = bcTable.getAxisLeft();
        //是否启用右边Y轴
        axisLeft.setEnabled(true);
        axisLeft.setAxisMinimum(0);
        //设置横向的线为虚线
        axisLeft.enableGridDashedLine(10f, 10f, 0f);

        //====================设置右边的Y轴===============
        YAxis axisRight = bcTable.getAxisRight();
        //是否启用右边Y轴
        axisRight.setEnabled(false);
        axisRight.enableGridDashedLine(10f, 10f, 0f);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (immersionBar != null) {
            immersionBar.destroy();
        }
    }

    @OnClick(R.id.ll_statistical_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.ll_statistical_time)
    public void time() {
        if (tpv != null) {
            tpv.show();
        }
    }


    @Override
    public String getTime() {
        return tvTime.getText().toString();
    }

    /**
     * 加载折线图数据
     *
     * @param billCharts
     */
    @Override
    public void setYearMonthBillCount(final BillYearMonthCharts billCharts) {
        if (billCharts != null) {
            XAxis xAxis = lcTable.getXAxis();
            xAxis.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    int xIndex = new Float(value).intValue();

                    if (xIndex < billCharts.getDates().size()) {
                        return TimeUtils.changeDateY2M(billCharts.getDates().get(xIndex));
                    } else {
                        return "";
                    }
                }
            });

            // 入库
            List<Entry> noUpBill = new ArrayList<Entry>();
            for (int i = 0; i < billCharts.getInBillCounts().size(); i++) {
                noUpBill.add(new Entry(i, billCharts.getInBillCounts().get(i)));
            }
            LineDataSet linenoUp = new LineDataSet(noUpBill, names[0]);
            linenoUp.setColor(ContextCompat.getColor(this, R.color.chart_in));
            linenoUp.setValueTextColor(ContextCompat.getColor(this, R.color.chart_in));
            //设置线一面部分是否填充颜色
            linenoUp.setDrawFilled(false);
            //设置填充的颜色
            linenoUp.setFillColor(ContextCompat.getColor(this, R.color.chart_in));
            //设置是否画圆
            linenoUp.setDrawCircles(false);
            // 设置平滑曲线模式
            linenoUp.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
            //设置是否显示点的坐标值
            linenoUp.setDrawValues(false);

            // 出库
            List<Entry> upBill = new ArrayList<Entry>();
            for (int i = 0; i < billCharts.getOutBillCounts().size(); i++) {
                upBill.add(new Entry(i, billCharts.getOutBillCounts().get(i)));
            }
            LineDataSet lineup = new LineDataSet(upBill, names[1]);
            lineup.setColor(ContextCompat.getColor(this, R.color.chart_out));
            lineup.setValueTextColor(ContextCompat.getColor(this, R.color.chart_out));
            //设置线一面部分是否填充颜色
            lineup.setDrawFilled(false);
            //设置填充的颜色
            lineup.setFillColor(ContextCompat.getColor(this, R.color.chart_out));
            //设置是否画圆
            lineup.setDrawCircles(false);
            // 设置平滑曲线模式
            lineup.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
            //设置是否显示点的坐标值
            lineup.setDrawValues(false);

            // 退货
            List<Entry> reUpBill = new ArrayList<Entry>();
            for (int i = 0; i < billCharts.getBackBillCounts().size(); i++) {
                reUpBill.add(new Entry(i, billCharts.getBackBillCounts().get(i)));
            }
            LineDataSet linereup = new LineDataSet(reUpBill, names[2]);
            linereup.setColor(ContextCompat.getColor(this, R.color.chart_back));
            linereup.setValueTextColor(ContextCompat.getColor(this, R.color.chart_back));
            //设置线一面部分是否填充颜色
            linereup.setDrawFilled(false);
            //设置填充的颜色
            linereup.setFillColor(ContextCompat.getColor(this, R.color.chart_back));
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

    /**
     * 设置饼状图
     *
     * @param billYearCharts
     */
    @Override
    public void setYearBillPcCount(BillYearCharts billYearCharts) {
        if (billYearCharts != null) {
            //设置饼图右下角的文字描述
            Description description = new Description();
            description.setText(getTime() + "年度订单数量");
            description.setTextSize(15);
            pcTable.setDescription(description);

            /**
             * valueList将一个饼形图分成三部分，各个区域的百分比的值
             * Entry构造函数中
             * 第一个值代表所占比例，
             * 第二个值代表区域位置
             * （可以有第三个参数，表示携带的数据object）这里没用到
             */
            List<PieEntry> valueList = new ArrayList<PieEntry>();
            valueList.add(new PieEntry(billYearCharts.getIn(), names[0]));
            valueList.add(new PieEntry(billYearCharts.getOut(), names[1]));
            valueList.add(new PieEntry(billYearCharts.getBack(), names[2]));

            //显示在比例图上
            PieDataSet dataSet = new PieDataSet(valueList, names[0] + ":" + billYearCharts.getIn() + " " + names[1] + ":" + billYearCharts.getOut() + " " + names[2] + ":" + billYearCharts.getBack());
            //设置个饼状图之间的距离
            dataSet.setSliceSpace(3f);
            // 部分区域被选中时多出的长度
            dataSet.setSelectionShift(8f);

            // 设置饼图各个区域颜色
            ArrayList<Integer> colors = new ArrayList<Integer>();
            colors.add(ContextCompat.getColor(this, R.color.chart_in));
            colors.add(ContextCompat.getColor(this, R.color.chart_out));
            colors.add(ContextCompat.getColor(this, R.color.chart_back));
            dataSet.setColors(colors);

            PieData data = new PieData(dataSet);
            //设置以百分比显示
            data.setValueFormatter(new PercentFormatter());
            //区域文字的大小
            data.setValueTextSize(11f);
            //设置区域文字的颜色
            data.setValueTextColor(Color.WHITE);
            //设置区域文字的字体
            data.setValueTypeface(Typeface.DEFAULT);

            pcTable.setData(data);

            //设置是否显示区域文字内容
            //pcTable.setDrawSliceText(pcTable.isDrawSliceTextEnabled());
            pcTable.setDrawEntryLabels(pcTable.isDrawSlicesUnderHoleEnabled());
            //设置是否显示区域百分比的值
            for (IDataSet<?> set : pcTable.getData().getDataSets()) {
                set.setDrawValues(set.isDrawValuesEnabled());
            }
            // undo all highlights
            pcTable.highlightValues(null);
            pcTable.invalidate();
        }
    }

    /**
     * 设置条形图
     *
     * @param billNearYearCharts
     */
    @Override
    public void setNearYearBillBcCount(BillNearYearCharts billNearYearCharts) {
        if (billNearYearCharts != null) {
            ArrayList<BarEntry> yVals = new ArrayList<>();//Y轴方向第一组数组
            ArrayList<BarEntry> yVals2 = new ArrayList<>();//Y轴方向第二组数组
            ArrayList<BarEntry> yVals3 = new ArrayList<>();//Y轴方向第三组数组

            for (int i = 0; i < 5; i++) {//添加数据源
                yVals.add(new BarEntry(i,new Random().nextInt(10000), (i + 1) + "月"));
                yVals2.add(new BarEntry(i,new Random().nextInt(10000), (i + 1) + "月"));
                yVals3.add(new BarEntry(i,new Random().nextInt(10000), (i + 1) + "月"));
            }

            BarDataSet barDataSet = new BarDataSet(yVals, "小明每月支出");
            barDataSet.setColor(ContextCompat.getColor(this, R.color.chart_in));//设置第一组数据颜色

            BarDataSet barDataSet2 = new BarDataSet(yVals2, "小花每月支出");
            barDataSet2.setColor(ContextCompat.getColor(this, R.color.chart_out));//设置第二组数据颜色

            BarDataSet barDataSet3 = new BarDataSet(yVals3, "小蔡每月支出");
            barDataSet3.setColor(ContextCompat.getColor(this, R.color.chart_back));//设置第三组数据颜色

            ArrayList<IBarDataSet> threebardata = new ArrayList<>();//IBarDataSet 接口很关键，是添加多组数据的关键结构，LineChart也是可以采用对应的接口类，也可以添加多组数据
            threebardata.add(barDataSet);
            threebardata.add(barDataSet2);
            threebardata.add(barDataSet3);

            BarData bardata = new BarData(threebardata);
            bcTable.setData(bardata);

            bcTable.animateXY(1000, 2000);//设置动画

            bcTable.setData(bardata);
            bcTable.invalidate();
        }
    }


    @Override
    public int getUpType() {
        return upType;
    }

    @Override
    public boolean getIsUp() {
        return isUp;
    }

    @Override
    public void refreshDismiss() {
        if (srlStatisticalRefresh != null) {
            if (srlStatisticalRefresh.isRefreshing()) {
                srlStatisticalRefresh.setRefreshing(false);
            }
        }
    }

    @OnClick(R.id.ll_statistical_choose)
    public void choose() {
        if (options1Items != null && optionChoose != null) {
            optionChoose.show();
        }
    }
}
