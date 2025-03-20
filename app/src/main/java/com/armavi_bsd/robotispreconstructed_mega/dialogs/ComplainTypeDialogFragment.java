package com.armavi_bsd.robotispreconstructed_mega.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.armavi_bsd.robotispreconstructed_mega.adapter.ComplainTypeAdapter;
import com.armavi_bsd.robotispreconstructed_mega.model.ComplainTypeModel;
import com.armavi_bsd.robotispreconstructed_mega.urlStorage.URLStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ComplainTypeDialogFragment extends DialogFragment {

    URLStorage urlStorage = new URLStorage();
    private RecyclerView recyclerView;
    private ComplainTypeAdapter adapter;
    private List<ComplainTypeModel> complainTypeModelList = new ArrayList<>();
    private ComplainTypeDialogFragment.OnCustomerSelectedListener listener;

    public interface OnCustomerSelectedListener {
        void onCustomerSelected(String idTxt, String template);
    }

    public void setOnCustomerSelectedListener(ComplainTypeDialogFragment.OnCustomerSelectedListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_complain_type, container, false);

        recyclerView = view.findViewById(R.id.recyclerComplainType);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ComplainTypeAdapter(complainTypeModelList, complainTypeModel -> {
            if (listener != null) {
                listener.onCustomerSelected(complainTypeModel.getId(), complainTypeModel.getTemplate());
                dismiss();
            }
        });
        recyclerView.setAdapter(adapter);

        fetchData();

        return view;
    }

    private void fetchData() {
//        String url = "http://192.168.0.126/newisp/rest_api_mob_dx/complainType.php";
        String url = urlStorage.getHttpStd()
                + urlStorage.getBaseUrl()
                + urlStorage.getComplainTypeEndpoint();
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    complainTypeModelList.clear();
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            String idTxt = obj.getString("id");
                            String template = obj.getString("template");
                            complainTypeModelList.add(new ComplainTypeModel(idTxt, template));
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

}
