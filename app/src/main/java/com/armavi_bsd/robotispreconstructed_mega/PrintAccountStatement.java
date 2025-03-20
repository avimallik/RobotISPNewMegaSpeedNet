package com.armavi_bsd.robotispreconstructed_mega;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.armavi_bsd.robotispreconstructed_mega.databinding.ActivityAccountStatementBinding;
import com.armavi_bsd.robotispreconstructed_mega.databinding.ActivityPrintAccountStatementBinding;
import com.armavi_bsd.robotispreconstructed_mega.urlStorage.URLStorage;
import com.armavi_bsd.robotispreconstructed_mega.util.Pref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.UUID;

public class PrintAccountStatement extends AppCompatActivity {

    ActivityPrintAccountStatementBinding binding;

    String urlPrintAccStmt = "http://192.168.0.122/mega/rest_api_mob_dx/posPrintAccountStatement.php?userid=19";

    protected static final String TAG = "PrintTest";
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    BluetoothAdapter mBluetoothAdapter;
    private UUID applicationUUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    String PRINTER_ADDRESS;
    private ProgressDialog mBluetoothConnectProgressDialog;
    private BluetoothSocket mBluetoothSocket;
    BluetoothDevice mBluetoothDevice;

    SharedPreferences sharedPreferences;
    Pref pref = new Pref();
    String accWholeString;
    URLStorage urlStorage = new URLStorage();
    String userId;

    //String builders//
    StringBuilder customerDataString = new StringBuilder();
    StringBuilder accTitelsTExt = new StringBuilder();
    StringBuilder accTotalTitleText = new StringBuilder();

