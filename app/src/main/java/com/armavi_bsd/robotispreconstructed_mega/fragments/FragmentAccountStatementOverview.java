package com.armavi_bsd.robotispreconstructed_mega.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.armavi_bsd.robotispreconstructed_mega.Payment;
import com.armavi_bsd.robotispreconstructed_mega.R;
import com.armavi_bsd.robotispreconstructed_mega.adapter.AccountStatementAdapter;
import com.armavi_bsd.robotispreconstructed_mega.databinding.FragmentAccountstetmentOverviewBinding;
import com.armavi_bsd.robotispreconstructed_mega.urlStorage.URLStorage;
import com.armavi_bsd.robotispreconstructed_mega.util.Pref;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

public class FragmentAccountStatementOverview extends Fragment {

    private FragmentAccountstetmentOverviewBinding binding;
    SharedPreferences sharedPreferences;

    Pref pref = new Pref();
    URLStorage urlStorage = new URLStorage();

    String userId;

    Handler handler;
    Runnable runnable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAccountstetmentOverviewBinding.inflate(inflater, container, false);

        sharedPreferences = getActivity().getSharedPreferences(pref.getPrefUserCred(), MODE_PRIVATE);
        userId = sharedPreferences.getString(pref.getPrefUserID(), "0");
//
//        fetchAccountStatementOverviewData();

        handler = new Handler(Looper.getMainLooper());
        runnable = new Runnable() {
            @Override
            public void run() {
                // Fetch the account statement data every 2 seconds
                if(isAdded()){
                    fetchAccountStatementOverviewData();
                    // Post the runnable again after 2 seconds (2000 milliseconds)
                    handler.postDelayed(this, 1000);
                }

            }
        };

        // Start the runnable immediately when the fragment is created
        handler.post(runnable);

        return binding.getRoot();
    }

    public void fetchAccountStatementOverviewData() {

        // URL to fetch data from the API
//        String url = "http://192.168.0.122/mega/rest_api_mob_dx/viewAccountStatementOverview.php?userid="+userId;

        String url = urlStorage.getHttpStd()+urlStorage.getBaseUrl()
                +urlStorage.getAccountStatementOverviewEndpoint()
                +userId;

        // Create a RequestQueue
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        // Create a JsonArrayRequest to fetch the JSON data
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Assuming the response is an array with only one object, as in your example
                            JSONObject jsonObject = response.getJSONObject(0);

                            // Extracting the values from the JSON object
                            String debit_total = jsonObject.getString("debit_total");
                            String credit_total = jsonObject.getString("credit_total");
                            String balance = jsonObject.getString("balance");


                            binding.accStmtCredit.setText(credit_total);
                            binding.accStmtDebit.setText(debit_total);
                            binding.accStmtBalance.setText(balance);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );

        // Add the request to the RequestQueue
        queue.add(jsonArrayRequest);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Remove the Runnable callback when the fragment is detached to avoid memory leaks
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
    }

}
