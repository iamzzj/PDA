package com.jc.pda.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.jc.pda.R;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by z on 2018/1/2.
 */

public class InputDialog extends Dialog {

    @BindView(R.id.tv_input_dialog_title)
    TextView tvTitle;
    @BindView(R.id.met_input_dialog_content)
    MaterialEditText metContent;
    @BindView(R.id.tv_input_dialog_sure)
    TextView tvSure;
    @BindView(R.id.tv_input_dialog_cancel)
    TextView tvCancel;

    private OnClickListeners clickListeners;

    private String title;

    private String helper;

    private int inputType;

    private String content;

    public InputDialog(@NonNull Context context) {
        super(context);
    }

    public InputDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected InputDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置为居中
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_input, null);
        setContentView(view);
        ButterKnife.bind(this, view);

        WindowManager windowManager = getWindow().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = display.getWidth() * 4 / 5; // 设置dialog宽度为屏幕的4/5
        getWindow().setAttributes(lp);
        setCanceledOnTouchOutside(true);// 点击Dialog外部消失

        if(!TextUtils.isEmpty(title))
            tvTitle.setText(title);

        if(!TextUtils.isEmpty(helper))
            metContent.setHelperText(helper);

        if(inputType != 0){
            metContent.setInputType(inputType);
        }

        if(!TextUtils.isEmpty(content))
            metContent.setText(content);

        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(clickListeners!=null){
                    clickListeners.sure();
                }
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(clickListeners!=null){
                    clickListeners.cancel();
                }
            }
        });
    }

    public String getContent(){
        if(metContent!=null){
            return metContent.getText().toString();
        }else{
            return "";
        }
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setMaterialEditText(String helper){
        this.helper = helper;
    }

    public void setInputType(int inputType){
        this.inputType = inputType;
    }

    public void setContent(String content){
        this.content = content;
    }

    public void setOnclickListeners(OnClickListeners clickListeners){
        this.clickListeners = clickListeners;
    }

    public interface OnClickListeners{
        void sure();
        void cancel();
    }

}
