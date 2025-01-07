package com.example.smart_packet.data;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.example.smart_packet.R;
import com.example.smart_packet.VerPaqueteActivity;

import java.util.List;
import java.util.Objects;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>{
    private List<ListElement> mData;
    private LayoutInflater mInflater;
    private Context context;
    private String tipo;

    public ListAdapter(List<ListElement> mData, Context context, String tipo) {
        this.mData = mData;
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.tipo = tipo;
    }

    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_element, null);
        return new ListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListAdapter.ViewHolder holder, final int position) {
        holder.bindData(mData.get(position));
        ListElement items = mData.get(position);
        if (Objects.equals(items.getEstado(), "Envio")) {
            // Agregar el OnClickListener a la imagen
            holder.icon.setOnClickListener(v -> {
                // Aquí se maneja el clic en la imagen y se navega a la siguiente actividad/pestaña
                Context context = holder.icon.getContext();
                Intent intent = new Intent(context, VerPaqueteActivity.class);  // O la Activity/Fragment que desees
                var id = items.getId();
                intent.putExtra("idEnvio", id);
                intent.putExtra("tipo", tipo);
                intent.putExtra("id", items.getIdCliente());
                context.startActivity(intent);
            });
        }

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
