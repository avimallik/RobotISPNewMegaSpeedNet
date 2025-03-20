package com.armavi_bsd.robotispreconstructed_mega;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import com.armavi_bsd.robotispreconstructed_mega.adapter.AgentAdapter;
import com.armavi_bsd.robotispreconstructed_mega.databinding.ActivityAgentDisplayBinding;
import com.armavi_bsd.robotispreconstructed_mega.model.AgentModel;
import com.armavi_bsd.robotispreconstructed_mega.retroFitClient.RretrofitClient;
import com.armavi_bsd.robotispreconstructed_mega.viewModel.AgentViewModel;

import java.util.ArrayList;
import java.util.List;

public class AgentDisplay extends AppCompatActivity {

    ActivityAgentDisplayBinding binding;
    private AgentAdapter adapter;
    private AgentViewModel viewModel;
    private ProgressDialog progressDialog;
    private List<AgentModel> allAgents = new ArrayList<>();
    RretrofitClient retrofitClient = new RretrofitClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAgentDisplayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Adapter and RecyclerView
        adapter = new AgentAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(AgentViewModel.class);

        // Initialize ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        // Observe ViewModel
        viewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) {
                progressDialog.show();
            } else {
                progressDialog.dismiss();
            }
        });

        viewModel.getAgents().observe(this, agents -> {
            allAgents = agents;
            adapter.setAgents(agents);
        });

        // Fetch Agents
        viewModel.fetchAgents();

        // Add Search Functionality
        binding.searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterAgents(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filterAgents(String query) {
        List<AgentModel> filteredList = new ArrayList<>();
        for (AgentModel agent : allAgents) {
            if (agent.getAg_name().toLowerCase().contains(query.toLowerCase())
                    || agent.getAg_mobile_no().contains(query)
                    || agent.getIp().toLowerCase().contains(query.toLowerCase())
                    || agent.getCus_id().toLowerCase().contains(query.toLowerCase())
            ) {
                filteredList.add(agent);
            }
        }
        adapter.setAgents(filteredList);

    }

}