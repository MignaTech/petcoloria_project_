package com.android.usecases.menu.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.android.GlobalVar;
import com.android.java.app.petcoloria.R;
import com.android.java.app.petcoloria.databinding.FDoctorMenuBinding;
import com.android.usecases.home.login.auth.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class f_doctor_menu extends Fragment {
    private FirebaseAuth authProfile;

    public f_doctor_menu() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FDoctorMenuBinding binding = FDoctorMenuBinding.inflate(inflater,container,false);
        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        if (firebaseUser == null){
            authProfile.signOut();
            GlobalVar.currentUser = null;
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        } else {
            binding.nameuser.setText(GlobalVar.currentUser.getName());
            binding.typeuser.setText(GlobalVar.currentUser.getTypeUser());
        }
        binding.logout.setOnClickListener(view -> {
            // Cerramos Sesion
            authProfile.signOut();
            GlobalVar.currentUser = null;
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        });
        binding.solicitud.setOnClickListener(view->{
            Navigation.findNavController(view).navigate(R.id.action_f_doctor_menu_to_f_doctor_notifications);
        });
        binding.perfil.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.action_f_doctor_menu_to_f_doctor_profile);
        });
        binding.citas.setOnClickListener(view ->{
            Navigation.findNavController(view).navigate(R.id.action_f_doctor_menu_to_f_doctor_date);
        });
        binding.guia.setOnClickListener(view ->{
            Navigation.findNavController(view).navigate(R.id.action_f_doctor_menu_to_f_doctor_guide);
        });
        return binding.getRoot();
    }
}