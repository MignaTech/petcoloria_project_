package com.android.usecases.launch;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.GlobalVar;
import com.android.java.app.petcoloria.R;
import com.android.usecases.home.login.auth.LoginActivity;
import com.android.usecases.home.models.user;
import com.android.usecases.menu.activities.act_customer_menu;
import com.android.usecases.menu.activities.act_doctor_menu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class splash_screen extends AppCompatActivity {
    FirebaseAuth authProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        authProfile = FirebaseAuth.getInstance();
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                if (authProfile.getCurrentUser()!=null){
                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(authProfile.getCurrentUser().getUid())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    GlobalVar.currentUser = snapshot.getValue(user.class);
                                    if (GlobalVar.currentUser != null) {
                                        if (GlobalVar.currentUser.getTypeUser().equals("Usuario Cliente")){
                                            startActivity(new Intent(splash_screen.this, act_customer_menu.class));
                                        } else {
                                            startActivity(new Intent(splash_screen.this, act_doctor_menu.class));
                                        }
                                    }
                                    overridePendingTransition(R.anim.enter_left_to_right, R.anim.exit_right);
                                    Toast.makeText(splash_screen.this, "Bienvenido", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) { }
                            });
                }else{
                    startActivity(new Intent(splash_screen.this, LoginActivity.class));
                    overridePendingTransition(R.anim.enter_left_to_right, R.anim.exit_right);
                    finish();
                }
            }
        }, 3000);
    }
}

