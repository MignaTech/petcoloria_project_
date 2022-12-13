package com.android.usecases.home.login.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.android.GlobalVar;
import com.android.java.app.petcoloria.R;
import com.android.java.app.petcoloria.databinding.FLoginBinding;
import com.android.usecases.home.models.user;
import com.android.usecases.home.register.regis.RegisterActivity;
import com.android.usecases.menu.activities.act_customer_menu;
import com.android.usecases.menu.activities.act_doctor_menu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginFragment extends Fragment {

    FirebaseAuth authProfile;
    ProgressDialog loadingBar;

    public LoginFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FLoginBinding binding = FLoginBinding.inflate(inflater,container,false);
        authProfile = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(getActivity());
        binding.btnLogin.setOnClickListener(view -> {
            String Email = binding.username.getText().toString();
            String Pwd = binding.password.getText().toString();
            if (TextUtils.isEmpty(Email)){
                Toast.makeText(getActivity(), "Por favor ingrese su Email", Toast.LENGTH_SHORT).show();
                binding.username.setError("Email es requerido");
                binding.username.requestFocus();
            }else if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
                Toast.makeText(getActivity(), "Por favor re ingrese su Email", Toast.LENGTH_SHORT).show();
                binding.username.setError("Valida Email");
                binding.username.requestFocus();
            }else if(TextUtils.isEmpty(Pwd)){
                Toast.makeText(getActivity(), "Por favor ingrese su contraseña", Toast.LENGTH_SHORT).show();
                binding.password.setError("La Contraseña es requerida");
                binding.password.requestFocus();
            }else{
                loginUser(view,binding,Email,Pwd);
            }
        });

        binding.btnRegistrar.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), RegisterActivity.class));
            getActivity().finish();
        });
        return binding.getRoot();
    }
    private void loginUser(View view, FLoginBinding binding, String email, String pwd) {
        loadingBar.setTitle("Iniciando sesion");
        loadingBar.setMessage("Por favor, espere.....");
        loadingBar.show();
        authProfile.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(getActivity(), task -> {
            if (task.isSuccessful()){
                loadingBar.dismiss();
                FirebaseDatabase.getInstance().getReference("Users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
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
                            public void onCancelled(@NonNull DatabaseError error) { }
                        });
            }else{
                loadingBar.dismiss();
                try{
                    throw task.getException();
                } catch (FirebaseAuthInvalidUserException e){
                    binding.username.setError("El usuario no es valido");
                    binding.username.requestFocus();
                } catch (FirebaseAuthInvalidCredentialsException e){
                    binding.username.setError("Credenciales no validas. Por favor, verifique y vuelva a ingresar");
                    binding.username.requestFocus();
                } catch (Exception e) {
                    Log.e("ERRROR:/// ",e.getMessage());
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            binding.progreso.setVisibility(View.VISIBLE);
        });
    }

}