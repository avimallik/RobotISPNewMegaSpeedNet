package com.armavi_bsd.robotispreconstructed_mega;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.armavi_bsd.robotispreconstructed_mega.util.Intentkey;
import com.armavi_bsd.robotispreconstructed_mega.util.Pref;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class PrintServiceTest extends AppCompatActivity {

    String printAgentName, printAgentMobileNo, printCustomerID,
            printPackage, printPackageBill, printTotalDue,
            printAmount, printDate, printEntryUserName, printtAgentAddress;


    SharedPreferences sharedPreferences;
    Pref pref = new Pref();
    Intentkey intentkey = new Intentkey();

    String PRINTER_MAC_ADDRESS; // Replace with actual MAC address
    private static final UUID PRINTER_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // Standard Bluetooth UUID
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 102;
    private static final int BLUETOOTH_PERMISSION_REQUEST_CODE = 103;

    private BluetoothSocket bluetoothSocket;
    private OutputStream outputStream;
    private BluetoothDevice printerDevice;
    private BluetoothAdapter bluetoothAdapter;

    Button printBtn;

    TextView posInfoDate,
            posInfoAgentName,
            posInfoCustomerID,
            posInfoPhone,
            posAgentAddress,
            posInfoPackage,
            posInfoPackageBill,
            posInfoPaidBill,
            posInfoDueBill,
            posInfoCDate,
            posInfoReceivedBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_print_service_test);
        printBtn = (Button) findViewById(R.id.printBtn);

        sharedPreferences = getSharedPreferences(pref.getPrefUserCred(), MODE_PRIVATE);
        PRINTER_MAC_ADDRESS = sharedPreferences.getString(pref.getPrefPrintingDeviceMAC(), "");

        posInfoDate = (TextView) findViewById(R.id.posInfoDate) ;
        posInfoAgentName = (TextView) findViewById(R.id.posInfoAgentName) ;
        posInfoCustomerID = (TextView) findViewById(R.id.posInfoCustomerID) ;
        posInfoPhone = (TextView) findViewById(R.id.posInfoPhone) ;
        posAgentAddress = (TextView) findViewById(R.id.posAgentAddress) ;
        posInfoPackage = (TextView) findViewById(R.id.posInfoPackage) ;
        posInfoPackageBill = (TextView) findViewById(R.id.posInfoPackageBill) ;
        posInfoPaidBill = (TextView) findViewById(R.id.posInfoPaidBill) ;
        posInfoDueBill = (TextView) findViewById(R.id.posInfoDueBill) ;
        posInfoCDate = (TextView) findViewById(R.id.posInfoCDate) ;
        posInfoReceivedBy = (TextView) findViewById(R.id.posInfoReceivedBy);

        printAgentName = getIntent().getExtras().getString(intentkey.getPosAgentNameIntentKey());
        printDate = getIntent().getExtras().getString(intentkey.getPosDateIntentKey());
        printCustomerID = getIntent().getExtras().getString(intentkey.getPosCusIDIntentKey());
        printAgentMobileNo = getIntent().getExtras().getString(intentkey.getPosAgentMobileNoIntentKey());
        printPackage = getIntent().getExtras().getString(intentkey.getPosMBIntentKey());
        printPackageBill = getIntent().getExtras().getString(intentkey.getPosPackageBill());
        printTotalDue = getIntent().getExtras().getString(intentkey.getPosAgentTotalDueIntentKey());
        printEntryUserName = getIntent().getExtras().getString(intentkey.getPosEntryUserNameIntentKey());
        printtAgentAddress = getIntent().getExtras().getString(intentkey.getPosAgentAddressIntentKey());
        printAmount = getIntent().getExtras().getString(intentkey.getPosAmountIntentKey());

        if(PRINTER_MAC_ADDRESS.equals("")){
            Toast.makeText(getApplicationContext(), "Please select Printer info", Toast.LENGTH_SHORT).show();
        } else {
            checkPermissionsAndPrint();
        }

        printBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                checkPermissionsAndPrint();
//                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        checkPermissionsAndPrint(); // Call your function here
//                    }
//                }, 6000);
                checkPermissionsAndPrint();
            }
        });
    }

    private void checkPermissionsAndPrint() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // Android 12+
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.BLUETOOTH_CONNECT}, BLUETOOTH_PERMISSION_REQUEST_CODE);
                return;
            }
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) { // Android 11 and below
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
                return;
            }
        }
        printText();
    }

    private boolean connectToPrinter() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth not supported", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check permission before accessing Bluetooth device
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.BLUETOOTH, android.Manifest.permission.BLUETOOTH_ADMIN, android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            return false;
        }

        try {
            printerDevice = bluetoothAdapter.getRemoteDevice(PRINTER_MAC_ADDRESS);
            bluetoothSocket = printerDevice.createRfcommSocketToServiceRecord(PRINTER_UUID);
            bluetoothSocket.connect();
            outputStream = bluetoothSocket.getOutputStream();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to connect to printer", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void printText() {
        if (!connectToPrinter()) {
            return;
        }

        try {
            // 1. Reset Printer to Clear Any Previous Data
            byte[] resetPrinter = {0x1B, 0x40}; // ESC @ (Initialize Printer)
            outputStream.write(resetPrinter);

            // 2. Print Simple Receipt with New Lines
            String printData =  "+++Receipt+++\n" +
                    "Print Date : " + posInfoDate.getText().toString().trim() + "\n" +
                    "Agent Name : " + posInfoAgentName.getText().toString().trim() + "\n" +
                    "Agent Mobile: " + posInfoPhone.getText().toString().trim() + "\n" +
                    "Agent Address: " + posAgentAddress.getText().toString().trim() + "\n" +
                    "------------------------------------\n" +
                    "Customer ID : " + posInfoCustomerID.getText().toString().trim() + "\n" +
                    "Package     : " + posInfoPackage.getText().toString().trim() + "\n" +
                    "Package Bill: " + posInfoPackageBill.getText().toString().trim() + "\n" +
                    "Paid Amount : " + posInfoPaidBill.getText().toString().trim() + "\n" +
                    "Total Due   : " + posInfoDueBill.getText().toString().trim() + "\n" +
                    "------------------------------------\n" +
                    "Entry By    : " + printEntryUserName + "\n" +
                    "************************************\n\n\n";

            // Send the entire text block in **ONE** write operation
            outputStream.write(printData.getBytes("UTF-8"));

            // 3. Add Line Feed to Ensure Proper Printing
            outputStream.write(new byte[]{0x1B, 0x64, 0x03}); // ESC d 3 (3 extra blank lines)

            // 4. Send Paper Cut Command (Ensures Printer Stops After Printing)
            outputStream.write(new byte[]{0x1D, 0x56, 0x41, 0x10}); // ESC i (Full Cut)

            // 5. Final Flush and Close (Prevents Resending Data)
            outputStream.flush();
            outputStream.close();
//            outputStream.write(resetPrinter);
            bluetoothSocket.close();

            Toast.makeText(this, "Print Successful", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to print", Toast.LENGTH_SHORT).show();
        } finally {
            closeConnection();
        }
    }

    private void closeConnection() {
        try {
            if (outputStream != null) outputStream.close();
            if (bluetoothSocket != null) bluetoothSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Handle permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE || requestCode == BLUETOOTH_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                printText();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


}



