package com.armavi_bsd.robotispreconstructed_mega.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.armavi_bsd.robotispreconstructed_mega.R;
import com.armavi_bsd.robotispreconstructed_mega.model.Mikrotik;

import java.util.List;

public class MikrotikAdapter extends RecyclerView.Adapter<MikrotikAdapter.ViewHolder> {

    private List<Mikrotik> mikrotikList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Mikrotik mikrotik);
    }

    public MikrotikAdapter(List<Mikrotik> mikrotikList, OnItemClickListener listener) {
        this.mikrotikList = mikrotikList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mikrotik, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Mikrotik mikrotik = mikrotikList.get(position);
        holder.idTextView.setText(mikrotik.getId());
        holder.mikIp.setText(mikrotik.getMikIp());

        holder.itemView.setOnClickListener(v -> listener.onItemClick(mikrotik));
    }

    @Override
    public int getItemCount() {
        return mikrotikList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView idTextView, mikIp;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.idTextView);
            mikIp = itemView.findViewById(R.id.mikIp);
        }
    }
}
