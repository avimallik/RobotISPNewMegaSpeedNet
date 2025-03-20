package com.armavi_bsd.robotispreconstructed_mega.dialogs;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.armavi_bsd.robotispreconstructed_mega.R;
import com.armavi_bsd.robotispreconstructed_mega.adapter.AgentDialogRecyclerAdapter;
import com.armavi_bsd.robotispreconstructed_mega.model.AgentDialogModel;
import com.armavi_bsd.robotispreconstructed_mega.urlStorage.URLStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class CustomerDialogFragment extends DialogFragment {

    URLStorage urlStorage = new URLStorage();
    private RecyclerView recyclerView;
    private EditText searchBar;
    private AgentDialogRecyclerAdapter adapter;
    private List<AgentDialogModel> agentDialogList = new ArrayList<>();
    private OnCustomerSelectedListener listener;

    public interface OnCustomerSelectedListener {
        void onCustomerSelected(String agId, String agName);
    }

    public void setOnCustomerSelectedListener(OnCustomerSelectedListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_customer_selection, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewCustomer);
        searchBar = view.findViewById(R.id.searchBar);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AgentDialogRecyclerAdapter(agentDialogList, customer -> {
            if (listener != null) {
                listener.onCustomerSelected(customer.getAgId(), customer.getAgName());
                dismiss();
            }
        });
        recyclerView.setAdapter(adapter);

        fetchData();

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        return view;
    }

    private void fetchData() {
//        String url = "http://192.168.0.126/newisp/rest_api_mob_dx/testCustomerView.php";
//        String url = "http://192.168.0.132/mega/rest_api_mob_dx/robotispagentdisplay.php";
        String url = urlStorage.getHttpStd()
                +urlStorage.getBaseUrl()
                +urlStorage.getCustomerProfilerIntersect();
//        String url = urlStorage.getHttpStd()
//                + urlStorage.getBaseUrl()
//                + urlStorage.getCustomerDialogResourceEndpoint();
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
            agentDialogList.clear();
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            String agId = obj.getString("ag_id");
                            String agName = obj.getString("ag_name");
                            String agMobile = obj.getString("ag_mobile_no");
                            String agIP = obj.getString("ip");
                            agentDialogList.add(new AgentDialogModel(agId, agName, agMobile, agIP));
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error parsing data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(getContext(), "Error fetching data", Toast.LENGTH_SHORT).show());
        queue.add(request);
    }

    private void filter(String text) {
        List<AgentDialogModel> filteredList = new ArrayList<>();
        for (AgentDialogModel agentDialogModel : agentDialogList) {
            if (agentDialogModel.getAgName().toLowerCase().contains(text.toLowerCase())
                    || agentDialogModel.getAgMobileNumber().toLowerCase().contains(text.toLowerCase())
                    || agentDialogModel.getAgIp().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(agentDialogModel);
            }
        }
        adapter.updateList(filteredList);
    }
}
