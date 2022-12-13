package com.android.usecases.home.register.regis;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.android.GlobalVar;
import com.android.java.app.petcoloria.R;
import com.android.java.app.petcoloria.databinding.FRegisterBinding;
import com.android.usecases.home.login.auth.LoginActivity;
import com.android.usecases.home.models.user;
import com.android.usecases.menu.activities.act_customer_menu;
import com.android.usecases.menu.activities.act_doctor_menu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private user User = new user();
    ProgressDialog loadingBar;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    public RegisterFragment() { }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FRegisterBinding binding = FRegisterBinding.inflate(inflater,container,false);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(binding.getRoot().getContext(), R.array.lista, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //LOGICA DE LOS ELEMENTOS DEL FRAGMENT
        binding.typeUser.setAdapter(adapter);
        binding.typeUser.setOnItemSelectedListener(this);
        loadingBar = new ProgressDialog(getActivity());
        binding.paraCelula.setVisibility(View.GONE);
        binding.typeUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(adapterView.getSelectedItem().equals("Usuario Veterinario")){
                    binding.paraCelula.setVisibility(View.VISIBLE);
                }else{
                    binding.cedula.setText("");
                    binding.paraCelula.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        /*
        * FUNCIÓN REGISTRAR
        * */
        binding.btnRegistrar.setOnClickListener(view -> {
            /*
            * OBTIENE LOS DATOS DE LOS CAMPOS DE TEXTO, Y LOS DATOS LOS ALMACENA DENTRO DEL OBJETO USER
            *
            * */
            User.setName(binding.namePerson.getText().toString());
            User.setUsername(binding.username.getText().toString());
            User.setEmail(binding.email.getText().toString());
            User.setPassword(binding.password.getText().toString());
            User.setTypeUser(binding.typeUser.getSelectedItem().toString());
            if(User.getTypeUser().equals("Usuario Veterinario")){//cambiar los que se agregaran
                User.setCedula(binding.cedula.getText().toString());
                User.setAddress(binding.domicilio.getText().toString());
                User.setPhone(binding.telefono.getText().toString());
                User.setInstitucion(binding.institucion.getText().toString());
                if(!User.getName().isEmpty() && !User.getUsername().isEmpty() &&
                        !User.getEmail().isEmpty() && !User.getPassword().isEmpty() && !User.getTypeUser().isEmpty() &&
                        !User.getCedula().isEmpty() && !User.getAddress().isEmpty() && !User.getPhone().isEmpty()){
                    if(User.getPassword().length() >= 6) {
                        registerUser();
                    }else {
                        Toast.makeText(getActivity(), "La contraseña debe contener al menos 6 carácteres", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getActivity(), "Porfavor completa todos los campos", Toast.LENGTH_SHORT).show();
                }
            } else{
                User.setAddress("Desconocido");
                User.setPhone("(xxx)-xxx-xxxx");
                if(!User.getName().isEmpty() && !User.getUsername().isEmpty() &&
                        !User.getEmail().isEmpty() && !User.getPassword().isEmpty() && !User.getTypeUser().isEmpty()){
                    if(User.getPassword().length() >= 6) {
                        registerUser();
                    }else {
                        Toast.makeText(getActivity(), "La contraseña debe contener al menos 6 carácteres", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getActivity(), "Porfavor completa todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.btnLogin.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        });
        return binding.getRoot();
    }
    private void registerUser(){
        mAuth = FirebaseAuth.getInstance();
        loadingBar.setTitle("Registro");
        loadingBar.setMessage("Espere por favor, registrando...");
        loadingBar.show();
        mAuth.createUserWithEmailAndPassword(User.getEmail(), User.getPassword()).addOnCompleteListener(Firsttask -> {
            if(Firsttask.isSuccessful()){
                FirebaseUser fireUser = mAuth.getCurrentUser();
                // Actualiza el nombre de usuario a mostrar
                UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(User.getName()).build();
                fireUser.updateProfile(profileChangeRequest);
                mDatabase = FirebaseDatabase.getInstance().getReference("Users");
                mDatabase.child(fireUser.getUid()).setValue(User).addOnCompleteListener(Secondtask -> {
                    if(Secondtask.isSuccessful()) {
                        mDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        GlobalVar.currentUser = snapshot.getValue(user.class);
                                        if (GlobalVar.currentUser.getTypeUser().equals("Usuario Cliente")){
                                            startActivity(new Intent(getActivity(), act_customer_menu.class));
                                        } else {
                                            startActivity(new Intent(getActivity(), act_doctor_menu.class));
                                        }
                                        Toast.makeText(getActivity(), "Sesion iniciada con exito", Toast.LENGTH_SHORT).show();
                                        getActivity().finish();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                    }else{
                        Toast.makeText(getActivity(), "No fue posible concluir el registro, intente más tarde", Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                Toast.makeText(getActivity(), "No fue posible concluir el registro, intente más tarde", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String elegido = adapterView.getItemAtPosition(i).toString();
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}
}