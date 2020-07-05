package com.app.plyss.ui.add_form;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.plyss.R;
import com.app.plyss.data.api.ApiClient;
import com.app.plyss.data.api.ApiService;
import com.app.plyss.data.local.LocalDataSource;
import com.app.plyss.data.model.Form;
import com.app.plyss.data.model.Household;
import com.app.plyss.data.model.User;
import com.app.plyss.utils.AppGlobals;
import com.app.plyss.utils.AppUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.app.plyss.utils.AppGlobals.PICK_IMAGE_REQUEST;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddHouseholdFragment extends Fragment {
    private static final String TAG = "AddHouseholdFragment";
    private static final int REQUEST_TAKE_PHOTO = 101;
    private EditText fatherName;
    private EditText motherName;
    private EditText childrenNumber;
    private EditText dependantsNumber;
    private AutoCompleteTextView marital_status;
    private AutoCompleteTextView state_origin;
    private EditText street;
    private AutoCompleteTextView lga_origin;
    private EditText occupation;
    private EditText address;
    private EditText wives;
    private ProgressBar addLoading;
    private ImageView image;
    private ImageButton add_image;

    private String imageURL;
    private String imageNamePath;

    private AddFragmentViewModel addFragmentViewModel;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseCrashlytics crashlytics;
    private FirebaseFirestore db;
    private NavController navController;

    private Household household;
    private String currentPhotoPath;

    private List<String> stateOriginLgas = new ArrayList<>();
    private List<String> stateResidenceLgas = new ArrayList<>();

    @BindView(R.id.add_household_btn)
    MaterialButton addHouseholdBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add_household, container, false);

        AddFragmentViewModelFactory factory = new AddFragmentViewModelFactory(requireActivity().getApplication());
        addFragmentViewModel = new ViewModelProvider(this, factory).get(AddFragmentViewModel.class);
        ButterKnife.bind(this, root);

        //Init firebase analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity());
        mFirebaseAnalytics.setCurrentScreen(requireActivity(), this.getClass().getSimpleName(),
                this.getClass().getSimpleName());
        crashlytics = FirebaseCrashlytics.getInstance();
        db = FirebaseFirestore.getInstance();
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        household = new Household();

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        //views
        fatherName = root.findViewById(R.id.h_father_name);
        motherName = root.findViewById(R.id.h_mother_name);
        dependantsNumber = root.findViewById(R.id.h_dependant_number);
        childrenNumber = root.findViewById(R.id.h_children_number);
        street = root.findViewById(R.id.h_street);
        address = root.findViewById(R.id.h_address);
        marital_status = root.findViewById(R.id.h_marital_status);
        lga_origin = root.findViewById(R.id.h_lga_origin);
        state_origin = root.findViewById(R.id.h_state_origin);
        image = root.findViewById(R.id.household_image);
        add_image = root.findViewById(R.id.add_h_image_btn);
        occupation = root.findViewById(R.id.h_occupation);
        wives = root.findViewById(R.id.h_wives);
        addLoading = root.findViewById(R.id.loading_household);

        add_image.setOnClickListener(v -> dispatchTakePictureIntent());

        ArrayAdapter stateAdapter = new ArrayAdapter<>(requireActivity(), R.layout.dropdown_pop_up_item, LocalDataSource.ALL_STATES);
        state_origin.setAdapter(stateAdapter);
        state_origin.setKeyListener(null);
        state_origin.setOnItemClickListener((parent, view, position, id) -> {
            Object obj = parent.getItemAtPosition(position);
            getOriginStateLgas(obj.toString());
        });

        lga_origin.setOnClickListener(v -> getOriginStateLgas(state_origin.getText().toString()));

        ArrayAdapter maritalAdapter = new ArrayAdapter<>(requireActivity(), R.layout.dropdown_pop_up_item, LocalDataSource.MARITAL_STATUS);
        marital_status.setKeyListener(null);
        marital_status.setAdapter(maritalAdapter);


        return root;

    }

    private void openHome() {
        navController.navigate(R.id.navigation_home);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Go back home");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);
    }

    private String extractString(EditText view) {
        return view.getText().toString().trim();
    }

    private void enableBtn() {
        addLoading.setVisibility(View.GONE);
        addHouseholdBtn.setEnabled(true);
        addHouseholdBtn.setBackgroundResource(R.drawable.button_background);
    }

    private void disableBtn() {
        addHouseholdBtn.setEnabled(false);
        addLoading.setVisibility(View.VISIBLE);
        addHouseholdBtn.setBackgroundResource(R.drawable.inactive_button_bg);
    }

    @OnClick(R.id.add_household_btn)
    void saveHousehold() {
        disableBtn();
        User user = addFragmentViewModel.getUser(AppGlobals.logged_in_user_email);
        String uuid = user.getUuid();


        String mFatherName = extractString(fatherName);
        String mMotherName =  extractString(motherName);
        String mAddress = extractString(address);
        String mStreet = extractString(street);
        String mChildren = extractString(childrenNumber);
        String mDependants = extractString(dependantsNumber);
        String mOccupation = extractString(occupation);
        String mMaritalStatus = extractString(marital_status);
        String mWives = extractString(wives);
        String mLgaOrigin = extractString(lga_origin);
        String mStateOrigin = extractString(state_origin);

        if (mFatherName.isEmpty()) {
            fatherName.setError("Field required");
            fatherName.requestFocus();
            enableBtn();
        } else if (mMotherName.isEmpty()) {
            motherName.setError("Field required");
            motherName.requestFocus();
            enableBtn();
        } else if (mAddress.isEmpty()) {
            address.setError("Field required");
            address.requestFocus();
            enableBtn();
        } else if (mStreet.isEmpty()) {
            street.setError("Field required");
            street.requestFocus();
            enableBtn();
        } else if (mChildren.isEmpty()) {
            childrenNumber.setError("Field required");
            childrenNumber.requestFocus();
            enableBtn();
        } else if (mDependants.isEmpty()) {
            dependantsNumber.setError("Field required");
            dependantsNumber.requestFocus();
            enableBtn();
        } else if (mOccupation.isEmpty()) {
            occupation.setError("Field required");
            occupation.requestFocus();
            enableBtn();
        } else if (mMaritalStatus.isEmpty()) {
            marital_status.setError("Field required");
            marital_status.requestFocus();
            enableBtn();
        } else if (mWives.isEmpty()) {
            wives.setError("Field required");
            wives.requestFocus();
            enableBtn();
        } else if (mLgaOrigin.isEmpty()) {
            lga_origin.setError("Field required");
            lga_origin.requestFocus();
            enableBtn();
        } else if (mStateOrigin.isEmpty()) {
            state_origin.setError("Field required");
            state_origin.requestFocus();
            enableBtn();
        } else  if (imageURL.isEmpty() || imageURL == null) {
            Toast.makeText(requireActivity(), "No image chosen", Toast.LENGTH_SHORT).show();
            enableBtn();
        } else {

            household.setAgentId(AppGlobals.logged_in_user_email);
            household.setAddress(mAddress);
            household.setChildren_number(Integer.parseInt(mChildren));
            household.setDependants_number(Integer.parseInt(mDependants));
            household.setDate_of_capture(AppUtils.currentDate());
            household.setTime_of_capture(AppUtils.currentTime());
            household.setFather_name(mFatherName);
            household.setMother_name(mMotherName);
            household.setMarital_status(mMaritalStatus);
            household.setState_of_origin(mStateOrigin);
            household.setLga_of_origin(mLgaOrigin);
            household.setStreet(mStreet);
            household.setOccupation(mOccupation);
            household.setNumber_of_wives(mWives);
            household.setImage(imageURL);


            db.collection(AppGlobals.HOUSEHOLD_CAPTURES)
                    .add(household)
                    .addOnSuccessListener(documentReference -> {
                        openHome();
                        Toasty.success(requireActivity(), "Data saved", Toasty.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toasty.error(requireActivity(), "Data not saved", Toasty.LENGTH_SHORT).show();
                        crashlytics.recordException(e);
                        enableBtn();
                    });
        }
    }

    private void getOriginStateLgas(String state) {
        Log.d(TAG, "getStateLgas called .....: ");
        //Fetching states
        ApiService service = ApiClient.getApiService(ApiService.class);
        Call<List<String>> call = service.getStateLgas(state);

        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(@NotNull Call<List<String>> call, @NotNull Response<List<String>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        List<String> lgas = response.body();
                        ArrayAdapter lgaOriginAdapter = new ArrayAdapter<>(requireActivity(), R.layout.dropdown_pop_up_item, lgas);
                        lga_origin.setKeyListener(null);
                        lga_origin.setAdapter(lgaOriginAdapter);
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<String>> call, @NotNull Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                crashlytics.recordException(t);
            }
        });
    }

    private void selectImage() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, PICK_IMAGE_REQUEST);

