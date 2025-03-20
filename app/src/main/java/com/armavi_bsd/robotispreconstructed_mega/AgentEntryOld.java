package com.armavi_bsd.robotispreconstructed_mega;

import static org.apache.http.impl.cookie.DateUtils.formatDate;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.armavi_bsd.robotispreconstructed_mega.adapter.BillingPersonAdapter;
import com.armavi_bsd.robotispreconstructed_mega.adapter.ListViewAdapter;
import com.armavi_bsd.robotispreconstructed_mega.adapter.PackageAdapter;
import com.armavi_bsd.robotispreconstructed_mega.adapter.ZoneAdapter;
import com.armavi_bsd.robotispreconstructed_mega.model.BillingPersonModel;
import com.armavi_bsd.robotispreconstructed_mega.model.PackageModel;
import com.armavi_bsd.robotispreconstructed_mega.model.ZoneModel;
import com.armavi_bsd.robotispreconstructed_mega.urlStorage.URLStorage;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AgentEntryOld extends AppCompatActivity {

    //Regular Expressions
    String email_regular_exp = "^[\\\\p{L}\\\\p{N}\\\\._%+-]+@[\\\\p{L}\\\\p{N}\\\\.\\\\-]+\\\\.[\\\\p{L}]{2,}$",
            ip_regular_exp = "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)",
            phone_regular_exp = "\\\\d{10}|(?:\\\\d{3}-){2}\\\\d{4}|\\\\(\\\\d{3}\\\\)\\\\d{3}-?\\\\d{4}";

    String profileToken;
    FloatingActionButton cleanFab;
    ProgressDialog progressDialog;
    ListViewAdapter listViewAdapter;
    ListView listView;
    ListView listViewProfile;
    ProgressBar progressBar;
    ImageView datePicker;
    DatePickerDialog picker;
    Spinner spinnerStatus;
//    String HttpURL = "http://ispnew.bsdbd.com/robotispzone.php";

    String spinner_status_val;

    //Variables
    final URLStorage urlStorage = new URLStorage();
    //Data Post Token
    public static final String KEY_CUSTOMER_NAME = "ag_name";
    public static final String KEY_EMAIL = "ag_email";
    public static final String KEY_MOBILE_NUMBER = "ag_mobile_no";
    public static final String KEY_BLOOD_GRUP = "blood_group";

    public static final String KEY_NATIONAL_ID = "national_id";
    public static final String KEY_OCCUPATION = "occupation";
    public static final String KEY_ADDRESS = "ag_office_address";
    public static final String KEY_ZONE = "zone";
    public static final String KEY_ZONE_ID = "zone";

    public static final String KEY_CONNECTION_DATE = "connection_date";
    public static final String KEY_IP_ADDRESS = "ip";
    public static final String KEY_SPEED = "mb";

    public static final String KEY_STATUS = "ag_status";

    public static final String KEY_NEW_ZONE = "zone_name";

    //For Zone Token
    public static final String KEY_CREATED_BY = "created_by";

    //For Entry Token
    public static final String KEY_ENTRY_BY = "entry_by";
    public static final String KEY_UPDATE_BY = "update_by";
    //    public static final String KEY_ENTRY_DATE = "entry_date";
    public static final String KEY_BILLING_PERSON_ID = "billing_person_id";

    //For Accounts Token
    public static final String KEY_RUNNING_MONTH_AMOUNT = "acc_amount_run";
    public static final String KEY_CONNECTION_CHAGER_AMOUNT = "acc_amount_charge";

    //pay Amount Token
    public static final String  KEY_PAY_AMOUNT = "taka";

    //Mac Address
    public static final String KEY_MAC_ADDRESS = "mac_address";

    // bill date
    public static final String KEY_BILL_DATE = "bill_date";
    //other phone
    public static final String KEY_OTHER_PHONE = "regular_mobile";

    //Due Token
    public static final String KEY_RUNNING_MONTH_DUE_AMOUNT = "running_month_due";
    public static final String KEY_CHARGE_DUE_AMOUNT = "connection_charge_due";

    //SMS Switch Token ;)
    public static final String KEY_SMS_SWITCH = "sms_switch";
    String sms_switch_val_temp = "0";

    //Mikrotik pass & Package
    public static final String KEY_PACKAGE = "package";
    public static final String KEY_MIKROTIK_PASS = "password";

    private List<BillingPersonModel> billingPersonList = new ArrayList<>();
    private BillingPersonAdapter adapterBillingPerson;
    String billngPersonName, billingPersonId;

    private List<ZoneModel> zoneList = new ArrayList<>();
    private ZoneAdapter adapter;
    String zoneName, zoneId;

    private PackageAdapter adapterPackage;
    private List<PackageModel> packageList = new ArrayList<>();
    String packageName, netSpeed, billAmount;
    //////////////////////////////////////////////////////////////////////////

    String running_month_due_temp;
    String charge_due_temp;

    EditText customerName, email, mobileNumber, nationalID, address,speed, ipAddress, runningMthAmt, connectChargeAmt, billAmt,dueRun,
            dueCharge, macAddress, billDate, otherPhone, mikrotikPass, selectProfile;

    TextView zone, zoneIdTxt, timeDisp, customerId, customerInfo, bilingPerson, billingPersonIdTxt ;

    Button submit, test;
    Spinner bloodSpinner;

    CheckBox checkBoxRun, checkBoxBoxCharge, smsNotification;

    String spinnerRecordBlood;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agent_entry_old);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));

            //View definition
            customerName = (EditText) findViewById(R.id.customer_name);
            email = (EditText) findViewById(R.id.email);
            mobileNumber = (EditText) findViewById(R.id.mobileNumber);
            bloodSpinner = (Spinner) findViewById(R.id.bloodSpinner);
            zone = (TextView) findViewById(R.id.zone);
            zoneIdTxt = (TextView) findViewById(R.id.zoneId);
            nationalID = (EditText) findViewById(R.id.nationalID);
            address = (EditText) findViewById(R.id.address);
            timeDisp = (TextView) findViewById(R.id.timeDisp);
            speed = (EditText) findViewById(R.id.speed);
            ipAddress = (EditText) findViewById(R.id.ipAddress);
            runningMthAmt = (EditText) findViewById(R.id.runningMthAmt);
            connectChargeAmt = (EditText) findViewById(R.id.connectChargeAmt);
            billAmt = (EditText) findViewById(R.id.billAmt);
            dueCharge = (EditText) findViewById(R.id.dueCharge);
            dueRun = (EditText) findViewById(R.id.dueRun);
            macAddress = (EditText) findViewById(R.id.mac_address);
            datePicker = (ImageView) findViewById(R.id.datePicker);
            otherPhone = (EditText) findViewById(R.id.other_phone);
            billDate = (EditText) findViewById(R.id.bill_date);
            //CheckBox
            checkBoxBoxCharge = (CheckBox) findViewById(R.id.checkBoxCharge);
            checkBoxRun = (CheckBox) findViewById(R.id.checkBoxRun);
            smsNotification = (CheckBox) findViewById(R.id.smsNotification);
            progressDialog = new ProgressDialog(AgentEntryOld.this);
            //User View
            bilingPerson = (TextView) findViewById(R.id.bilingPerson);
            billingPersonIdTxt = (TextView) findViewById(R.id.billingPersonId);
            selectProfile = (EditText) findViewById(R.id.selectProfile);
            mikrotikPass = (EditText) findViewById(R.id.mikrotikPass);
            submit = (Button) findViewById(R.id.submitBtn);
            //////////////////////////////////////////////////////////////

            checkBoxRun.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (buttonView.isChecked()) {
                        dueRun.setVisibility(View.VISIBLE);
                    }
                    else {
                        dueRun.setVisibility(View.INVISIBLE);
                        dueRun.setText("");
                    }
                }
            });

            checkBoxBoxCharge.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (buttonView.isChecked()) {
                        dueCharge.setVisibility(View.VISIBLE);
                    }
                    else {
                        dueCharge.setVisibility(View.INVISIBLE);
                        dueCharge.setText("");
                    }
                }
            });

            bilingPerson.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fetchBillingPerson();
                }
            });

            zone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fetchZone();
                }
            });

            selectProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fetchPackage();
                }
            });
            //SMS Notification Switch
            smsNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (buttonView.isChecked()) {
                        // checked
                        sms_switch_val_temp = "1";
                        Toast.makeText(getApplicationContext(),"SMS Notification On", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        // not checked
                        sms_switch_val_temp = "0";
                        Toast.makeText(getApplicationContext(),"SMS Notification Off", Toast.LENGTH_SHORT).show();
                    }
                }

            });

            //Spinner Status
            spinnerStatus = (Spinner) findViewById(R.id.spinnerStatus);
            List<String> statusType = new ArrayList<String>();
            statusType.add("Active");
            statusType.add("Inactive");

            ArrayAdapter<String> statusTypeTemp = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, statusType);
            statusTypeTemp.setDropDownViewResource(android.R.layout.simple_list_item_1);
            spinnerStatus.setAdapter(statusTypeTemp);
            spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //Spinner Position Value
                    switch (position){

                        case 0:
                            spinner_status_val = "1";
                            break;

                        case 1:
                            spinner_status_val = "0";
                            break;
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            ////////////////////////////////////////////////////////////////
            datePicker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDatePickerDialog();
                }
            });

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    submitInformation();
                }
            });
        }
    }

    ////Billing person selection/////
    private void fetchBillingPerson() {

        String urlBillingPerson = urlStorage.getHttpStd()
                + urlStorage.getBaseUrl()
                + urlStorage.getBillingPersonEndpoint();

        RequestQueue queue = Volley.newRequestQueue(AgentEntryOld.this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlBillingPerson, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        billingPersonList.clear();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = response.getJSONObject(i);
                                billngPersonName = obj.getString("FullName");
                                billingPersonId = obj.getString("UserId");
                                billingPersonList.add(new BillingPersonModel(billingPersonId, billngPersonName));
                            }
                            showDialogBillingPerson();
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
    private void showDialogBillingPerson() {
        Dialog dialogBillingPerson = new Dialog(AgentEntryOld.this);
        dialogBillingPerson.setContentView(R.layout.dialog_billing_person);

        RecyclerView recyclerViewBillingPerson = dialogBillingPerson.findViewById(R.id.recycler_billing_person);
        recyclerViewBillingPerson.setLayoutManager(new LinearLayoutManager(AgentEntryOld.this));

        adapterBillingPerson = new BillingPersonAdapter(billingPersonList, billingPersonModel -> {
            bilingPerson.setText(billingPersonModel.getFullName());
            billingPersonIdTxt.setText(billingPersonModel.getUserId());
            dialogBillingPerson.dismiss();
        });

        recyclerViewBillingPerson.setAdapter(adapterBillingPerson);
        adapterBillingPerson.notifyDataSetChanged();
//        subZoneList.clear();
        dialogBillingPerson.show();
    }
    //////////////////////////////////

    ////////////Zone Selection//////////
    private void fetchZone() {
//        String url = urlStorage.getHttpStd()
//                +urlStorage.getBaseUrl()
//                +urlStorage.getZoneSeletionWithChildCount();
        String url = "http://192.168.0.114/mega/rest_api_mob_dx/robotispzone.php";
        RequestQueue queue = Volley.newRequestQueue(AgentEntryOld.this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        zoneList.clear();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = response.getJSONObject(i);
                                zoneId = obj.getString("zone_id");
                                zoneName = obj.getString("zone_name");
                                zoneList.add(new ZoneModel(zoneId, zoneName));
                            }
                            showDialogZone();
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
    private void showDialogZone() {
        Dialog dialog = new Dialog(AgentEntryOld.this);
        dialog.setContentView(R.layout.dialog_zone_selection);

        RecyclerView recyclerViewZone = dialog.findViewById(R.id.recycler_view_zone);
        recyclerViewZone.setLayoutManager(new LinearLayoutManager(AgentEntryOld.this));

        adapter = new ZoneAdapter(zoneList, zoneModel -> {

            zoneIdTxt.setText(zoneModel.getZoneId());
            zone.setText(zoneModel.getZoneName());

            dialog.dismiss();
        });

        recyclerViewZone.setAdapter(adapter);
        dialog.show();
    }
    ///////////////////////////////////////////

    ////////Package selection////
    private void fetchPackage() {
        String urlPackage = "http://192.168.0.114/mega/rest_api_mob_dx/robotispPackage.php";
//        String urlPackage = urlStorage.getHttpStd()
//                + urlStorage.getBaseUrl()
//                + urlStorage.getPackageEndpoint();

        RequestQueue queue = Volley.newRequestQueue(AgentEntryOld.this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlPackage, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        packageList.clear();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = response.getJSONObject(i);
                                packageName = obj.getString("package_name");
                                netSpeed = obj.getString("net_speed");
                                billAmount = obj.getString("bill_amount");
                                packageList.add(new PackageModel(packageName, netSpeed, billAmount));
                            }
                            showDialogPackage();
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
    private void showDialogPackage() {
        Dialog dialogPackage = new Dialog(AgentEntryOld.this);
        dialogPackage.setContentView(R.layout.dialog_package);

        RecyclerView recyclerViewPackage = dialogPackage.findViewById(R.id.recycler_package);
        recyclerViewPackage.setLayoutManager(new LinearLayoutManager(AgentEntryOld.this));

        adapterPackage = new PackageAdapter(packageList, packageModel -> {
//            binding.areaIDText.setText(pack.getZoneId());
//            binding.areaSelect.setText(areaModel.getZoneName());
            selectProfile.setText(packageModel.getNetSpeed());
            billAmt.setText(packageModel.getBillAmount().toString().trim());
            speed.setText(packageModel.getNetSpeed().toString().trim());
            dialogPackage.dismiss();
        });

        recyclerViewPackage.setAdapter(adapterPackage);
        adapterPackage.notifyDataSetChanged();
//        subZoneList.clear();
        dialogPackage.show();
    }
    /////////////////////////////

    //Date Picker////////////////////
    // Method to show the DatePicker Dialog
    private void showDatePickerDialog() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog and set a listener for when the date is picked
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                AgentEntryOld.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                        // Adjust the month since the DatePicker returns 0-based months (0 = January)
                        selectedMonth = selectedMonth + 1;
                        // Format the date as a String
                        String formattedDate = formatDate(selectedYear, selectedMonth, selectedDay);
                        // Set the formatted date to the TextView
                        timeDisp.setText(formattedDate);
                    }
                },
                year, month, day
        );
        // Show the DatePickerDialog
        datePickerDialog.show();
    }

    // Helper method to format the selected date
    private String formatDate(int year, int month, int day) {
        // Format the date to "dd-MM-yyyy"
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day); // Month is 0-based in Calendar
        return sdf.format(calendar.getTime());
    }
    //////////////////////////////////////////

    private void submitInformation(){
        //Agent Entry URL Shared Pref
//        final SharedPreferences sharedPreferences=getSharedPreferences("USER_CREDENTIALS",MODE_PRIVATE);
//        final String baseURL = sharedPreferences.getString("URL_BASE","DEFAULT_NAME");
//        final String user_id = sharedPreferences.getString("USER_ID","DEFAULT_NAME");

        final String user_id = "1";
        final String customer_name_temp = customerName.getText().toString().trim();
        final String email_temp;
        final String mobile_temp = mobileNumber.getText().toString().trim();

        final String national_id_temp;
        final String address_temp;
        final String zone_id_temp;

        final String conection_date_temp = timeDisp.getText().toString().trim();
        final String speed_temp;
        final String ipaddress_temp;
        final String bill_amount_temp = billAmt.getText().toString();

        final String statusVal = spinner_status_val;
//        final String entry_date_temp = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        final String mac_address;
        final String other_phone;
        final String bill_date;
        final String billing_person_id;
        //For Acc
        final String running_month_amt_temp = runningMthAmt.getText().toString().trim();
        final String charge_amt_temp;

        //Mikrotik pass & Package
        final String mikrotik_pass_temp = mikrotikPass.getText().toString().trim();
        final String package_temp = selectProfile.getText().toString().trim();

        //SMS Switch
//        if(smsNotification.isChecked()){
//            sms_switch_val_temp = "1";
//            Toast.makeText(getApplicationContext(), sms_switch_val_temp, Toast.LENGTH_SHORT).show();
//        }

        ////////////////////////////////////////////////////////////////////
        //Email
        if(email.getText().toString().isEmpty()){
            email_temp = "";
        }else{
            email_temp = email.getText().toString().trim();
        }

        //other phone
        if(otherPhone.getText().toString().isEmpty()){
            other_phone = "";
        }else {
            other_phone = otherPhone.getText().toString().trim();
        }

        //NID
        if(nationalID.getText().toString().isEmpty()){
            national_id_temp = "";
        }else {
            national_id_temp = nationalID.getText().toString().trim();
        }

        //billing person
        if(billingPersonIdTxt.getText().toString().isEmpty()){
            billing_person_id = "0";
        }else{
            billing_person_id = billingPersonIdTxt.getText().toString().trim();
        }

        //mac add
        if(macAddress.getText().toString().isEmpty()){
            mac_address = "";
        }else {
            mac_address = macAddress.getText().toString().trim();
        }

        //speed
        if(speed.getText().toString().isEmpty()){
            speed_temp = "0";
        }else {
            speed_temp = speed.getText().toString();
        }

        //ip address
        if(ipAddress.getText().toString().isEmpty()){
            ipaddress_temp = "";
        }else {
            ipaddress_temp = ipAddress.getText().toString().trim();
        }

        //Zone
        if(zoneIdTxt.getText().toString().isEmpty()){
            zone_id_temp = "0";
        }else {
            zone_id_temp = zoneIdTxt.getText().toString().trim();
        }

        //address
        if(address.getText().toString().isEmpty()){
            address_temp = "";
        }else {
            address_temp = address.getText().toString().trim();
        }

        //Bill date
        if(billDate.getText().toString().isEmpty()){
            bill_date = "0";
        }else {
            bill_date = billDate.getText().toString().trim();
        }
        ///////////////////////////////////////////////////////////////////

        if(connectChargeAmt.getText().toString().isEmpty()){
            charge_amt_temp = "0";
        }else{
            charge_amt_temp = connectChargeAmt.getText().toString().trim();
        }

        // For Due


        running_month_due_temp = dueRun.getText().toString().trim();
        charge_due_temp = dueCharge.getText().toString().trim();

        if(running_month_due_temp.isEmpty()){
            running_month_due_temp = "0";
        }else if(charge_due_temp.isEmpty()){
            charge_due_temp = "0";
        }

//        String url_concat = urlStorage.getHttpStd()+baseURL+urlStorage.getUserEntryIntersect();
        String url_concat = "https://meganew.robotispsoft.net/rest_api_mob_dx/robotispagententry.php";
        final StringRequest stringRequest = new StringRequest(Request.Method.POST,url_concat,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volley inspection", response.toString());
//                        if(response.trim().contains("1")){
////                            decoratedDialog("Congrats , You successfully registered to RobotISP System, Thank You for joining RobotISP System");
//                            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
//                        }
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        decoratedDialog(error.toString());
//                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("Volley inspection", error.toString());
                        progressDialog.dismiss();
                    }

                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();

                params.put(KEY_CUSTOMER_NAME,customer_name_temp);
                params.put(KEY_EMAIL,email_temp);
                params.put(KEY_MOBILE_NUMBER,mobile_temp);
//                params.put(KEY_BLOOD_GRUP, spinnerRecordBlood);

                params.put(KEY_NATIONAL_ID, national_id_temp);
//                params.put(KEY_OCCUPATION, occupation_temp);
                params.put(KEY_ADDRESS, address_temp);
                params.put(KEY_ZONE_ID, zone_id_temp);

                params.put(KEY_CONNECTION_DATE, conection_date_temp);
                params.put(KEY_SPEED, speed_temp);
                params.put(KEY_IP_ADDRESS, ipaddress_temp);
                params.put(KEY_STATUS, statusVal);
                params.put(KEY_ENTRY_BY, user_id);
//                params.put(KEY_ENTRY_DATE, entry_date_temp);
                params.put(KEY_UPDATE_BY, user_id);
                params.put(KEY_PAY_AMOUNT, bill_amount_temp);

                params.put(KEY_CONNECTION_CHAGER_AMOUNT, charge_amt_temp);
                params.put(KEY_RUNNING_MONTH_AMOUNT, running_month_amt_temp);

                params.put(KEY_RUNNING_MONTH_DUE_AMOUNT, running_month_due_temp);
                params.put(KEY_CHARGE_DUE_AMOUNT, charge_due_temp);
                params.put(KEY_MAC_ADDRESS , mac_address);
                params.put(KEY_OTHER_PHONE, other_phone);
                params.put(KEY_BILL_DATE, bill_date);
                params.put(KEY_BILLING_PERSON_ID, billing_person_id);
                params.put(KEY_SMS_SWITCH, sms_switch_val_temp);

                params.put(KEY_MIKROTIK_PASS, mikrotik_pass_temp);
                params.put(KEY_PACKAGE, package_temp);

                //Search Key is Here

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}