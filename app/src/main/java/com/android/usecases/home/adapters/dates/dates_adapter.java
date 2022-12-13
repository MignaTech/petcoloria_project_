package com.android.usecases.home.adapters.dates;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.android.java.app.petcoloria.R;
import com.android.usecases.home.models.date;
import com.android.usecases.home.models.pet;
import com.android.usecases.home.models.user;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class dates_adapter extends RecyclerView.Adapter<dates_adapter.ViewHolder>{

    final private int resource;
    final private ArrayList<date> dateList;
    final private int id;
    private date dates = new date();
    private pet pet = new pet();
    private user user = new user();
    DatabaseReference mDataBase;


    public dates_adapter(ArrayList<date> dateList, int resource, int id) {
        this.dateList = dateList;
        this.resource = resource;
        this.id = id;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int index) {
        dates = dateList.get(index);
        if (id == 0) {
            //Para customer dates
            searchDB(holder, "Users", dateList.get(index).getId_doctor());
        } else {
            //Para doctor dates
            searchDB(holder, "Users", dateList.get(index).getId_customer());
            holder.description.setVisibility(View.VISIBLE);
            holder.description.setText(dateList.get(index).getDescription());
            holder.iditem.setText(dateList.get(index).getDateID());
            if(dates.getRequest().equals("concluido")){
                holder.btn_conclude.setVisibility(View.GONE);
            }else{
                holder.btn_conclude.setVisibility(View.VISIBLE);
            }
            if (id == 2) {
                //Para doctor notifications
                holder.buttons.setVisibility(View.VISIBLE);
                holder.btn_conclude.setVisibility(View.GONE);
                holder.icon.setImageResource(R.drawable.img_notification_icon);
            }
            holder.setEventsButtons();
        }
        searchDB(holder, "Pets", dates.getId_pet());
        requestText(holder);
    };


    private void requestText(@NonNull ViewHolder holder) {
        switch (dates.getRequest()){
            case "pendiente":
                holder.request.setText("Estado: Pendiente");
                holder.request.setTextColor(Color.parseColor("#ECB814"));
                break;
            case "declinado":
                holder.request.setText("Estado: Declinada");
                holder.request.setTextColor(Color.parseColor("#E00000"));
                break;
            case "aprobado":
                holder.request.setText("Estado: Aprobada");
                holder.request.setTextColor(Color.parseColor("#69EC14"));
                break;
            case "concluido":
                holder.request.setText("Estado: Concluida");
                holder.request.setTextColor(Color.parseColor("#149AEC"));
                break;
        }
    }

    private void searchDB(@NonNull ViewHolder holder, String childParent, String ID) {
        mDataBase = FirebaseDatabase.getInstance().getReference();
        mDataBase.child(childParent).child(ID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    if (childParent.equals("Users")) {
                        user = snapshot.getValue(user.class);
                        holder.name_user.setText(user.getName());
                    } else {
                        pet = snapshot.getValue(pet.class);
                        holder.namePet.setText(pet.getName());
                    }
                }else{
                    Toast.makeText(holder.context, "Sin informacion", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return dateList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView namePet, name_user, request, description, iditem;
        ImageView icon;
        Button btn_accept, btn_decline, btn_conclude;
        LinearLayout item, buttons;
        Context context;
        public ViewHolder(View view){
            super(view);
            context = view.getContext();
            this.icon = view.findViewById(R.id.icon);
            this.namePet = view.findViewById(R.id.item_name);
            this.name_user = view.findViewById(R.id.name);
            this.request = view.findViewById(R.id.request);
            this.description = view.findViewById(R.id.description);
            this.item = view.findViewById(R.id.item_date);
            this.iditem = view.findViewById(R.id.iditem);
            this.buttons = view.findViewById(R.id.buttons);
            this.btn_accept = view.findViewById(R.id.btn_accept);
            this.btn_decline = view.findViewById(R.id.btn_decline);
            this.btn_conclude = view.findViewById(R.id.btn_conclude);

        }


        public void setEventsButtons() {
            btn_accept.setOnClickListener(this);
            btn_decline.setOnClickListener(this);
            btn_conclude.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            optionsButtons event;
            switch (view.getId()){
                case R.id.btn_accept:
                    event = new optionsButtons(iditem.getText().toString(), context);
                    event.events(1);
                    Navigation.findNavController(view).navigate(R.id.action_f_doctor_notifications_to_f_doctor_menu);
                    Navigation.findNavController(view).navigate(R.id.action_f_doctor_menu_to_f_doctor_date);
                    break;
                case R.id.btn_decline:
                    event = new optionsButtons(iditem.getText().toString(), context);
                    event.events(2);
                    Navigation.findNavController(view).navigate(R.id.action_f_doctor_notifications_to_f_doctor_menu);
                    Navigation.findNavController(view).navigate(R.id.action_f_doctor_menu_to_f_doctor_date);
                    break;
                case R.id.btn_conclude:
                    event = new optionsButtons(iditem.getText().toString(),context);
                    event.events(3);
                    Navigation.findNavController(view).navigate(R.id.action_f_doctor_date_to_f_doctor_menu);
                    break;
            }
        }
    }

}
