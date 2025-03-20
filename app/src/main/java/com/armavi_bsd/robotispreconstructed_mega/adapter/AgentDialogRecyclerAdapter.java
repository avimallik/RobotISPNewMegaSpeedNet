package com.armavi_bsd.robotispreconstructed_mega.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.armavi_bsd.robotispreconstructed_mega.R;
import com.armavi_bsd.robotispreconstructed_mega.model.AgentDialogModel;

import java.util.List;

public class AgentDialogRecyclerAdapter extends RecyclerView.Adapter<AgentDialogRecyclerAdapter.AgentViewHolder> {

    private List<AgentDialogModel> agentModelList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(AgentDialogModel agentDialogModel);
    }

    public AgentDialogRecyclerAdapter(List<AgentDialogModel> agentModelList, OnItemClickListener listener) {
        this.agentModelList = agentModelList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AgentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dialog_customer, parent, false);
        return new AgentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AgentViewHolder holder, int position) {
        AgentDialogModel agentDialogModel = agentModelList.get(position);
        holder.agNameTextView.setText(agentDialogModel.getAgName().toString().trim());
        holder.agIdTextView.setText(agentDialogModel.getAgId().toString().trim());
        holder.agMobileTextView.setText("Mobile: "+agentDialogModel.getAgMobileNumber().toString().trim());
        holder.agIPTextView.setText("IP: "+agentDialogModel.getAgIp().toString().trim());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(agentDialogModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return agentModelList.size();
    }

    public void updateList(List<AgentDialogModel> filteredList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallback(agentModelList, filteredList));
        this.agentModelList = filteredList;
        diffResult.dispatchUpdatesTo(this);
    }

    public static class AgentViewHolder extends RecyclerView.ViewHolder {
        TextView agNameTextView,
                agIdTextView,
                agIPTextView,
                agMobileTextView;

        public AgentViewHolder(@NonNull View itemView) {
            super(itemView);
            agNameTextView = itemView.findViewById(R.id.agNameTextView);
            agIdTextView = itemView.findViewById(R.id.agIdTextView);
            agIPTextView = itemView.findViewById(R.id.agIPTextView);
            agMobileTextView = itemView.findViewById(R.id.agMobileTextView);
        }
    }

    public static class DiffCallback extends DiffUtil.Callback {
        private final List<AgentDialogModel> oldList;
        private final List<AgentDialogModel> newList;

        public DiffCallback(List<AgentDialogModel> oldList, List<AgentDialogModel> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).getAgId().equals(newList.get(newItemPosition).getAgId());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
        }
    }
}
