package com.armavi_bsd.robotispreconstructed_mega.dialogs;



import android.app.Dialog;
import android.content.Context;

import com.armavi_bsd.robotispreconstructed_mega.R;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
public class WarningDialog {
    public static void warningDialog(Context context, String message) {
        // Create a dialog
        Dialog dialog = new Dialog(context);
        dialog.setCancelable(true);

        // Inflate custom layout
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_message, null);
        dialog.setContentView(dialogView);

        // Set up the TextView for the message
        TextView messageTextView = dialogView.findViewById(R.id.dialogMessage);
        messageTextView.setText(message);

        // Set up the OK button
        Button okButton = dialogView.findViewById(R.id.dialogButton);
        okButton.setOnClickListener(v -> dialog.dismiss());

        // Show the dialog
        dialog.show();
    }
}
