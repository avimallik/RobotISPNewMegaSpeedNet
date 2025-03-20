package com.armavi_bsd.robotispreconstructed_mega;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.armavi_bsd.robotispreconstructed_mega.urlStorage.URLStorage;
import com.armavi_bsd.robotispreconstructed_mega.util.Intentkey;
import com.armavi_bsd.robotispreconstructed_mega.util.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    Pref pref = new Pref();
    Intentkey intentkey = new Intentkey();

    Intent next_activity;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static final String KEY_USER_NAME= "userName";
    public static final String KEY_PASSWORD = "password";

    EditText urlEdi, usernameEdi, passwordEdi;
    Button loginBtn;
    ProgressDialog progressDialog;

    final URLStorage urlStorage = new URLStorage();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo wifi = cm
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        android.net.NetworkInfo datac = cm
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        next_activity = new Intent(this, AdminPanel.class);
        progressDialog = new ProgressDialog(Login.this);

        final SharedPreferences sharedPreferences = getSharedPreferences(pref.getPrefUserCred(),MODE_PRIVATE);
        final Boolean isloggedin = sharedPreferences.getBoolean(pref.getPrefIsloggedIn(),false);
        if(isloggedin) {
            Intent main = new Intent(Login.this, AdminPanel.class);
            startActivity(main);
        }

        //NukeSSL
        NukeSSLCerts.nuke();

        usernameEdi = (EditText) findViewById(R.id.usernameEdi);
        passwordEdi = (EditText) findViewById(R.id.passwordEdi);
        loginBtn = (Button) findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ((wifi != null & datac != null)
                        && (wifi.isConnected() | datac.isConnected())) {
                    if(usernameEdi.getText().toString().isEmpty()){
                        decoratedDialog("Please enter right User Name !");
                    }else if(passwordEdi.getText().toString().isEmpty()){
                        decoratedDialog("Please Enter right Password !");
                    }else{
                        progressDialog.setMessage("Logging into RobotISP System !");
                        progressDialog.show();
                        loginUser();
                    }

                }else{
                    Context context = getApplicationContext();
                    AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                    builder.setTitle("Confirmation");
                    builder.setMessage("Please Activate Data Connection !");

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing but close the dialog
                            final Intent intent = new Intent(Intent.ACTION_MAIN, null);
                            intent.addCategory(Intent.CATEGORY_LAUNCHER);
                            final ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.wifi.WifiSettings");
                            intent.setComponent(cn);
                            intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });

    }

    private void loginUser(){

        //user system and database ip
        String userNameTemp = usernameEdi.getText().toString();
        String passwordTemp = passwordEdi.getText().toString();

        String server_uri_user_insert = urlStorage.getHttpStd()
                +urlStorage.getBaseUrl()
                +urlStorage.getLoginIntercect();

        final StringRequest stringRequestSendAlert = new StringRequest(Request.Method.POST,server_uri_user_insert,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("text", response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i=0; i<jsonArray.length(); i++){

                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String id = jsonObject.getString("UserId");
                                String name_temp = jsonObject.getString("FullName");
                                String userName_temp = jsonObject.getString("UserName");
                                String email_temp = jsonObject.getString("Email");
                                String address_temp = jsonObject.getString("Address");
                                String image_url_temp = jsonObject.getString("PhotoPath");
                                String phone_temp = jsonObject.getString("MobileNo");
                                String admin_Type_temp = jsonObject.getString("UserType");

                                //Store data in sharedpref
                                sharedPreferences = getSharedPreferences(pref.getPrefUserCred(),MODE_PRIVATE);
                                editor = sharedPreferences.edit();

                                editor.putString(pref.getPrefUserName(),userName_temp);
                                editor.putString(pref.getPreAdminPasswordID(), passwordTemp);
                                editor.putString(pref.getPrefUserID(),id);
                                editor.putString(pref.getPrefFullNameID(),name_temp);
                                editor.putString(pref.getPrefEmailID(),email_temp);
                                editor.putString(pref.getPrefAddressID(),address_temp);
                                editor.putString(pref.getPrefImagePathID(),image_url_temp);
                                editor.putString(pref.getPrefPhoneID(),phone_temp);
                                editor.putString(pref.getPrefAdminTypeID(),admin_Type_temp);

                                editor.putBoolean(pref.getPrefIsloggedIn(),true);
                                editor.commit();
                                ////////////////////////////

                                startActivity(next_activity);
                                Toast.makeText(getApplicationContext(), userNameTemp+" "+email_temp, Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(),Toast.LENGTH_SHORT).show();
                        Log.d("volley_error", error.toString());
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_USER_NAME, userNameTemp);
                params.put(KEY_PASSWORD, passwordTemp);
                return params;
            }
        };

        stringRequestSendAlert.setRetryPolicy(new RetryPolicy() {
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
        RequestQueue requestQueueSendAlert = Volley.newRequestQueue(this);
        requestQueueSendAlert.add(stringRequestSendAlert);
    }

    public void decoratedDialog(String dialogMsg){
        LayoutInflater layoutInflater = LayoutInflater.from(Login.this);
        View promptView = layoutInflater.inflate(R.layout.custom_dialog_alart, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Login.this);
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