package com.armavi_bsd.robotispreconstructed_mega.adapter;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.armavi_bsd.robotispreconstructed_mega.AgentInfo;
import com.armavi_bsd.robotispreconstructed_mega.R;
import com.armavi_bsd.robotispreconstructed_mega.model.AgentModel;

import java.util.ArrayList;
import java.util.List;

public class AgentAdapter extends RecyclerView.Adapter<AgentAdapter.AgentViewHolder> {

    private List<AgentModel> agents = new ArrayList<>();

//    public void setAgents(List<AgentModel> agents) {
//        this.agents = agents;
//        notifyDataSetChanged();
//    }

    public void setAgents(List<AgentModel> newAgents) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return agents.size();
            }

            @Override
            public int getNewListSize() {
                return newAgents.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return agents.get(oldItemPosition).getCus_id().equals(newAgents.get(newItemPosition).getCus_id());
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return agents.get(oldItemPosition).equals(newAgents.get(newItemPosition));
            }
        });

        agents = new ArrayList<>(newAgents); // Replace the list
        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public AgentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_agent, parent, false);
        return new AgentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AgentViewHolder holder, int position) {
        AgentModel agent = agents.get(position);
        holder.bind(agent);
    }

    @Override
    public int getItemCount() {
        return agents.size();
    }

    class AgentViewHolder extends RecyclerView.ViewHolder {
        private TextView name, mobile, cusId,ipTxt;

        public AgentViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.agent_name);
            mobile = itemView.findViewById(R.id.ag_mobile_no);
            cusId = itemView.findViewById(R.id.cus_id);
            ipTxt = itemView.findViewById(R.id.ipTxt);
        }

        public void bind(AgentModel agent) {
            name.setText(agent.getAg_name().toString().trim());
            mobile.setText("Mobile no : "
                    +agent.getAg_mobile_no().toString().trim());
            ipTxt.setText("IP : "+agent.getIp().toString().trim());
            cusId.setText(agent.getCus_id().toString().trim());

            itemView.setOnClickListener(v -> {
//                Toast.makeText(
//                        itemView.getContext(),
//                        "Details:\nName: " + agent.getAg_name() +
//                                "\nMobile: " + agent.getAg_mobile_no() +
//                                "\nCustomer ID: " + agent.getCus_id(),
//                        Toast.LENGTH_LONG
//                ).show();
                Intent agentInfoIntent = new Intent(v.getContext(), AgentInfo.class);
                agentInfoIntent.putExtra("intent_agent_name", agent.getAg_name());
                agentInfoIntent.putExtra("intent_agent_address", agent.getAg_office_address());
                agentInfoIntent.putExtra("intent_agent_mobile", agent.getAg_mobile_no());
                agentInfoIntent.putExtra("intent_agent_package", agent.getMb());
                agentInfoIntent.putExtra("intent_agent_bill", agent.getBill_date());
                agentInfoIntent.putExtra("intent_agent_condate", agent.getConnection_date());
                agentInfoIntent.putExtra("intent_agent_zone_name", agent.getZone_name());
                agentInfoIntent.putExtra("intent_agent_cus_id", agent.getCus_id());
                agentInfoIntent.putExtra("intent_agent_cus_ip", agent.getIp());
                agentInfoIntent.putExtra("intent_agent_taka", String.valueOf(agent.getTaka()));
                v.getContext().startActivity(agentInfoIntent);

            });
        }
    }
}