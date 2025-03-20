package com.armavi_bsd.robotispreconstructed_mega.mikrotikStatusChecker;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.armavi_bsd.robotispreconstructed_mega.urlStorage.URLStorage;
import com.armavi_bsd.robotispreconstructed_mega.util.Pref;

import org.json.JSONArray;
import org.json.JSONObject;

public class MikrotikStatusChecker {
    private Context context;
    URLStorage urlStorage = new URLStorage();
    SharedPreferences sharedPreferences;
    Pref pref = new Pref();
    private RequestQueue requestQueue;

    // Callback interface for status updates
    public interface StatusCallback {
        void onStatusFetched(int status);
    }

    public MikrotikStatusChecker(Context context) {
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(context);
    }

    // Method to fetch the status
    public void fetchStatus(StatusCallback callback) {
        sharedPreferences = context.getSharedPreferences(pref.getPrefUserCred(), Context.MODE_PRIVATE);
        String mikrotikIpTemp = sharedPreferences.getString(pref.getPrefMikrotikIP(), "");
//        String mikrotikStatusUrl = "http://192.168.0.119/newisp/rest_api_mob_dx/singleMikrotikTest.php?mik_ip="
        String mikrotikStatusUrl = urlStorage.getHttpStd()
                + urlStorage.getBaseUrl()
                + urlStorage.getMikrotikSingleIPCheckStatusEndpoint()
                +mikrotikIpTemp;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                mikrotikStatusUrl,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Parse the response
                            if (response.length() > 0) {
                                JSONObject jsonObject = response.getJSONObject(0);
                                int status = jsonObject.getInt("status");
                                callback.onStatusFetched(status); // Trigger callback
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            callback.onStatusFetched(-1); // Handle error case
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        callback.onStatusFetched(-1); // Handle error case
                    }
                }
        );

        // Add the request to the queue
        requestQueue.add(jsonArrayRequest);
    }
}
