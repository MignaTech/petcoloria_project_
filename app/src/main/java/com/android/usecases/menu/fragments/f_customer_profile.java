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
import com.android.java.app.petcoloria.databinding.FCustomerProfileBinding;
import com.android.usecases.home.login.auth.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class f_customer_profile extends Fragment {
    private FirebaseAuth authProfile;
    DatabaseReference mDataBase;
    public f_customer_profile() {}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FCustomerProfileBinding binding = FCustomerProfileBinding.inflate(inflater,container,false);
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

        binding.btnEditCustomer.setOnClickListener(view -> {
            binding.addressCustomer.setEnabled(true);
            binding.nameUserCustomerMc.setEnabled(true);
            binding.phoneCustomer.setEnabled(true);
            binding.nameUserCustomerMc.requestFocus();
            binding.btnUpdate.setVisibility(View.VISIBLE);
            binding.btnDelete.setVisibility(View.INVISIBLE);
        });

        binding.btnUpdate.setOnClickListener(view ->{
            GlobalVar.currentUser.setName(binding.nameUserCustomerMc.getText().toString());
            GlobalVar.currentUser.setPhone(binding.phoneCustomer.getText().toString());
            GlobalVar.currentUser.setAddress(binding.addressCustomer.getText().toString());
            updateProfile(firebaseUser,binding);
        });

        binding.btnDelete.setOnClickListener(view -> {
            String userID = firebaseUser.getUid();
            mDataBase = FirebaseDatabase.getInstance().getReference();
            mDataBase.child("Users").child(userID).removeValue().addOnSuccessListener(unused -> {
                Toast.makeText(getActivity(), "Usuario Eliminado", Toast.LENGTH_SHORT).show();
                deleteUser(firebaseUser, binding);
            }).addOnFailureListener(e ->
                    Toast.makeText(getActivity(), "Error "+e.getMessage(), Toast.LENGTH_SHORT).show());
        });
        binding.btnReturnProfileMc.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.action_profile_menu_customer_to_customer_menu2));
        return binding.getRoot();
    }

    private void updateProfile(FirebaseUser f_user, FCustomerProfileBinding binding) {
        String userID = f_user.getUid();
        mDataBase = FirebaseDatabase.getInstance().getReference();
        mDataBase.child("Users").child(userID).setValue(GlobalVar.currentUser).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().build();
                f_user.updateProfile(profileUpdate);
                Toast.makeText(getActivity(), "Actualizado con Exito", Toast.LENGTH_SHORT).show();
            }
            binding.nameUserCustomerMc.setEnabled(false);
            binding.phoneCustomer.setEnabled(false);
            binding.addressCustomer.setEnabled(false);
            binding.btnUpdate.setVisibility(View.INVISIBLE);
            binding.btnDelete.setVisibility(View.VISIBLE);
        });
    }

    private void showUserProfile(FCustomerProfileBinding binding) {
        binding.nameUserCustomerMc.setText(GlobalVar.currentUser.getName());
        binding.typeUserCustomerMc.setText(GlobalVar.currentUser.getTypeUser());
        binding.phoneCustomer.setText(GlobalVar.currentUser.getPhone());
        binding.addressCustomer.setText(GlobalVar.currentUser.getAddress());
        binding.emailCustomer.setText(GlobalVar.currentUser.getEmail());
    }

    private void deleteUser(FirebaseUser f_user, FCustomerProfileBinding binding) {
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