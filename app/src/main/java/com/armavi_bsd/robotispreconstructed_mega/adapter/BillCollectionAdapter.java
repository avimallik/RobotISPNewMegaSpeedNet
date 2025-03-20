package com.armavi_bsd.robotispreconstructed_mega.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.armavi_bsd.robotispreconstructed_mega.BillPay;
import com.armavi_bsd.robotispreconstructed_mega.Payment;
import com.armavi_bsd.robotispreconstructed_mega.R;
import com.armavi_bsd.robotispreconstructed_mega.model.BillCollectionModel;
import com.armavi_bsd.robotispreconstructed_mega.navigationEndState.NavigationWithEndState;
import com.armavi_bsd.robotispreconstructed_mega.urlStorage.URLStorage;
import com.armavi_bsd.robotispreconstructed_mega.util.Intentkey;
import com.armavi_bsd.robotispreconstructed_mega.util.Pref;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BillCollectionAdapter extends RecyclerView.Adapter<BillCollectionAdapter.BillCollectionViewHolder> {
    BottomSheetDialog bottomSheetBillInfo;
    BottomSheetDialog bottomSheetPayment;
    SharedPreferences sharedPreferences;
    Pref pref = new Pref();
    Intentkey intentkey = new Intentkey();
    NavigationWithEndState navigationWithEndState = new NavigationWithEndState();
    Context context;
    String userID;
    URLStorage urlStorage = new URLStorage();
    private List<BillCollectionModel> billCollections = new ArrayList<>();

    public BillCollectionAdapter(Context context) {
        this.context = context;
    }
    public void setAgents(List<BillCollectionModel> billCollections) {
        this.billCollections = billCollections;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BillCollectionAdapter.BillCollectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bill_collection, parent, false);
        return new BillCollectionAdapter.BillCollectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillCollectionAdapter.BillCollectionViewHolder holder, int position) {
        BillCollectionModel billCollection = billCollections.get(position);
        holder.bind(billCollection);
    }

    @Override
    public int getItemCount() {
        return billCollections.size();
    }

    class BillCollectionViewHolder extends RecyclerView.ViewHolder {
        private TextView agent_name, ag_mobile_no, zone_name, cus_id, ipTxt, bill, ag_address, ag_customer_id;
        Button displayPaymentInput;
        LinearLayout billSlackContainer;

        public BillCollectionViewHolder(@NonNull View itemView) {
            super(itemView);
            agent_name = itemView.findViewById(R.id.agent_name);
            ag_mobile_no = itemView.findViewById(R.id.ag_mobile_no);
            zone_name = itemView.findViewById(R.id.zoneName);
            cus_id = itemView.findViewById(R.id.cus_id);
            ipTxt = itemView.findViewById(R.id.ipTxt);
            bill = itemView.findViewById(R.id.bill_amt);
            ag_address = itemView.findViewById(R.id.ag_address);
            ag_customer_id = itemView.findViewById(R.id.ag_customer_id);
            billSlackContainer = itemView.findViewById(R.id.bill_slack_container);
            displayPaymentInput = (Button) itemView.findViewById(R.id.displayPaymentInput);
        }

        public void bind(BillCollectionModel billCollection) {

            agent_name.setText(billCollection.getAgent_name());
            ag_mobile_no.setText("Mobile no : "
                    +billCollection.getMobile());
            zone_name.setText("Zone : "
                    +billCollection.getZone());
            ipTxt.setText(billCollection.getIp().toString().trim());
            cus_id.setText(billCollection.getCustomer_id());
            bill.setText(billCollection.getBill()+" Tk");
            ag_customer_id.setText("Customer ID : " + billCollection.getCustomer_id());
            ag_address.setText("Address : " + billCollection.getAgent_address());

            itemView.setOnClickListener(v -> {
//                billInfoDisplay(itemView.getContext(), billCollection);
            });

            displayPaymentInput.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    billPaymentDialog(itemView.getContext(), billCollection);
                }
            });

            billSlackContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, String.valueOf(billCollection.getAgent_id()), Toast.LENGTH_SHORT).show();
                    Intent paymentIntent = new Intent(context, Payment.class);
                    paymentIntent.putExtra(intentkey.getAgentIdIntentkey(), String.valueOf(billCollection.getAgent_id()));
                    context.startActivity(paymentIntent);
                }
            });
        }
    }

