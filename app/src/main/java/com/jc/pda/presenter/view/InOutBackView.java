package com.jc.pda.presenter.view;

import com.jc.pda.entitynet.WlCodeCheckNet;

import java.util.List;

/**
 * Created by z on 2018/1/8.
 */

public interface InOutBackView {
    void dialogShow(String title);
    void dialogDismiss();
    String getCodeValue();
    String getCodeType();
    void setWlCodeCheckSuccess();
    void setWlCodeCheckFail(List<WlCodeCheckNet> wlCodeCheckNets);
    void err(String err);
    void success(String success);
    void upSuccess();
    void count();
    void installIscan();
}
