package com.armavi_bsd.robotispreconstructed_mega;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.armavi_bsd.robotispreconstructed_mega.adapter.MenuRVAdapter;
import com.armavi_bsd.robotispreconstructed_mega.fragments.FragmentAdminBio;
import com.armavi_bsd.robotispreconstructed_mega.model.MenuRVModel;
import com.armavi_bsd.robotispreconstructed_mega.navigationEndState.NavigationState;
import com.armavi_bsd.robotispreconstructed_mega.navigationEndState.NavigationWithEndState;
import com.armavi_bsd.robotispreconstructed_mega.util.Intentkey;
import com.armavi_bsd.robotispreconstructed_mega.util.Pref;

import java.util.ArrayList;
import java.util.List;

public class AdminPanel extends AppCompatActivity {

    Pref pref = new Pref();
    NavigationWithEndState navigationWithEndState = new NavigationWithEndState();
    NavigationState navigationState = new NavigationState();
    Boolean intentStateCount;
    Intent next_activity;
    SharedPreferences sharedPreferences;
    Intentkey intentkey = new Intentkey();
    private MenuRVAdapter mAdapter;
    private RecyclerView mRecycleview;
    private ImageView toolbarLogoutBtn, toolbarPrinter;
    private List<MenuRVModel> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        sharedPreferences = getSharedPreferences(pref.getPrefUserCred(), MODE_PRIVATE);

        mRecycleview = (RecyclerView) findViewById(R.id.my_recycler_view);
        toolbarLogoutBtn = (ImageView) findViewById(R.id.toolbarLogoutBtn);
        toolbarPrinter = (ImageView) findViewById(R.id.toolBarPrinter);

        toolbarLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences.edit().clear().commit();
                navigationWithEndState.navigateToActivity(AdminPanel.this, Login.class);
            }
        });

        toolbarPrinter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationState.navigateToActivity(getApplicationContext(), BluetoothDeviceN.class);
            }
        });

        Fragment fragmentAdminBio = new FragmentAdminBio();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentAdminBio, fragmentAdminBio);
        fragmentTransaction.commit();

        addList();
        adapter();
    }

    private void addList(){
        MenuRVModel itemAdapter = new MenuRVModel();
        itemAdapter.setImage(R.drawable.addcustomerico_green);
        itemAdapter.setText("Add Customer");
        mList.add(itemAdapter);

        itemAdapter = new MenuRVModel();
        itemAdapter.setImage(R.drawable.greencomplain);
        itemAdapter.setText("Complain");
        mList.add(itemAdapter);

        itemAdapter = new MenuRVModel();
        itemAdapter.setImage(R.drawable.taka_green);
        itemAdapter.setText("Bill Pay");
        mList.add(itemAdapter);

        itemAdapter = new MenuRVModel();
        itemAdapter.setImage(R.drawable.customer_green);
        itemAdapter.setText("Customers");
        mList.add(itemAdapter);

        itemAdapter = new MenuRVModel();
        itemAdapter.setImage(R.drawable.expense_green);
        itemAdapter.setText("Expense");
        mList.add(itemAdapter);

        itemAdapter = new MenuRVModel();
        itemAdapter.setImage(R.drawable.acc_head_green);
        itemAdapter.setText("Account Heads");
        mList.add(itemAdapter);

        itemAdapter = new MenuRVModel();
        itemAdapter.setImage(R.drawable.icon_stmt);
        itemAdapter.setText("Account Statement");
        mList.add(itemAdapter);
    }

    private void adapter(){
        Log.d("anhtt","mlist : " +mList.size());
        mAdapter = new MenuRVAdapter(mList, this);
        mRecycleview.setAdapter(mAdapter);
        mRecycleview.setLayoutManager(new LinearLayoutManager(this));
        mRecycleview.setLayoutManager(new GridLayoutManager(this, 2));
        mAdapter.notifyDataSetChanged();
    }
}