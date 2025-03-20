package com.armavi_bsd.robotispreconstructed_mega.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.armavi_bsd.robotispreconstructed_mega.R;
import com.armavi_bsd.robotispreconstructed_mega.model.PackageModel;

import java.util.List;

public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.ViewHolder> {

    private List<PackageModel> packageList;
    private PackageAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(PackageModel packageModel);
    }

    public PackageAdapter(List<PackageModel> packageList, PackageAdapter.OnItemClickListener listener) {
        this.packageList = packageList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PackageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_package, parent, false);
        return new PackageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PackageAdapter.ViewHolder holder, int position) {
        PackageModel packageModel = packageList.get(position);
        holder.packageName.setText(packageModel.getPackageName());
        holder.netSpeed.setText(packageModel.getNetSpeed());
        holder.billAmount.setText(packageModel.getBillAmount()+" Tk");
        holder.itemView.setOnClickListener(v -> listener.onItemClick(packageModel));
    }

    @Override
    public int getItemCount() {
        return packageList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView packageName, netSpeed, billAmount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            packageName = itemView.findViewById(R.id.package_name);
            netSpeed = itemView.findViewById(R.id.net_speed);
            billAmount = itemView.findViewById(R.id.bill_amount);
        }
    }
}
