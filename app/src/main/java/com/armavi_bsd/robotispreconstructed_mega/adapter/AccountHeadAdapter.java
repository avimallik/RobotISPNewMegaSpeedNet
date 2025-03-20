package com.armavi_bsd.robotispreconstructed_mega.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

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
import com.armavi_bsd.robotispreconstructed_mega.R;
import com.armavi_bsd.robotispreconstructed_mega.urlStorage.URLStorage;
import com.armavi_bsd.robotispreconstructed_mega.AccountHead;
import com.armavi_bsd.robotispreconstructed_mega.model.AccountheadModel;
import com.armavi_bsd.robotispreconstructed_mega.model.ProfileView;
import com.armavi_bsd.robotispreconstructed_mega.model.Subjects;
import com.armavi_bsd.robotispreconstructed_mega.model.UserView;
import com.armavi_bsd.robotispreconstructed_mega.util.Pref;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Arm Avi on 12/17/2020.
 */

public class AccountHeadAdapter extends RecyclerView.Adapter<AccountHeadAdapter.ViewHolder>{

    //Deserialize val obj
    AccountheadModel pu;
    public static final String ACC_ID = "acc_id";
    public static final String ACC_HEAD_ID = "acc_head_id";
    public static final String ACC_DESCRIPTION = "acc_description";

    private Context context;
    private List<AccountheadModel> personUtils;
    int maxLength = 12;
    //    private JAFFSplitedUrl jaffSplitedUrl =  new JAFFSplitedUrl();
    private URLStorage urlStorage;
    Pref pref = new Pref();
    //////////////Acchead UI////
    ProgressBar progressBarZone;
    ListView listView;
    ArrayList<Subjects> SubjectList = new ArrayList<Subjects>();
    ArrayList<UserView> UserList = new ArrayList<UserView>();
    ArrayList<ProfileView> profileList = new ArrayList<ProfileView>();
    ListViewAdapter listViewAdapter;
    String recentTime;
    AlertDialog.Builder builder;

    ////Acc head prompt///
    TextView accHeadIDUi, diag_exp_name, diag_exp_date, diag_exp_head, accHeadDetails;
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


    public AccountHeadAdapter(Context context, List personUtils) {
        this.context = context;
        this.personUtils = personUtils;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_account_head, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.itemView.setTag(personUtils.get(position));

        pu = personUtils.get(position);
        urlStorage = new URLStorage();

        holder.acc_head.setText(pu.getAcc_name());
        accHeadDetails.setText(pu.getAcc_desc());

    }

    @Override
    public int getItemCount() {
        return personUtils.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView acc_head;
        public ImageView acc_head_del_btn;
        int position;

        public ViewHolder(View itemView) {
            super(itemView);

            acc_head = (TextView) itemView.findViewById(R.id.expense_acc_head);
            accHeadDetails = (TextView) itemView.findViewById(R.id.expense_acc_head_desc);
            acc_head_del_btn = (ImageView) itemView.findViewById(R.id.acc_head_del_btn);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {

                    pu = (AccountheadModel) view.getTag();

                    new AlertDialog.Builder(context)
                            .setTitle("Delete entry")
                            .setMessage("Are you sure you want to delete this entry?")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    String request_url = urlStorage.getHttpStd()
                                            +urlStorage.getBaseUrl()
                                            +urlStorage.getAccountheadDeleteIntersect();

                                    final StringRequest stringRequest = new StringRequest(Request.Method.POST,request_url,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
//                                            decoratedDialog(response.toString());
                                                    Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent(context, AccountHead.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                                    ((Activity)context).finish();
                                                    context.startActivity(intent);
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
                                            params.put(ACC_ID, pu.getAcc_head_id().toString().trim());
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
                            })

                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
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

        zoneAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater layoutInflater = LayoutInflater.from(context);
                View promptView = layoutInflater.inflate(R.layout.phone_update, null);
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setView(promptView);

                final SharedPreferences sharedPreferences = context.getSharedPreferences("USER_CREDENTIALS",MODE_PRIVATE);
                final String baseURL = sharedPreferences.getString("URL_BASE","DEFAULT_NAME");
                final String user_id = sharedPreferences.getString("USER_ID","DEFAULT_NAME");
                final String zone_insert_url = urlStorage.getHttpStd()+baseURL+urlStorage.getZoneInsertIntersect();
                final EditText zoneCollector = (EditText) promptView.findViewById(R.id.zoneCollector);

                // create an alert dialog
                AlertDialog alert = alertDialogBuilder.create();
                alert.show();

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
//        new ParseJSonDataClass(context).execute();
        dialog.show();
    }

//    private class ParseJSonDataClass extends AsyncTask<Void, Void, Void> {
//        public Context context;
//        String FinalJSonResult;
//
//        public ParseJSonDataClass(Context context) {
//            this.context = context;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected Void doInBackground(Void... arg0) {
//
//            final SharedPreferences sharedPreferences = context.getSharedPreferences("USER_CREDENTIALS",MODE_PRIVATE);
//            final String baseURL = sharedPreferences.getString("URL_BASE","DEFAULT_NAME");
//            final String zone_details_url = urlStorage.getHttpStd()+baseURL+urlStorage.getAccountHeadListIntersect();
//            HttpParseClass httpParseClass = new HttpParseClass(zone_details_url);
//            try {
//                httpParseClass.ExecutePostRequest();
//                if (httpParseClass.getResponseCode() == 200) {
//                    FinalJSonResult = httpParseClass.getResponse();
//                    if (FinalJSonResult != null) {
//                        JSONArray jsonArray = null;
//                        try {
//                            jsonArray = new JSONArray(FinalJSonResult);
//                            JSONObject jsonObject;
//                            Subjects subjects;
//                            SubjectList = new ArrayList<Subjects>();
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                jsonObject = jsonArray.getJSONObject(i);
//                                String tempName = jsonObject.getString("acc_id").toString();
//                                String tempFullForm = jsonObject.getString("acc_name").toString();
//                                subjects = new Subjects(tempName, tempFullForm);
//                                SubjectList.add(subjects);
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                } else {
//
//                    Toast.makeText(context, httpParseClass.getErrorMessage(), Toast.LENGTH_SHORT).show();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            progressBarZone.setVisibility(View.INVISIBLE);
//            listViewAdapter = new ListViewAdapter(context, R.layout.listview_items_layout, SubjectList);
//            listView.setAdapter(listViewAdapter);
//        }
//    }

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

