package com.armavi_bsd.robotispreconstructed_mega.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.armavi_bsd.robotispreconstructed_mega.AgentEntry;
import com.armavi_bsd.robotispreconstructed_mega.R;
import com.armavi_bsd.robotispreconstructed_mega.navigationEndState.NavigationState;
import com.armavi_bsd.robotispreconstructed_mega.navigationEndState.NavigationWithEndStateAfterAgentEntry;

public class SuccessDialogNewAgentEntry {
    public static void successDialogNewAgentEntry(Context context, String message){
        // Create a dialog
        NavigationState navigationState = new NavigationState();
        Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);

        // Inflate custom layout
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_success, null);
        dialog.setContentView(dialogView);

        // Set up the TextView for the message
        TextView messageTextView = dialogView.findViewById(R.id.dialogMessage);
        messageTextView.setText(message);

        // Set up the OK button
        Button okButton = dialogView.findViewById(R.id.dialogButton);
//        okButton.setOnClickListener(v -> dialog.dismiss());
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                navigationState.navigateToActivity(context, AgentEntry.class);
            }
        });

        // Show the dialog
        dialog.show();
    }
}
