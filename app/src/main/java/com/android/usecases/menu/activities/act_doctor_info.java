package com.android.usecases.menu.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.Navigation;

import com.android.java.app.petcoloria.R;
import com.android.java.app.petcoloria.databinding.ActivityActDoctorInfoBinding;
import com.android.usecases.home.models.date;
import com.android.usecases.home.models.pet;
import com.android.usecases.home.models.user;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class act_doctor_info extends AppCompatActivity {
    private user doctor = new user();
    private ActivityActDoctorInfoBinding binding;
    private ArrayList<pet> petList = new ArrayList<>();
    DatabaseReference mDatabase;
    private FirebaseAuth authProfile;
    FirebaseUser firebaseUser;
    private date date = new date();
    private String cmID="";
    private String id_dr="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityActDoctorInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        authProfile = FirebaseAuth.getInstance();
        firebaseUser = authProfile.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            id_dr = extras.getString("id");
        }
        loadPets();
        referenceDataBase(id_dr);
        buttonsEvents();
    }
    private void buttonsEvents() {
        binding.btnDate.setOnClickListener(view->{
            setInfoDate();
            if(!date.getId_doctor().isEmpty() && !date.getId_customer().isEmpty() && !date.getId_pet().isEmpty() && !date.getDescription().isEmpty()){
                mDatabase.child("Dates").push().setValue(date).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Toast.makeText(view.getContext(), "Solicitud Enviada", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(this, "Hubo un error: Intenta de nuevo", Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                Toast.makeText(this, "Por favor completa todos los datos", Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(view.getContext(), act_customer_menu.class);
            view.getContext().startActivity(intent);
            finish();
        });
    }
    private void setInfoDate() {
        date.setId_doctor(id_dr);
        date.setId_pet(cmID);
        date.setId_customer(firebaseUser.getUid());
        date.setDescription(binding.motivo.getText().toString());
        date.setRequest("pendiente");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
    private void referenceDataBase(String indice) {
        FirebaseDatabase.getInstance().getReference("Users")
                .child(indice)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        doctor = snapshot.getValue(user.class);
                        binding.docName.setText(doctor.getName());
                        binding.drName.setText(doctor.getName());
                        binding.dirDoc.setText(doctor.getAddress());
                        binding.celDr.setText(doctor.getPhone());
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
    }
    public void loadPets(){
        petList.clear();
        String userID = firebaseUser.getUid();
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
                            pet.setId_pet(petID);
                            pet.setName(name);
                            petList.add(pet);
                        }
                    }
                    ArrayAdapter<pet> arrayAdapter = new ArrayAdapter<>(act_doctor_info.this, android.R.layout.simple_dropdown_item_1line, petList);
                    binding.spinerPets.setAdapter(arrayAdapter);
                    binding.spinerPets.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            cmID = petList.get(i).getId_pet();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {}
                    });

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
    public void onClickLlamada(View view) {
        if (!binding.celDr.getText().toString().isEmpty() && !binding.celDr.getText().toString().equals("(xxx)-xxx-xxxx")){
            final Integer REQUEST_CODE = 1;
            Intent it = new Intent(Intent.ACTION_CALL);
            it.setData(Uri.parse("tel:"+binding.celDr.getText().toString()));
            if (ActivityCompat.checkSelfPermission( act_doctor_info.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                startActivity(it);
            } else {
                ActivityCompat.requestPermissions(
                        act_doctor_info.this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        REQUEST_CODE);
            }
        }
    }
}