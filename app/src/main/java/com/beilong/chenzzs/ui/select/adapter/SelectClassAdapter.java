package com.beilong.chenzzs.ui.select.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import com.beilong.chenzzs.R;

/**
 * Created by LBL on 2016/11/1.
 */

public class SelectClassAdapter extends RecyclerView.Adapter<SelectClassAdapter.ViewHolder>{

    private List<String> mData;

    public SelectClassAdapter(List<String> data){
        this.mData = data;
    }

    private OnItemClickListener mOnItemClickListener;
    public interface OnItemClickListener{
        void onItemClick(View view, int postion);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public SelectClassAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_activity_select_class,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SelectClassAdapter.ViewHolder holder, final int position) {
        if (mOnItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(holder.itemView,holder.getLayoutPosition());
                }
            });
        }
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.content);
        }

        public void setData(int position){
            textView.setText(mData.get(position));
        }
    }
}
