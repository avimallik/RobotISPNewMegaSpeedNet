package com.armavi_bsd.robotispreconstructed_mega.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.armavi_bsd.robotispreconstructed_mega.R;
import com.armavi_bsd.robotispreconstructed_mega.model.AreaModel;

import java.util.List;

public class AreaAdapter extends RecyclerView.Adapter<AreaAdapter.ViewHolder> {

    private List<AreaModel> areaList;
    private AreaAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(AreaModel areaModel);
    }

    public AreaAdapter(List<AreaModel> areaList, AreaAdapter.OnItemClickListener listener) {
        this.areaList = areaList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AreaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_area, parent, false);
        return new AreaAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AreaAdapter.ViewHolder holder, int position) {
        AreaModel areaModel = areaList.get(position);
        holder.areaName.setText(areaModel.getZoneName());
        holder.areaId.setText(areaModel.getZoneId());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(areaModel));
    }

    @Override
    public int getItemCount() {
        return areaList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView areaId, areaName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            areaId = itemView.findViewById(R.id.areaId);
            areaName = itemView.findViewById(R.id.areaName);
        }
    }
}

