package com.armavi_bsd.robotispreconstructed_mega.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.armavi_bsd.robotispreconstructed_mega.R;
import com.armavi_bsd.robotispreconstructed_mega.model.SubzoneModel;


import java.util.List;

public class SubZoneAdapter extends RecyclerView.Adapter<SubZoneAdapter.ViewHolder> {

    private List<SubzoneModel> subZoneList;
    private SubZoneAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(SubzoneModel subzoneModel);
    }

    public SubZoneAdapter(List<SubzoneModel> subZoneList, SubZoneAdapter.OnItemClickListener listener) {
        this.subZoneList = subZoneList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SubZoneAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sub_zone, parent, false);
        return new SubZoneAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubZoneAdapter.ViewHolder holder, int position) {
        SubzoneModel subzoneModel = subZoneList.get(position);
        holder.subZoneName.setText(subzoneModel.getZoneName());
        holder.subZoneID.setText(subzoneModel.getZoneId());
        holder.areaCount.setText(subzoneModel.getAreaCount());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(subzoneModel));
    }

    @Override
    public int getItemCount() {
        return subZoneList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView subZoneID, subZoneName, areaCount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subZoneID = itemView.findViewById(R.id.subZoneId);
            subZoneName = itemView.findViewById(R.id.subZoneName);
            areaCount = itemView.findViewById(R.id.areaCount);
        }
    }
}
