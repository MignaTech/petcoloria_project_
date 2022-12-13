package com.android.usecases.menu.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.android.java.app.petcoloria.R;
import com.android.java.app.petcoloria.databinding.FDoctorScheduleBinding;

public class f_doctor_schedule extends Fragment {
    FDoctorScheduleBinding binding;
    public f_doctor_schedule() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FDoctorScheduleBinding.inflate(inflater,container,false);
        binding.btnRegresar.setOnClickListener(view->{
            Navigation.findNavController(view).navigate(R.id.action_f_doctor_schedule_to_f_doctor_profile);
        });
        return binding.getRoot();
    }
}