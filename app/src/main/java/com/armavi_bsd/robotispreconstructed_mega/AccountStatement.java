package com.armavi_bsd.robotispreconstructed_mega;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.armavi_bsd.robotispreconstructed_mega.adapter.AccountStatementAdapter;
import com.armavi_bsd.robotispreconstructed_mega.databinding.ActivityAccountStatementBinding;
import com.armavi_bsd.robotispreconstructed_mega.fragments.FragmentAccountStatementOverview;
import com.armavi_bsd.robotispreconstructed_mega.navigationEndState.NavigationState;
import com.armavi_bsd.robotispreconstructed_mega.util.Pref;
import com.armavi_bsd.robotispreconstructed_mega.viewModel.AccountStatementViewModel;

public class AccountStatement extends AppCompatActivity {

    private AccountStatementAdapter adapter;
    private AccountStatementViewModel accountStatementViewModel;
    ActivityAccountStatementBinding binding;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;

    NavigationState navigationState = new NavigationState();

    Pref pref = new Pref();
    String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAccountStatementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences(pref.getPrefUserCred(), MODE_PRIVATE);
        userId = sharedPreferences.getString(pref.getPrefUserID(), "0");

        Fragment fragmentAccountStatementOverview = new FragmentAccountStatementOverview();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentAccountStatementOverview, fragmentAccountStatementOverview);
        fragmentTransaction.commit();

        binding.recyclerViewAccountStmt.setLayoutManager(new LinearLayoutManager(this));

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(true);
        progressDialog.show();

        accountStatementViewModel = new ViewModelProvider(this).get(AccountStatementViewModel.class);
        accountStatementViewModel.getAccountStatements(userId).observe(this, customers -> {
            if (customers != null) {
                adapter = new AccountStatementAdapter(customers);
                binding.recyclerViewAccountStmt.setAdapter(adapter);
                progressDialog.dismiss();
            }
        });

        binding.btnPrintAccStatement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationState.navigateToActivity(AccountStatement.this, PrintAccountStatement.class);
            }
        });

    }

    private void changeStatusBarColor(int colorResId) {
        // For devices running Lollipop (API 21) or above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS); // Allows background color change
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS); // Clears any transparency setting
            window.setStatusBarColor(getResources().getColor(colorResId)); // Set the color
        }
    }
}