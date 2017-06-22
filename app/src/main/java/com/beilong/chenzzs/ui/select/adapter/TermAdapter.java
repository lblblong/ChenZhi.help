package com.beilong.chenzzs.ui.select.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import com.beilong.chenzzs.R;
import com.beilong.chenzzs.bean.Term;

/**
 * Created by LBL on 2016/11/1.
 */

public class TermAdapter extends RecyclerView.Adapter<TermAdapter.ViewHolder>{

    private List<Term> mData;

    public TermAdapter(List<Term> data){
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
    public TermAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_dialog_selectclass,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TermAdapter.ViewHolder holder, final int position) {
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
            textView.setText(mData.get(position).getName());
        }
    }
}
