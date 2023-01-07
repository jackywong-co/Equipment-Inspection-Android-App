package com.example.intelligentequipmentinspectionsystem;

import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EquipmentAdapter2 extends RecyclerView.Adapter<EquipmentAdapter2.ViewHolder> {
    private Context context;
    private List<Question> questions;

    public EquipmentAdapter2(Context context, List<Question> questions) {
        this.context = context;
        this.questions = questions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.default_question, parent, false);

        final ViewHolder holder = new ViewHolder(view);

        holder.checkItemRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((Integer) holder.arrow.getTag() == R.drawable.ic_baseline_navigate_next_24){
                    System.out.println("arrow down");
                    holder.arrow.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                    holder.arrow.setTag(R.drawable.ic_baseline_keyboard_arrow_down_24);
                    holder.checkOptions.setVisibility(View.VISIBLE);
                    if (holder.defective.isChecked()){
                        holder.followUpAction.setVisibility(View.VISIBLE);
                    }
                } else {
                    System.out.println("arrow right");
                    holder.arrow.setImageResource(R.drawable.ic_baseline_navigate_next_24);
                    holder.arrow.setTag(R.drawable.ic_baseline_navigate_next_24);
                    holder.checkOptions.setVisibility(View.GONE);
                    holder.followUpAction.setVisibility(View.GONE);
                }
            }
        });
        holder.normalOrDefective.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                holder.theWholeRow.setBackgroundResource(R.drawable.green_outline);
                if (i == R.id.defective) {
                    // selected defective, show follow up action editText
                    questions.get(holder.getAdapterPosition()).setNormalOrDefective("defective");
                    // show reason editText
                    holder.followUpAction.setVisibility(View.VISIBLE);
                } else {
                    // selected normal, remove follow up action for defective
                    holder.followUpAction.setVisibility(View.GONE);
                    questions.get(holder.getAdapterPosition()).setNormalOrDefective("normal");
                    questions.get(holder.getAdapterPosition()).setFollowUpAction("");
                }
            }
        });

        // record the reason
        holder.followUpAction.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                questions.get(holder.getAdapterPosition()).setFollowUpAction(editable.toString());
                System.out.println("Reason: " + questions.get(holder.getAdapterPosition()).getFollowUpAction());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.checkItemName.setText(questions.get(position).getQuestionTitle());
        holder.arrow.setTag(R.drawable.ic_baseline_navigate_next_24);
        if (questions.get(position).getNormalOrDefective() == "normal"){
            holder.normal.setChecked(true);
        } else if(questions.get(position).getNormalOrDefective() == "defective"){
            holder.defective.setChecked(true);
        }
        holder.followUpAction.setText(questions.get(position).getFollowUpAction());
        holder.followUpAction.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout checkItemRow;
        TextView checkItemName;
        ImageView arrow;
        RadioGroup normalOrDefective;
        RadioButton normal;
        RadioButton defective;
        EditText followUpAction;
        LinearLayout checkOptions;
        LinearLayout theWholeRow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkItemRow = (RelativeLayout) itemView.findViewById(R.id.checkItemRow);
            checkItemName = (TextView) itemView.findViewById(R.id.checkItemName);
            arrow = (ImageView) itemView.findViewById(R.id.arrow);
            normalOrDefective = (RadioGroup) itemView.findViewById(R.id.normalOrDefective);
            normal = (RadioButton) itemView.findViewById(R.id.normal);
            defective = (RadioButton) itemView.findViewById(R.id.defective);
            followUpAction = (EditText) itemView.findViewById(R.id.followUpAction);
            checkOptions = (LinearLayout) itemView.findViewById(R.id.checkOptions);
            theWholeRow = (LinearLayout) itemView.findViewById(R.id.theWholeRow);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String validation() {
        for (int i=0; i<questions.size() ; i++){
            if(questions.get(i).getNormalOrDefective()=="null"){
                System.out.println("Validation: Missing");
                return "missing";
            }
        }
        for (int i=0; i<questions.size() ; i++){
            if(questions.get(i).getNormalOrDefective()=="defective"){
                System.out.println("Validation: defective");
                GlobalVariable.globalQuestions = questions;
                return "defective";
            }
        }
        System.out.println("Validation: pass");
        GlobalVariable.globalQuestions = questions;
        return "pass";
    }
}
