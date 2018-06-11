package com.jc.pda.menuFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jc.pda.R;
import com.jc.pda.activity.BackActivity;
import com.jc.pda.activity.InActivity;
import com.jc.pda.activity.OutActivity;
import com.jc.pda.adapter.WorkAdapter;
import com.jc.pda.entity.Work;
import com.jc.pda.utils.Constant;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoaderInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by z on 2017/12/20.
 */

public class WorkFragment extends Fragment implements WorkAdapter.OnItemClickListener{
    @BindView(R.id.rv_work_works)
    RecyclerView rvWorkWorks;
    @BindView(R.id.b_work_tops)
    Banner bWorkTops;
    private Unbinder unBinder;

    private List<Work> list;
    private WorkAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_work, container, false);
        unBinder = ButterKnife.bind(this, view);

        list = new ArrayList<>();
        list.add(new Work("入库", Constant.IN));
        list.add(new Work("出库",Constant.OUT));
        list.add(new Work("退货",Constant.BACK));
        adapter = new WorkAdapter(getActivity(),list);
        adapter.setOnItemClickListner(this);
        rvWorkWorks.setLayoutManager(new GridLayoutManager(getActivity(),3));
        rvWorkWorks.setAdapter(adapter);

        List<String> images = new ArrayList<>();
        images.add("http://www.jbk.com.cn/upload/unohacha_20170401082816.jpg");
        images.add("http://www.jbk.com.cn/upload/unohacha_20170407040452.jpg");
        images.add("http://www.jbk.com.cn/upload/unohacha_20170407040701.jpg");
        images.add("http://www.jbk.com.cn/upload/unohacha_20170401082852.jpg");
        images.add("http://www.jbk.com.cn/upload/unohacha_20170327043557.jpg");
        //设置banner样式
        bWorkTops.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        bWorkTops.setImageLoader(new ImageLoaderInterface() {
            @Override
            public void displayImage(Context context, Object path, View view) {
                ImageView imageView = (ImageView) view;
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                Glide.with(context).load(path).into(imageView);
            }

            @Override
            public View createImageView(Context context) {
                return null;
            }
        });
        //设置图片集合
        bWorkTops.setImages(images);
        //设置banner动画效果
        bWorkTops.setBannerAnimation(Transformer.Default);
        //设置自动轮播，默认为true
        bWorkTops.isAutoPlay(true);
        //设置轮播时间
        bWorkTops.setDelayTime(2000);
        //banner设置方法全部调用完毕时最后调用
        bWorkTops.start();

        return view;
    }

    @Override
    public void onItemClick(int position) {
        switch (position){
            case Constant.IN:
                getActivity().startActivity(new Intent(getActivity(), InActivity.class));
                break;
            case Constant.OUT:
                getActivity().startActivity(new Intent(getActivity(), OutActivity.class));
                break;
            case Constant.BACK:
                getActivity().startActivity(new Intent(getActivity(), BackActivity.class));
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        //开始轮播
        bWorkTops.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        //结束轮播
        bWorkTops.stopAutoPlay();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(unBinder!=null)
        unBinder.unbind();
    }
}
