package com.jc.pda.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.jc.pda.R;
import com.jc.pda.entity.Code;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by z on 2017/12/21.
 */

public class CodeAdapter extends RecyclerSwipeAdapter<CodeAdapter.ViewHolder> {

    private Context context;
    private List<Code> list;

    private RightOnItemClickListener rightOnItemClickListener;

    public CodeAdapter(Context context, List<Code> list) {
        this.context = context;
        if (list == null) {
            list = new ArrayList<>();
        }
        this.list = list;
    }

    public void setRightOnItemClickListener(RightOnItemClickListener rightOnItemClickListener){
        this.rightOnItemClickListener = rightOnItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_code_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        viewHolder.slCodeItemSwipe.setShowMode(SwipeLayout.ShowMode.LayDown);


        if(rightOnItemClickListener!=null){
            viewHolder.tvCodeItemDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rightOnItemClickListener.delete(position);
                    viewHolder.slCodeItemSwipe.close(false);
                }
            });
        }

        viewHolder.tvCodeItemCode.setText(list.get(position).getCode());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.sl_code_item_swipe;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_code_item_delete)
        TextView tvCodeItemDelete;
        @BindView(R.id.tv_code_item_code)
        TextView tvCodeItemCode;
        @BindView(R.id.sl_code_item_swipe)
        SwipeLayout slCodeItemSwipe;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface RightOnItemClickListener{
        void delete(int position);
    }
}
