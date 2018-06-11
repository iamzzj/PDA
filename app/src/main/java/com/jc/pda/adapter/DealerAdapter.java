package com.jc.pda.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jc.pda.R;
import com.jc.pda.entity.Dealer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by z on 2017/12/28.
 */

public class DealerAdapter extends RecyclerView.Adapter<DealerAdapter.ViewHolder> {
    private Context context;
    private List<Dealer> list;

    public DealerAdapter(Context context, List<Dealer> list) {
        this.context = context;
        if (list == null) {
            list = new ArrayList<>();
        }
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_dealer_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvDealerItemNo.setText(String.valueOf(position+1));
        holder.tvDealerItemDealerNo.setText(list.get(position).getDealerNo());
        holder.tvDealerItemDealerName.setText(list.get(position).getDealerName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_dealer_item_no)
        TextView tvDealerItemNo;
        @BindView(R.id.tv_dealer_item_dealer_no)
        TextView tvDealerItemDealerNo;
        @BindView(R.id.tv_dealer_item_dealer_name)
        TextView tvDealerItemDealerName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
