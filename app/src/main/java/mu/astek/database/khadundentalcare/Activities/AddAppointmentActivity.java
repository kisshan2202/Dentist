package mu.astek.database.khadundentalcare.Activities;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import mu.astek.database.khadundentalcare.DTO.AppointmentDTO;
import mu.astek.database.khadundentalcare.DTO.PatientDTO;
import mu.astek.database.khadundentalcare.Database.DatabaseService;
import mu.astek.database.khadundentalcare.PatientRecyclerAdapter;
import mu.astek.database.khadundentalcare.R;

public class AddAppointmentActivity extends AppCompatActivity implements CalendarDatePickerDialogFragment.OnDateSetListener, RadialTimePickerDialogFragment.OnTimeSetListener {

    Spinner spinnerPatient;
    DatabaseService service;
    List<PatientDTO> list;
    List<PatientDTO> list2;
    Calendar now = Calendar.getInstance();
    TextInputEditText txtDate, txtTime;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    Button btnSave;
    LinearLayout linearNewPatient;
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<PatientDTO> stringArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);
        spinnerPatient = findViewById(R.id.spinnerPatient);
        service = new DatabaseService(this);

        txtDate = findViewById(R.id.txtDate);
        txtTime = findViewById(R.id.txtTime);
        btnSave = findViewById(R.id.btnSave);
        linearNewPatient = findViewById(R.id.linearNewPatient);
        txtTime.setText(timeFormat.format(now.getTime()));
        txtDate.setText(dateFormat.format(now.getTime()));
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                        .setOnDateSetListener(AddAppointmentActivity.this)
                        .setPreselectedDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH))
                        .setDoneText("Ok")
                        .setCancelText("Cancel");
                cdp.show(getSupportFragmentManager(), "Show date");
            }
        });
        txtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                        .setOnTimeSetListener(AddAppointmentActivity.this)
                        .setStartTime(now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE))
                        .setDoneText("Ok")
                        .setCancelText("Cancel");

                rtpd.show(getSupportFragmentManager(), "Show time");
            }
        });

        linearNewPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddAppointmentActivity.this, AddPatientActivity.class));
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!list.isEmpty()) {
                    int x = spinnerPatient.getSelectedItemPosition();
                    PatientDTO patientDTO = list.get(x);
                    AppointmentDTO appointmentDTO = new AppointmentDTO();
                    appointmentDTO.setPatientDTO(patientDTO);
                    appointmentDTO.setDate(now.getTimeInMillis());
                    appointmentDTO.setSavedOffline(false);
                    new DatabaseService(AddAppointmentActivity.this).createAppointment(appointmentDTO);
                    finish();
                }
            }
        });


    }

    private String[] getPatient(List<PatientDTO> list) {
        String[] array = new String[list.size()];
        int index = 0;
        for (PatientDTO patientDTO : list) {
            String val = patientDTO.getFirstname() + " " + patientDTO.getLastname();
            array[index] = val;
            index++;
        }

        return array;
    }

    @Override
    protected void onResume() {
        super.onResume();
        list = service.getPatientList();
        list2 = service.getPatientList();
        if (!list.isEmpty()) {
            ArrayAdapter adapter = new ArrayAdapter(AddAppointmentActivity.this,
                    R.layout.spinner_item,
                    getPatient(list));

            spinnerPatient.setAdapter(adapter);
        }

        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        if (!list2.isEmpty()) {
            list2 = sortList(list2);
            stringArrayAdapter = getAdapter(list);
            autoCompleteTextView.setThreshold(1);//will start working from first character
            autoCompleteTextView.setAdapter(stringArrayAdapter);//setting the adapter data into the AutoCompleteTextView
            autoCompleteTextView.setTextColor(Color.BLACK);

        }

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //add call to method here
                if (TextUtils.isEmpty(s)) {
                    if (!list2.isEmpty()) {
                        list2 = sortList(list2);
                        stringArrayAdapter = getAdapter(list);
                        autoCompleteTextView.setThreshold(1);//will start working from first character
                        autoCompleteTextView.setAdapter(stringArrayAdapter);//setting the adapter data into the AutoCompleteTextView
                        autoCompleteTextView.setTextColor(Color.BLACK);

                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int
                    after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(parent.getApplicationWindowToken(), 0);

                PatientDTO str = stringArrayAdapter.getItem(position);
                final int post = getPosition(str);
                if(post > -1){
                    spinnerPatient.post(new Runnable() {
                        @Override
                        public void run() {
                            spinnerPatient.setSelection(post,true);
                        }
                    });

                }

            }
        });

    }

    private int getPosition(PatientDTO patientDTO) {
        int x = -1;
        int index = 0;
        for(PatientDTO patient: list){
            if(patient.getFirstname().equalsIgnoreCase(patientDTO.getFirstname()) &&
                    patient.getLastname().equalsIgnoreCase(patientDTO.getLastname())){
                x = index;
            }
            index++;
        }

        return x;
    }

    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        now.set(year, monthOfYear, dayOfMonth);
        txtDate.setText(dateFormat.format(now.getTime()));
    }


    @Override
    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
        now.set(Calendar.HOUR_OF_DAY, hourOfDay);
        now.set(Calendar.MINUTE, minute);

        txtTime.setText(timeFormat.format(now.getTime()));
    }

    private List<PatientDTO> sortList(List<PatientDTO> list) {
        Collections.sort(list, new Comparator<PatientDTO>() {
            public int compare(PatientDTO t, PatientDTO t1) {
                return t.getFirstname().compareTo(t1.getFirstname());
            }
        });

        return list;
    }


    private ArrayAdapter<PatientDTO> getAdapter(List<PatientDTO> list) {
        return new ArrayAdapter<>
                (this, android.R.layout.select_dialog_item, list);

    }
}
