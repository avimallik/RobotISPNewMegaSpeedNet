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
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.armavi_bsd.robotispreconstructed_mega.util.Pref;


import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class XprinterTest extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    Pref pref = new Pref();
    String PRINTER_MAC_ADDRESS; // Replace with actual MAC address
    private static final UUID PRINTER_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // Standard Bluetooth UUID
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 102;
    private static final int BLUETOOTH_PERMISSION_REQUEST_CODE = 103;
    private static final int REQUEST_BLUETOOTH_PERMISSIONS = 102;

    private BluetoothSocket bluetoothSocket;
    private OutputStream outputStream;
    private BluetoothDevice printerDevice;
    private BluetoothAdapter bluetoothAdapter;

    Button printbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_xprinter_test);

        printbtn = (Button) findViewById(R.id.printX);

        sharedPreferences = getSharedPreferences(pref.getPrefUserCred(), MODE_PRIVATE);
        PRINTER_MAC_ADDRESS = sharedPreferences.getString(pref.getPrefPrintingDeviceMAC(), "");

        printbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                checkPermissionsAndPrint();
//                printText();
//                Toast.makeText(getApplicationContext(), PRINTER_MAC_ADDRESS, Toast.LENGTH_SHORT).show();
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
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { // Android 10 & 11
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, BLUETOOTH_PERMISSION_REQUEST_CODE);
                return;
            }
        }
        new Thread(this::printReceipt).start(); // Run in a background thread to prevent ANR
    }

    private boolean connectToPrinter() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            runOnUiThread(() -> Toast.makeText(this, "Enable Bluetooth first", Toast.LENGTH_SHORT).show());
            return false;
        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.BLUETOOTH, android.Manifest.permission.BLUETOOTH_ADMIN, android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            return false;
        }
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device : pairedDevices) {
            if (device.getAddress().equals(PRINTER_MAC_ADDRESS)) {
                try {
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED ||
                            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED ||
                            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.BLUETOOTH, android.Manifest.permission.BLUETOOTH_ADMIN, android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
                        return false;
                    }
                    bluetoothSocket = device.createRfcommSocketToServiceRecord(PRINTER_UUID);
                    bluetoothSocket.connect();
                    outputStream = bluetoothSocket.getOutputStream();
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(this, "Failed to connect. Unpair & Re-pair your printer.", Toast.LENGTH_LONG).show());
                    return false;
                }
            }
        }
        runOnUiThread(() -> Toast.makeText(this, "Printer not found! Pair it first in Bluetooth settings.", Toast.LENGTH_LONG).show());
        return false;
    }

    private void printReceipt() {
        if (!connectToPrinter()) return;

        try {
            // Reset printer (fixes duplicate/garbage printing issues)
            outputStream.write(new byte[]{0x1B, 0x40}); // ESC @ - Reset

            // Properly formatted receipt
//            String receiptData = "** Mega Speed Net **\n" +
//                    "Print Date: 12.03.2025\n" +
//                    "Name: John Doe\n" +
//                    "ID: CUS112\n" +
//                    "Phone: 01956273133\n" +
//                    "Address: Bal sal address\n" +
//                    "Package: 10MB\n" +
//                    "Paid Bill: 500\n" +
//                    "Due Bill: 500\n";

            byte[] Packet1={
                    (byte)0X8A,(byte)0XC6,(byte)0X94,(byte)0XF4,(byte)0X0B,(byte)0X5E,(byte)0X30,(byte)0X25,(byte)
                    0X01,(byte)0X5E,(byte)0X04,(byte)0X24,(byte)0X01,(byte)0X0C,(byte)0X5E,(byte)0X03,(byte)0X24,(byte)0X01,(byte)0X08,(byte)0X5E,(byte)0X27,(byte)0X25,(byte)
                    0X01,(byte)0X5E,(byte)0X04,(byte)0X24,(byte)0X05,(byte)0X0C,(byte)0X00,(byte)0X60,(byte)0X00,(byte)0X18,(byte)0X5E,(byte)0X27,(byte)0X25,(byte)
                    0X01,(byte)0X5E,(byte)0X03,(byte)0X24,(byte)0X06,(byte)0X30,(byte)0X1E,(byte)0X10,(byte)0X60,(byte)0X00,(byte)0X18,(byte)0X5E,(byte)0X27,(byte)0X25,(byte)
                    0X01,(byte)0X5E,(byte)0X03,(byte)0X24,(byte)0X06,(byte)0X70,(byte)0X3F,(byte)0X18,(byte)0XF0,(byte)0X00,(byte)0X3E,(byte)0X5E,(byte)0X27,(byte)0X25,(byte)
                    0X01,(byte)0X5E,(byte)0X03,(byte)0X24,(byte)0X06,(byte)0X70,(byte)0X3C,(byte)0X39,(byte)0XF1,(byte)0X80,(byte)0X3E,(byte)0X5E,(byte)0X27,(byte)0X25,(byte)
                    0X01,(byte)0X5E,(byte)0X03,(byte)0X24,(byte)0X06,(byte)0XF8,(byte)0X7C,(byte)0X9F,(byte)0XF1,(byte)0X80,(byte)0X7F,(byte)0X5E,(byte)0X27,(byte)0X25,(byte)
                    0X01,(byte)0X5E,(byte)0X03,(byte)0X24,(byte)0X06,(byte)0XF9,(byte)0X9E,(byte)0X1C,(byte)0XFF,(byte)0XC2,(byte)0X7E,(byte)0X5E,(byte)0X27,(byte)0X25,(byte)
                    0X01,(byte)0X5E,(byte)0X03,(byte)0X24,(byte)0X06,(byte)0XF9,(byte)0X9E,(byte)0X1C,(byte)0XE7,(byte)0XE2,(byte)0X7E,(byte)0X5E,(byte)0X27,(byte)0X25,(byte)
                    0X01,(byte)0X5E,(byte)0X03,(byte)0X24,(byte)0X06,(byte)0XFB,(byte)0X1E,(byte)0X1C,(byte)0XFF,(byte)0XE7,(byte)0XBE,(byte)0X5E,(byte)0X27,(byte)0X25,(byte)
                    0X01,(byte)0X5E,(byte)0X03,(byte)0X24,(byte)0X06,(byte)0X7B,(byte)0X16,(byte)0X1C,(byte)0XFF,(byte)0XDF,(byte)0X3E,(byte)0X5E,(byte)0X27,(byte)0X25,(byte)
                    0X01,(byte)0X5E,(byte)0X03,(byte)0X24,(byte)0X06,(byte)0X71,(byte)0X12,(byte)0X1C,(byte)0XE7,(byte)0XF7,(byte)0X34,(byte)0X5E,(byte)0X27,(byte)0X25,(byte)
                    0X01,(byte)0X5E,(byte)0X03,(byte)0X24,(byte)0X06,(byte)0X51,(byte)0X12,(byte)0X1C,(byte)0XF7,(byte)0XF7,(byte)0X24,(byte)0X5E,(byte)0X27,(byte)0X25,(byte)
                    0X01,(byte)0X5E,(byte)0X03,(byte)0X24,(byte)0X06,(byte)0X49,(byte)0X12,(byte)0X1C,(byte)0XFF,(byte)0XF3,(byte)0X24,(byte)0X5E,(byte)0X27,(byte)0X25,(byte)
                    0X01,(byte)0X5E,(byte)0X03,(byte)0X24,(byte)0X06,(byte)0X49,(byte)0X12,(byte)0X3F,(byte)0XFD,(byte)0XF3,(byte)0X24,(byte)0X5E,(byte)0X27,(byte)0X25,(byte)
                    0X01,(byte)0X5E,(byte)0X03,(byte)0X24,(byte)0X06,(byte)0X49,(byte)0X96,(byte)0X3F,(byte)0XFC,(byte)0XF3,(byte)0X24,(byte)0X5E,(byte)0X27,(byte)0X25,(byte)
                    0X01,(byte)0X5E,(byte)0X03,(byte)0X24,(byte)0X05,(byte)0X49,(byte)0X80,(byte)0X00,(byte)0X08,(byte)0X10,(byte)0X5E,(byte)0X28,(byte)0X25,(byte)
                    0X01,(byte)0X5E,(byte)0X30,(byte)0X25,(byte)
                    0X01,(byte)0X5E,(byte)0X03,(byte)0X24,(byte)0X06,(byte)0XE0,(byte)0X74,(byte)0XA9,(byte)0X33,(byte)0X23,(byte)0X26,(byte)0X5E,(byte)0X27,(byte)0X25,(byte)0X04
            };

            outputStream.write(Packet1);

            // Add Space (Prevents incomplete prints)
            outputStream.write(new byte[]{0x1B, 0x64, 0x03}); // ESC d 3 (3 blank lines)

            // Cut the paper
            outputStream.write(new byte[]{0x1D, 0x56, 0x41, 0x10});

            outputStream.flush();
            runOnUiThread(() -> Toast.makeText(this, "Print Successful", Toast.LENGTH_SHORT).show());
            closePrinterConnection();
        } catch (Exception e) {
            e.printStackTrace();
            runOnUiThread(() -> Toast.makeText(this, "Print Failed", Toast.LENGTH_SHORT).show());
        }
    }

    private void closePrinterConnection() {
        try {
            if (outputStream != null) {
                outputStream.flush();
                outputStream.close();
            }
            if (bluetoothSocket != null) {
                bluetoothSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == BLUETOOTH_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new Thread(this::printReceipt).start();
            } else {
                Toast.makeText(this, "Bluetooth permissions denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}