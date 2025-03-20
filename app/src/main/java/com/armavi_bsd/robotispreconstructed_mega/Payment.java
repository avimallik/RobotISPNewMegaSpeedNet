package com.armavi_bsd.robotispreconstructed_mega;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.armavi_bsd.robotispreconstructed_mega.databinding.ActivityPaymentBinding;
import com.armavi_bsd.robotispreconstructed_mega.dialogs.WarningDialog;
import com.armavi_bsd.robotispreconstructed_mega.navigationEndState.NavigationState;
import com.armavi_bsd.robotispreconstructed_mega.navigationEndState.NavigationWithEndState;
import com.armavi_bsd.robotispreconstructed_mega.urlStorage.URLStorage;
import com.armavi_bsd.robotispreconstructed_mega.util.Intentkey;
import com.armavi_bsd.robotispreconstructed_mega.util.Pref;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Payment extends AppCompatActivity {

    ActivityPaymentBinding binding;

    URLStorage urlStorage = new URLStorage();

    NavigationWithEndState navigationWithEndState = new NavigationWithEndState();
    NavigationState navigationState = new NavigationState();

    SharedPreferences sharedPreferences;

    Handler handler;
    Runnable runnable;

    Intentkey intentkey = new Intentkey();
    Pref pref = new Pref();

    String agentId;
    String userId;

    Long dueDisplayed = 0L;
    Long dueInputed = 0L;

    String smsPaymentNotifyToggleVal = "1";
    String totalDue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        changeStatusBarColor(R.color.colorPrimary);

        handler = new Handler(Looper.getMainLooper());

        sharedPreferences = getSharedPreferences(pref.getPrefUserCred(), MODE_PRIVATE);

        agentId = getIntent().getExtras().getString(intentkey.getAgentIdIntentkey());
        userId = sharedPreferences.getString(pref.getPrefUserID(), "");

//        if(binding.billDueTxt.getText().toString().trim().equals("0")){
//          navigationWithEndState.navigateToActivity(Payment.this, BillPay.class);
//        }

        runnable = new Runnable() {
            @Override
            public void run() {
//                if(binding.billDueTxt.getText().toString().trim().equals("0")){
//                    navigationWithEndState.navigateToActivity(Payment.this, BillPay.class);
//                }
                fetchBillInfoData();
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(runnable);

        binding.smsPaymentNotify.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    smsPaymentNotifyToggleVal = "1";
                }else {
                    smsPaymentNotifyToggleVal = "0";
                }
            }
        });
        binding.addPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dueDisplayed = Long.parseLong(binding.billDueTxt.getText().toString().trim());
