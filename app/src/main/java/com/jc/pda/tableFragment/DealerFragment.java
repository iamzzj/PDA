package com.jc.pda.tableFragment;

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
import com.jc.pda.adapter.DealerAdapter;
import com.jc.pda.component.DaggerDealerFragmentComponent;
import com.jc.pda.entity.Dealer;
import com.jc.pda.module.ContextModule;
import com.jc.pda.module.DealerFragmentModule;
import com.jc.pda.presenter.DealerFragmentPresenter;
import com.jc.pda.presenter.view.DealerFragmentView;
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

public class DealerFragment extends Fragment implements DealerFragmentView {
    @BindView(R.id.rv_dealer_recyclerview)
    RecyclerView rvDealer;
    Unbinder unbinder;
    @BindView(R.id.srl_dealer_swiperefreshlayout)
    SwipeRefreshLayout srlDealer;

    private List<Dealer> list;
    private DealerAdapter adapter;

    @Inject
    DealerFragmentPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerDealerFragmentComponent.builder()
                .contextModule(new ContextModule(getActivity()))
                .dealerFragmentModule(new DealerFragmentModule(this))
                .build()
                .inject(this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dealer, container, false);
        unbinder = ButterKnife.bind(this, view);

        srlDealer.setColorSchemeResources(R.color.app_main);
        srlDealer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getDealers();
            }
        });

        rvDealer.setLayoutManager(new LinearLayoutManager(getActivity()));
        Paint paint = new Paint();
        paint.setStrokeWidth(1);
        paint.setColor(ContextCompat.getColor(getActivity(), R.color.divider));
        rvDealer.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).paint(paint).build());
        //rvProductProducts.setNestedScrollingEnabled(false);

        list = new ArrayList<>();
        adapter = new DealerAdapter(getActivity(), list);
        rvDealer.setAdapter(adapter);

        if (presenter != null)
            presenter.getDealers();

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
    public void setDealers(List<Dealer> dealers) {
        if (dealers != null) {
            list.clear();
            list.addAll(dealers);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void dialogDismiss() {
        if(srlDealer.isRefreshing()){
            srlDealer.setRefreshing(false);
        }
    }
}
