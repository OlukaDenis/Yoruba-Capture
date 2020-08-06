package com.app.plyss.ui.add_form;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.app.plyss.R;
import com.app.plyss.data.api.ApiClient;
import com.app.plyss.data.api.ApiService;
import com.app.plyss.data.local.LocalDataSource;
import com.app.plyss.data.model.Form;
import com.app.plyss.data.model.User;
import com.app.plyss.utils.AppGlobals;
import com.app.plyss.utils.AppUtils;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.app.plyss.utils.AppGlobals.PICK_IMAGE_REQUEST;

public class AddFormFragment extends Fragment {
    private static final String TAG = "AddFormFragment";
    private static final int REQUEST_TAKE_PHOTO = 105;
    private EditText fname;
    private EditText lname;
    private EditText phone;
    private EditText address;
    private AutoCompleteTextView marital_status;
    private AutoCompleteTextView state_origin;
    private AutoCompleteTextView state_residence;
    private EditText email;
    private AutoCompleteTextView gender;
    private AutoCompleteTextView lga_origin;
    private AutoCompleteTextView lga_residence;
    private EditText vin;
    private EditText occupation;
    private TextInputLayout dobLayout;
    private EditText date_of_birth;
    private ProgressBar loading_add;
    private ImageView image;
    private ImageButton add_image;

    private String imageURL;
    private String imageNamePath;
    private String currentPhotoPath;
    private DatePickerDialog picker;


    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseCrashlytics crashlytics;
    private FirebaseFirestore db;

    @BindView(R.id.add_form_data_btn)
    Button add_form_data_btn;

    private AddFragmentViewModel addFragmentViewModel;
    private FirebaseAnalytics mFirebaseAnalytics;
    private NavController navController;

    private Form form;

