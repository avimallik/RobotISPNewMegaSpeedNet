package com.armavi_bsd.robotispreconstructed_mega.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
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
import com.armavi_bsd.robotispreconstructed_mega.Complain;
import com.armavi_bsd.robotispreconstructed_mega.R;
import com.armavi_bsd.robotispreconstructed_mega.model.ComplainModel;
import com.armavi_bsd.robotispreconstructed_mega.navigationEndState.NavigationWithEndState;
import com.armavi_bsd.robotispreconstructed_mega.urlStorage.URLStorage;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComplainAdapter extends RecyclerView.Adapter<ComplainAdapter.ComplainViewHolder> {

    URLStorage urlStorage = new URLStorage();
    BottomSheetDialog bottomSheetDialog;
    TextView bsDetailsTxt, bsComplainID;
    Button bsStatusChangeBtn;
    Spinner bsComplainStatusSpinner;

    ComplainModel complain = new ComplainModel();
    Context context;
    String complainStatusValStore = "", complainIDValStore;
    private List<ComplainModel> complains = new ArrayList<>();
    NavigationWithEndState navigationWithEndState = new NavigationWithEndState();
    public ComplainAdapter(Context context) {
        this.context = context;
    }

    public void setComplains(List<ComplainModel> complains) {
        this.complains = complains;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ComplainAdapter.ComplainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_complain, parent, false);
        return new ComplainAdapter.ComplainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComplainAdapter.ComplainViewHolder holder, int position) {
        ComplainModel complain = complains.get(position);
        holder.bind(complain);
    }

    @Override
    public int getItemCount() {
        return complains.size();
    }

    class ComplainViewHolder extends RecyclerView.ViewHolder {
        private TextView itemCustomerName, itemComplainType, itemComplainIP, itemComplainTime, itemComplainStatus;

        public ComplainViewHolder(@NonNull View itemView) {
            super(itemView);
           itemCustomerName = itemView.findViewById(R.id.itemCustomerName);
           itemComplainType = itemView.findViewById(R.id.itemComplainType);
           itemComplainIP = itemView.findViewById(R.id.itemComplainIP);
           itemComplainTime = itemView.findViewById(R.id.itemComplainTime);
           itemComplainStatus = itemView.findViewById(R.id.itemComplainStatus);
        }

        public void bind(ComplainModel complain) {

//            name.setText(agent.getAg_name());
//            mobile.setText(agent.getAg_mobile_no());
//            cusId.setText(agent.getCus_id());

            itemCustomerName.setText(complain.getAg_name());
            itemComplainType.setText("Problem : "+complain.getDetails());
            itemComplainIP.setText("IP : "+complain.getIp());
            itemComplainTime.setText("Date : "+complain.getComplain_date());
//            itemComplainStatus.setText(complain.getStatus());

            if(complain.getStatus().equals("1")){
                itemComplainStatus.setText("Pending");
                itemComplainStatus.setBackgroundResource(R.color.pending);
            } else if (complain.getStatus().equals("2")) {
                itemComplainStatus.setText("Reviewed");
                itemComplainStatus.setBackgroundResource(R.color.processing);
            } else if (complain.getStatus().equals("3")) {
                itemComplainStatus.setText("Solved");
                itemComplainStatus.setBackgroundResource(R.color.solved);
            }
            itemView.setOnClickListener(v -> {
//                Intent agentInfoIntent = new Intent(v.getContext(), AgentInfo.class);
//                agentInfoIntent.putExtra("intent_agent_name", agent.getAg_name());
//                agentInfoIntent.putExtra("intent_agent_address", agent.getAg_office_address());
//                agentInfoIntent.putExtra("intent_agent_mobile", agent.getAg_mobile_no());
//                agentInfoIntent.putExtra("intent_agent_package", agent.getMb());
//                agentInfoIntent.putExtra("intent_agent_bill", agent.getTaka());
//                agentInfoIntent.putExtra("intent_agent_condate", agent.getConnection_date());
//                agentInfoIntent.putExtra("intent_agent_zone_name", agent.getZone_name());
//                agentInfoIntent.putExtra("intent_agent_cus_id", agent.getCus_id());
//                agentInfoIntent.putExtra("intent_agent_cus_ip", agent.getIp());
//                v.getContext().startActivity(agentInfoIntent);
                statusToggler(itemView.getContext(), complain);
            });
        }
    }

    private void statusToggler(Context context, ComplainModel complain) {
        bottomSheetDialog = new BottomSheetDialog(context);
        View bottomSheetView = LayoutInflater.from(context)
                .inflate(R.layout.dialog_complain_status_change, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        bsDetailsTxt = bottomSheetView.findViewById(R.id.bsDetailsTxt);
        bsStatusChangeBtn = bottomSheetDialog.findViewById(R.id.bsStatusChangeBtn);
        bsComplainStatusSpinner = bottomSheetDialog.findViewById(R.id.bsComplainStatusSpinner);
        bsComplainID = bottomSheetDialog.findViewById(R.id.bsComplainID);

        bsComplainStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    complainStatusValStore = "";
                } else if (position == 1) {
                    complainStatusValStore = "1";
                } else if (position == 2) {
                    complainStatusValStore = "2";
                } else if (position == 3) {
                    complainStatusValStore = "3";
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        bsStatusChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, complainStatusValStore, Toast.LENGTH_SHORT).show();
                updateComplain();
            }
        });

        bsDetailsTxt.setText(complain.getDetails());
        complainIDValStore = complain.getComplain_id();
        bottomSheetDialog.show();
    }

    private void updateComplain() {
//        String urlSubmitComplain = "http://192.168.0.126/newisp/rest_api_mob_dx/robotispComplainStatusUpdate.php";
        String urlSubmitComplain = urlStorage.getHttpStd()
                + urlStorage.getBaseUrl()
                + urlStorage.getComplainStatusChangeEndpoint();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlSubmitComplain,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the response from the API
                        Log.d("Volley Response", response.toString());
                        if(response.trim().equals("1")){
                            Toast.makeText(context, "Status changes successfully!", Toast.LENGTH_SHORT).show();
                            bottomSheetDialog.dismiss();
                            navigationWithEndState.navigateToActivity(context, Complain.class);
                        } else if (response.trim().equals("0")) {
                            Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show();
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
                params.put("id", complainIDValStore);
                params.put("status", complainStatusValStore);
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
