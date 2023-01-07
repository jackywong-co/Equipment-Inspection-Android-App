package com.example.intelligentequipmentinspectionsystem;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gkemon.XMLtoPDF.PdfGenerator;
import com.gkemon.XMLtoPDF.PdfGeneratorListener;
import com.gkemon.XMLtoPDF.model.FailureResponse;
import com.gkemon.XMLtoPDF.model.SuccessResponse;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FormPart2Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FormPart2Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final int CAMERA_REQUEST = 1;
    private LinearLayout takePicture;
    private ImageView imageView;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private Button send, clear;
    private SignatureView signatureView;
    DataService dataService = new DataService();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FormPart2Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FormPart2Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FormPart2Fragment newInstance(String param1, String param2) {
        FormPart2Fragment fragment = new FormPart2Fragment();
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

        // camera stuff
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getData() != null) {
                    Bundle bundle = result.getData().getExtras();
                    try {
                        Bitmap bitmap = (Bitmap) bundle.get("data");
                        imageView.setImageBitmap(bitmap);
                    } catch (NullPointerException e) {
                        System.out.println(e);
                    }
                }
            }
        });
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_form_part2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // camera stuff
        takePicture = (LinearLayout) view.findViewById(R.id.part2_take_picture_button);
        imageView = (ImageView) view.findViewById(R.id.part2_picture_preview);
        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);
                } else {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    activityResultLauncher.launch(takePictureIntent);
                }
            }
        });

        // send button
        send = (Button) view.findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap emptyBitmap = Bitmap.createBitmap(signatureView.getImage().getWidth(), signatureView.getImage().getHeight(), signatureView.getImage().getConfig());
                if (signatureView.getImage().sameAs(emptyBitmap)) {
                    Toast toast = Toast.makeText(getContext(), "Please Sign", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    saveBitmap(signatureView.getImage(),"signature");
                    if (imageView.getDrawable() != null){
                        BitmapDrawable draw = (BitmapDrawable) imageView.getDrawable();
                        Bitmap bitmap = draw.getBitmap();
                        saveBitmap(bitmap,"equipment");
                    }

                    setQuestions();

                    Toast toast = Toast.makeText(getContext(), "Form Sent", Toast.LENGTH_SHORT);
                    toast.show();
                    // get data from last fragment
//                    FormPart2FragmentArgs args = FormPart2FragmentArgs.fromBundle(getArguments());
                    // prepare bundle for next fragment
//                    Bundle bundle = new Bundle();
//                    bundle.putString("roomId", args.getRoomId());
                    //generatePDF(getContext());
                    // navigate to inspectionFragment
//                    Getting question id
                    GlobalVariable.backPressed = false;
                    NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                    navController.navigateUp();
                    navController.navigateUp();
//                    Navigation.findNavController(view).navigate(R.id.equipmentFragment, bundle);
                }
            }
        });

        // signature stuff
        signatureView = (SignatureView) view.findViewById(R.id.signature);
        clear = (Button) view.findViewById(R.id.clearSignature);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signatureView.clearSignature();
            }
        });
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                clear.callOnClick();
            }
        }, 1);
    }

    public void saveBitmap(Bitmap bmp, String imageName) {
        try {
            String root = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
            String filepath = root + "Download/"+imageName+".jpg";

            FileOutputStream fos = new FileOutputStream(filepath);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            Log.e("Could not save", e.getMessage());
            e.printStackTrace();
        }
    }

    public void setQuestions(){
        dataService.getQuestions(new DataService.ResponseListenerForQuestions() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponse(List<Question> questions) {

                // find question id for the 3
                String normalId = "";
                String defectiveId = "";
                String followUpId = "";
                for (int i=0; i < questions.size(); i++){
                    if(questions.get(i).getQuestionTitle().toLowerCase().contains("normal")){
                        normalId = questions.get(i).getQuestionId();
                    }
                    if(questions.get(i).getQuestionTitle().toLowerCase().contains("defect")){
                        defectiveId = questions.get(i).getQuestionId();
                    }
                    if(questions.get(i).getQuestionTitle().toLowerCase().contains("follow")){
                        followUpId = questions.get(i).getQuestionId();
                    }
                }

                // set question id for all
                for (int i = 0; i < GlobalVariable.globalQuestions.size(); i++) {
                    if(GlobalVariable.globalQuestions.get(i).getNormalOrDefective().equals("normal")){
                        GlobalVariable.globalQuestions.get(i).setQuestionId(normalId);
                    } else {
                        if(!GlobalVariable.globalQuestions.get(i).getFollowUpAction().equals("")){
                            GlobalVariable.globalQuestions.get(i).setQuestionId(followUpId);
                        } else {
                            GlobalVariable.globalQuestions.get(i).setQuestionId(defectiveId);
                        }
                    }
                    System.out.println("Set question id for question " +i+" = "+GlobalVariable.globalQuestions.get(i).getQuestionId());
                }

                // next to set form id
                setFormId();
            }
        });
    }

    public void setFormId(){
        dataService.getForms(new DataService.ResponseListenerForFormId() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponse(HashMap<String, String> equipmentIdForFormId) {
                // set form id
                for(int i = 0; i < GlobalVariable.globalQuestions.size(); i++){
                    System.out.println("EquipmentId: "+i+" = " + GlobalVariable.globalQuestions.get(i).getEquipmentId());
                    System.out.println("FormId: " +i+" = "+ equipmentIdForFormId.get(GlobalVariable.globalQuestions.get(i).getEquipmentId()));
                    GlobalVariable.globalQuestions.get(i).setFormId(equipmentIdForFormId.get(GlobalVariable.globalQuestions.get(i).getEquipmentId()));
                    System.out.println("Set form id for question " +i+" = "+GlobalVariable.globalQuestions.get(i).getFormId());
                }
                // next to post answers
                postAnswers();
            }
        });
    }

    public void postAnswers(){
        boolean hasImage = true;
        if (imageView.getDrawable() == null){
            hasImage = false;
        }
        for (int i = 0; i < GlobalVariable.globalQuestions.size(); i++) {
            dataService.postAnswer(GlobalVariable.globalQuestions.get(i),hasImage);
        }
    }
    private void generatePDF(Context context) {
//        System.out.println("generatePDF");
//        PdfGenerator.getBuilder()
//                .setContext(getContext())
//                .fromViewSource()
//                .fromView(view)
//                .setFileName("Test-PDF")
//                .setFolderName("Test-PDF-folder")
//                .openPDFafterGeneration(true)
//                .build(new PdfGeneratorListener() {
//                    @Override
//                    public void onFailure(FailureResponse failureResponse) {
//                        super.onFailure(failureResponse);
//                        System.out.println("It failed: " + failureResponse.getErrorMessage());
//                    }
//
//                    @Override
//                    public void showLog(String log) {
//                        super.showLog(log);
//                    }
//
//                    @Override
//                    public void onStartPDFGeneration() {
//                        /*When PDF generation begins to start*/
//                    }
//
//                    @Override
//                    public void onFinishPDFGeneration() {
//                        /*When PDF generation is finished*/
//                    }
//
//                    @Override
//                    public void onSuccess(SuccessResponse response) {
//                        super.onSuccess(response);
//
//                        System.out.println("PDF Path" + response.getPath());
//                    }
//                });
        PdfGenerator.getBuilder()
                .setContext(context)
                .fromLayoutXMLSource()
                .fromLayoutXML(R.layout.fragment_form_part2)
                /* "fromLayoutXML()" takes array of layout resources.
                 * You can also invoke "fromLayoutXMLList()" method here which takes list of layout resources instead of array. */
                .setFileName("Test-PDF")
                /* It is file name */
                .setFolderName("FolderA/FolderB/FolderC")
                /* It is folder name. If you set the folder name like this pattern (FolderA/FolderB/FolderC), then
                 * FolderA creates first.Then FolderB inside FolderB and also FolderC inside the FolderB and finally
                 * the pdf file named "Test-PDF.pdf" will be store inside the FolderB. */
                .openPDFafterGeneration(true)
                /* It true then the generated pdf will be shown after generated. */
                .build(new PdfGeneratorListener() {
                    @Override
                    public void onFailure(FailureResponse failureResponse) {
                        super.onFailure(failureResponse);
                        System.out.println("It failed: " + failureResponse.getErrorMessage());
                        /* If pdf is not generated by an error then you will findout the reason behind it
                         * from this FailureResponse. */
                    }

                    @Override
                    public void onStartPDFGeneration() {
                        /*When PDF generation begins to start*/
                    }

                    @Override
                    public void onFinishPDFGeneration() {
                        /*When PDF generation is finished*/
                    }

                    @Override
                    public void showLog(String log) {
                        super.showLog(log);
                        /*It shows logs of events inside the pdf generation process*/
                    }

                    @Override
                    public void onSuccess(SuccessResponse response) {
                        super.onSuccess(response);
                        /* If PDF is generated successfully then you will find SuccessResponse
                         * which holds the PdfDocument,File and path (where generated pdf is stored)*/
                        System.out.println("Successfully made PDF" + response.getPath());
                    }
                });
    }

}