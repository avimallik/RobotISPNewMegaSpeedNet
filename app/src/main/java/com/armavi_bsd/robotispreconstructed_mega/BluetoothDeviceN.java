package com.armavi_bsd.robotispreconstructed_mega;

import android.bluetooth.BluetoothAdapter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.armavi_bsd.robotispreconstructed_mega.adapter.BluetoothDeviceAdapter;
import com.armavi_bsd.robotispreconstructed_mega.databinding.ActivityBluetoothDeviceBinding;
import com.armavi_bsd.robotispreconstructed_mega.model.BluetoothDeviceModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BluetoothDeviceN extends AppCompatActivity {

    ActivityBluetoothDeviceBinding binding;

    private BluetoothAdapter bluetoothAdapter;
    private RecyclerView recyclerView;
    private BluetoothDeviceAdapter adapter;
    private List<BluetoothDeviceModel> deviceList = new ArrayList<>();
    private static final int REQUEST_BLUETOOTH_PERMISSION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bluetooth_device);
        binding = ActivityBluetoothDeviceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BluetoothDeviceAdapter(this, deviceList);
        binding.recyclerView.setAdapter(adapter);
        checkBluetoothPermissionsAndScan();
    }

    private void checkBluetoothPermissionsAndScan() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.BLUETOOTH_SCAN, android.Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_BLUETOOTH_PERMISSION);
                return;
            }
        }

        scanBluetoothDevices();
    }

    private boolean scanBluetoothDevices() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth not supported", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!bluetoothAdapter.isEnabled()) {
            Toast.makeText(this, "Enable Bluetooth first", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check permission before accessing Bluetooth device
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }

        deviceList.clear();

        Set<android.bluetooth.BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        for (android.bluetooth.BluetoothDevice device : pairedDevices) {
            deviceList.add(new BluetoothDeviceModel(device.getName(), device.getAddress()));
        }

        adapter.notifyDataSetChanged();
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_BLUETOOTH_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                scanBluetoothDevices();
            } else {
                Toast.makeText(this, "Bluetooth permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

}