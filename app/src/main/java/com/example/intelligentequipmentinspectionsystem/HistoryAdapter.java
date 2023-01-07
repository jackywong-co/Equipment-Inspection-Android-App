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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    Context context;
    List<JSONObject> jsonObjects;

    public HistoryAdapter(Context context, List<JSONObject> jsonObjects) {
        this.context = context;
        this.jsonObjects = jsonObjects;
    }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.three_string_row, parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalVariable.backPressed = false;
//                NavController navController = Navigation.findNavController(view);
//                HistoryFragmentDirections.ActionHistoryFragmentToFormFragment action = HistoryFragmentDirections.actionHistoryFragmentToFormFragment(answerIds.get(holder.getAdapterPosition()));
//                navController.navigate(action);
                NavController navController = Navigation.findNavController(view);
                HistoryFragmentDirections.ActionHistoryFragmentToFormFragment action = null;
                try {
                    action = HistoryFragmentDirections.actionHistoryFragmentToFormFragment(jsonObjects.get(holder.getAdapterPosition()).getString("unique_id"));
                    navController.navigate(action);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder holder, int position) {
        try {
            holder.titleTV.setText(jsonObjects.get(position).getString("room_name"));
            holder.subtitleTV.setText(jsonObjects.get(position).getString("room_location"));
            holder.date.setText(jsonObjects.get(position).getString("created_at").substring(0,10));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return jsonObjects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTV;
        TextView subtitleTV;
        TextView date;
        LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTV = itemView.findViewById(R.id.historyRowTitle);
            subtitleTV = itemView.findViewById(R.id.historyRowSubtitle);
            date = itemView.findViewById(R.id.historyRowDate);
            linearLayout = itemView.findViewById(R.id.threeStringRow);
        }
    }
}
