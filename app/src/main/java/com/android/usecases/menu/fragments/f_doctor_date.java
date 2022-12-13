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
import com.android.java.app.petcoloria.databinding.FCustomerDateBinding;
import com.android.java.app.petcoloria.databinding.FDoctorDateBinding;
import com.android.usecases.home.adapters.dates.dates_adapter;
import com.android.usecases.home.login.auth.LoginActivity;
import com.android.usecases.home.models.date;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class f_doctor_date extends Fragment {

    private dates_adapter mAdapter;
    private FirebaseAuth authProfile;
    private ArrayList<date> dateList = new ArrayList<>();
    DatabaseReference mDatabase;
    public f_doctor_date() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dateList.clear();
        FDoctorDateBinding binding = FDoctorDateBinding.inflate(inflater,container,false);
        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        if (firebaseUser == null){
            Toast.makeText(getActivity(), "SESIÓN INVALIDA", Toast.LENGTH_SHORT).show();
            authProfile.signOut();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        }
        binding.dateList.setLayoutManager(new LinearLayoutManager(getContext()));
        getDatesFromFirebase(firebaseUser, binding);
        binding.btnRegresar.setOnClickListener(view ->{
            Navigation.findNavController(view).navigate(R.id.action_f_doctor_date_to_f_doctor_menu);
        });
        return binding.getRoot();
    }
    private void getDatesFromFirebase(FirebaseUser f_user, FDoctorDateBinding binding){
        dateList.clear();
        String userID = f_user.getUid();
        mDatabase.child("Dates").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot dt: snapshot.getChildren()) {
                        String id = dt.child("id_doctor").getValue().toString();
                        String request = dt.child("request").getValue().toString();
                        if(id.equals(userID)&&(request.equals("aprobado")||request.equals("concluido"))){
                            date date_temp = new date();
                            date_temp.setId_customer(dt.child("id_customer").getValue().toString());
                            date_temp.setId_doctor(dt.child("id_doctor").getValue().toString());
                            date_temp.setId_pet(dt.child("id_pet").getValue().toString());
                            date_temp.setDescription(dt.child("description").getValue().toString());
                            date_temp.setRequest(request);
                            date_temp.setDateID(dt.getKey());
                            dateList.add(date_temp);
                        }
                    }
                    mAdapter = new dates_adapter(dateList, R.layout.element_list_date, 1);
                    binding.dateList.setAdapter(mAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}