    String jsonData = "[ " +
            "{ \"SLNo\": 1, \"customerName\": \"Foyez Ahmed\", \"customerIp\": \"59.153.28.49\", \"customerid\": \"CUS1870\", \"credit\": \"1000\", \"debit\": \"0\", \"balance\": \"1000\", \"date\": \"10-03-2025\" }," +
            "{ \"SLNo\": 2, \"customerName\": \"Ismail Hussain Rubel\", \"customerIp\": \"59.153.28.97\", \"customerid\": \"CUS04222\", \"credit\": \"500\", \"debit\": \"0\", \"balance\": \"1500\", \"date\": \"10-03-2025\" } " +
            "]";  // Shortened for brevity


    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_print_account_statement);

        binding = ActivityPrintAccountStatementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        changeStatusBarColor(R.color.colorPrimary);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Bluetooth is not supported
            Toast.makeText(this, "Bluetooth not supported", Toast.LENGTH_SHORT).show();
            finish();
        }

        sharedPreferences = getSharedPreferences(pref.getPrefUserCred(), MODE_PRIVATE);
        PRINTER_ADDRESS = sharedPreferences.getString(pref.getPrefPrintingDeviceMAC(), "");
        userId = sharedPreferences.getString(pref.getPrefUserID(), "");

        urlPrintAccStmt = urlStorage.getHttpStd()
                +urlStorage.getBaseUrl()
                +urlStorage.getPosPrintAccountStatementEndpoint()
                +userId;

        binding.accTitles.setText(accTitelsTExt.append(String.format(String.format("%-8s %-8s %-8s %-8s", "CUSID", "Credit", "Debit", "Balance"))));
        binding.accTotalTitles.setText(accTotalTitleText.append(String.format("%-12s %-8s %-8s %-8s", " ", "Credit", "Debit", "Balance")));

        fetchAccountStmtData();

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            connectToPrinter();
        }

        binding.btnAccStamtPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                } else {
                    connectToPrinter();
                }
            }
        });
    }


    // Method to connect to the printer
    private void connectToPrinter() {
        if (PRINTER_ADDRESS != "") {
            mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(PRINTER_ADDRESS);
            mBluetoothConnectProgressDialog = ProgressDialog.show(this, "Connecting...", "Connecting to printer...", true, false);
            new Thread(this::connectPrinter).start();
        } else if(PRINTER_ADDRESS.trim().equals("")){
            Toast.makeText(this, "Printer address is not found, Select Printer first from Admin Panel!", Toast.LENGTH_SHORT).show();
//            navigationState.navigateToActivity(getApplicationContext(), BluetoothDeviceList.class);
        }
    }

    // Thread to connect to Bluetooth printer
    @SuppressLint("MissingPermission")
    private void connectPrinter() {
        try {
            mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(applicationUUID);
            mBluetoothAdapter.cancelDiscovery();
            mBluetoothSocket.connect();
            mHandler.sendEmptyMessage(0);
        } catch (IOException e) {
            Log.e(TAG, "Connection failed", e);
            mHandler.sendEmptyMessage(1); // Failure
        }
    }

    // Handler to update UI after connection
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mBluetoothConnectProgressDialog.dismiss();
            if (msg.what == 0) {
                binding.status.setText("Connected");
                binding.status.setTextColor(Color.rgb(97, 170, 74));
                printTest();
            } else {
                binding.status.setText("Connection Failed");
                binding.status.setTextColor(Color.RED);
            }
        }
    };

    // Method to print test data
    public void printTest() {
        if (mBluetoothSocket != null && mBluetoothSocket.isConnected()) {
            try {

                ///Image print///

                /////////////////
                OutputStream os = mBluetoothSocket.getOutputStream();
                //Headers company details
                //////////////////////////////////////////////////////////////////////

                ///For Mega Speed Net
                byte[] smallFontCommand = new byte[] { 0x1B, 0x21, 0x01 };
                String header = "          Mega Speed Net\n";
//                String header = "          Roms Network\n";
                String companyAddress = " House-32-A2,Road-12,Sector-13.Uttara-1230\n";
                String companyMobile = "     Mobile: +8801866976644/45\n";
                String companyWeb = "       Web: www.megaspeednet.com\n";
                String companyFacebook = "    facebook.com/megaspeednet\n";

                StringBuilder rawTitleAccoutList = new StringBuilder();
                rawTitleAccoutList.append(String.format("%-8s %-8s %-8s %-8s\n", "CUSID", "Credit", "Debit", "Balance"));

                StringBuilder rawTitleTotal = new StringBuilder();
                rawTitleTotal.append(String.format("%-8s %-8s %-8s %-8s\n", " ", "Credit", "Debit", "Balance"));


                String singleEnter = "\n";
                String doubleEnter = "\n\n";
                String singleLineBreak = "\n";

                /////////////////////////////////////////////////////////////////////

                String underLine = "*********************************\n";
                String underLineHifen = "--------------------------------\n";
                String vio = "================================\n";


                os.write(smallFontCommand);
                os.write(header.getBytes());
                os.write(companyAddress.getBytes());
                os.write(companyMobile.getBytes());
                os.write(companyWeb.getBytes());
                os.write(companyFacebook.getBytes());
                ///////////////////////////////////////////////////////////
                os.write(underLine.getBytes());

                // Parse the JSON data

                ///////////////////////////////////////////////////////////

                ///Printing details////////////////////////////////////////
                os.write(smallFontCommand);
                os.write(rawTitleAccoutList.toString().getBytes());
                os.write(binding.accValues.getText().toString().getBytes());
                os.write(rawTitleTotal.toString().getBytes());
                os.write(binding.accTotal.getText().toString().getBytes());
                ///////////////////////////////////////////////////////////
                os.write(underLine.getBytes());
                os.write(doubleEnter.getBytes());

                // Paper settings (height and width)
                os.write(intToByteArray(29)); // GS
                os.write(intToByteArray(150)); // Height
                os.write(intToByteArray(170)); // Height

                os.write(intToByteArray(29)); // GS
                os.write(intToByteArray(119)); // Width
                os.write(intToByteArray(2)); // Width

            } catch (IOException e) {
                Log.e(TAG, "Error during printing", e);
            }
        } else {
            Toast.makeText(this, "Printer not connected", Toast.LENGTH_SHORT).show();
        }
    }

    public static byte[] intToByteArray(int value) {
        byte[] b = ByteBuffer.allocate(4).putInt(value).array();
        return new byte[]{b[3]};
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBluetoothSocket != null) {
            try {
                mBluetoothSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Error closing socket", e);
            }
        }
    }

    void fetchAccountStmtData(){
        StringBuilder totalDataString = new StringBuilder();
        Volley.newRequestQueue(this).add(new JsonObjectRequest(Request.Method.GET, urlPrintAccStmt, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    // Parse the customer data
                                    JSONArray customerDataArray = response.getJSONArray("customerData");
                                    for (int i = 0; i < customerDataArray.length(); i++) {
                                        JSONObject customer = customerDataArray.getJSONObject(i);
                                        String customerId = customer.getString("customerid");
                                        String credit = customer.getString("credit");
                                        String debit = customer.getString("debit");
                                        String balance = customer.getString("balance");

                                        // Format customer details to display
                                        customerDataString.append(String.format("%-8s %-8s %-8s %-8s\n", customerId, credit, debit, balance));
                                    }

                                    // Set the customer data to the TextView
                                    binding.accValues.setText(customerDataString.toString());
//                                    os.write(customerDataString.toString().getBytes());

                                    // Parse the total data
                                    JSONObject totalData = response.getJSONObject("total");
                                    String totalCredit = totalData.getString("total_credit");
                                    String totalDebit = totalData.getString("total_debit");
                                    String totalBalance = totalData.getString("balance");

                                    // Display the total data in the second TextView

                                    totalDataString.append(String.format("%-8s %-8s %-8s %-8s\n","Total : ", totalCredit, totalDebit, totalBalance));
                                    binding.accTotal.setText(totalDataString.toString());

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(PrintAccountStatement.this, "Error parsing JSON", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(com.android.volley.VolleyError error) {
                                error.printStackTrace();
                                Toast.makeText(PrintAccountStatement.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                            }
                        })
        );
    }

    private void changeStatusBarColor(int colorResId) {
        // For devices running Lollipop (API 21) or above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS); // Allows background color change
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS); // Clears any transparency setting
            window.setStatusBarColor(getResources().getColor(colorResId)); // Set the color
        }
    }
}