//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("image/*");
//        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
//        startActivityForResult(intent.createChooser(intent,
//                "Choose image"), PICK_IMAGE_REQUEST);

    }

    private void saveImage(Uri imageURI) {
        final ProgressDialog progressDialog = new ProgressDialog(requireActivity());
        progressDialog.setMessage("Uploading....");
        progressDialog.show();

        final StorageReference ref = storageReference.child(imageURI.getLastPathSegment() + AppUtils.currentDate());

        ref.putFile(imageURI).addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    progressDialog.dismiss();
                    imageURL = uri.toString();
                    imageNamePath = taskSnapshot.getStorage().getPath();

                }))
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toasty.error(requireActivity(), "" + e.getMessage(), Toasty.LENGTH_SHORT).show();
                    crashlytics.recordException(e);
                })

                .addOnProgressListener(taskSnapshot -> {
                    double progress = (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploading " + progress + "%");

                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_TAKE_PHOTO) {

            if (resultCode == RESULT_OK) {
                File f = new File(currentPhotoPath);
                image.setImageURI(Uri.fromFile(f));

                saveImage(Uri.fromFile(f));

            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toasty.error(requireActivity(), "Camera errro", Toasty.LENGTH_LONG).show();
                crashlytics.recordException(ex);
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(requireActivity(),
                        "com.app.plyss.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

}
