package com.example.intelligentequipmentinspectionsystem;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FormFragment extends Fragment {
    private RecyclerView recyclerView;
    private ImageView signature, roomImg;
    private TextView formDate, roomNameAndLocation, inspector;
    private FloatingActionButton fab;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FormFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FormFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FormFragment newInstance(String param1, String param2) {
        FormFragment fragment = new FormFragment();
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
        return inflater.inflate(R.layout.fragment_form, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.formRecyclerView);
        formDate = (TextView) view.findViewById(R.id.formDate);
        roomNameAndLocation = (TextView) view.findViewById(R.id.roomNameAndLocation);
        inspector = (TextView) view.findViewById(R.id.inspector);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        signature = (ImageView) view.findViewById(R.id.historySignature);
        roomImg = (ImageView) view.findViewById(R.id.historyRoomImg);

        if (getArguments() != null) {
            List<JSONObject> jsonObjects = new ArrayList<>();
            // get data from last fragment
            FormFragmentArgs args = FormFragmentArgs.fromBundle(getArguments());

            // open dataService to start getting data
            DataService dataService = new DataService();
            dataService.getJSONArray("answer", new DataService.ReturnJsonArray() {
                @Override
                public void onError(String message) {

                }

                @Override
                public void onResponse(JSONArray jsonArray) {
                    // Get list of jsonObjects
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            if (jsonArray.getJSONObject(i).getString("unique_id").equals(args.getAnswerGroupId())) {
                                jsonObjects.add(jsonArray.getJSONObject(i));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("List of JSONObjects in FormFragment: " + jsonObjects);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            // set date, room name and location, inspector
                            try {
                                JSONObject obj1 = jsonObjects.get(0);
                                formDate.setText(obj1.getString("created_at"));

                                String nameAndLocation = obj1.getString("room_name") + " (" + obj1.getString("room_location") + ")";
                                roomNameAndLocation.setText(nameAndLocation);

                                JSONObject obj2 = obj1.getJSONObject("created_by");
                                inspector.setText(obj2.getString("username"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            // set roomImg, might not have
                            try {
                                if (!jsonObjects.get(0).getString("image").equals("")) {
                                    String path = GlobalVariable.BASE_URL.substring(0, GlobalVariable.BASE_URL.length() - 1) + jsonObjects.get(0).getString("image");
                                    System.out.println(path);
                                    Picasso.get().load(path).into(roomImg);
                                } else {
                                    roomImg.setVisibility(View.GONE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            // set signature
                            try {
                                Picasso.get().load(GlobalVariable.BASE_URL.substring(0, GlobalVariable.BASE_URL.length() - 1) + jsonObjects.get(0).getString("signature")).into(signature);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            // set adapter
                            FormAdapter formAdapter = new FormAdapter(getContext(), jsonObjects);
                            recyclerView.setAdapter(formAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        }
                    });

                }
            });

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dataService.getReportExport(args.getAnswerGroupId(), new DataService.ResponseListenerForSuccess() {
                        @Override
                        public void onError(String message) {

                        }

                        @Override
                        public void onResponse(String filepath) {
                            File file = new File(filepath);
                            Uri contentUri = FileProvider.getUriForFile(requireContext(),
                                    BuildConfig.APPLICATION_ID + ".provider", file);
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("application/pdf");
                            intent.putExtra(Intent.EXTRA_STREAM, contentUri);
                            startActivity(Intent.createChooser(intent, "Send Email"));


//                            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
//                            StrictMode.setVmPolicy(builder.build());
//                            Intent emailIntent = new Intent(Intent.ACTION_SEND);
//                            File file = new File(filepath);
//                            Uri uri = Uri.fromFile(file);
//                            emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
//                            startActivity(Intent.createChooser(emailIntent, "Pick an Email provider"));
                        }
                    });
                }
            });
        }
    }
}