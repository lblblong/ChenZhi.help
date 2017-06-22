package com.beilong.chenzzs.ui.query.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import com.beilong.chenzzs.R;
import com.beilong.chenzzs.bean.Mark;

/**
 * Created by LBL on 2016/11/5.
 */

public class QueryMarkAdapter extends RecyclerView.Adapter<QueryMarkAdapter.ViewHolder>{

    private List<Mark> mData;

    public QueryMarkAdapter(List<Mark> data){
        this.mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_activity_query_mark,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView term;
        TextView fraction;
        TextView subject;

        public ViewHolder(View itemView) {
            super(itemView);
            term = (TextView) itemView.findViewById(R.id.term);
            fraction = (TextView) itemView.findViewById(R.id.fraction);
            subject = (TextView) itemView.findViewById(R.id.subject);
        }

        public void setData(int position){
            term.setText(mData.get(position).getTerm());
            fraction.setText(mData.get(position).getFraction());
            subject.setText(mData.get(position).getSubject());
        }
    }
}
