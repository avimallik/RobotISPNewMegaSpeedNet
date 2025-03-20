package com.armavi_bsd.robotispreconstructed_mega.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.armavi_bsd.robotispreconstructed_mega.AccountStatement;
import com.armavi_bsd.robotispreconstructed_mega.AccountSubHead;
import com.armavi_bsd.robotispreconstructed_mega.AgentDisplay;
import com.armavi_bsd.robotispreconstructed_mega.AgentEntry;
import com.armavi_bsd.robotispreconstructed_mega.BillPay;
import com.armavi_bsd.robotispreconstructed_mega.Complain;
import com.armavi_bsd.robotispreconstructed_mega.Expense;
import com.armavi_bsd.robotispreconstructed_mega.R;
import com.armavi_bsd.robotispreconstructed_mega.AccountHead;
import com.armavi_bsd.robotispreconstructed_mega.model.MenuRVModel;
import com.armavi_bsd.robotispreconstructed_mega.model.Mikrotik;
import com.armavi_bsd.robotispreconstructed_mega.navigationEndState.NavigationState;
import com.armavi_bsd.robotispreconstructed_mega.navigationEndState.NavigationWithEndState;
import com.armavi_bsd.robotispreconstructed_mega.urlStorage.URLStorage;
import com.armavi_bsd.robotispreconstructed_mega.util.Intentkey;
import com.armavi_bsd.robotispreconstructed_mega.util.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    Intentkey intentkey = new Intentkey();
    Pref pref = new Pref();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    NavigationWithEndState navigationWithEndState = new NavigationWithEndState();
    NavigationState navigationState = new NavigationState();
    TextView diag_mik_ip, diag_mik_id;
    Button diag_exp_insert;
    private TextView mikrotikListTextView, selectedIpTextView, selectedIdTextView;
    private List<Mikrotik> mikrotikList = new ArrayList<>();
    private MikrotikAdapter adapter;
    private List<MenuRVModel> mList;
    private Context mContext;
    URLStorage urlStorage = new URLStorage();
    String KEY_MIK_IP = "mik_ip";
    public MenuRVAdapter(List<MenuRVModel> list, Context context){
        super();
        mList = list;
        mContext = context;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.menu_rv_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder( RecyclerView.ViewHolder viewHolder, int position) {
        MenuRVModel itemAdapter = mList.get(position);
        ((ViewHolder) viewHolder).mTv_name.setText(itemAdapter.getText());
        ((ViewHolder) viewHolder).mImg.setImageResource(itemAdapter.getImage());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mTv_name;
        public ImageView mImg;
        public CardView takla;

        public ViewHolder(View itemView) {

            super(itemView);
            mTv_name = (TextView) itemView.findViewById(R.id.firstLine);
            mImg = (ImageView) itemView.findViewById(R.id.icon);
            takla = (CardView) itemView.findViewById(R.id.takla);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getAdapterPosition() == 0){
//                        bottomSheetMikrotikCredential(v);
//                        sharedPreferences = mContext.getSharedPreferences(pref.getPrefUserCred(), Context.MODE_PRIVATE);
//                        Boolean checkMikrotikStatus = sharedPreferences.getBoolean(pref.getPrefMikrotikCheckStatus(), false);
//                        if(checkMikrotikStatus == true){
//                            navigationState.navigateToActivity(mContext, AgentEntry.class);
//                        }else {
//                            bottomSheetMikrotikCredential(v);
//                        }
//                        Toast.makeText(mContext, "Will be fixed as soon possible, stay with us!", Toast.LENGTH_SHORT).show();
                        navigationState.navigateToActivity(mContext, AgentEntry.class);
                    }else if(getAdapterPosition() == 1){
                        navigationState.navigateToActivity(mContext, Complain.class);
                    }else if(getAdapterPosition() == 2){
                        navigationState.navigateToActivity(mContext, BillPay.class);
                    }else if(getAdapterPosition() == 3){
                        navigationState.navigateToActivity(mContext, AgentDisplay.class);
                    }else if(getAdapterPosition() == 4){
                        navigationState.navigateToActivity(mContext, Expense.class);
                    }else if(getAdapterPosition() == 5){
//                        Intent accHead = new Intent(mContext, AccountHead.class);
//                        mContext.startActivity(accHead);
//                        showListDialog();
                        navigationState.navigateToActivity(mContext, AccountHead.class);
                    } else if (getAdapterPosition() == 6) {
                        navigationState.navigateToActivity(mContext, AccountStatement.class);
                    }

                }
            });
        }
    }

    private void showListDialog() {
        // Define the items in the list
        List<String> options = new ArrayList<>();
        options.add("Account Head");
        options.add("Account Sub-head");

        // Inflate the custom dialog layout
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View dialogView = inflater.inflate(R.layout.dialog_account_head_selector, null);

        // Find the ListView in the dialog
        ListView listView = dialogView.findViewById(R.id.listView);
        AccountHeadSelectorAdapter adapter = new AccountHeadSelectorAdapter(mContext, options);
        listView.setAdapter(adapter);

        // Set the item click listener
        listView.setOnItemClickListener((parent, view, position, id) -> {
            switch (position) {
                case 0: // Account Head
                    mContext.startActivity(new Intent(mContext, AccountHead.class));
                    break;
                case 1: // Account Sub Head
                    mContext.startActivity(new Intent(mContext, AccountSubHead.class));
                    break;
                case 3:
                    mContext.startActivity(new Intent(mContext, AgentDisplay.class));
                case 4:
                    mContext.startActivity(new Intent(mContext, Expense.class));
            }
        });

        // Build and show the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(dialogView);
        builder.show();
    }

    void bottomSheetMikrotikCredential(View bootmSheetView){
        LayoutInflater inflaterSetting = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        bootmSheetView = inflaterSetting.inflate(R.layout.dialog_mikrotik_connection_check, null);

        diag_mik_ip = (TextView) bootmSheetView.findViewById(R.id.diag_mik_ip);
        diag_exp_insert = (Button) bootmSheetView.findViewById(R.id.diag_exp_insert_btn);
        diag_mik_id = (TextView) bootmSheetView.findViewById(R.id.diag_mik_id);

        diag_mik_ip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchMikrotikList();
            }
        });

        diag_exp_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkMikrotikConnection(diag_mik_ip.getText().toString().trim());
