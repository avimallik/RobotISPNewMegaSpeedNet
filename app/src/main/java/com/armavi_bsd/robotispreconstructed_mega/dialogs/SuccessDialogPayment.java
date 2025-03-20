package com.armavi_bsd.robotispreconstructed_mega.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.armavi_bsd.robotispreconstructed_mega.BillPay;
import com.armavi_bsd.robotispreconstructed_mega.R;
import com.armavi_bsd.robotispreconstructed_mega.navigationEndState.NavigationWithEndState;

public class SuccessDialogPayment {

    public static void SuccessDialogPayment(Context context, String message){
        // Create a dialog
        NavigationWithEndState navigationWithEndState = new NavigationWithEndState();
        Dialog dialog = new Dialog(context);
        dialog.setCancelable(true);

        // Inflate custom layout
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_success_payment, null);
        dialog.setContentView(dialogView);

        // Set up the TextView for the message
        TextView messageTextView = dialogView.findViewById(R.id.dialogMessage);
        messageTextView.setText(message);

        // Set up the OK button
        Button okButton = dialogView.findViewById(R.id.dialogButton);
//        okButton.setOnClickListener(v ->
//                navigationWithEndState.navigateToActivity(context, BillPay.class)
//        );

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationWithEndState.navigateToActivity(context, BillPay.class);
                dialog.dismiss();
            }
        });
        // Show the dialog
        dialog.show();
    }
}
