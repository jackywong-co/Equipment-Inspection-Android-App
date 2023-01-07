package com.example.intelligentequipmentinspectionsystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.ViewHolder> {

    Context context;
    List<String> data1;
    List<String> data2;
    List<String> roomIds;

    public RoomAdapter(Context context, List<String> data1, List<String> data2, List<String> roomIds) {
        this.context = context;
        this.data1 = data1;
        this.data2 = data2;
        this.roomIds = roomIds;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.two_string_row, parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    System.out.println("holder.getAdapterPosition(): "+ holder.getAdapterPosition());
                    GlobalVariable.backPressed = false;
                    NavController navController = Navigation.findNavController(view);
                    InspectionFragmentDirections.ActionInspectionFragmentToEquipmentFragment action = InspectionFragmentDirections.actionInspectionFragmentToEquipmentFragment(roomIds.get(holder.getAdapterPosition()),data1.get(holder.getAdapterPosition()));
                    navController.navigate(action);
                } catch (Exception e) {
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.titleTV.setText(data1.get(position));
        holder.subtitleTV.setText(data2.get(position));
    }

    @Override
    public int getItemCount() {
        return data1.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView titleTV;
        TextView subtitleTV;
        LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTV = itemView.findViewById(R.id.rowTitle);
            subtitleTV = itemView.findViewById(R.id.rowSubtitle);
            linearLayout = itemView.findViewById(R.id.twoStringRow);
        }
    }
}
