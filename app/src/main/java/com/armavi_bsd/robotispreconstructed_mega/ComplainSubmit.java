package com.armavi_bsd.robotispreconstructed_mega;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.armavi_bsd.robotispreconstructed_mega.databinding.ActivityComplainSubmitBinding;
import com.armavi_bsd.robotispreconstructed_mega.dialogs.ComplainTypeDialogFragment;
import com.armavi_bsd.robotispreconstructed_mega.dialogs.CustomerDialogFragment;
import com.armavi_bsd.robotispreconstructed_mega.dialogs.EmployeeDialogFragment;
import com.armavi_bsd.robotispreconstructed_mega.dialogs.WarningDialog;
import com.armavi_bsd.robotispreconstructed_mega.navigationEndState.NavigationWithEndState;
import com.armavi_bsd.robotispreconstructed_mega.urlStorage.URLStorage;
import com.armavi_bsd.robotispreconstructed_mega.util.Pref;
import com.google.android.material.chip.Chip;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComplainSubmit extends AppCompatActivity {

    URLStorage urlStorage = new URLStorage();
    Pref pref = new Pref();
    NavigationWithEndState navigationWithEndState = new NavigationWithEndState();
    private List<String> selectedIds = new ArrayList<>();
    SharedPreferences sharedPreferences;
    CustomerDialogFragment dialog = new CustomerDialogFragment();
    ComplainTypeDialogFragment dialogComplainType = new ComplainTypeDialogFragment();
    EmployeeDialogFragment dialogEmployeeFragment = new EmployeeDialogFragment();
    private ActivityComplainSubmitBinding binding;
    String priorityValStore, smsCheckValStore = "", employeeCheckValStore;
    String userID;
    String sub_solve_by = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityComplainSubmitBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences(pref.getPrefUserCred(),MODE_PRIVATE);
        userID = sharedPreferences.getString(pref.getPrefUserID(), "");


        binding.customerSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setOnCustomerSelectedListener((agId, agName) -> {
                    // Set selected values in TextViews
                    binding.customerSelect.setText(agName);
                    binding.customerIDTxt.setText(agId);
                });
                dialog.show(getSupportFragmentManager(), "CustomerDialog");
            }
        });
        binding.complainTypeSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogComplainType.setOnCustomerSelectedListener((id, template) -> {
                    // Set selected values in TextViews
                    binding.complainTypeSelect.setText(template);
                    binding.complainTypeIDText.setText(id);
                    binding.complainDetailsText.setText(template);
                });
                dialogComplainType.show(getSupportFragmentManager(), "CustomerDialog");
            }
        });

        binding.employeeSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogEmployeeFragment.setOnCustomerSelectedListener((id, employeeName) -> {
                    // Set selected values in TextViews
                    binding.employeeSelect.setText(employeeName);
                    binding.employeeIdText.setText(id);
                });
                dialogEmployeeFragment.show(getSupportFragmentManager(), "CustomerDialog");
            }
        });

        binding.employeeAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogEmployeeFragment.setOnCustomerSelectedListener((id, employeeName) -> {
                    // Set selected values in TextViews
//                    binding.employeeSelect.setText(employeeName);
                    addChip(employeeName, id);
                });
                dialogEmployeeFragment.show(getSupportFragmentManager(), "CustomerDialog");
            }
        });

        if(binding.smsCheck.isChecked()){
            smsCheckValStore = "1";
        } else {
            smsCheckValStore = "";
        }

        if(binding.employeeCheck.isChecked()){
            employeeCheckValStore = "1";
        }else {
            employeeCheckValStore = "";
        }

        binding.spinnerPriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    priorityValStore = "1";
                }else if(position == 1){
                    priorityValStore = "2";
                } else if (position == 2) {
                    priorityValStore = "3";
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        binding.complainPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(binding.smsCheck.isChecked()){
                    smsCheckValStore = "1";
                } else {
                    smsCheckValStore = "";
                }

                if(binding.employeeCheck.isChecked()){
                    employeeCheckValStore = "1";
                }else {
                    employeeCheckValStore = "";
                }

                if(binding.customerSelect.getText().toString().trim().isEmpty()){
                    WarningDialog.warningDialog(ComplainSubmit.this, "Select Customer");
                }else if (binding.complainDetailsText.getText().toString().trim().isEmpty()) {
                    WarningDialog.warningDialog(ComplainSubmit.this, "Type complain Details");
                } else if (binding.employeeSelect.getText().toString().trim().isEmpty()) {
                    WarningDialog.warningDialog(ComplainSubmit.this, "Select Employee");
                }  else {
                    submitComplain();
                }
            }
        });
    }
    private void addChip(String employeeName, String id) {
        if (selectedIds.contains(id)) {
            Toast.makeText(this, employeeName + " is already selected", Toast.LENGTH_SHORT).show();
            return;
        }

        Chip chip = new Chip(this);
        chip.setText(employeeName);
        chip.setCloseIconVisible(true);
        chip.setOnCloseIconClickListener(v -> {
            binding.chipGroup.removeView(chip);
            selectedIds.remove(id);
            updateSelectedIdsTextView();
        });

        binding.chipGroup.addView(chip);
        selectedIds.add(id);
        updateSelectedIdsTextView();
    }

    private void updateSelectedIdsTextView() {
        binding.selectedIdsTextView.setText(String.join(",", selectedIds));
    }

    private void submitComplain() {
        sub_solve_by = binding.selectedIdsTextView.getText().toString().trim();

//        String urlSubmitComplain = "http://192.168.0.132/mitisp/rest_api_mob_dx/robotispcomplaininsert.php";

        String urlSubmitComplain = urlStorage.getHttpStd()
                +urlStorage.getBaseUrl()
                +urlStorage.getComplaindataInsertIntersect();

        Log.d("Volley Debug", "Submitting complain to URL: " + urlSubmitComplain);

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlSubmitComplain,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volley Response", "Raw Response: " + response);
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            int status = jsonResponse.getInt("status");
                            String message = jsonResponse.getString("message");

                            Log.d("Volley Response", "Parsed Response: Status = " + status + ", Message = " + message);

                            if (status == 1) {
                                Toast.makeText(getApplicationContext(),
                                        "Complain Posted Successfully!",
                                        Toast.LENGTH_SHORT).show();
                                navigationWithEndState.navigateToActivity(ComplainSubmit.this, Complain.class);
                            } else {
                                WarningDialog.warningDialog(ComplainSubmit.this, "Error: " + message);
                            }
                        } catch (JSONException e) {
                            Log.e("Volley Response", "JSON Parsing Error: " + e.getMessage());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            String errorData = new String(error.networkResponse.data);
                            Log.e("Volley Error", "Status Code: " + error.networkResponse.statusCode);
                            Log.e("Volley Error", "Response Data: " + errorData);
                            Toast.makeText(ComplainSubmit.this, "Error: " + errorData, Toast.LENGTH_LONG).show();
                        } else {
                            Log.e("Volley Error", "Network error: " + error.getMessage());
                            Toast.makeText(ComplainSubmit.this, "Network Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                if (binding != null) { // Prevents NullPointerException
                    params.put("details", binding.complainDetailsText.getText().toString().trim());
                    params.put("note", binding.complainNoteText.getText().toString().trim());
                    params.put("customer_id", binding.customerIDTxt.getText().toString().trim());
                    params.put("employee_id", binding.employeeIdText.getText().toString().trim());
                    params.put("user_id", userID);
                    params.put("sub_solve_by", sub_solve_by);
                } else {
                    Log.e("Volley Debug", "Binding is NULL");
                }
                return params;
            }
        };

        // ✅ Properly set Retry Policy (Timeout: 15 sec, Default Retries)
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(stringRequest);
    }


    void toastFunc(String tMessage){
        Toast.makeText(getApplicationContext(), tMessage, Toast.LENGTH_SHORT).show();
    }

}