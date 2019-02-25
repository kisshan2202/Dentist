package mu.astek.database.khadundentalcare.Activities;


import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;


import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import mu.astek.database.khadundentalcare.DTO.AppointmentDTO;
import mu.astek.database.khadundentalcare.DTO.PatientDTO;
import mu.astek.database.khadundentalcare.Database.DatabaseService;
import mu.astek.database.khadundentalcare.R;

public class AddAppointmentActivity extends AppCompatActivity implements CalendarDatePickerDialogFragment.OnDateSetListener, RadialTimePickerDialogFragment.OnTimeSetListener {

    Spinner spinnerPatient;
    DatabaseService service;
    List<PatientDTO> list;
    Calendar now = Calendar.getInstance();
    TextInputEditText txtDate,txtTime;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    Button btnSave;
    LinearLayout linearNewPatient;
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
                        .setPreselectedDate(now.get(Calendar.YEAR),now.get(Calendar.MONTH),now.get(Calendar.DAY_OF_MONTH))
                        .setDoneText("Ok")
                        .setCancelText("Cancel");
                cdp.show(getSupportFragmentManager(),"Show date" );
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
                if(!list.isEmpty()){
                   int x =  spinnerPatient.getSelectedItemPosition();
                    PatientDTO patientDTO = list.get(x);
                    AppointmentDTO appointmentDTO = new AppointmentDTO();
                    appointmentDTO.setPatientDTO(patientDTO);
                    appointmentDTO.setDate(now.getTimeInMillis());
                    new DatabaseService(AddAppointmentActivity.this).createAppointment(appointmentDTO);
                    finish();
                }
            }
        });
    }

    private String[] getPatient(List<PatientDTO> list) {
        String[] array = new String[list.size()];
        int index = 0;
        for(PatientDTO patientDTO:list){
            String val = patientDTO.getFirstname()+ " "+patientDTO.getLastname();
            array[index] = val;
            index++;
        }

        return array;
    }

    @Override
    protected void onResume() {
        super.onResume();
        list = service.getPatientList();
        if(!list.isEmpty()){
            ArrayAdapter adapter = new ArrayAdapter(AddAppointmentActivity.this,
                    android.R.layout.simple_spinner_item,
                    getPatient(list));

            spinnerPatient.setAdapter(adapter);
        }
    }

    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        now.set(year,monthOfYear,dayOfMonth);
        txtDate.setText(dateFormat.format(now.getTime()));
    }


    @Override
    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
        now.set(Calendar.HOUR_OF_DAY,hourOfDay);
        now.set(Calendar.MINUTE,minute);

        txtTime.setText(timeFormat.format(now.getTime()));
    }
}
