package com.armavi_bsd.robotispreconstructed_mega.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.armavi_bsd.robotispreconstructed_mega.R;
import com.armavi_bsd.robotispreconstructed_mega.model.EmployeeDialogModel;

import java.util.List;

public class EmployeeDialogAdapter extends RecyclerView.Adapter<EmployeeDialogAdapter.EmployeeViewHolder> {

    private List<EmployeeDialogModel> employeeDialogModelList;
    private EmployeeDialogAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(EmployeeDialogModel employeeDialogModel);
    }

    public EmployeeDialogAdapter(List<EmployeeDialogModel> employeeDialogModelList, EmployeeDialogAdapter.OnItemClickListener listener) {
        this.employeeDialogModelList = employeeDialogModelList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EmployeeDialogAdapter.EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_employee, parent, false);
        return new EmployeeDialogAdapter.EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeDialogAdapter.EmployeeViewHolder holder, int position) {
        EmployeeDialogModel employeeDialogModel = employeeDialogModelList.get(position);
        holder.employeeIdText.setText(employeeDialogModel.getId());
        holder.employeeNameText.setText(employeeDialogModel.getEmployee_name());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(employeeDialogModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return employeeDialogModelList.size();
    }

    public void updateList(List<EmployeeDialogModel> filteredList) {
        this.employeeDialogModelList = filteredList;
        notifyDataSetChanged();
    }

    public static class EmployeeViewHolder extends RecyclerView.ViewHolder {
        TextView employeeNameText, employeeIdText;

        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);
            employeeIdText = itemView.findViewById(R.id.employeeIdText);
            employeeNameText = itemView.findViewById(R.id.employeeNameText);
        }
    }
}