//                dueInputed = Long.parseLong(binding.inputPayAmount.getText().toString().trim());
//
//                Toast.makeText(getApplicationContext(), dueDisplayed + " " +dueInputed, Toast.LENGTH_SHORT).show();

                if(binding.inputPayAmount.getText().toString().trim().equals("")){
                    WarningDialog.warningDialog(Payment.this, "Payment Amount is empty!");
                }else{
                    submitPayment();
//                    jumpToTheBillList();
//                    Intent printServiceIntent = new Intent(getApplicationContext(), PrintServiceTest.class);
//                    printServiceIntent.putExtra(intentkey.getAgentIdIntentkey(),
//                            binding.infoAgentId.getText().toString().trim());
//                    startActivity(printServiceIntent);
                }
            }
        });

    }

    public void fetchBillInfoData() {

        // URL to fetch data from the API
//        String url = "http://192.168.0.100/mega/rest_api_mob_dx/billinfo.php?token="
//                +agentId;

        String url = urlStorage.getHttpStd()+urlStorage.getBaseUrl()+urlStorage.getBillInfoEndpoint()
                +agentId;

        // Create a RequestQueue
        RequestQueue queue = Volley.newRequestQueue(Payment.this);

        // Create a JsonArrayRequest to fetch the JSON data
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Assuming the response is an array with only one object, as in your example
                            JSONObject jsonObject = response.getJSONObject(0);

                            // Extracting the values from the JSON object
                            String agentId = jsonObject.getString("agent_id");
                            String agentName = jsonObject.getString("agent_name");
                            String customerId = jsonObject.getString("customer_id");
                            String total = jsonObject.getString("total");
                            String totalDue = jsonObject.getString("total_due");
                            String bill = jsonObject.getString("bill");
                            String mobile = jsonObject.getString("mobile");

                            binding.billDueTxt.setText(totalDue);
                            binding.infoAgentId.setText(agentId);
                            binding.inforCustomerID.setText(customerId);
                            binding.infoCustomerName.setText(agentName);
                            binding.billPayAmountTxt.setText(total);
                            binding.billAmountTxt.setText(bill);
                            binding.infoCustomerMobile.setText(mobile);

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

//    void submitPayment(){
//
////        String urlPayment = "http://192.168.0.100/mega/rest_api_mob_dx/robotisppaymentinsert.php";
//        String urlPayment = urlStorage.getHttpStd()
//                +urlStorage.getBaseUrl()
//                +urlStorage.getBillpaymentIntersect();
//        final StringRequest stringRequest = new StringRequest(Request.Method.POST, urlPayment,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
////                        Log.d("Volley inspection", response.toString());
////                        if(response.toString().trim().equals("1")){
////                            Log.d("Msg Volley", "Success");
//////                            SuccessDialogPayment.SuccessDialogPayment(Payment.this, "Payment added successfully");
//////                            SuccessDialog.successDialog(Payment.this, "Payment added successfully");
////                        }else if(response.toString().trim().equals("0")){
////                            Log.d("Msg Volley", "Error");
////                            WarningDialog.warningDialog(Payment.this, "Error");
////                        }
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//
//                            // ✅ Read root-level JSON values
//                            String date = jsonObject.getString("date");
//                            String amount = jsonObject.getString("amount");
////                            String accDescription = jsonObject.getString("acc_description");
////                            String entryDate = jsonObject.getString("entry_date");
////                            String month = jsonObject.getString("month");
////                            String prefix = jsonObject.getString("prefix");
//                            String entryBy = jsonObject.getString("entry_by");
//                            String entryUserName = jsonObject.getString("entry_user_name");
////                            String agentId = jsonObject.getString("agent_id");
//                            String dues = jsonObject.getString("dues");
//                            String status = jsonObject.getString("status");
//
//                            String acc_id = jsonObject.getString("acc_id");
////
////                            // ✅ Read nested JSON object "agent_details"
//                            JSONObject agentDetails = jsonObject.getJSONObject("agent_details");
//                            String agentName = agentDetails.getString("ag_name");
//                            String cusID = agentDetails.getString("cus_id");
//                            String agentAddress = agentDetails.getString("ag_office_address");
//                            String agentMobile = agentDetails.getString("ag_mobile_no");
////                            String connectionType = agentDetails.getString("connection_type");
////                            String zone = agentDetails.getString("zone");
//                            String mb = agentDetails.getString("mb");
//                            String taka = agentDetails.getString("taka");
////                            String acc_id = agentDetails.getString("acc_id");
//
////                            Intent printServiceIntent = new Intent(Payment.this, ESCPOSTest.class);
////                            printServiceIntent.putExtra(intentkey.getPosDateIntentKey(), date);
////                            printServiceIntent.putExtra(intentkey.getPosAgentNameIntentKey(), agentName);
////                            printServiceIntent.putExtra(intentkey.getPosCusIDIntentKey(), cusID);
////                            printServiceIntent.putExtra(intentkey.getPosAgentMobileNoIntentKey(), agentMobile);
////                            printServiceIntent.putExtra(intentkey.getPosMBIntentKey(), mb);
////                            printServiceIntent.putExtra(intentkey.getPosAmountIntentKey(), amount);
////                            printServiceIntent.putExtra(intentkey.getPosAgentTotalDueIntentKey(), dues);
////                            printServiceIntent.putExtra(intentkey.getPosEntryUserNameIntentKey(), entryUserName);
////                            printServiceIntent.putExtra(intentkey.getPosAgentAddressIntentKey(), agentAddress);
////                            printServiceIntent.putExtra(intentkey.getPosPackageBill(), taka);
////                            printServiceIntent.putExtra(intentkey.getPosAccountIDIntentKey(), acc_id);
////                            startActivity(printServiceIntent);
//
//                            Intent printTestIntent = new Intent(Payment.this, PrintTest.class);
//                            printTestIntent.putExtra(intentkey.getPosAgentTotalDueIntentKey(), dues);
//                            printTestIntent.putExtra(intentkey.getPosAgentNameIntentKey(), agentName);
//                            printTestIntent.putExtra(intentkey.getPosAgentAddressIntentKey(), agentAddress);
//                            printTestIntent.putExtra(intentkey.getPosCusIDIntentKey(), cusID);
//                            printTestIntent.putExtra(intentkey.getPosPackageBill(), taka);
//                            printTestIntent.putExtra(intentkey.getPosAgentMobileNoIntentKey(), agentMobile);
//                            printTestIntent.putExtra(intentkey.getPosMBIntentKey(), mb);
//                            printTestIntent.putExtra(intentkey.getPosAmountIntentKey(), amount);
//                            printTestIntent.putExtra(intentkey.getPosEntryUserNameIntentKey(), entryUserName);
//                            printTestIntent.putExtra(intentkey.getPosDateIntentKey(), date);
//
//                            startActivity(printTestIntent);
//
//                            Log.d("Account ID", acc_id);
//                            Log.d("Dues", dues);
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            Log.e("JSON Parsing Error", "Error parsing JSON: " + e.getMessage());
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.d("Volley error inspection", error.toString());
//                    }
//
//                }) {
//            @Override
//            protected Map<String, String> getParams() {
//
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("agent_id", binding.infoAgentId.getText().toString().trim());
//                params.put("acc_amount", binding.inputPayAmount.getText().toString().trim());
//                params.put("entry_by", userId);
//                params.put("update_by", userId);
//                params.put("acc_description", binding.inputPaymentDescription.getText().toString().trim());
//                params.put("sms_toggle", smsPaymentNotifyToggleVal);
//                params.put("mobile_number", binding.infoCustomerMobile.getText().toString().trim());
//                return params;
//            }
//
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(Payment.this);
//        requestQueue.add(stringRequest);
//    }

    //New payment submission code
    void submitPayment(){

//        String urlPayment = "http://192.168.0.100/mega/rest_api_mob_dx/robotisppaymentinsert.php";
        String urlPayment = urlStorage.getHttpStd()
                +urlStorage.getBaseUrl()
                +urlStorage.getBillpaymentIntersect();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, urlPayment,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.d("Volley inspection", response.toString());
//                        if(response.toString().trim().equals("1")){
//                            Log.d("Msg Volley", "Success");
////                            SuccessDialogPayment.SuccessDialogPayment(Payment.this, "Payment added successfully");
////                            SuccessDialog.successDialog(Payment.this, "Payment added successfully");
//                        }else if(response.toString().trim().equals("0")){
//                            Log.d("Msg Volley", "Error");
//                            WarningDialog.warningDialog(Payment.this, "Error");
//                        }
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            // ✅ Read root-level JSON values
                            String date = jsonObject.getString("date");
                            String amount = jsonObject.getString("amount");
//                            String accDescription = jsonObject.getString("acc_description");
//                            String entryDate = jsonObject.getString("entry_date");
//                            String month = jsonObject.getString("month");
//                            String prefix = jsonObject.getString("prefix");
                            String entryBy = jsonObject.getString("entry_by");
                            String entryUserName = jsonObject.getString("entry_user_name");
//                            String agentId = jsonObject.getString("agent_id");
                            String dues = jsonObject.getString("dues");
                            String status = jsonObject.getString("status");

                            String acc_id = jsonObject.getString("acc_id");
//
//                            // ✅ Read nested JSON object "agent_details"
                            JSONObject agentDetails = jsonObject.getJSONObject("agent_details");
                            String agentName = agentDetails.getString("ag_name");
                            String cusID = agentDetails.getString("cus_id");
                            String agentAddress = agentDetails.getString("ag_office_address");
                            String agentMobile = agentDetails.getString("ag_mobile_no");
//                            String connectionType = agentDetails.getString("connection_type");
//                            String zone = agentDetails.getString("zone");
                            String mb = agentDetails.getString("mb");
                            String taka = agentDetails.getString("taka");
//                            String acc_id = agentDetails.getString("acc_id");

//                            Intent printServiceIntent = new Intent(Payment.this, ESCPOSTest.class);
//                            printServiceIntent.putExtra(intentkey.getPosDateIntentKey(), date);
//                            printServiceIntent.putExtra(intentkey.getPosAgentNameIntentKey(), agentName);
//                            printServiceIntent.putExtra(intentkey.getPosCusIDIntentKey(), cusID);
//                            printServiceIntent.putExtra(intentkey.getPosAgentMobileNoIntentKey(), agentMobile);
//                            printServiceIntent.putExtra(intentkey.getPosMBIntentKey(), mb);
//                            printServiceIntent.putExtra(intentkey.getPosAmountIntentKey(), amount);
//                            printServiceIntent.putExtra(intentkey.getPosAgentTotalDueIntentKey(), dues);
//                            printServiceIntent.putExtra(intentkey.getPosEntryUserNameIntentKey(), entryUserName);
//                            printServiceIntent.putExtra(intentkey.getPosAgentAddressIntentKey(), agentAddress);
//                            printServiceIntent.putExtra(intentkey.getPosPackageBill(), taka);
//                            printServiceIntent.putExtra(intentkey.getPosAccountIDIntentKey(), acc_id);
//                            startActivity(printServiceIntent);

                            if(status.trim().equals("1")){
                                Intent printTestIntent = new Intent(Payment.this, PrintTest.class);
                                printTestIntent.putExtra(intentkey.getPosAgentTotalDueIntentKey(), dues);
                                printTestIntent.putExtra(intentkey.getPosAgentNameIntentKey(), agentName);
                                printTestIntent.putExtra(intentkey.getPosAgentAddressIntentKey(), agentAddress);
                                printTestIntent.putExtra(intentkey.getPosCusIDIntentKey(), cusID);
                                printTestIntent.putExtra(intentkey.getPosPackageBill(), taka);
                                printTestIntent.putExtra(intentkey.getPosAgentMobileNoIntentKey(), agentMobile);
                                printTestIntent.putExtra(intentkey.getPosMBIntentKey(), mb);
                                printTestIntent.putExtra(intentkey.getPosAmountIntentKey(), amount);
                                printTestIntent.putExtra(intentkey.getPosEntryUserNameIntentKey(), entryUserName);
                                printTestIntent.putExtra(intentkey.getPosDateIntentKey(), date);
                                startActivity(printTestIntent);

                                Log.d("Account ID", acc_id);
                                Log.d("Dues", dues);
                                Log.d("Volley Raw Response", response);

                                Toast.makeText(getApplicationContext(), "Payment Successful!", Toast.LENGTH_SHORT).show();

                            }else if (status.trim().equals("0")){
                                Toast.makeText(getApplicationContext(), "Payment Unsuccessful!", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("JSON Parsing Error", "Error parsing JSON: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Volley error inspection", error.toString());
                    }

                }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("agent_id", binding.infoAgentId.getText().toString().trim());
                params.put("acc_amount", binding.inputPayAmount.getText().toString().trim());
                params.put("entry_by", userId);
                params.put("update_by", userId);
                params.put("acc_description", binding.inputPaymentDescription.getText().toString().trim());
                params.put("sms_toggle", smsPaymentNotifyToggleVal);
                params.put("mobile_number", binding.infoCustomerMobile.getText().toString().trim());
                params.put("cus_id", binding.inforCustomerID.getText().toString().trim());
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(Payment.this);
        requestQueue.add(stringRequest);
    }

    void jumpToTheBillList(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // After 3 seconds, navigate to the next activity
                // Optionally close the current activity
                navigationWithEndState.navigateToActivity(Payment.this, BillPay.class);
            }
        }, 3000); // 3
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove any pending callbacks to prevent memory leaks
        handler.removeCallbacks(runnable);
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