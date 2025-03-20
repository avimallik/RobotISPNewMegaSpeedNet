package com.armavi_bsd.robotispreconstructed_mega.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.armavi_bsd.robotispreconstructed_mega.R;
import com.armavi_bsd.robotispreconstructed_mega.model.AccountStatementModel;

import java.util.List;

public class AccountStatementAdapter extends RecyclerView.Adapter<AccountStatementAdapter.ViewHolder> {

    private List<AccountStatementModel> accountStatementList;

    public AccountStatementAdapter(List<AccountStatementModel> accountStatementList) {
        this.accountStatementList = accountStatementList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_account_statement, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AccountStatementModel accountStatementModel = accountStatementList.get(position);
        holder.itemAccStmtBalance.setText(accountStatementModel.getBalance());
        holder.itemAccStmtCredit.setText(accountStatementModel.getCredit());
        holder.itemAccStmtDebit.setText(accountStatementModel.getDebit());
        holder.itemAccStmtClientName.setText(accountStatementModel.getCustomerName());
        holder.itemAccStmtDate.setText(accountStatementModel.getDate());
        holder.itemAccStmtIP.setText(accountStatementModel.getCustomerIp());
        holder.itemAccStmtID.setText(accountStatementModel.getCustomerid());
    }

    @Override
    public int getItemCount() {
        return accountStatementList != null ? accountStatementList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemAccStmtBalance, itemAccStmtCredit, itemAccStmtDebit,
                itemAccStmtDate, itemAccStmtClientName, itemAccStmtID, itemAccStmtIP;

        public ViewHolder(View itemView) {
            super(itemView);
            itemAccStmtBalance = itemView.findViewById(R.id.itemAccStmtBalance);
            itemAccStmtCredit = itemView.findViewById(R.id.itemAccStmtCredit);
            itemAccStmtDebit = itemView.findViewById(R.id.itemAccStmtDebit);
            itemAccStmtDate = itemView.findViewById(R.id.itemAccStmtDate);
            itemAccStmtClientName = itemView.findViewById(R.id.itemAccStmtClientName);
            itemAccStmtID = itemView.findViewById(R.id.itemAccStmtID);
            itemAccStmtIP = itemView.findViewById(R.id.itemAccStmtIP);
        }
    }
}