//                Toast.makeText(mContext, "Hoise", Toast.LENGTH_LONG).show();
            }
        });

        final Dialog mBottomSheetDialogSetting = new Dialog(mContext, R.style.BottomSheetTheme);
        mBottomSheetDialogSetting.setContentView(bootmSheetView);
        mBottomSheetDialogSetting.setCancelable(true);
        mBottomSheetDialogSetting.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialogSetting.getWindow().setGravity(Gravity.BOTTOM);
        mBottomSheetDialogSetting.show();
    }

    void checkMikrotikConnection(String mikIP){
        String request_url = urlStorage.getHttpStd()
                + urlStorage.getBaseUrl()
                + urlStorage.getMikrottikConnectionCheck();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, request_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.toString().equals("0")){
                            Toast.makeText(mContext, "Not connected!", Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(mContext, "Successfully connected", Toast.LENGTH_LONG).show();
                            Log.d("Response", response.toString());
                            sharedPreferences = mContext.getSharedPreferences(pref.getPrefUserCred(), Context.MODE_PRIVATE);
                            editor = sharedPreferences.edit();
                            editor.putString(pref.getPrefMikrotikID(), diag_mik_id.getText().toString().trim());
                            editor.putString(pref.getPrefMikrotikIP(), diag_mik_ip.getText().toString().trim());
                            editor.putBoolean(pref.getPrefMikrotikCheckStatus(), true);
                            editor.commit();
                            navigationState.navigateToActivity(mContext, AgentEntry.class);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(Expense.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_MIK_IP, mikIP);
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

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }

    private void fetchMikrotikList() {
        String url = urlStorage.getHttpStd()
                +urlStorage.getBaseUrl()
                +urlStorage.getMikrotikList();

        RequestQueue queue = Volley.newRequestQueue(mContext);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        mikrotikList.clear();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = response.getJSONObject(i);

                                String id = obj.getString("id");
                                String mikIp = obj.getString("mik_ip");

                                mikrotikList.add(new Mikrotik(id, mikIp));
                            }
                            showDialogWithRecyclerView();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        queue.add(jsonArrayRequest);
    }

    private void showDialogWithRecyclerView() {
        Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_mikrotik_list);

        RecyclerView recyclerView = dialog.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        adapter = new MikrotikAdapter(mikrotikList, mikrotik -> {
//            selectedIpTextView.setText("IP: " + mikrotik.getMikIp());
//            selectedIdTextView.setText("ID: " + mikrotik.getId());
            diag_mik_id.setText(mikrotik.getId());
            diag_mik_ip.setText(mikrotik.getMikIp());
            dialog.dismiss();
        });

        recyclerView.setAdapter(adapter);
        dialog.show();
    }

}