package com.android.usecases.menu.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.android.GlobalVar;
import com.android.java.app.petcoloria.R;
import com.android.java.app.petcoloria.databinding.FCustomerMenuBinding;
import com.android.usecases.home.login.auth.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class f_customer_menu extends Fragment {
    private FirebaseAuth authProfile;
    public f_customer_menu() { }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FCustomerMenuBinding binding = FCustomerMenuBinding.inflate(inflater,container,false);
        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        if (firebaseUser == null){
            Toast.makeText(getActivity(), "Sin informacion", Toast.LENGTH_SHORT).show();
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
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        });
        binding.busqueda.setOnClickListener(view->{
            Navigation.findNavController(view).navigate(R.id.action_customer_menu_to_search_menu);
        });
        binding.btnPerfil.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.action_customer_menu_to_profile_menu_customer);
        });
        binding.citas.setOnClickListener(view ->{
            Navigation.findNavController(view).navigate(R.id.action_customer_menu_to_f_customer_date);
        });
        binding.mascota.setOnClickListener(view ->{
            Navigation.findNavController(view).navigate(R.id.action_customer_menu_to_pet_menu);
        });
        binding.guia.setOnClickListener(view ->{
            Navigation.findNavController(view).navigate(R.id.action_customer_menu_to_f_customer_guide);
        });
        return binding.getRoot();
    }


}


