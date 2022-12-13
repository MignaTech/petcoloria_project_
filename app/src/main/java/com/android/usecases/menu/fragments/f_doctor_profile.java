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
import com.android.GlobalVar;
import com.android.java.app.petcoloria.R;
import com.android.java.app.petcoloria.databinding.FDoctorProfileBinding;
import com.android.usecases.home.login.auth.LoginActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class f_doctor_profile extends Fragment {
    private FirebaseAuth authProfile;
    DatabaseReference mDataBase;
    public f_doctor_profile() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FDoctorProfileBinding binding = FDoctorProfileBinding.inflate(inflater,container,false);
        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        if (firebaseUser == null){
            Toast.makeText(getActivity(), "SESIÓN INVALIDA", Toast.LENGTH_SHORT).show();
            authProfile.signOut();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        } else {
            showUserProfile(binding);
        }
        binding.btnEditDr.setOnClickListener(view -> {
            binding.name1Dr.setEnabled(true);
            binding.name1Dr.requestFocus();
            binding.institutoDr.setEnabled(true);
            binding.phoneDr.setEnabled(true);
            binding.addressDr.setEnabled(true);
            binding.btnUpdate.setVisibility(View.VISIBLE);
            binding.btnDelete.setVisibility(View.INVISIBLE);
        });
        binding.btnUpdate.setOnClickListener(view ->{
            if (binding.phoneDr.getText().toString().isEmpty() || binding.addressDr.getText().toString().equals("(xxx)-xxx-xxxx")){
                binding.phoneDr.setText("(xxx)-xxx-xxxx");
            }else{
                GlobalVar.currentUser.setPhone(binding.phoneDr.getText().toString());
            }
            if (binding.addressDr.getText().toString().isEmpty() || binding.addressDr.getText().toString().equals("Desconocido")){
                binding.addressDr.setText("Desconocido");
            }else{
                GlobalVar.currentUser.setAddress(binding.addressDr.getText().toString());
            }
            GlobalVar.currentUser.setName(binding.name1Dr.getText().toString());
            binding.nameUserDr.setText(binding.name1Dr.getText().toString());
            GlobalVar.currentUser.setInstitucion(binding.institutoDr.getText().toString());
            updateProfile(firebaseUser,binding);
        });
        binding.btnDelete.setOnClickListener((View.OnClickListener) view -> {
            String userID = firebaseUser.getUid();
            mDataBase = FirebaseDatabase.getInstance().getReference();
            mDataBase.child("Users").child(userID).removeValue().addOnSuccessListener(unused -> {
                Toast.makeText(getActivity(), "Usuario Eliminado", Toast.LENGTH_SHORT).show();
                deleteUser(firebaseUser, binding);
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
        binding.btnReturnProfileMd.setOnClickListener(view ->{
            Navigation.findNavController(view).navigate(R.id.action_f_doctor_profile_to_f_doctor_menu);
        });
        binding.btnHorarios.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.action_f_doctor_profile_to_f_doctor_schedule);
        });
        return binding.getRoot();
    }
    private void showUserProfile(FDoctorProfileBinding binding) {
        binding.nameUserDr.setText(GlobalVar.currentUser.getName());
        binding.typeUserDr.setText(GlobalVar.currentUser.getTypeUser());
        binding.name1Dr.setText(GlobalVar.currentUser.getName());
        binding.cedulaDr.setText(GlobalVar.currentUser.getCedula());
        binding.institutoDr.setText(GlobalVar.currentUser.getInstitucion());
        binding.phoneDr.setText(GlobalVar.currentUser.getPhone());
        if(GlobalVar.currentUser.getAddress().toString().equals(""))
            binding.addressDr.setText("Desconocido");
        else
            binding.addressDr.setText(GlobalVar.currentUser.getAddress());
    }
    private void updateProfile(FirebaseUser f_user, FDoctorProfileBinding binding) {
        String userID = f_user.getUid();
        mDataBase = FirebaseDatabase.getInstance().getReference();
        mDataBase.child("Users").child(userID).setValue(GlobalVar.currentUser).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().build();
                f_user.updateProfile(profileUpdate);
                Toast.makeText(getActivity(), "Actualizado con Exito", Toast.LENGTH_SHORT).show();
            }
            binding.name1Dr.setEnabled(false);
            binding.institutoDr.setEnabled(false);
            binding.phoneDr.setEnabled(false);
            binding.addressDr.setEnabled(false);
            binding.btnUpdate.setVisibility(View.INVISIBLE);
            binding.btnDelete.setVisibility(View.VISIBLE);
        });
    }
    private void deleteUser(FirebaseUser f_user, FDoctorProfileBinding binding) {
        f_user.delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                authProfile.signOut();
                Toast.makeText(getActivity(), "Datos Eliminados", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            } else {
                try {
                    throw  task.getException();
                } catch (Exception e){
                    Toast.makeText(getActivity(), "ERROR "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}