package com.armavi_bsd.robotispreconstructed_mega;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.armavi_bsd.robotispreconstructed_mega.adapter.AccountHeadAdapter;
import com.armavi_bsd.robotispreconstructed_mega.databinding.ActivityAccountHeadBinding;
import com.armavi_bsd.robotispreconstructed_mega.model.AccountheadModel;
import com.armavi_bsd.robotispreconstructed_mega.urlStorage.URLStorage;
import com.armavi_bsd.robotispreconstructed_mega.util.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountHead extends AppCompatActivity {

    RecyclerView.LayoutManager layoutManagerAcchead;
    List<AccountheadModel> personUtilsListAcchead;
    RequestQueue rqGroundAcchead;
    RecyclerView.Adapter mAdapterAcchead;
    SharedPreferences sharedPreferences;
    Pref pref = new Pref();

    ///Acc head diag///
    EditText diag_acc_des, diag_acc_head;
    Button diag_acc_insert;
    Spinner diag_acc_status_select;
    TextView diag_acc_head_status;

    private ActivityAccountHeadBinding binding;

    URLStorage urlStorage = new URLStorage();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAccountHeadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        acccheadRVRecycler();

        binding.addAccountHeadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());

                final View promptView = layoutInflater.inflate(R.layout.diag_acchead_inser, null);
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccountHead.this);
                alertDialogBuilder.setView(promptView);

                diag_acc_head = (EditText) promptView.findViewById(R.id.diag_acc_head);
                diag_acc_des = (EditText)  promptView.findViewById(R.id.diag_acc_des);
                diag_acc_insert = (Button) promptView.findViewById(R.id.diag_acc_insert);
                diag_acc_status_select = (Spinner) promptView.findViewById(R.id.diag_acc_status_select);
                diag_acc_head_status = (TextView) promptView.findViewById(R.id.diag_acc_head_status);

                sharedPreferences = getApplicationContext().getSharedPreferences(pref.getPrefUserCred(),MODE_PRIVATE);
                final String userId = sharedPreferences.getString(pref.getPrefUserID(), "");
                final String request_url = urlStorage.getHttpStd()
                        +urlStorage.getBaseUrl()
                        +urlStorage.getAccountheadInsertIntersect();

                //Status selector///
                HashMap<String, String> statusMap = new HashMap<>();
                statusMap.put("1", "Active");
                statusMap.put("0", "Inactive");
                List<String> statusNames = new ArrayList<>(statusMap.values());

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, statusNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                diag_acc_status_select.setAdapter(adapter);

                // Handle spinner item selection
                diag_acc_status_select.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selectedName = statusNames.get(position);
                        String selectedId = getKeyByValue(statusMap, selectedName);
                        diag_acc_head_status.setText(selectedId);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        diag_acc_head_status.setText(""); // Clear text if nothing is selected
                    }
                });

                ///////////////////

                diag_acc_insert.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(diag_acc_head.getText().toString().isEmpty()){
                            decoratedDialog("Please Type Account Head !");

                        }else {
                            final StringRequest stringRequest = new StringRequest(Request.Method.POST,request_url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
//                                            decoratedDialog(response.toString());
                                            Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(getApplicationContext(), AccountHead.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                            finish();
                                            startActivity(intent);

                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                                        }

                                    }){
                                @Override
                                protected Map<String,String> getParams(){
                                    Map<String,String> params = new HashMap<String, String>();
                                    params.put("entry_by", userId);
                                    params.put("acc_name", diag_acc_head.getText().toString().trim());
                                    params.put("acc_desc", diag_acc_des.getText().toString().trim());
                                    params.put("acc_status", diag_acc_head_status.getText().toString().trim());
                                    params.put("acc_type", "1");
                                    return params;
                                }

                            };
                            stringRequest.setRetryPolicy(new RetryPolicy() {
                                @Override
                                public int getCurrentTimeout() {
                                    return 10000;
                                }

                                @Override
                                public int getCurrentRetryCount() {
                                    return 10000;
                                }

                                @Override
                                public void retry(VolleyError error) throws VolleyError {}
                            });
                            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                            requestQueue.add(stringRequest);
                        }
                    }
                });
                AlertDialog alert = alertDialogBuilder.create();
                alert.show();
            }
        });
    }

    public void sendRequestExpese(){

        String request_url = urlStorage.getHttpStd()
                +urlStorage.getBaseUrl()
                +urlStorage.getAccountHeadListIntersect();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, request_url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                for(int i = 0; i < response.length(); i++){
                    AccountheadModel personUtils = new AccountheadModel();
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        personUtils.setAcc_head_id(jsonObject.getString("acc_id"));
                        personUtils.setAcc_name(jsonObject.getString("acc_name"));
                        personUtils.setAcc_desc(jsonObject.getString("acc_desc"));
                        personUtils.setAcc_type(jsonObject.getString("acc_type"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    Toast.makeText(getApplicationContext(), String.valueOf(tempSumStore), Toast.LENGTH_SHORT).show();
                    personUtilsListAcchead.add(personUtils);
                }
                mAdapterAcchead = new AccountHeadAdapter(AccountHead.this, personUtilsListAcchead);
                binding.recyclerViewAccHead.setAdapter(mAdapterAcchead);
                binding.progressBarExpense.setVisibility(View.INVISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Volley Error: ", error.toString());
            }
        });
        rqGroundAcchead.add(jsonArrayRequest);
    }

    void acccheadRVRecycler(){
        rqGroundAcchead = Volley.newRequestQueue(AccountHead.this);
        binding.recyclerViewAccHead.setHasFixedSize(true);
        layoutManagerAcchead = new LinearLayoutManager(AccountHead.this, LinearLayoutManager.VERTICAL, false);
        binding.recyclerViewAccHead.setVerticalScrollbarPosition(View.SCROLLBAR_POSITION_LEFT);
        binding.recyclerViewAccHead.setLayoutManager(layoutManagerAcchead);
        personUtilsListAcchead = new ArrayList<>();
        sendRequestExpese();
    }

    private String getKeyByValue(HashMap<String, String> map, String value) {
        for (HashMap.Entry<String, String> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null; // Return null if not found
    }

    public void decoratedDialog(String dialogMsg){
        LayoutInflater layoutInflater = LayoutInflater.from(AccountHead.this);
        View promptView = layoutInflater.inflate(R.layout.custom_dialog_alart, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccountHead.this);
        alertDialogBuilder.setView(promptView);
        final TextView dialogAlert = (TextView) promptView.findViewById(R.id.custom_dialog_alert);
        dialogAlert.setText(dialogMsg);

        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

}