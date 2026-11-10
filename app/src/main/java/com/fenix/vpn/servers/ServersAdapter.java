package com.fenix.vpn.servers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.fenix.vpn.R;

import java.util.List;

public class ServersAdapter extends RecyclerView.Adapter<ServersViewHolder>{
    
    private List<ServersModel> servidores;
    private onItemClickListener listener;
    
    public ServersAdapter(List<ServersModel> model) {
        servidores = model;
    }
    
    public void setOnItemClick(onItemClickListener mlistener) {
        listener = mlistener;
    }
    
    @NonNull
    @Override
    public ServersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.servers_item, parent, false);
        
        return new ServersViewHolder(view, listener);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ServersViewHolder holder, int position) {
        ServersModel model = servidores.get(position);
        holder.bind(model);
    }
    
    @Override
    public int getItemCount() {
        return servidores.size();
    }
    
    public interface onItemClickListener {
        void onItemClick(int position);
    }
}
