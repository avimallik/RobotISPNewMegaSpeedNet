package com.armavi_bsd.robotispreconstructed_mega;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import com.armavi_bsd.robotispreconstructed_mega.adapter.BillCollectionAdapter;
import com.armavi_bsd.robotispreconstructed_mega.databinding.ActivityBillPayBinding;
import com.armavi_bsd.robotispreconstructed_mega.model.BillCollectionModel;
import com.armavi_bsd.robotispreconstructed_mega.retroFitClient.RretrofitClient;
import com.armavi_bsd.robotispreconstructed_mega.viewModel.BillCollectionViewModel;

import java.util.ArrayList;
import java.util.List;

public class BillPay extends AppCompatActivity {

    ActivityBillPayBinding binding;
    private BillCollectionAdapter adapter;
    private BillCollectionViewModel viewModel;
    private ProgressDialog progressDialog;
    private List<BillCollectionModel> allBills = new ArrayList<>();
    RretrofitClient retrofitClient = new RretrofitClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBillPayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Adapter and RecyclerView
        adapter = new BillCollectionAdapter(this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(BillCollectionViewModel.class);

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

        viewModel.getBillCollection().observe(this, billCollection -> {
            allBills = billCollection;
            adapter.setAgents(billCollection);
        });

        // Fetch Agents
        viewModel.fetchBillCollection();

        // Add Search Functionality
        binding.searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterBills(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

    }

    private void filterBills(String query) {
        List<BillCollectionModel> filteredList = new ArrayList<>();
        for (BillCollectionModel billCollection : allBills) {
            if (billCollection.getAgent_name().toLowerCase().contains(query.toLowerCase())
                    || billCollection.getMobile().contains(query)
                    || billCollection.getZone().toLowerCase().contains(query.toLowerCase())
                    || billCollection.getIp().toLowerCase().contains(query.toLowerCase())
                    || billCollection.getCustomer_id().toLowerCase().contains(query.toLowerCase())
                    || billCollection.getAgent_address().toLowerCase().contains(query.toLowerCase())
            ) {
                filteredList.add(billCollection);
            }
        }
        adapter.setAgents(filteredList);
    }
}