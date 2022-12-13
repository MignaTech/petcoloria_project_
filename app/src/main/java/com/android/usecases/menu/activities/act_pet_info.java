package com.android.usecases.menu.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.java.app.petcoloria.R;
import com.android.java.app.petcoloria.databinding.ActivityActPetInfoBinding;
import com.android.usecases.home.models.pet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class act_pet_info extends AppCompatActivity {
    private pet pet = new pet();
    private ActivityActPetInfoBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityActPetInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String petID="";
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            petID = extras.getString("id");
        }

        referenceDataBase(petID);
    }

    private void referenceDataBase(String indice) {
        FirebaseDatabase.getInstance().getReference("Pets")
                .child(indice)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        pet = snapshot.getValue(pet.class);
                        binding.petName.setText(pet.getName());
                        binding.speciePet.setText(pet.getSpecie());
                        binding.agePet.setText(pet.getAge());
                        binding.weightPet.setText(pet.getWeight());
                        binding.colorPet.setText(pet.getColor());
                        binding.lastVaccine.setText(pet.getLast_vaccine());
                        binding.sexPet.setText(pet.getSex());
                        binding.breedPet.setText(pet.getBreed());
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
    }
}