package com.armavi_bsd.robotispreconstructed_mega.adapter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.armavi_bsd.robotispreconstructed_mega.Expense;
import com.armavi_bsd.robotispreconstructed_mega.R;
import com.armavi_bsd.robotispreconstructed_mega.urlStorage.URLStorage;
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

/**
 * Created by Arm Avi on 12/14/2020.
 */

public class ExpenseRVAdapter extends RecyclerView.Adapter<ExpenseRVAdapter.ViewHolder>{

    public static final String ACC_ID = "acc_id";
    public static final String ACC_HEAD_ID = "acc_head_id";
    public static final String ACC_SUB_HEAD_ID = "acc_sub_head";
    public static final String ACC_AMOUNT = "acc_amount";
    public static final String ACC_DESCRIPTION = "acc_description";
    Pref pref = new Pref();
    private Context context;
    private List<ExpenseModel> personUtils;
    int maxLength = 12;
    //    private JAFFSplitedUrl jaffSplitedUrl =  new JAFFSplitedUrl();
    private URLStorage urlStorage;

    //////////////Acchead UI////
    ProgressBar progressBarZone;
    ListView listView;
    ArrayList<Subjects> SubjectList = new ArrayList<Subjects>();
    ArrayList<UserView> UserList = new ArrayList<UserView>();
    ArrayList<ProfileView> profileList = new ArrayList<ProfileView>();
    ListViewAdapter listViewAdapter;
    String recentTime;

    ////Acc head prompt///
    TextView accHeadIDUi, diag_exp_name, diag_exp_date, diag_exp_head, diag_exp_amount, accSubHeadIdU, diag_exp_sub_head;
    EditText diag_exp_des;
    DatePickerDialog picker;

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public ExpenseRVAdapter(Context context, List personUtils) {
        this.context = context;
        this.personUtils = personUtils;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_expense_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(personUtils.get(position));

        ExpenseModel pu = personUtils.get(position);
        urlStorage = new URLStorage();

        holder.expense_head.setText(pu.getAcc_name());
        holder.expense_acc_amount.setText(pu.getAcc_expense_amount());
        holder.expense_entry_date.setText(pu.getEntry_date());
    }

    @Override
    public int getItemCount() {
        return personUtils.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView expense_head;
        public TextView expense_acc_amount;
        public TextView expense_entry_date;

        public ViewHolder(View itemView) {
            super(itemView);

            expense_head = (TextView) itemView.findViewById(R.id.expense_acc_head);
            expense_acc_amount = (TextView) itemView.findViewById(R.id.expense_acc_amount);
            expense_entry_date = (TextView) itemView.findViewById(R.id.expense_entry_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
//                    CategoryModel categoryModel = (CategoryModel) view.getTag();
//                    Intent categoryIntent = new Intent(context, Category.class);
//                    categoryIntent.putExtra("cat_id_hsbd", categoryModel.getId());
//                    categoryIntent.putExtra("cat_name_hsbd", categoryModel.getName());
//                    categoryIntent.putExtra("cat_image_hsbd", categoryModel.getImage());
//                    context.startActivity(categoryIntent);

                    final ExpenseModel expenseModel = (ExpenseModel) view.getTag();

                    LayoutInflater layoutInflater = LayoutInflater.from(context);
                    final View promptView = layoutInflater.inflate(R.layout.dialog_expense, null);
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setView(promptView);

//                    final TextView bookIdDiag = (TextView) promptView.findViewById(R.id.bookIdDiag);
                    diag_exp_head = (TextView) promptView.findViewById(R.id.diag_exp_head);
                    diag_exp_head.setText(expenseModel.getAcc_name());

                    diag_exp_des = (EditText) promptView.findViewById(R.id.diag_exp_des);
                    diag_exp_des.setText(expenseModel.getAcc_description());

                    diag_exp_sub_head = (TextView) promptView.findViewById(R.id.diag_exp_sub_head);
                    diag_exp_sub_head.setText(expenseModel.getAcc_sub_head_name());

                    diag_exp_amount = (TextView) promptView.findViewById(R.id.diag_exp_amount);
                    diag_exp_amount.setText(expenseModel.getAcc_expense_amount());

                    accHeadIDUi = (TextView) promptView.findViewById(R.id.acc_head_id);
                    accHeadIDUi.setText(expenseModel.getAcc_head());

                    accSubHeadIdU = (TextView) promptView.findViewById(R.id.acc_sub_head_id);
                    accSubHeadIdU.setText(expenseModel.getAcc_sub_head());

                    final Button diag_exp_edit_btn = (Button) promptView.findViewById(R.id.diag_exp_edit_btn);
                    final Button diag_exp_delete_btn = (Button) promptView.findViewById(R.id.diag_exp_delete_btn);

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

                    diag_exp_delete_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

//                            String request_url = urlStorage.getHttpStd()
//                                    +urlStorage.getBaseUrl()
//                                    +urlStorage.getExpenseDeleteIntersect();
//                            String request_url = "http://192.168.0.132/mega/rest_api_mob_dx/robotispexpensedelete.php";
                            String request_url = urlStorage.getHttpStd()
                                    +urlStorage.getBaseUrl()
                                    +urlStorage.getExpenseDeleteIntersect();
                            final StringRequest stringRequest = new StringRequest(Request.Method.POST,request_url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
//                                            decoratedDialog(response.toString());
                                            if(response.trim().equals("1")){

                                                Toast.makeText(context, "Expense deleted successfully!", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(context, Expense.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                                ((Activity)context).finish();
                                                context.startActivity(intent);

                                            } else if (response.trim().equals("0")) {
                                                Toast.makeText(context, "Error occurred!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                                        }

                                    }){
                                @Override
                                protected Map<String,String> getParams(){
                                    Map<String,String> params = new HashMap<String, String>();
                                    params.put(ACC_ID, expenseModel.getAcc_id());
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

                            RequestQueue requestQueue = Volley.newRequestQueue(context);
                            requestQueue.add(stringRequest);
                        }
                    });

//                    diag_exp_edit_btn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Toast.makeText(context, expenseModel.getAcc_id(), Toast.LENGTH_SHORT).show();
//                            String request_url = urlStorage.getHttpStd()
//                                    +urlStorage.getBaseUrl()
//                                    +urlStorage.getExpenseEditIntersect();
//                            final StringRequest stringRequest = new StringRequest(Request.Method.POST,request_url,
//                                    new Response.Listener<String>() {
//                                        @Override
//                                        public void onResponse(String response) {
////                                            decoratedDialog(response.toString());
//                                            Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show();
//                                            Intent intent = new Intent(context, Expense.class);
//                                            intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//                                            ((Activity)context).finish();
//                                            context.startActivity(intent);
//                                        }
//                                    },
//                                    new Response.ErrorListener() {
//                                        @Override
//                                        public void onErrorResponse(VolleyError error) {
//                                            Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
//                                        }
//
//                                    }){
//                                @Override
//                                protected Map<String,String> getParams(){
//                                    Map<String,String> params = new HashMap<String, String>();
//                                    params.put(ACC_ID, expenseModel.getAcc_id());
//                                    params.put(ACC_HEAD_ID, accHeadIDUi.getText().toString().trim());
////                                    params.put(ACC_SUB_HEAD_ID, accSubHeadIdU.getText().toString().trim());
//                                    params.put(ACC_AMOUNT, diag_exp_amount.getText().toString().trim());
//                                    params.put(ACC_DESCRIPTION, diag_exp_des.getText().toString().trim());
//                                    return params;
//                                }
//
//                            };
//
//                            stringRequest.setRetryPolicy(new RetryPolicy() {
//                                @Override
//                                public int getCurrentTimeout() {
//                                    return 10000;
//                                }
//
//                                @Override
//                                public int getCurrentRetryCount() {
//                                    return 10000;
//                                }
//
//                                @Override
//                                public void retry(VolleyError error) throws VolleyError {
//
//                                }
//                            });
//
//                            RequestQueue requestQueue = Volley.newRequestQueue(context);
//                            requestQueue.add(stringRequest);
//                        }
//                    });

                    AlertDialog alert = alertDialogBuilder.create();
                    alert.show();
                }
            });
        }
    }

