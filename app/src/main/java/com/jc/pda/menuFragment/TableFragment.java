package com.jc.pda.menuFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jc.pda.R;
import com.jc.pda.tableFragment.BackFragment;
import com.jc.pda.tableFragment.DealerFragment;
import com.jc.pda.tableFragment.InFragment;
import com.jc.pda.tableFragment.OutFragment;
import com.jc.pda.tableFragment.ProductFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by z on 2017/12/20.
 */

public class TableFragment extends Fragment {
    @BindView(R.id.tl_table)
    TabLayout tlTable;
    @BindView(R.id.vp_table_viewpager)
    ViewPager vpTableViewpager;
    private Unbinder unBinder;

    private static final String[] titles = {"产品","经销商","入库","出库","退货"};
    private static final Fragment[] fragments = {
            new ProductFragment(),
            new DealerFragment(),
            new InFragment(),
            new OutFragment(),
            new BackFragment()
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_table, container, false);
        unBinder = ButterKnife.bind(this, view);

        vpTableViewpager.setAdapter(new InnerFragmentAdapter(getChildFragmentManager()));
        vpTableViewpager.setOffscreenPageLimit(titles.length);

        tlTable.setupWithViewPager(vpTableViewpager);

        return view;
    }

    private class InnerFragmentAdapter extends FragmentPagerAdapter{

        public InnerFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(unBinder!=null)
        unBinder.unbind();
    }
}
