package com.armavi_bsd.robotispreconstructed_mega;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import com.armavi_bsd.robotispreconstructed_mega.adapter.ExpenseRVAdapter;
import com.armavi_bsd.robotispreconstructed_mega.adapter.ListViewAdapter;
import com.armavi_bsd.robotispreconstructed_mega.urlStorage.URLStorage;
import com.armavi_bsd.robotispreconstructed_mega.databinding.ActivityExpenseBinding;
import com.armavi_bsd.robotispreconstructed_mega.httpParser.HttpParseClass;
import com.armavi_bsd.robotispreconstructed_mega.model.ExpenseModel;
import com.armavi_bsd.robotispreconstructed_mega.model.ProfileView;
import com.armavi_bsd.robotispreconstructed_mega.model.Subjects;
import com.armavi_bsd.robotispreconstructed_mega.model.UserView;
import com.armavi_bsd.robotispreconstructed_mega.util.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Expense extends AppCompatActivity {

    RecyclerView recyclerViewExpense;
    ProgressBar progressBarExpense;
    TextView totalExpense;

    RecyclerView.LayoutManager layoutManagerExpense;
    List<ExpenseModel> personUtilsListExpense;
    RequestQueue rqGroundExpense;
    RecyclerView.Adapter mAdapterExpense;
    URLStorage urlStorage;
    SharedPreferences sharedPreferences;
    Pref pref = new Pref();
    int maxSum;
    int tempSumStore = 0 ;

    //////////////Acchead UI////
    ProgressBar progressBarZone;
    ListView listView;
    ArrayList<Subjects> SubjectList = new ArrayList<Subjects>();
    ArrayList<UserView> UserList = new ArrayList<UserView>();
    ArrayList<ProfileView> profileList = new ArrayList<ProfileView>();
    ListViewAdapter listViewAdapter;
    String recentTime;
    Button diagInsertBtn;

    ////Acc head prompt///
    TextView accHeadIDUi, diag_exp_name, diag_exp_date, diag_exp_head, diag_exp_sub_head, accSubHeadIdUI;
    EditText diag_exp_des, diag_exp_amount;
    DatePickerDialog picker;

    ////Insert Token///
    String ACC_HEAD_ID = "acc_head",
            ACC_AMOUNT = "acc_amount",
            ACC_DESCRIPTION = "acc_description",
            ENTRY_BY = "entry_by",
            ENTRY_DATE = "entry_date",
            ACC_SUB_HEAD = "acc_sub_head";

    ActivityExpenseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExpenseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences(pref.getPrefUserCred(), MODE_PRIVATE);
        urlStorage = new URLStorage();

        expenseRVRecycler();

        binding.addAccountHeadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View bootmSheetView) {
                LayoutInflater inflaterSetting = (LayoutInflater) Expense.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                bootmSheetView = inflaterSetting.inflate(R.layout.dialog_expense_insert, null);

                diag_exp_head = (TextView) bootmSheetView.findViewById(R.id.diag_exp_head);
                diag_exp_des = (EditText) bootmSheetView.findViewById(R.id.diag_exp_des);
//                diag_exp_date = (TextView) bootmSheetView.findViewById(R.id.diag_exp_date);
//                diag_exp_name = (TextView) bootmSheetView.findViewById(R.id.diag_exp_id);
                diag_exp_sub_head = (TextView) bootmSheetView.findViewById(R.id.diag_exp_sub_head);

                accHeadIDUi = (TextView) bootmSheetView.findViewById(R.id.acc_head_id);
                accSubHeadIdUI = (TextView) bootmSheetView.findViewById(R.id.acc_sub_head_id);

                diag_exp_amount = (EditText) bootmSheetView.findViewById(R.id.diag_exp_amount);
                diagInsertBtn = (Button) bootmSheetView.findViewById(R.id.diag_exp_insert);

                diag_exp_head.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialog();
                    }
                });

                diag_exp_sub_head.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showSubHeadDialog();
                    }
                });

                diagInsertBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (diag_exp_head.getText().toString().isEmpty()) {
                            decoratedDialog("Some Field is empty !");
                        }else if(diag_exp_amount.getText().toString().toString().isEmpty()){
                            decoratedDialog("Some Field is empty !");
                        }else{
                            final String user_id = sharedPreferences.getString(pref.getPrefUserID(), "");
                            String request_url = urlStorage.getHttpStd()
                                    + urlStorage.getBaseUrl()
                                    + urlStorage.getExpenseInsertIntersect();
                            final StringRequest stringRequest = new StringRequest(Request.Method.POST, request_url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            if(response.trim().equals("1")){
                                                Intent intent = new Intent(Expense.this, Expense.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                                finish();
                                                startActivity(intent);
                                                Toast.makeText(Expense.this, "Expense added successfully!", Toast.LENGTH_LONG).show();
                                            } else if (response.trim().equals("0")) {
                                                Toast.makeText(Expense.this, "Error occurred!", Toast.LENGTH_LONG).show();
                                            }

                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(Expense.this, error.toString(), Toast.LENGTH_LONG).show();
                                        }
                                    }) {
                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put(ACC_HEAD_ID, accHeadIDUi.getText().toString().trim());
                                    params.put(ACC_AMOUNT, diag_exp_amount.getText().toString());
                                    params.put(ENTRY_BY, user_id);
                                    params.put(ACC_DESCRIPTION, diag_exp_des.getText().toString().trim());
                                    params.put(ACC_SUB_HEAD, accSubHeadIdUI.getText().toString().trim());
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

                            RequestQueue requestQueue = Volley.newRequestQueue(Expense.this);
                            requestQueue.add(stringRequest);
                        }
                    }
                });

                final Dialog mBottomSheetDialogSetting = new Dialog(Expense.this, R.style.BottomSheetTheme);
                mBottomSheetDialogSetting.setContentView(bootmSheetView);
                mBottomSheetDialogSetting.setCancelable(true);
                mBottomSheetDialogSetting.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                mBottomSheetDialogSetting.getWindow().setGravity(Gravity.BOTTOM);
                mBottomSheetDialogSetting.show();
            }
        });
    }

    public void sendRequestExpese(){

//        String userId = sharedPreferences.getString(pref.getPrefUserID(), "");
//        String request_url = urlStorage.getHttpStd()
//                +urlStorage.getBaseUrl()
//                +urlStorage.getExpenseIntersect()
//                +"?"+"entry_by="+userId;
//        String request_url = "http://192.168.0.132/mega/rest_api_mob_dx/testExpenseDisplay.php";

        String request_url = urlStorage.getHttpStd()
                +urlStorage.getBaseUrl()
                +urlStorage.getExpenseIntersect();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, request_url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for(int i = 0; i < response.length(); i++){
                    ExpenseModel personUtils = new ExpenseModel();
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        personUtils.setAcc_name(jsonObject.getString("acc_name"));
                        personUtils.setAcc_id(jsonObject.getString("acc_id"));
                        personUtils.setAcc_expense_amount(jsonObject.getString("acc_amount"));
                        personUtils.setAcc_description(jsonObject.getString("acc_description"));
                        maxSum = Integer.parseInt(personUtils.getAcc_expense_amount());
                        tempSumStore+=maxSum;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    binding.totalExpense.setText(String.valueOf(tempSumStore)+"Tk");
//                    Toast.makeText(getApplicationContext(), String.valueOf(tempSumStore), Toast.LENGTH_SHORT).show();
                    personUtilsListExpense.add(personUtils);
                }
                mAdapterExpense = new ExpenseRVAdapter(Expense.this, personUtilsListExpense);
                binding.recyclerViewExpense.setAdapter(mAdapterExpense);
                binding.progressBarExpense.setVisibility(View.INVISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Volley Error: ", error.toString());
            }
        });
        rqGroundExpense.add(jsonArrayRequest);
    }

    void timePicker(){
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        picker = new DatePickerDialog(Expense.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        diag_exp_date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                }, year, month, day);
        picker.show();

    }

    void expenseRVRecycler(){
        rqGroundExpense = Volley.newRequestQueue(Expense.this);
        binding.recyclerViewExpense.setHasFixedSize(true);
        layoutManagerExpense = new LinearLayoutManager(Expense.this, LinearLayoutManager.VERTICAL, false);
        binding.recyclerViewExpense.setVerticalScrollbarPosition(View.SCROLLBAR_POSITION_LEFT);
        binding.recyclerViewExpense.setLayoutManager(layoutManagerExpense);
        personUtilsListExpense = new ArrayList<>();
        sendRequestExpese();
    }

    //Account Head list///
    public void showDialog(){

        final Dialog dialog = new Dialog(Expense.this);
        // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_list);

        ImageView zoneAdd;
        EditText searchBar;
        ArrayList<Subjects> SubjectList = new ArrayList<Subjects>();
        Button dialogDone;
        progressBarZone = (ProgressBar)dialog.findViewById(R.id.progressbar);
        zoneAdd = (ImageView) dialog.findViewById(R.id.addZone);

        searchBar = (EditText) dialog.findViewById(R.id.searchBar);
        dialogDone = (Button) dialog.findViewById(R.id.dialogDone);
        listView = (ListView) dialog.findViewById(R.id.listView1);
        listView.setTextFilterEnabled(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Subjects ListViewClickData = (Subjects) parent.getItemAtPosition(position);
                accHeadIDUi.setText(ListViewClickData.getSubName());
                diag_exp_head.setText(ListViewClickData.getSubFullForm());
                dialog.dismiss();
            }
        });

        searchBar.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence stringVar, int start, int before, int count) {
                listViewAdapter.getFilter().filter(stringVar.toString());
            }
        });

        dialogDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        new Expense.ParseJSonDataClassAccHead(Expense.this).execute();
        dialog.show();
    }
    private class ParseJSonDataClassAccHead extends AsyncTask<Void, Void, Void> {
        public Context context;
        String FinalJSonResult;
        public ParseJSonDataClassAccHead(Context context) {
            this.context = context;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... arg0) {
            final String accountHeadsUrl = urlStorage.getHttpStd()
                    +urlStorage.getBaseUrl()
                    +urlStorage.getAccountHeadListIntersect();
            HttpParseClass httpParseClass = new HttpParseClass(accountHeadsUrl);
            try {
                httpParseClass.ExecutePostRequest();
                if (httpParseClass.getResponseCode() == 200) {
                    FinalJSonResult = httpParseClass.getResponse();
                    if (FinalJSonResult != null) {
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(FinalJSonResult);
                            JSONObject jsonObject;
                            Subjects subjects;
                            SubjectList = new ArrayList<Subjects>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonObject = jsonArray.getJSONObject(i);
                                String tempName = jsonObject.getString("acc_id").toString();
                                String tempFullForm = jsonObject.getString("acc_name").toString();
                                subjects = new Subjects(tempName, tempFullForm);
                                SubjectList.add(subjects);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else {

                    Toast.makeText(context, httpParseClass.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            progressBarZone.setVisibility(View.INVISIBLE);
            listViewAdapter = new ListViewAdapter(context, R.layout.listview_items_layout, SubjectList);
            listView.setAdapter(listViewAdapter);
        }
    }
    /////////////////////

    ////Account Sub Head list//////
    public void showSubHeadDialog(){

        final Dialog dialogSubhead = new Dialog(Expense.this);
        // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSubhead.setCancelable(false);
        dialogSubhead.setContentView(R.layout.dialog_list);

        ImageView zoneAdd;
        EditText searchBar;
        ArrayList<Subjects> SubjectList = new ArrayList<Subjects>();
        Button dialogDone;
        progressBarZone = (ProgressBar)dialogSubhead.findViewById(R.id.progressbar);
        zoneAdd = (ImageView) dialogSubhead.findViewById(R.id.addZone);

        searchBar = (EditText) dialogSubhead.findViewById(R.id.searchBar);
        dialogDone = (Button) dialogSubhead.findViewById(R.id.dialogDone);
        listView = (ListView) dialogSubhead.findViewById(R.id.listView1);
        listView.setTextFilterEnabled(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Subjects ListViewClickData = (Subjects) parent.getItemAtPosition(position);
                accSubHeadIdUI.setText(ListViewClickData.getSubName());
                diag_exp_sub_head.setText(ListViewClickData.getSubFullForm());
                dialogSubhead.dismiss();
            }
        });

        searchBar.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence stringVar, int start, int before, int count) {
                listViewAdapter.getFilter().filter(stringVar.toString());
            }
        });

        dialogDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogSubhead.dismiss();
            }
        });
        new Expense.ParseJSonDataClassAccSubHead(Expense.this).execute();
        dialogSubhead.show();
    }
    private class ParseJSonDataClassAccSubHead extends AsyncTask<Void, Void, Void> {
        public Context context;
        String FinalJSonResult;
        public ParseJSonDataClassAccSubHead(Context context) {
            this.context = context;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... arg0) {

            final String accountHeadsUrl = urlStorage.getHttpStd()
                    +urlStorage.getBaseUrl()
                    +urlStorage.getAccountSubHeadListEndpoint();
            HttpParseClass httpParseClass = new HttpParseClass(accountHeadsUrl);
            try {
                httpParseClass.ExecutePostRequest();
                if (httpParseClass.getResponseCode() == 200) {
                    FinalJSonResult = httpParseClass.getResponse();
                    if (FinalJSonResult != null) {
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(FinalJSonResult);
                            JSONObject jsonObject;
                            Subjects subjects;
                            SubjectList = new ArrayList<Subjects>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonObject = jsonArray.getJSONObject(i);
                                String tempName = jsonObject.getString("acc_id").toString();
                                String tempFullForm = jsonObject.getString("acc_name").toString();
                                subjects = new Subjects(tempName, tempFullForm);
                                SubjectList.add(subjects);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else {

                    Toast.makeText(context, httpParseClass.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            progressBarZone.setVisibility(View.INVISIBLE);
            listViewAdapter = new ListViewAdapter(context, R.layout.listview_items_layout, SubjectList);
            listView.setAdapter(listViewAdapter);
        }
    }
    ///////////////////////////////
    public void decoratedDialog(String dialogMsg){
        LayoutInflater layoutInflater = LayoutInflater.from(Expense.this);
        View promptView = layoutInflater.inflate(R.layout.custom_dialog_alart, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Expense.this);
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