package com.armavi_bsd.robotispreconstructed_mega.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.armavi_bsd.robotispreconstructed_mega.R;
import com.armavi_bsd.robotispreconstructed_mega.model.ComplainTypeModel;

import java.util.List;

public class ComplainTypeAdapter extends RecyclerView.Adapter<ComplainTypeAdapter.ComplainTypeViewHolder> {

    private List<ComplainTypeModel> complainTypeModelList;
    private ComplainTypeAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ComplainTypeModel complainTypeModel);
    }

    public ComplainTypeAdapter(List<ComplainTypeModel> complainTypeModelList, ComplainTypeAdapter.OnItemClickListener listener) {
        this.complainTypeModelList = complainTypeModelList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ComplainTypeAdapter.ComplainTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_complain_type, parent, false);
        return new ComplainTypeAdapter.ComplainTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComplainTypeAdapter.ComplainTypeViewHolder holder, int position) {
        ComplainTypeModel complainTypeModel = complainTypeModelList.get(position);
        holder.complainIdText.setText(complainTypeModel.getId());
        holder.complainTypeText.setText(complainTypeModel.getTemplate());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(complainTypeModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return complainTypeModelList.size();
    }

    public void updateList(List<ComplainTypeModel> filteredList) {
        this.complainTypeModelList = filteredList;
        notifyDataSetChanged();
    }

    public static class ComplainTypeViewHolder extends RecyclerView.ViewHolder {
        TextView complainIdText, complainTypeText;

        public ComplainTypeViewHolder(@NonNull View itemView) {
            super(itemView);
            complainIdText = itemView.findViewById(R.id.complainIdText);
            complainTypeText = itemView.findViewById(R.id.complainTypeText);
        }
    }
}

