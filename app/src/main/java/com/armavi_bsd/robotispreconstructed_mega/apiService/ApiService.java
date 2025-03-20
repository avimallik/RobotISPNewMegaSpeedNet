package com.armavi_bsd.robotispreconstructed_mega.apiService;

import com.armavi_bsd.robotispreconstructed_mega.model.AccountStatementModel;
import com.armavi_bsd.robotispreconstructed_mega.model.AgentModel;
import com.armavi_bsd.robotispreconstructed_mega.model.BillCollectionModel;
import com.armavi_bsd.robotispreconstructed_mega.model.ComplainModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
//    @GET("rest_api_mob_dx/testCustomerView.php")
//    Call<List<AgentModel>> getAgents();

    @GET("rest_api_mob_dx/robotispagentdisplay.php")
    Call<List<AgentModel>> getAgents();

    @GET("rest_api_mob_dx/robotispcomplainview.php")
    Call<List<ComplainModel>> getComplain();

    @GET("rest_api_mob_dx/robotispviewpayment.php")
    Call<List<BillCollectionModel>> getBillCollection();

    @GET("rest_api_mob_dx/viewListAccountStatement.php")
    Call<List<AccountStatementModel>> getAccountStatements(@Query("userid") String userId);
}
