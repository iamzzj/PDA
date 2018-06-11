package com.jc.pda.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jc.pda.R;
import com.jc.pda.entity.Batch;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by z on 2017/12/28.
 */

public class BatchAdapter extends RecyclerView.Adapter<BatchAdapter.ViewHolder> {
    private Context context;
    private List<Batch> list;

    public BatchAdapter(Context context, List<Batch> list) {
        this.context = context;
        if (list == null) {
            list = new ArrayList<>();
        }
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_batch_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvBatchItemNo.setText(String.valueOf(position+1));
        holder.tvBatchItemBatchName.setText(list.get(position).getBatchName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_batch_item_no)
        TextView tvBatchItemNo;
        @BindView(R.id.tv_batch_item_batch_no)
        TextView tvBatchItemBatchNo;
        @BindView(R.id.tv_batch_item_batch_name)
        TextView tvBatchItemBatchName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
