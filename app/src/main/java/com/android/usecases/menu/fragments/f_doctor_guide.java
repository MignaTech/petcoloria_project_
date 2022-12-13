package com.android.usecases.menu.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.android.java.app.petcoloria.R;
import com.android.java.app.petcoloria.databinding.FDoctorGuideBinding;

public class f_doctor_guide extends Fragment {
    public f_doctor_guide() { }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FDoctorGuideBinding binding = FDoctorGuideBinding.inflate(inflater,container,false);
        binding.pdfView.fromAsset("Guia_Doctor.pdf").load();
        binding.btnRegresar.setOnClickListener(view->{
            Navigation.findNavController(view).navigate(R.id.action_f_doctor_guide_to_f_doctor_menu);
        });
        return binding.getRoot();
    }
}
