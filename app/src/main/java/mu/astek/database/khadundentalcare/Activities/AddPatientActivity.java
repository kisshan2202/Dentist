package mu.astek.database.khadundentalcare.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
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

public class AddPatientActivity extends FragmentActivity implements CalendarDatePickerDialogFragment.OnDateSetListener {

    EditText fname, lname, address, phone;
    Spinner gender;
    Button btnSave;
    TextView dateOfBirth;
    Calendar now = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
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
        dateOfBirth = findViewById(R.id.txtDOB);
        gender = findViewById(R.id.spinnerGender);
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


        dateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                        .setOnDateSetListener(AddPatientActivity.this)
                        .setPreselectedDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH))
                        .setFirstDayOfWeek(Calendar.SUNDAY)
                        .setDoneText("Ok")
                        .setCancelText("Cancel");
                cdp.show(getSupportFragmentManager(), "Show");
            }
        });

        if (isEdit && patientDTO != null) {
            now.setTimeInMillis(patientDTO.getDateOfBirth());
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
        dateOfBirth.setText(dateFormat.format(now.getTime()));
    }

    private void updatePatient() {
        patientDTO.setFirstname(fname.getText().toString());
        patientDTO.setLastname(lname.getText().toString());
        patientDTO.setAddress(address.getText().toString());
        patientDTO.setPhone(phone.getText().toString());
        patientDTO.setGender(gender.getSelectedItem().toString());
        patientDTO.setDateOfBirth(now.getTimeInMillis());

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
        patientDTO.setDateOfBirth(now.getTimeInMillis());

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
        } else if (TextUtils.isEmpty(dateOfBirth.getText())) {
            dateOfBirth.setError(getText(R.string.error_empty_field));
            valid = false;
        } else if (!TextUtils.isEmpty(phone.getText()) && phone.getText().length() < 7) {
            dateOfBirth.setError(getText(R.string.invalide_phone_));
            valid = false;
        }

        return valid;
    }


    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        now.set(year, monthOfYear, dayOfMonth);
        dateOfBirth.setText(dateFormat.format(now.getTime()));
    }
}
