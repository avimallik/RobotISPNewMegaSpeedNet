package com.armavi_bsd.robotispreconstructed_mega.viewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.armavi_bsd.robotispreconstructed_mega.apiService.ApiService;
import com.armavi_bsd.robotispreconstructed_mega.model.AccountStatementModel;
import com.armavi_bsd.robotispreconstructed_mega.retroFitClient.RretrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AccountStatementViewModel extends ViewModel {

    private MutableLiveData<List<AccountStatementModel>> accountStatements;
    RretrofitClient retrofitClient = new RretrofitClient();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public AccountStatementViewModel() {
        accountStatements = new MutableLiveData<>();
    }

    public LiveData<List<AccountStatementModel>> getAccountStatements(String userId) {
        isLoading.setValue(true);
        ApiService apiService = retrofitClient.getData().create(ApiService.class);
        apiService.getAccountStatements(userId).enqueue(new Callback<List<AccountStatementModel>>() {
            @Override
            public void onResponse(Call<List<AccountStatementModel>> call, Response<List<AccountStatementModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    accountStatements.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<AccountStatementModel>> call, Throwable t) {
                // Handle failure
            }
        });

        return accountStatements;
    }
}
