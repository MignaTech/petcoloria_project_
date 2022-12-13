package com.android.usecases.menu.fragments;

import static java.lang.Integer.parseInt;

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

public class f_customer_date extends Fragment {
    private dates_adapter mAdapter;
    private FirebaseAuth authProfile;
    private ArrayList<date> dateList = new ArrayList<>();
    DatabaseReference mDatabase;

    public f_customer_date() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dateList.clear();
        FCustomerDateBinding binding = FCustomerDateBinding.inflate(inflater,container,false);
        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        if (firebaseUser == null){
            Toast.makeText(getActivity(), "SESIÓN INVALIDA", Toast.LENGTH_SHORT).show();
            authProfile.signOut();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        }
        binding.datesList.setLayoutManager(new LinearLayoutManager(getContext()));
        getDatesFromFirebase(firebaseUser, binding);
        binding.btnRegresar.setOnClickListener(view->{
            Navigation.findNavController(view).navigate(R.id.action_f_customer_date_to_customer_menu);
        });
        return binding.getRoot();
    }
    private void getDatesFromFirebase(FirebaseUser f_user, FCustomerDateBinding binding){
        dateList.clear();
        String userID = f_user.getUid();
        mDatabase.child("Dates").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot dt: snapshot.getChildren()) {
                        String id = dt.child("id_customer").getValue().toString();
                        if(id.equals(userID)){
                            date date_temp = new date();
                            date_temp.setId_customer(dt.child("id_customer").getValue().toString());
                            date_temp.setId_doctor(dt.child("id_doctor").getValue().toString());
                            date_temp.setId_pet(dt.child("id_pet").getValue().toString());
                            date_temp.setDescription(dt.child("description").getValue().toString());
                            date_temp.setRequest(dt.child("request").getValue().toString());
                            date_temp.setDateID(dt.getKey());
                            dateList.add(date_temp);
                        }
                    }
                    mAdapter = new dates_adapter(dateList, R.layout.element_list_date, 0);
                    binding.datesList.setAdapter(mAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}