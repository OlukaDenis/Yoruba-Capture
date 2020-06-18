package com.dennytech.datacapture.ui.add_form;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.dennytech.datacapture.R;
import com.dennytech.datacapture.data.local.LocalDataSource;
import com.dennytech.datacapture.data.model.Form;
import com.dennytech.datacapture.utils.AppGlobals;
import com.dennytech.datacapture.utils.AppUtils;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static com.dennytech.datacapture.utils.AppGlobals.PICK_IMAGE_REQUEST;

public class AddFormFragment extends Fragment {
    private static final String TAG = "AddFormFragment";
    private EditText fname;
    private EditText lname;
    private EditText phone;
    private EditText address;
    private AutoCompleteTextView marital_status;
    private AutoCompleteTextView state_origin;
    private AutoCompleteTextView state_residence;
    private EditText email;
    private AutoCompleteTextView gender;
    private EditText lga_origin;
    private EditText lga_residence;
    private EditText vin;
    private EditText occupation;
    private EditText date_of_birth;
    private ProgressBar loading_add;
    private ImageView image;
    private ImageButton add_image;
    private Uri filePath;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    @BindView(R.id.add_form_data_btn)
    Button add_form_data_btn;

    private AddFragmentViewModel addFragmentViewModel;
    private FirebaseAnalytics mFirebaseAnalytics;
    private NavController navController;

    private Form form;

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


        ArrayAdapter originAdapter = new ArrayAdapter<>(requireActivity(), R.layout.dropdown_pop_up_item, LocalDataSource.ALL_STATES);
        state_origin.setAdapter(originAdapter);
        state_origin.setKeyListener(null);

        ArrayAdapter residenceAdapter = new ArrayAdapter<>(requireActivity(), R.layout.dropdown_pop_up_item, LocalDataSource.ALL_STATES);
        state_residence.setKeyListener(null);
        state_residence.setAdapter(residenceAdapter);



        ArrayAdapter genderAdapter = new ArrayAdapter<>(requireActivity(), R.layout.dropdown_pop_up_item, LocalDataSource.GENDER);
        gender.setKeyListener(null);
        gender.setAdapter(genderAdapter);

        ArrayAdapter maritalAdapter = new ArrayAdapter<>(requireActivity(), R.layout.dropdown_pop_up_item, LocalDataSource.GENDER);
        marital_status.setKeyListener(null);
        marital_status.setAdapter(maritalAdapter);

        add_image.setOnClickListener(v -> selectImage());

        return root;
    }

    private void selectImage() {
        Intent imageIntent = new Intent();
        imageIntent.setAction(Intent.ACTION_GET_CONTENT);
        imageIntent.setType("image/*");
        requireActivity().startActivityForResult(Intent.createChooser(imageIntent, "Select image"), PICK_IMAGE_REQUEST);
    }

    private void openHome() {
        navController.navigate(R.id.navigation_home);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Go back home");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);
    }

    @OnClick(R.id.add_form_data_btn)
    void sendFormData() {

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

            addFragmentViewModel.saveFormData(form);

            openHome();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            filePath = data.getData();
            image.setImageURI(filePath);
        }
    }

    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}