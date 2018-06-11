package com.jc.pda.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.gyf.barlibrary.ImmersionBar;
import com.gyf.barlibrary.OnKeyboardListener;
import com.jauker.widget.BadgeView;
import com.jc.pda.R;
import com.jc.pda.entity.Code;
import com.suke.widget.SwitchButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import me.dm7.barcodescanner.core.IViewFinder;
import me.dm7.barcodescanner.core.ViewFinderView;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanCodeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    public static final int SCAN_CODE_RESULT_SINGLE = 12341234;
    public static final int SCAN_CODE_RESULT_MULTIPLE = 67896789;
    public static final String CODE = "CODE";
    @BindView(R.id.content_frame)
    FrameLayout contentFrame;
    @BindView(R.id.sb_scan_code_type)
    SwitchButton sbType;
    @BindView(R.id.tv_scan_code_save)
    TextView tvSave;
    BadgeView badge;

    private ZXingScannerView mScannerView;

    private ImmersionBar immersionBar;

    private ArrayList<String> codes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_code);
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

        mScannerView = new ZXingScannerView(this) {
            @Override
            protected IViewFinder createViewFinderView(Context context) {
                return new CustomViewFinderView(context);
            }
        };
        contentFrame.addView(mScannerView);

        sbType.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    tvSave.setVisibility(View.GONE);
                    badge.setVisibility(View.GONE);
                } else {
                    tvSave.setVisibility(View.VISIBLE);
                    badge.setVisibility(View.VISIBLE);
                }
            }
        });

        badge = new BadgeView(this);
        badge.setVisibility(View.GONE);
        badge.setTargetView(tvSave);
        badge.setBadgeCount(0);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        /*Toast.makeText(this, "Contents = " + rawResult.getText() +
                ", Format = " + rawResult.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();*/
        String scanResult = rawResult.getText();

        // Note:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        // * I don't know why this is the case but I don't have the time to figure out.

        if (getCheckedType()) {
            Intent intent = new Intent();
            intent.putExtra(CODE, scanResult);
            setResult(SCAN_CODE_RESULT_SINGLE, intent);
            finish();
        } else {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScannerView.resumeCameraPreview(ScanCodeActivity.this);
                }
            }, 1500);

            if(haveCodeInList(codes,scanResult)){
                Toasty.error(this,"已添加该条码",Toast.LENGTH_LONG).show();
            }else{
                codes.add(scanResult);
                badge.setBadgeCount(codes.size());
            }
        }
    }

    private boolean haveCodeInList(List<String> codes,String code){
        for (String c : codes){
            if(c!=null){
                if (c.equals(code)){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * false 多个
     * true 单个
     *
     * @return
     */
    private boolean getCheckedType() {
        return sbType.isChecked();
    }

    @OnClick(R.id.tv_scan_code_save)
    public void save() {
        if(codes.size()>0){
            Intent intent = new Intent();
            intent.putStringArrayListExtra(CODE,codes);
            setResult(SCAN_CODE_RESULT_MULTIPLE, intent);
            finish();
        }else{
            Toasty.error(this,"列表中没有条码",Toast.LENGTH_LONG).show();
        }
    }

    private static class CustomViewFinderView extends ViewFinderView {
        public static final String TRADE_MARK_TEXT = "";
        public static final int TRADE_MARK_TEXT_SIZE_SP = 40;
        public final Paint PAINT = new Paint();

        public CustomViewFinderView(Context context) {
            super(context);
            init();
        }

        public CustomViewFinderView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        private void init() {
            PAINT.setColor(Color.WHITE);
            PAINT.setAntiAlias(true);
            float textPixelSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                    TRADE_MARK_TEXT_SIZE_SP, getResources().getDisplayMetrics());
            PAINT.setTextSize(textPixelSize);
            setSquareViewFinder(true);
        }

        @Override
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            drawTradeMark(canvas);
        }

        private void drawTradeMark(Canvas canvas) {
            Rect framingRect = getFramingRect();
            float tradeMarkTop;
            float tradeMarkLeft;
            if (framingRect != null) {
                tradeMarkTop = framingRect.bottom + PAINT.getTextSize() + 10;
                tradeMarkLeft = framingRect.left;
            } else {
                tradeMarkTop = 10;
                tradeMarkLeft = canvas.getHeight() - PAINT.getTextSize() - 10;
            }
            canvas.drawText(TRADE_MARK_TEXT, tradeMarkLeft, tradeMarkTop, PAINT);
        }
    }

    @OnClick(R.id.ll_scan_cod_back)
    public void back() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (immersionBar != null) {
            immersionBar.destroy();
        }
    }
}