    private List<String> stateOriginLgas = new ArrayList<>();
    private List<String> stateResidenceLgas = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_add_form, container, false);

        AddFragmentViewModelFactory factory = new AddFragmentViewModelFactory(requireActivity().getApplication());
        addFragmentViewModel = new ViewModelProvider(this, factory).get(AddFragmentViewModel.class);
        ButterKnife.bind(this, root);

        //Init firebase analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity());
        mFirebaseAnalytics.setCurrentScreen(requireActivity(), this.getClass().getSimpleName(),
                this.getClass().getSimpleName());
        crashlytics = FirebaseCrashlytics.getInstance();
        db = FirebaseFirestore.getInstance();

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        form = new Form();

        //views
        fname = root.findViewById(R.id.form_fname);
        lname = root.findViewById(R.id.form_lname);
        email = root.findViewById(R.id.form_email);
        phone = root.findViewById(R.id.form_phone);
        gender = root.findViewById(R.id.form_gender);
        vin = root.findViewById(R.id.form_vin);
        occupation = root.findViewById(R.id.form_occupation);
        address = root.findViewById(R.id.form_address);
        date_of_birth = root.findViewById(R.id.form_dob);
        marital_status = root.findViewById(R.id.form_marital_status);
        state_origin = root.findViewById(R.id.form_state_origin);
        state_residence = root.findViewById(R.id.form_state_residence);
        lga_origin = root.findViewById(R.id.form_lga_origin);
        lga_residence = root.findViewById(R.id.form_lga_residence);
        loading_add = root.findViewById(R.id.loading_add);
        image = root.findViewById(R.id.data_image);
        add_image = root.findViewById(R.id.add_image_btn);

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);


        date_of_birth.setInputType(InputType.TYPE_NULL);
        date_of_birth.setOnClickListener(v -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            // date picker dialog
            picker = new DatePickerDialog(requireActivity(),
                    (view, year1, monthOfYear, dayOfMonth) -> date_of_birth.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year1), year, month, day);
            picker.show();
        });

        ArrayAdapter originAdapter = new ArrayAdapter<>(requireActivity(), R.layout.dropdown_pop_up_item, LocalDataSource.getStates());
        state_origin.setAdapter(originAdapter);
        state_origin.setKeyListener(null);
        state_origin.setOnItemClickListener((parent, view, position, id) -> {
            Object obj = parent.getItemAtPosition(position);
            getOriginStateLgas(obj.toString());
        });

        ArrayAdapter residenceAdapter = new ArrayAdapter<>(requireActivity(), R.layout.dropdown_pop_up_item, LocalDataSource.getStates());
        state_residence.setKeyListener(null);
        state_residence.setAdapter(residenceAdapter);
        state_residence.setOnItemClickListener((parent, view, position, id) -> {
            Object obj = parent.getItemAtPosition(position);
            getResidenceStateLgas(obj.toString());
        });

        ArrayAdapter genderAdapter = new ArrayAdapter<>(requireActivity(), R.layout.dropdown_pop_up_item, LocalDataSource.GENDER);
        gender.setKeyListener(null);
        gender.setAdapter(genderAdapter);

        ArrayAdapter maritalAdapter = new ArrayAdapter<>(requireActivity(), R.layout.dropdown_pop_up_item, LocalDataSource.MARITAL_STATUS);
        marital_status.setKeyListener(null);
        marital_status.setAdapter(maritalAdapter);

        add_image.setOnClickListener(v -> dispatchTakePictureIntent());

        return root;
    }

    private void getOriginStateLgas(String state) {
        ArrayAdapter lgaOriginAdapter = new ArrayAdapter<>(requireActivity(), R.layout.dropdown_pop_up_item, LocalDataSource.getLGAS(state));
        lga_origin.setKeyListener(null);
        lga_origin.setAdapter(lgaOriginAdapter);
    }

    private void getResidenceStateLgas(String state) {
        ArrayAdapter lgaOriginAdapter = new ArrayAdapter<>(requireActivity(), R.layout.dropdown_pop_up_item, LocalDataSource.getLGAS(state));
        lga_residence.setKeyListener(null);
        lga_residence.setAdapter(lgaOriginAdapter);
    }

    private void openHome() {
        navController.navigate(R.id.navigation_home);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Go back home");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);
    }

    @OnClick(R.id.add_form_data_btn)
    void sendFormData() {

        User user = addFragmentViewModel.getUser(AppGlobals.logged_in_user_email);
        String uuid = user.getUuid();

        String m_fname = extractString(fname);
        String m_lname = extractString(lname);
        String m_email = extractString(email);
        String m_phone = extractString(phone);
        String m_gender = extractString(gender);
        String m_vin = extractString(vin);
        String m_occupation = extractString(occupation);
        String m_address = extractString(address);
        String m_dob = extractString(date_of_birth);
        String m_marital = extractString(marital_status);
        String m_state_origin = extractString(state_origin);
        String m_state_reside = extractString(state_residence);
        String m_lga_origin = extractString(lga_origin);
        String m_lga_residence = extractString(lga_residence);

        if (m_fname.isEmpty()) {
            fname.setError("Field required");
            fname.requestFocus();
        } else if (m_lname.isEmpty()) {
            lname.setError("Field required");
            lname.requestFocus();
        } else if (m_dob.isEmpty()) {
            date_of_birth.setError("Field required");
            date_of_birth.requestFocus();
        }  else if (m_state_origin.isEmpty()) {
            state_origin.setError("Field required");
            state_origin.requestFocus();
        } else if (m_state_reside.isEmpty()) {
            state_residence.setError("Field required");
            state_residence.requestFocus();
        } else if (m_lga_origin.isEmpty()) {
            lga_origin.setError("Field required");
            lga_origin.requestFocus();
        } else if (m_lga_residence.isEmpty()) {
            lga_residence.setError("Field required");
            lga_residence.requestFocus();
        } else if (m_vin.isEmpty()) {
            vin.setError("Field required");
            vin.requestFocus();
        } else if (m_address.isEmpty()) {
            address.setError("Field required");
            address.requestFocus();
        } else if (m_phone.isEmpty()) {
            phone.setError("Field required");
            phone.requestFocus();
        } else if (m_gender.isEmpty()) {
            gender.setError("Field required");
            gender.requestFocus();
        } else if (m_email.isEmpty()) {
            email.setError("Field required");
            email.requestFocus();
        } else if (m_occupation.isEmpty()) {
            occupation.setError("Field required");
            occupation.requestFocus();
        } else if (m_marital.isEmpty()) {
            marital_status.setError("Field required");
            marital_status.requestFocus();
        } else  if (imageURL.isEmpty() || imageURL == null){
            Toast.makeText(requireActivity(), "No image chosen", Toast.LENGTH_SHORT).show();
        } else {
            disableBtn();

            form.setFirst_name(m_fname);
            form.setLast_name(m_lname);
            form.setAddress(m_address);
            form.setEmail(m_email);
            form.setAgentId(AppGlobals.logged_in_user_email);
            form.setDate_of_birth(m_dob);
            form.setGender(m_gender);
            form.setMarital_status(m_marital);
            form.setPhone(m_phone);
            form.setVoters_reg_number(m_vin);
            form.setState_of_origin(m_state_origin);
            form.setState_of_residence(m_state_reside);
            form.setLga_of_origin(m_lga_origin);
            form.setLga_of_residence(m_lga_residence);
            form.setOccupation(m_occupation);
            form.setDate_of_capture(AppUtils.currentDate());
            form.setTime_of_capture(AppUtils.currentTime());
            form.setImage(imageURL);

            db.collection(AppGlobals.INDIVIDUAL_CAPTURES)
                    .add(form)
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

    private String extractString(EditText view) {
        return view.getText().toString().trim();
    }

    private void enableBtn() {
        loading_add.setVisibility(View.GONE);
        add_form_data_btn.setEnabled(true);
        add_form_data_btn.setBackgroundResource(R.drawable.button_background);
    }

    private void disableBtn() {
        add_form_data_btn.setEnabled(false);
        loading_add.setVisibility(View.VISIBLE);
        add_form_data_btn.setBackgroundResource(R.drawable.inactive_button_bg);
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
                        Toast.makeText(requireActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
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