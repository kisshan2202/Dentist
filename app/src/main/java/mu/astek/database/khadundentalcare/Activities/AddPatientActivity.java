package mu.astek.database.khadundentalcare.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import mu.astek.database.khadundentalcare.DTO.PatientDTO;
import mu.astek.database.khadundentalcare.Database.DatabaseService;
import mu.astek.database.khadundentalcare.R;

public class AddPatientActivity extends AppCompatActivity {

    EditText fname, lname, address, phone,txtDOB;
    Spinner gender;
    Button btnSave;
    PatientDTO patientDTO;
    boolean isEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);

        patientDTO = (PatientDTO) getIntent().getSerializableExtra("patient");
        isEdit = getIntent().getBooleanExtra("edit", false);

        fname = findViewById(R.id.txtFname);
        lname = findViewById(R.id.txtLname);
        address = findViewById(R.id.txtAddress);
        phone = findViewById(R.id.txtPhone);
        gender = findViewById(R.id.spinnerGender);
        txtDOB = findViewById(R.id.txtDOB);
        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    if (isEdit) {
                        updatePatient();
                    } else {
                        savePatient();
                    }
                }
            }
        });

        String[] mTestArray = getResources().getStringArray(R.array.gender_arrray);
        ArrayAdapter adapter = new ArrayAdapter(AddPatientActivity.this,
                R.layout.spinner_item,
                mTestArray);

        gender.setAdapter(adapter);


        if(!isEdit){
            gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        if (isEdit && patientDTO != null) {
            txtDOB.setText(patientDTO.getAge()+"");

            fname.setText(patientDTO.getFirstname());
            lname.setText(patientDTO.getLastname());
            address.setText(patientDTO.getAddress());
            phone.setText(patientDTO.getPhone());
            if (patientDTO.getGender().equalsIgnoreCase("Male")) {
                gender.setSelection(0);
            } else {
                gender.setSelection(1);
            }


            btnSave.setText("Update Details");

            int pos = fname.getText().length();
            fname.setSelection(pos);

            int pos2 = address.getText().length();
            address.setSelection(pos2);

            int pos3 = lname.getText().length();
            lname.setSelection(pos3);

            int pos4 = phone.getText().length();
            phone.setSelection(pos4);
        }
    }

    private void updatePatient() {
        patientDTO.setFirstname(fname.getText().toString());
        patientDTO.setLastname(lname.getText().toString());
        patientDTO.setAddress(address.getText().toString());
        patientDTO.setPhone(phone.getText().toString());
        patientDTO.setGender(gender.getSelectedItem().toString());
        patientDTO.setAge(Integer.valueOf(txtDOB.getText().toString()));

        new DatabaseService(this).updatePatient(patientDTO);
        finish();
    }

    private void savePatient() {
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setFirstname(fname.getText().toString());
        patientDTO.setLastname(lname.getText().toString());
        patientDTO.setAddress(address.getText().toString());
        patientDTO.setPhone(phone.getText().toString());
        patientDTO.setGender(gender.getSelectedItem().toString());
        patientDTO.setAge(Integer.valueOf(txtDOB.getText().toString()));

        new DatabaseService(this).createPatient(patientDTO);
        finish();
    }

    private boolean validateFields() {
        Boolean valid = true;
        if (TextUtils.isEmpty(fname.getText())) {
            fname.setError(getText(R.string.error_empty_field));
            valid = false;
        } else if (TextUtils.isEmpty(lname.getText())) {
            lname.setError(getText(R.string.error_empty_field));
            valid = false;
        } else if (TextUtils.isEmpty(address.getText())) {
            address.setError(getText(R.string.error_empty_field));
            valid = false;
        } else if (TextUtils.isEmpty(phone.getText())) {
            phone.setError(getText(R.string.error_empty_field));
            valid = false;
        } else if (!TextUtils.isEmpty(phone.getText()) && phone.getText().length() < 7) {
            phone.setError(getText(R.string.invalide_phone_));
            valid = false;
        }
        else if (TextUtils.isEmpty(txtDOB.getText())) {
            txtDOB.setError(getText(R.string.error_empty_field));
            valid = false;
        }

        return valid;
    }



}
