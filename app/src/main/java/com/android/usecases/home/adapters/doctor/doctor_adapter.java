package com.android.usecases.home.adapters.doctor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.android.java.app.petcoloria.R;
import com.android.usecases.home.models.user;
import com.android.usecases.menu.activities.act_doctor_info;

import java.util.ArrayList;

public class doctor_adapter extends RecyclerView.Adapter <doctor_adapter.ViewHolder> {
    private int resource;
    private ArrayList<user> drList;
    private user user_temp = new user();
    public doctor_adapter(ArrayList<user> drList, int resource){
        this.drList = drList;
        this.resource = resource;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int index) {
        user_temp = drList.get(index);
        viewHolder.dID.setText(user_temp.getUsername());
        viewHolder.namedr.setText(user_temp.getName());
        viewHolder.setOnClickListenerEvents();
    }
    @Override
    public int getItemCount() {
        return drList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView namedr,dID;
        RelativeLayout itemList;
        Context context;
        View view;
        public ViewHolder(View view){
            super(view);
            this.view = view;
            context = view.getContext();
            this.dID = view.findViewById(R.id.doc_ID);
            this.namedr = view.findViewById(R.id.name_dr);
            this.itemList = view.findViewById(R.id.item_list);
        }
        public void setOnClickListenerEvents() {
            itemList.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, act_doctor_info.class);
            intent.putExtra("id",dID.getText());
            context.startActivity(intent);
        }
    }
}
