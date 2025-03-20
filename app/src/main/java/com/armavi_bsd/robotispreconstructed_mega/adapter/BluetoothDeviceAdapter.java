package com.armavi_bsd.robotispreconstructed_mega.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.armavi_bsd.robotispreconstructed_mega.R;
import com.armavi_bsd.robotispreconstructed_mega.model.BluetoothDeviceModel;
import com.armavi_bsd.robotispreconstructed_mega.util.Pref;

import java.util.List;

public class BluetoothDeviceAdapter extends RecyclerView.Adapter<BluetoothDeviceAdapter.ViewHolder> {

    private List<BluetoothDeviceModel> deviceList;
    private Context context;
    Pref pref = new Pref();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public BluetoothDeviceAdapter(Context context, List<BluetoothDeviceModel> deviceList) {
        this.context = context;
        this.deviceList = deviceList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_buetooth_device, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        BluetoothDeviceModel device = deviceList.get(position);
        holder.deviceName.setText(device.getDeviceName());
        holder.deviceAddress.setText(device.getDeviceAddress());

        // Click Listener for RecyclerView Item
        holder.itemView.setOnClickListener(v -> {
//            String message = "Device: " + device.getDeviceName() + "\nMAC: " + device.getDeviceAddress();
//            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            sharedPreferences = context.getSharedPreferences(pref.getPrefUserCred(), Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
            editor.putString(pref.getPrefPrintingDeviceMAC(), device.getDeviceAddress());
            editor.commit();
            Toast.makeText(context,  "MAC : "
                    +sharedPreferences.getString(pref.getPrefPrintingDeviceMAC(), "")
                    +" Device Address has saved successfully!", Toast.LENGTH_SHORT).show();
            // Close the current activity
            ((Activity) context).finish();  // Casting context to Activity to call finish()
        });
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView deviceName, deviceAddress;

        public ViewHolder(View itemView) {
            super(itemView);
            deviceName = itemView.findViewById(R.id.tvDeviceName);
            deviceAddress = itemView.findViewById(R.id.tvDeviceAddress);
        }
    }
}