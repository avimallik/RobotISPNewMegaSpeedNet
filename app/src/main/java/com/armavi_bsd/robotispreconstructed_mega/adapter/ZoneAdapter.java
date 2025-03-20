package com.armavi_bsd.robotispreconstructed_mega.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.armavi_bsd.robotispreconstructed_mega.R;
import com.armavi_bsd.robotispreconstructed_mega.model.ZoneModel;

import java.util.List;

public class ZoneAdapter extends RecyclerView.Adapter<ZoneAdapter.ViewHolder> {

    private List<ZoneModel> zoneList;
    private ZoneAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ZoneModel zoneModel);
    }

    public ZoneAdapter(List<ZoneModel> zoneList, ZoneAdapter.OnItemClickListener listener) {
        this.zoneList = zoneList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ZoneAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_zone, parent, false);
        return new ZoneAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ZoneAdapter.ViewHolder holder, int position) {
        ZoneModel zoneModel = zoneList.get(position);
        holder.zoneName.setText(zoneModel.getZoneName());
        holder.zoneId.setText(zoneModel.getZoneId());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(zoneModel));
    }

    @Override
    public int getItemCount() {
        return zoneList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView zoneId, zoneName, subZoneCount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            zoneId = itemView.findViewById(R.id.zoneId);
            zoneName = itemView.findViewById(R.id.zoneName);
            subZoneCount = itemView.findViewById(R.id.subZoneCount);
        }
    }
}
