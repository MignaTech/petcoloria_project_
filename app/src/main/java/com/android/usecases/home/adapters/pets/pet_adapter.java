package com.android.usecases.home.adapters.pets;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.android.java.app.petcoloria.R;
import com.android.usecases.home.models.pet;
import com.android.usecases.menu.activities.act_pet_info;
import java.util.ArrayList;
public class pet_adapter extends RecyclerView.Adapter <pet_adapter.ViewHolder>{

    private int resource;
    private ArrayList<pet> petsList;
    private pet pets = new pet();

    public pet_adapter(ArrayList<pet> petsList, int resource){
        this.petsList = petsList;
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
        pets = petsList.get(index);
        viewHolder.petname.setText(pets.getName());
        viewHolder.petID.setText(pets.getId_pet());
        viewHolder.setOnClickListenerEvents();
    }

    @Override
    public int getItemCount() {
        return petsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView petname, petID;
        LinearLayout petView;
        Context context;
        View view;
        public ViewHolder(View view){
            super(view);
            this.view = view;
            context = view.getContext();
            this.petID = (TextView) view.findViewById(R.id.petID);
            this.petname = (TextView) view.findViewById(R.id.pet_name);
            this.petView = (LinearLayout) view.findViewById(R.id.pet_view);
        }
        public void setOnClickListenerEvents() {
            petView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, act_pet_info.class);
            intent.putExtra("id",petID.getText());
            context.startActivity(intent);
        }
    }
}
