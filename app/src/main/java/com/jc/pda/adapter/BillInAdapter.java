package com.jc.pda.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jc.pda.R;
import com.jc.pda.database.helper.ProductHelper;
import com.jc.pda.entity.Bill;
import com.jc.pda.utils.Constant;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by z on 2018/1/3.
 */

public class BillInAdapter extends RecyclerView.Adapter<BillInAdapter.ViewHolder> {
    private Context context;
    private List<Bill> list;
    private OnItemClickListener onItemClickListener;

    public BillInAdapter(Context context, List<Bill> list) {
        this.context = context;
        this.list = list;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_bill_in_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if(onItemClickListener != null){
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(position);
                }
            });
        }

        if (list.get(position).isUp()) {
            if (list.get(position).getUpType() == Constant.FISTUP) {
                //已上传
                holder.tvBillInItemBillNo.setTextColor(ContextCompat.getColor(context, R.color.chart_up));
            } else {
                //重复上传
                holder.tvBillInItemBillNo.setTextColor(ContextCompat.getColor(context, R.color.chart_reup));
            }
        } else {
            //未上传
            holder.tvBillInItemBillNo.setTextColor(ContextCompat.getColor(context, R.color.chart_noup));
        }

        holder.tvBillInItemBillNo.setText(list.get(position).getBillId());
        holder.tvBillInItemProductName.setText(ProductHelper.getSingleton(context).getProductNameById(list.get(position).getProductId()));
        holder.tvBillInItemBatchName.setText(list.get(position).getBatchId());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        @BindView(R.id.tv_bill_in_item_bill_no)
        TextView tvBillInItemBillNo;
        @BindView(R.id.tv_bill_in_item_product_name)
        TextView tvBillInItemProductName;
        @BindView(R.id.tv_bill_in_item_batch_name)
        TextView tvBillInItemBatchName;

        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
}
