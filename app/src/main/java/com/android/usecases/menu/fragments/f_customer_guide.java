package com.android.usecases.menu.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.android.java.app.petcoloria.R;
import com.android.java.app.petcoloria.databinding.FCustomerGuideBinding;

public class f_customer_guide extends Fragment {

    public f_customer_guide() { }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FCustomerGuideBinding binding = FCustomerGuideBinding.inflate(inflater,container,false);
        binding.pdfGuiaC.fromAsset("Guia_Customer.pdf").load();
        binding.btnRegresar.setOnClickListener(view->{
            Navigation.findNavController(view).navigate(R.id.action_f_customer_guide_to_customer_menu);
        });
        return binding.getRoot();
    }
}
