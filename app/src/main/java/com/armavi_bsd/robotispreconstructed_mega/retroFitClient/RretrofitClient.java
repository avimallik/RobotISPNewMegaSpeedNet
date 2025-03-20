package com.armavi_bsd.robotispreconstructed_mega.retroFitClient;

import com.armavi_bsd.robotispreconstructed_mega.urlStorage.URLStorage;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RretrofitClient {
    URLStorage urlStorage = new URLStorage();
    private final String http = urlStorage.getHttpStd();
    private final String BASE_URL = http
            +urlStorage.getBaseUrl()
            +urlStorage.getSlash();
    private static Retrofit retrofit;

    public Retrofit getData() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
