package com.jc.pda.presenter.view;

import com.jc.pda.entity.Product;

import java.util.List;

/**
 * Created by z on 2017/12/27.
 */

public interface ProductFragmentView {
    void setProducts(List<Product> products);
    void dialogDismiss();
}
