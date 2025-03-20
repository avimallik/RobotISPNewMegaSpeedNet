package com.armavi_bsd.robotispreconstructed_mega.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.armavi_bsd.robotispreconstructed_mega.R;

import java.util.List;

public class AccountHeadSelectorAdapter  extends ArrayAdapter<String> {

    private final Context context;
    private final List<String> items;

    public AccountHeadSelectorAdapter(Context context, List<String> items) {
        super(context, 0, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate the custom layout
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_account_head_selector, parent, false);
        }

        // Set the text for the list item
        TextView textView = convertView.findViewById(R.id.list_item_text);
        textView.setText(items.get(position));

        return convertView;
    }
}
