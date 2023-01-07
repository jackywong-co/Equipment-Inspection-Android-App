package com.example.intelligentequipmentinspectionsystem;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InspectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InspectionFragment extends Fragment {
    private RecyclerView recyclerView;
    private EditText searchBar;
    private ImageView historyIcon;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public InspectionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InspectionFragment newInstance(String param1, String param2) {
        InspectionFragment fragment = new InspectionFragment();
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
        View view = inflater.inflate(R.layout.fragment_inspection, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.roomRecyclerView);
        searchBar = (EditText) view.findViewById(R.id.roomSearchBar);
        historyIcon = (ImageView) view.findViewById(R.id.historyIcon);

        historyIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.historyFragment);
            }
        });

        DataService dataService = new DataService();
        dataService.getRooms(new DataService.ResponseListenerForList() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponse(List<String> data1, List<String> data2, List<String> id) {
                System.out.println("Front Room Name: " + data1);
                setRecyclerView(data1, data2, id);
                searchBar.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void afterTextChanged(Editable editable) {
                        List<Room> rooms = new ArrayList<>();
                        List<String> filteredData1 = new ArrayList<>();
                        List<String> filteredData2 = new ArrayList<>();
                        List<String> filteredId = new ArrayList<>();
                        for (int i = 0; i < data1.size(); i++) {
                            Room room = new Room(data1.get(i), data2.get(i), id.get(i));
                            rooms.add(room);
                        }
                        try {
                            List<Room> filter = rooms.stream()
                                    .filter(c -> c.getRoomName().toLowerCase().contains(editable.toString().toLowerCase()) || c.getRoomLocation().toLowerCase().contains(editable.toString().toLowerCase()))
                                    .collect(Collectors.toList());
                            filter.forEach(room -> {
                                filteredData1.add(room.getRoomName());
                                filteredData2.add(room.getRoomLocation());
                                filteredId.add(room.getRoomId());
                            });
                            System.out.println("filteredData1: " + filteredData1);
                            setRecyclerView(filteredData1, filteredData2, filteredId);
                        } catch (Exception e) {
                        }
                    }
                });
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    public void setRecyclerView(List<String> data1, List<String> data2, List<String> id) {
        RoomAdapter roomAdapter = new RoomAdapter(getContext(), data1, data2, id);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                recyclerView.setAdapter(roomAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            }
        });

    }
}