package com.jc.pda.tableFragment;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jc.pda.R;
import com.jc.pda.activity.BatchActivity;
import com.jc.pda.adapter.ProductAdapter;
import com.jc.pda.component.DaggerProductFragmentComponent;
import com.jc.pda.entity.Product;
import com.jc.pda.module.ContextModule;
import com.jc.pda.module.ProductFragmentModule;
import com.jc.pda.presenter.ProductFragmentPresenter;
import com.jc.pda.presenter.view.ProductFragmentView;
import com.jc.pda.view.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by z on 2017/12/27.
 */

public class ProductFragment extends Fragment implements ProductFragmentView , ProductAdapter.OnItemClickListener {

    @BindView(R.id.rv_product_recyclerview)
    RecyclerView rvProduct;
    @BindView(R.id.srl_product_swiperefreshlayout)
    SwipeRefreshLayout srlProduct;

    private List<Product> list;
    private ProductAdapter adapter;

    @Inject
    ProductFragmentPresenter presenter;

    Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerProductFragmentComponent.builder()
                .contextModule(new ContextModule(getActivity()))
                .productFragmentModule(new ProductFragmentModule(this))
                .build()
                .inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        unbinder = ButterKnife.bind(this, view);

        srlProduct.setColorSchemeResources(R.color.app_main);
        srlProduct.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getProducts();
            }
        });

        rvProduct.setLayoutManager(new LinearLayoutManager(getActivity()));
        Paint paint = new Paint();
        paint.setStrokeWidth(1);
        paint.setColor(ContextCompat.getColor(getActivity(), R.color.divider));
        rvProduct.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).paint(paint).build());
        //rvProductProducts.setNestedScrollingEnabled(false);

        list = new ArrayList<>();
        adapter = new ProductAdapter(list, getActivity());
        adapter.setOnItemClickListener(this);
        rvProduct.setAdapter(adapter);

        if (presenter != null) {
            presenter.getProducts();
        }

        return view;
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

    @Override
    public void setProducts(List<Product> products) {
        if (products != null) {
            if (rvProduct != null) {
                list.clear();
                list.addAll(products);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void dialogDismiss() {
        if(srlProduct.isRefreshing()){
            srlProduct.setRefreshing(false);
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), BatchActivity.class);
        intent.putExtra(BatchActivity.PRODUCTID,list.get(position).getProductNo());
        intent.putExtra(BatchActivity.PRODUCTNAME,list.get(position).getProductName());
        startActivity(intent);
    }
}
