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
import com.armavi_bsd.robotispreconstructed_mega.adapter.EmployeeDialogAdapter;
import com.armavi_bsd.robotispreconstructed_mega.model.EmployeeDialogModel;
import com.armavi_bsd.robotispreconstructed_mega.urlStorage.URLStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EmployeeDialogFragment extends DialogFragment {

    URLStorage urlStorage = new URLStorage();
    private RecyclerView recyclerView;
    private EmployeeDialogAdapter adapter;
    private List<EmployeeDialogModel> employeeDialogModelList = new ArrayList<>();
    private EmployeeDialogFragment.OnCustomerSelectedListener listener;

    public interface OnCustomerSelectedListener {
        void onCustomerSelected(String idTxt, String employeeName);
    }

    public void setOnCustomerSelectedListener(EmployeeDialogFragment.OnCustomerSelectedListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_employee, container, false);

        recyclerView = view.findViewById(R.id.recyclerEmployee);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new EmployeeDialogAdapter(employeeDialogModelList, employeeDialogModel -> {
            if (listener != null) {
                listener.onCustomerSelected(employeeDialogModel.getId(), employeeDialogModel.getEmployee_name());
                dismiss();
            }
        });
        recyclerView.setAdapter(adapter);

        fetchData();

        return view;
    }

    private void fetchData() {
//        String url = "http://192.168.0.126/newisp/rest_api_mob_dx/robotispEmployeeMinimalView.php";
        String url = urlStorage.getHttpStd()
                + urlStorage.getBaseUrl()
                + urlStorage.getEmployeeMinimalViewEndpoint();
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    employeeDialogModelList.clear();
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            String idTxt = obj.getString("id");
                            String employeeName = obj.getString("employee_name");
                            employeeDialogModelList.add(new EmployeeDialogModel(idTxt, employeeName));
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

