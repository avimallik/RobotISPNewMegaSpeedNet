package com.armavi_bsd.robotispreconstructed_mega;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.armavi_bsd.robotispreconstructed_mega.adapter.ComplainAdapter;
import com.armavi_bsd.robotispreconstructed_mega.databinding.ActivityComplainBinding;
import com.armavi_bsd.robotispreconstructed_mega.model.ComplainModel;
import com.armavi_bsd.robotispreconstructed_mega.retroFitClient.RretrofitClient;
import com.armavi_bsd.robotispreconstructed_mega.viewModel.ComplainViewModel;

import java.util.ArrayList;
import java.util.List;

public class Complain extends AppCompatActivity {

    ActivityComplainBinding binding;
    private ComplainAdapter adapter;
    private ComplainViewModel viewModel;
    private ProgressDialog progressDialog;
    private List<ComplainModel> allComplain = new ArrayList<>();
    RretrofitClient retrofitClient = new RretrofitClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityComplainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Adapter and RecyclerView
        adapter = new ComplainAdapter(this);
        binding.complainRecycler.setLayoutManager(new LinearLayoutManager(this));
        binding.complainRecycler.setAdapter(adapter);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(ComplainViewModel.class);

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

        viewModel.getComplains().observe(this, complains -> {
            allComplain = complains;
            adapter.setComplains(complains);
        });

        viewModel.fetchComplains();

        // Add Search Functionality
        binding.searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterComplains(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        binding.complainPostIntentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent complainSubmitIntent =  new Intent(Complain.this, ComplainSubmit.class);
                startActivity(complainSubmitIntent);
            }
        });
    }

    private void filterComplains(String query) {
        List<ComplainModel> filteredList = new ArrayList<>();
        for (ComplainModel complain : allComplain) {
            if (complain.getAg_name().toLowerCase().contains(query.toLowerCase()) ||
                    complain.getIp().contains(query)) {
                filteredList.add(complain);
            }
        }
        adapter.setComplains(filteredList);
    }
}