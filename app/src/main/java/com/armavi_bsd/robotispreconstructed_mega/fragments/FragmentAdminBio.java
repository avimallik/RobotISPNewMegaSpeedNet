package com.armavi_bsd.robotispreconstructed_mega.fragments;


import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;

import com.armavi_bsd.robotispreconstructed_mega.R;
import com.armavi_bsd.robotispreconstructed_mega.urlStorage.URLStorage;
import com.armavi_bsd.robotispreconstructed_mega.databinding.FragmentAdminBioBinding;
import com.armavi_bsd.robotispreconstructed_mega.util.Pref;
import com.squareup.picasso.Picasso;

public class FragmentAdminBio extends Fragment {

    String adminType;
    private FragmentAdminBioBinding binding;
    SharedPreferences sharedPreferences;
    Pref pref = new Pref();
    URLStorage urlStorage = new URLStorage();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAdminBioBinding.inflate(inflater, container, false);

        sharedPreferences = getActivity().getSharedPreferences(pref.getPrefUserCred(),MODE_PRIVATE);
        final String extraName_temp = sharedPreferences.getString(pref.getPrefFullNameID(),"");
        final String email_temp = sharedPreferences.getString(pref.getPrefEmailID(),"");
        final String admin_type_temp = sharedPreferences.getString(pref.getPrefAdminTypeID(),"");
        final String image_path_temp = sharedPreferences.getString(pref.getPrefImagePathID(),"");
        final String phone_temp = sharedPreferences.getString(pref.getPrefPhoneID(),"");

        //image_link_generator
        String image_link_gen = urlStorage.getHttpStd()
                +urlStorage.getBaseUrl()
                +urlStorage.getSlash()
                +image_path_temp;

        binding.uiAdminName.setText(extraName_temp);

        if(admin_type_temp.contains("SA")){
            adminType = "Super Admin";
        }else if(admin_type_temp.contains("EO")){
            adminType = "Entry Operator";
        }else{
            adminType = admin_type_temp;
        }

        binding.uiAdminType.setText(adminType);

        Picasso.get().load(image_link_gen)
                .placeholder(R.drawable.face_ico)
                .error(R.drawable.error_ico)
                .into(binding.uiProfileImage);

        return binding.getRoot();
    }
}
