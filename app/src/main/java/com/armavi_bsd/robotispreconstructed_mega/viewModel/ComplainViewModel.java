package com.armavi_bsd.robotispreconstructed_mega.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.armavi_bsd.robotispreconstructed_mega.apiService.ApiService;
import com.armavi_bsd.robotispreconstructed_mega.model.ComplainModel;
import com.armavi_bsd.robotispreconstructed_mega.retroFitClient.RretrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComplainViewModel extends ViewModel {
    private MutableLiveData<List<ComplainModel>> complains = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    RretrofitClient retrofitClient = new RretrofitClient();

    public LiveData<List<ComplainModel>> getComplains() {
        return complains;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void fetchComplains() {
        isLoading.setValue(true);
        ApiService apiService = retrofitClient.getData().create(ApiService.class);
        apiService.getComplain().enqueue(new Callback<List<ComplainModel>>() {
            @Override
            public void onResponse(Call<List<ComplainModel>> call, Response<List<ComplainModel>> response) {
                if (response.isSuccessful()) {
                    complains.setValue(response.body());
                }
                isLoading.setValue(false);
            }

            @Override
            public void onFailure(Call<List<ComplainModel>> call, Throwable t) {
                isLoading.setValue(false);
            }
        });
    }
}
