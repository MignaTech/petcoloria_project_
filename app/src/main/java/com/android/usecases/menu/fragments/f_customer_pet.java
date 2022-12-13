package com.android.usecases.menu.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.java.app.petcoloria.R;
import com.android.java.app.petcoloria.databinding.FCustomerPetBinding;
import com.android.usecases.home.adapters.pets.pet_adapter;
import com.android.usecases.home.login.auth.LoginActivity;
import com.android.usecases.home.models.pet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class f_customer_pet extends Fragment {
    private pet_adapter mAdapter;
    private FirebaseAuth authProfile;
    private ArrayList<pet> petList = new ArrayList<>();
    DatabaseReference mDatabase;
    public f_customer_pet() {}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FCustomerPetBinding binding = FCustomerPetBinding.inflate(inflater,container,false);
        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        if (firebaseUser == null){
            Toast.makeText(getActivity(), "SESIÓN INVALIDA", Toast.LENGTH_SHORT).show();
            authProfile.signOut();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        }
        binding.petList.setLayoutManager(new LinearLayoutManager(getContext()));
        getPetsFromFirebase(firebaseUser, binding);
        binding.btnPetAdd.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.action_pet_menu_to_addpetFragment);
        });
        binding.btnRegresar.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.action_pet_menu_to_customer_menu);
        });
        return binding.getRoot();
    }
    private void getPetsFromFirebase(FirebaseUser f_user, FCustomerPetBinding binding){
        petList.clear();
        String userID = f_user.getUid();
        mDatabase.child("Pets").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot pt: snapshot.getChildren()) {
                        String id = pt.child("id_user").getValue().toString();
                        if(id.equals(userID)){
                            String petID = pt.getKey();
                            String name = pt.child("name").getValue().toString();
                            pet pet = new pet();
                            pet.setName(name);
                            pet.setId_pet(petID);
                            petList.add(pet);
                        }
                    }
                    mAdapter = new pet_adapter(petList, R.layout.element_list_pet);
                    binding.petList.setAdapter(mAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}