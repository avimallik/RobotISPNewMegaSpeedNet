package com.armavi_bsd.robotispreconstructed_mega.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.armavi_bsd.robotispreconstructed_mega.apiService.ApiService;
import com.armavi_bsd.robotispreconstructed_mega.model.AgentModel;
import com.armavi_bsd.robotispreconstructed_mega.retroFitClient.RretrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgentViewModel extends ViewModel {

    private MutableLiveData<List<AgentModel>> agents = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    RretrofitClient retrofitClient = new RretrofitClient();

    public LiveData<List<AgentModel>> getAgents() {
        return agents;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void fetchAgents() {
        isLoading.setValue(true);
        ApiService apiService = retrofitClient.getData().create(ApiService.class);
        apiService.getAgents().enqueue(new Callback<List<AgentModel>>() {
            @Override
            public void onResponse(Call<List<AgentModel>> call, Response<List<AgentModel>> response) {
                if (response.isSuccessful()) {
                    agents.setValue(response.body());
                }
                isLoading.setValue(false);
            }

            @Override
            public void onFailure(Call<List<AgentModel>> call, Throwable t) {
                isLoading.setValue(false);
            }
        });
    }
}