//    private void billInfoDisplay(Context context, BillCollectionModel billCollection){
//        bottomSheetBillInfo = new BottomSheetDialog(context);
//        View billInfoBottomSheetView = LayoutInflater.from(context).inflate(R.layout.dialog_bill_info, null);
//        bottomSheetBillInfo.setContentView(billInfoBottomSheetView);
//
//        TextView bsMonthlyBill,
//                bsTotalDue,
//                bsBillDate,
//                bsZoneName,
//                bsBillingPersonName,
//                bsBillStatus,
//                bsPackage;
//
//        sharedPreferences = context.getSharedPreferences(pref.getPrefUserCred(), MODE_PRIVATE);
//        userID = sharedPreferences.getString(pref.getPrefUserID(), "1");
//
//        bsMonthlyBill = bottomSheetBillInfo.findViewById(R.id.bsMonthlyBill);
//        bsTotalDue = bottomSheetBillInfo.findViewById(R.id.bsTotalDue);
//        bsBillDate = bottomSheetBillInfo.findViewById(R.id.bsBillDate);
//        bsZoneName = bottomSheetBillInfo.findViewById(R.id.bsZoneName);
//        bsBillingPersonName = bottomSheetBillInfo.findViewById(R.id.bsBillingPersonName);
//        bsBillStatus = bottomSheetBillInfo.findViewById(R.id.bsBillStatus);
//        bsPackage = bottomSheetBillInfo.findViewById(R.id.bsPackage);
//
//        bsMonthlyBill.setText(String.valueOf(billCollection.getTaka())+" Tk");
//        bsTotalDue.setText(String.valueOf(billCollection.getDueadvance())+" Tk");
//        bsBillDate.setText(billCollection.getMikrotik_disconnect());
//        bsZoneName.setText(billCollection.getZone_name());
//        bsBillingPersonName.setText(billCollection.getBillingperson());
//        bsPackage.setText(billCollection.getMb());
//
//        if(billCollection.getBill_status() == 1){
//            bsBillStatus.setText("Partially Paid");
//        } else {
//            bsBillStatus.setText("Unpaid");
//            bsBillStatus.setTextColor(context.getColor(R.color.warning));
//        }
//        bottomSheetBillInfo.show();
//    }

//    private void billPaymentDialog(Context context, BillCollectionModel billCollection){
//
//        bottomSheetPayment = new BottomSheetDialog(context, R.style.BottomSheetTheme);
//        View billPaymentView = LayoutInflater.from(context).inflate(R.layout.dialog_bill_payment, null);
//        bottomSheetPayment.setContentView(billPaymentView);
//
//        TextView bsDueAmountTitle, bsDiscountAmountTitle, bsDescriptionTitle, bsAgID;
//        EditText bsDueAmountInput, bsDiscountAmountInput, bsDescriptionInput;
//        Button bsBillPaymentBtn;
//
//        //Titles
//        bsDueAmountTitle = bottomSheetPayment.findViewById(R.id.bsDueAmountTitle);
//        bsDueAmountTitle.setText("Due Amount: "
//                + String.valueOf(billCollection.getDueadvance())
//                + " BDT* Pay Amount:");
//        bsDiscountAmountTitle = bottomSheetPayment.findViewById(R.id.bsDiscountAmountTitle);
//        bsDescriptionTitle = bottomSheetPayment.findViewById(R.id.bsDescriptionTitle);
//        bsAgID = bottomSheetPayment.findViewById(R.id.bsAgID);
//        bsAgID.setText(String.valueOf(billCollection.getAg_id()));
//
//        //Inputs
//        bsDueAmountInput = bottomSheetPayment.findViewById(R.id.bsDueAmountInput);
//        bsDueAmountInput.setText(String.valueOf(billCollection.getDueadvance()));
//
//        bsDiscountAmountInput = bottomSheetPayment.findViewById(R.id.bsDiscountAmountInput);
//        bsDescriptionInput = bottomSheetPayment.findViewById(R.id.bsDescriptionInput);
//
//        //Button
//        bsBillPaymentBtn = bottomSheetPayment.findViewById(R.id.bsBillPaymentBtn);
//        bsBillPaymentBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sharedPreferences = context.getSharedPreferences(pref.getPrefUserCred(), MODE_PRIVATE);
//                userID = sharedPreferences.getString(pref.getPrefUserID(), "1");
//                billPaymentSubmit(bsDueAmountInput.getText().toString().trim(),
//                        bsDiscountAmountInput.getText().toString().trim(),
//                        bsAgID.getText().toString().trim(), userID);
//            }
//        });
//
//        bottomSheetPayment.show();
//    }

    private void billPaymentSubmit(String amountTemp, String discountTemp, String agIdTemp, String userID){
//        String urlSubmitPayment = "http://192.168.0.126/newisp/rest_api_mob_dx/testBillPay.php";
        String  urlSubmitPayment = urlStorage.getHttpStd()
                + urlStorage.getBaseUrl()
                + urlStorage.getTestBillPaySubmitEndpoint();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlSubmitPayment,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the response from the API
                        Log.d("Volley Response", response.toString());
                        try{
                            String jsonResponse = response;
                            JSONObject jsonObject = new JSONObject(jsonResponse);
                            boolean success = jsonObject.getBoolean("success");
                            String responseCode = jsonObject.getString("message");
                            Log.d("Response message", responseCode);
                            if(success){
                                Toast.makeText(context, "Payment is successfully submitted!", Toast.LENGTH_SHORT).show();
                                bottomSheetPayment.dismiss();
                                navigationWithEndState.navigateToActivity(context, BillPay.class);
                            }else {
                                Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle any error that occurs during the request
                        Log.d("Volley Response", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Create a map to hold the parameters for the POST request
                Map<String, String> params = new HashMap<>();
                params.put("amount", amountTemp);
                params.put("discount", discountTemp);
                params.put("ag_id", agIdTemp);
                params.put("user_id", userID);
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
            public void retry(VolleyError error) throws VolleyError {
            }
        });
        RequestQueue requestQueueSendAlert = Volley.newRequestQueue(context);
        requestQueueSendAlert.add(stringRequest);
    }
}