    /////////////////////Account Head////////////////////////////////

    public void showDialog(){

        final Dialog dialog = new Dialog(context);
        // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_list);

        ImageView zoneAdd;
        EditText searchBar;
        ArrayList<Subjects> SubjectList = new ArrayList<Subjects>();
        Button dialogDone;
//        String HttpURL = "http://ispnew.bsdbd.com/robotispzone.php";
//        final ListViewAdapter listViewAdapter;
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
        new ParseJSonDataClass(context).execute();
        dialog.show();
    }

    private class ParseJSonDataClass extends AsyncTask<Void, Void, Void> {
        public Context context;
        String FinalJSonResult;

        public ParseJSonDataClass(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            final String zone_details_url = urlStorage.getHttpStd()
                    +urlStorage.getBaseUrl()
                    +urlStorage.getAccountHeadListIntersect();
            HttpParseClass httpParseClass = new HttpParseClass(zone_details_url);
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

    ////Account Sub Head list//////
    public void showSubHeadDialog(){

        final Dialog dialog = new Dialog(context);
        // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_list);

        ImageView zoneAdd;
        EditText searchBar;
        ArrayList<Subjects> SubjectList = new ArrayList<Subjects>();
        Button dialogDone;
//        String HttpURL = "http://ispnew.bsdbd.com/robotispzone.php";
//        final ListViewAdapter listViewAdapter;
        progressBarZone = (ProgressBar)dialog.findViewById(R.id.progressbar);
        zoneAdd = (ImageView) dialog.findViewById(R.id.addZone);

        searchBar = (EditText) dialog.findViewById(R.id.searchBar);
        dialogDone = (Button) dialog.findViewById(R.id.dialogDone);
        listView = (ListView) dialog.findViewById(R.id.listView1);
        listView.setTextFilterEnabled(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Subjects ListViewClickData = (Subjects) parent.getItemAtPosition(position);
                accSubHeadIdU.setText(ListViewClickData.getSubName());
                diag_exp_sub_head.setText(ListViewClickData.getSubFullForm());
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
        new ParseJSonDataClassAccSubHead(context).execute();
        dialog.show();
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

    void timePicker(){
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        picker = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                                timeDisp.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
//                        timeDisp.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        diag_exp_date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                }, year, month, day);
        picker.show();

    }

    public void decoratedDialog(String dialogMsg){
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.custom_dialog_alart, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
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
