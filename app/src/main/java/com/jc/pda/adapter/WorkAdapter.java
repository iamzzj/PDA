package com.jc.pda.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jc.pda.R;
import com.jc.pda.entity.Work;
import com.jc.pda.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by z on 2017/12/20.
 */

public class WorkAdapter extends RecyclerView.Adapter<WorkAdapter.ViewHolder> {
    private List<Work> list;
    private Context context;
    private OnItemClickListener onItemClick;

    public WorkAdapter(Context context, List<Work> list) {
        if (list == null) {
            list = new ArrayList<>();
        }
        this.list = list;
        this.context = context;
    }

    public void setOnItemClickListner(OnItemClickListener onItemClick){
        this.onItemClick = onItemClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_work_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (onItemClick != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onItemClick.onItemClick(position);
                }
            });
        }
        holder.tvWoekItemName.setText(list.get(position).getName());

        switch (list.get(position).getId()){
            case Constant.IN:
                holder.ivWoekItemImage.setImageResource(R.mipmap.work_ruku);
                break;
            case Constant.OUT:
                holder.ivWoekItemImage.setImageResource(R.mipmap.work_chuku);
                break;
            case Constant.BACK:
                holder.ivWoekItemImage.setImageResource(R.mipmap.work_tuihuo);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        @BindView(R.id.iv_woek_item_image)
        ImageView ivWoekItemImage;
        @BindView(R.id.tv_woek_item_name)
        TextView tvWoekItemName;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
}
