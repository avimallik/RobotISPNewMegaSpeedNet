package com.armavi_bsd.robotispreconstructed_mega.adapter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.armavi_bsd.robotispreconstructed_mega.AccountSubHead;
import com.armavi_bsd.robotispreconstructed_mega.R;
import com.armavi_bsd.robotispreconstructed_mega.urlStorage.URLStorage;
import com.armavi_bsd.robotispreconstructed_mega.model.AccountSubHeadModel;
import com.armavi_bsd.robotispreconstructed_mega.model.ProfileView;
import com.armavi_bsd.robotispreconstructed_mega.model.Subjects;
import com.armavi_bsd.robotispreconstructed_mega.model.UserView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountSubHeadAdapter extends RecyclerView.Adapter<AccountSubHeadAdapter.ViewHolder> {

    //Deserialize val obj
    AccountSubHeadModel pu;

    public static final String ACC_ID = "acc_id";
    public static final String ACC_HEAD_ID = "acc_head_id";
    public static final String ACC_DESCRIPTION = "acc_description";

    private Context context;
    private List<AccountSubHeadModel> personUtils;
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

    AlertDialog.Builder builder;

    ////Acc head prompt///
    TextView accHeadIDUi, diag_exp_name, diag_exp_date, diag_exp_head;
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


    public AccountSubHeadAdapter(Context context, List personUtils) {
        this.context = context;
        this.personUtils = personUtils;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_account_sub_head, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.itemView.setTag(personUtils.get(position));

        pu = personUtils.get(position);
        urlStorage = new URLStorage();

        holder.acc_head.setText(pu.getAcc_name());
        holder.accAccountSubHeadDesc.setText(pu.getAcc_desc());

    }

    @Override
    public int getItemCount() {
        return personUtils.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView acc_head, accAccountSubHeadDesc;
        public ImageView acc_head_del_btn;
        int position;

        public ViewHolder(View itemView) {
            super(itemView);

            acc_head = (TextView) itemView.findViewById(R.id.expense_acc_head);
            accAccountSubHeadDesc = (TextView) itemView.findViewById(R.id.expense_acc_sub_head_desc);
            acc_head_del_btn = (ImageView) itemView.findViewById(R.id.acc_head_del_btn);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    pu = (AccountSubHeadModel) view.getTag();
                    new AlertDialog.Builder(context)
                            .setTitle("Delete entry")
                            .setMessage("Are you sure you want to delete this entry?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    String request_url = urlStorage.getHttpStd()
                                            + urlStorage.getBaseUrl()
                                            + urlStorage.getAccountheadDeleteIntersect();

                                    final StringRequest stringRequest = new StringRequest(Request.Method.POST, request_url,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
//                                            decoratedDialog(response.toString());
                                                    Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent(context, AccountSubHead.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                                    ((Activity) context).finish();
                                                    context.startActivity(intent);
                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                                                }

                                            }) {
                                        @Override
                                        protected Map<String, String> getParams() {
                                            Map<String, String> params = new HashMap<String, String>();
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
}
