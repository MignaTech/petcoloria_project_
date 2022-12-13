package com.android.usecases.home.addpet;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.android.java.app.petcoloria.R;
import com.android.java.app.petcoloria.databinding.FAddpetBinding;
import com.android.usecases.home.models.pet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class addpetFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    pet pet = new pet();
    private FirebaseAuth authProfile;
    DatabaseReference mDataBase;
    public addpetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FAddpetBinding  binding = FAddpetBinding.inflate(inflater,container,false);
        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        String userID = firebaseUser.getUid();
        ArrayAdapterMethod(binding);

        binding.btnRegresar.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.action_addpetFragment_to_pet_menu);
        });

        binding.btnPetAdd.setOnClickListener(view -> {
            setInfo(binding, userID);
            if(!pet.getName().isEmpty() && !pet.getSpecie().isEmpty() &&
                    !pet.getAge().isEmpty() && !pet.getColor().isEmpty() && !pet.getSex().isEmpty()){
                petinfo_add(view);
            }else {
                Toast.makeText(getActivity(), "Completa Todos los campos", Toast.LENGTH_SHORT).show();
            }
        });

        return binding.getRoot();
    }

    private void ArrayAdapterMethod(FAddpetBinding binding) {
        ArrayAdapter<CharSequence> adapter_gender = ArrayAdapter.createFromResource(binding.getRoot().getContext(), R.array.gender_list, android.R.layout.simple_spinner_item);
        adapter_gender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.genderPet.setAdapter(adapter_gender);
        binding.genderPet.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter_specie = ArrayAdapter.createFromResource(binding.getRoot().getContext(), R.array.specie_list, android.R.layout.simple_spinner_item);
        adapter_specie.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.typePet.setAdapter(adapter_specie);
        binding.typePet.setOnItemSelectedListener(this);
    }

    private void setInfo(FAddpetBinding binding, String userID) {
        pet.setName(binding.namePetAdd.getText().toString());
        pet.setSpecie(binding.typePet.getSelectedItem().toString());
        pet.setAge(binding.agePet.getText().toString());
        pet.setColor(binding.colorPet.getText().toString());
        pet.setWeight("");
        pet.setLast_vaccine("");
        pet.setSex(binding.genderPet.getSelectedItem().toString());
        pet.setBreed("");
        pet.setId_user(userID);
    }

    private void petinfo_add(View view) {
        Map<String, Object> petInfo = new HashMap<>();
        petInfo.put("name", pet.getName());
        petInfo.put("specie", pet.getSpecie());
        petInfo.put("age", pet.getAge());
        petInfo.put("color", pet.getColor());
        petInfo.put("weight", pet.getWeight());
        petInfo.put("last_vaccine", pet.getLast_vaccine());
        petInfo.put("sex", pet.getSex());
        petInfo.put("breed", pet.getBreed());
        petInfo.put("id_user", pet.getId_user());
        mDataBase = FirebaseDatabase.getInstance().getReference();
        mDataBase.child("Pets").push().setValue(petInfo).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(getActivity(), "Mascota Agregada", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(view).navigate(R.id.action_addpetFragment_to_pet_menu);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String elegido = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}