package com.example.wheatherstation_uah.data;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.example.wheatherstation_uah.R;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>{
    private List<ListElement> mData;
    private LayoutInflater mInflater;
    private Context context;

    public ListAdapter(List<ListElement> mData, Context context) {
        this.mData = mData;
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_element, null);
        return new ListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListAdapter.ViewHolder holder, final int position) {
        holder.bindData(mData.get(position));
    }

    @Override
    public  int getItemCount(){
        return mData.size();
    }


    public void setItems(List<ListElement> items){
        mData = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView id;

        ViewHolder(View itemView){
            super(itemView);
            icon = itemView.findViewById(R.id.img);
            id = itemView.findViewById(R.id.txt2);
        }

        void bindData(final ListElement item){
            icon.setColorFilter(Color.parseColor(item.getColor()), PorterDuff.Mode.SRC_IN);
            id.setText(item.getId());
        }
    }
}
