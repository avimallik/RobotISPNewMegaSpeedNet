package com.armavi_bsd.robotispreconstructed_mega.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.armavi_bsd.robotispreconstructed_mega.R;
import com.armavi_bsd.robotispreconstructed_mega.model.BillingPersonModel;

import java.util.List;

public class BillingPersonAdapter extends RecyclerView.Adapter<BillingPersonAdapter.ViewHolder> {

    private List<BillingPersonModel> billingpersonList;
    private BillingPersonAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(BillingPersonModel billingPersonModel);
    }

    public BillingPersonAdapter(List<BillingPersonModel> billingpersonList, BillingPersonAdapter.OnItemClickListener listener) {
        this.billingpersonList = billingpersonList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BillingPersonAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_billing_person, parent, false);
        return new BillingPersonAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillingPersonAdapter.ViewHolder holder, int position) {
        BillingPersonModel billingPersonModel = billingpersonList.get(position);
        holder.billingPersonId.setText(billingPersonModel.getUserId());
        holder.billingPersonName.setText(billingPersonModel.getFullName());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(billingPersonModel));
    }

    @Override
    public int getItemCount() {
        return billingpersonList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView billingPersonName, billingPersonId;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            billingPersonId = itemView.findViewById(R.id.billingPersonId);
            billingPersonName = itemView.findViewById(R.id.billgPersonName);
        }
    }
}
