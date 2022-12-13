package com.android.usecases.home.adapters.dates;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import com.android.usecases.home.models.date;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class optionsButtons {

    private String id_date;
    private date temp = new date();
    DatabaseReference mDataBase;
    Context context;

    public optionsButtons(String id_date, Context context){
        this.id_date = id_date;
        this.context = context;

    }

    public void events(int op){
        switch (op){
            case 1:
                updateInfoDates("aprobado", "Cita aprobada");
                break;
            case 2:
                updateInfoDates("declinado", "Cita Declinada");
                break;
            case 3:
                updateInfoDates("concluido", "Cita Concluida");
                break;
        }
    }

    private void updateInfoDates(String str,String msg) {
        mDataBase = FirebaseDatabase.getInstance().getReference();
        mDataBase.child("Dates").child(id_date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                temp = snapshot.getValue(date.class);
                temp.setRequest(str);
                setInfo(msg);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private void setInfo(String mensaje){
        Map<String, Object> map = new HashMap<>();
        map.put("description", temp.getDescription());
        map.put("id_customer", temp.getId_customer());
        map.put("id_doctor", temp.getId_doctor());
        map.put("id_pet", temp.getId_pet());
        map.put("request", temp.getRequest());
        mDataBase = FirebaseDatabase.getInstance().getReference();
        mDataBase.child("Dates").child(id_date).setValue(map).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
