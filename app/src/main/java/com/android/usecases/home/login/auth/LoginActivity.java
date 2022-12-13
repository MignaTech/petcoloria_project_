package com.android.usecases.home.login.auth;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.java.app.petcoloria.R;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth authProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        authProfile = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (authProfile.getCurrentUser()!=null){
            Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Por favor, inicia sesión", Toast.LENGTH_SHORT).show();
        }
    }
}