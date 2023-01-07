package com.example.intelligentequipmentinspectionsystem;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EquipmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EquipmentFragment extends Fragment {
    private RecyclerView recyclerView;
    private Button button;
    private EquipmentAdapter2 equipmentAdapter;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EquipmentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EquipmentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EquipmentFragment newInstance(String param1, String param2) {
        EquipmentFragment fragment = new EquipmentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_equipment, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.equipmentRecyclerView);
        button = (Button) view.findViewById(R.id.toSignPage);
        if (getArguments() != null) {
            EquipmentFragmentArgs args = EquipmentFragmentArgs.fromBundle(getArguments());

            DataService dataService = new DataService();
            dataService.getEquipmentsByRoomId(args.getRoomId(), new DataService.ResponseListenerForList() {
                @Override
                public void onError(String message) {

                }

                @Override
                public void onResponse(List<String> data1, List<String> data2, List<String> equipmentId) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            List<Question> questions = new ArrayList<>();
                            for (int i=0; i < data1.size(); i++){
                                Question question = new Question();
                                question.setQuestionTitle(data1.get(i));
                                question.setEquipmentId(equipmentId.get(i));
                                questions.add(question);
                            }

                            if (GlobalVariable.backPressed){
                                System.out.println("EquipmentFragment back data" + GlobalVariable.globalQuestions);
                                 equipmentAdapter = new EquipmentAdapter2(getContext(), GlobalVariable.globalQuestions);
                            } else {
                                System.out.println("EquipmentFragment data" + questions);
                                 equipmentAdapter = new EquipmentAdapter2(getContext(), questions);
                            }
                            recyclerView.setAdapter(equipmentAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        }
                    });
                }
            });
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (equipmentAdapter.validation() == "pass") {
                    // all good
                    GlobalVariable.backPressed = false;
                    Navigation.findNavController(view).navigate(R.id.formPart2Fragment);
                } else if (equipmentAdapter.validation() == "missing") {
                    // one of them is missing
                    Toast toast = Toast.makeText(getContext(), "Please Fill All Questions", Toast.LENGTH_SHORT);
                    toast.show();
                } else if (equipmentAdapter.validation() == "defective") {
                    // one of them is bad
                    GlobalVariable.backPressed = false;
                    Navigation.findNavController(view).navigate(R.id.formPart2Fragment);
                }

            }
        });

    }
}