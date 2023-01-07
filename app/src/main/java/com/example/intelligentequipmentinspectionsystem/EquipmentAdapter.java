//package com.example.intelligentequipmentinspectionsystem;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.navigation.NavController;
//import androidx.navigation.Navigation;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.List;
//
//public class EquipmentAdapter extends RecyclerView.Adapter<EquipmentAdapter.ViewHolder> {
//    Context context;
//    List<String> data1;
//    List<String> data2;
//    List<String> equipmentIds;
//    String roomId;
//
//    public EquipmentAdapter(Context context, List<String> data1, List<String> data2, List<String> equipmentIds, String roomId) {
//        this.context = context;
//        this.data1 = data1;
//        this.data2 = data2;
//        this.equipmentIds = equipmentIds;
//        this.roomId = roomId;
//    }
//
//    @NonNull
//    @Override
//    public EquipmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View view = inflater.inflate(R.layout.two_string_row, parent, false);
//
//        final ViewHolder holder = new ViewHolder(view);
//        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavController navController = Navigation.findNavController(view);
//                System.out.println("equipmentIds: " +equipmentIds);
//                System.out.println("holder.getAdapterPosition(): " + holder.getAdapterPosition());
//                Bundle bundle = new Bundle();
//                bundle.putString("roomId", roomId);
//                bundle.putString("equipmentId", equipmentIds.get(holder.getAdapterPosition()));
//                GlobalVariable.backPressed = false;
//                Navigation.findNavController(view).navigate(R.id.formFragment, bundle);
//            }
//        });
//
//        return holder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull EquipmentAdapter.ViewHolder holder, int position) {
//        holder.titleTV.setText(data1.get(position));
//        holder.subtitleTV.setText(data2.get(position));
//        String strId = equipmentIds.get(position);
//        System.out.println("equipmentID: " + strId);
//    }
//
//    @Override
//    public int getItemCount() {
//        return equipmentIds.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//
//        TextView titleTV;
//        TextView subtitleTV;
//        LinearLayout linearLayout;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            titleTV = itemView.findViewById(R.id.rowTitle);
//            subtitleTV = itemView.findViewById(R.id.rowSubtitle);
//            linearLayout = itemView.findViewById(R.id.twoStringRow);
//        }
//    }
//}
