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
import com.android.java.app.petcoloria.databinding.FSearchMenuBinding;
import com.android.usecases.home.adapters.doctor.doctor_adapter;
import com.android.usecases.home.login.auth.LoginActivity;
import com.android.usecases.home.models.user;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class f_search_menu extends Fragment {
    private doctor_adapter mAdapter;
    private FirebaseAuth authProfile;
    private ArrayList<user> drList = new ArrayList<>();
    DatabaseReference mDatabase;
    public f_search_menu() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FSearchMenuBinding binding = FSearchMenuBinding.inflate(inflater,container,false);
        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        if (firebaseUser == null){
            Toast.makeText(getActivity(), "SESIÓN INVALIDA", Toast.LENGTH_SHORT).show();
            authProfile.signOut();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        }
        binding.doctorList.setLayoutManager(new LinearLayoutManager(getContext()));

        getDoctorFromDatabase(binding);

        binding.btnRegresar.setOnClickListener(view->{
            Navigation.findNavController(view).navigate(R.id.action_search_menu_to_customer_menu);
        });
        return binding.getRoot();
    }
    private void getDoctorFromDatabase(FSearchMenuBinding binding) {
        drList.clear();
        mDatabase.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot dr: snapshot.getChildren()) {
                        String type = dr.child("typeUser").getValue().toString();
                        String tel = dr.child("phone").getValue().toString();
                        String dir = dr.child("address").getValue().toString();
                        if(type.equals("Usuario Veterinario")&&!tel.isEmpty()&&!dir.isEmpty()&&!tel.equals("(xxx)-xxx-xxxx")){
                            String dr_d_ID = dr.getKey();
                            String name = dr.child("name").getValue().toString();
                            user user_temp = new user();
                            user_temp.setName(name);
                            user_temp.setUsername(dr_d_ID);
                            drList.add(user_temp);
                        }
                    }
                    mAdapter = new doctor_adapter(drList, R.layout.element_list_search_doctor);
                    binding.doctorList.setAdapter(mAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}