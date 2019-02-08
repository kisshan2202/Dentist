package mu.astek.database.khadundentalcare.Activities;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import mu.astek.database.khadundentalcare.DTO.AppointmentDTO;
import mu.astek.database.khadundentalcare.DTO.TreatmentDTO;
import mu.astek.database.khadundentalcare.Database.DatabaseService;
import mu.astek.database.khadundentalcare.R;

public class AddTreatmentActivity extends AppCompatActivity {

    AppointmentDTO appointment;
    EditText txtDetails, txtFees;
    Button btnSave;
    Boolean isEdit = false;
    DatabaseService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_treatment);
        service = new DatabaseService(this);
        btnSave = findViewById(R.id.btnSave);
        txtDetails = findViewById(R.id.txtDetails);
        txtFees = findViewById(R.id.txtFees);

        appointment = (AppointmentDTO) getIntent().getSerializableExtra("appointment");
        isEdit = getIntent().getBooleanExtra("edit", false);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEdit) {
                    String comment = txtDetails.getText().toString();
                    Integer fee = Integer.valueOf(txtFees.getText().toString());
                    TreatmentDTO treatmentDTO = appointment.getTreatment();
                    treatmentDTO.setFees(fee);
                    treatmentDTO.setDetails(comment);
                    service.updateTreatment(treatmentDTO);
                    finish();
                } else {
                    if (!TextUtils.isEmpty(txtDetails.getText()) && !TextUtils.isEmpty(txtFees.getText())) {
                        String comment = txtDetails.getText().toString();
                        Integer fee = Integer.valueOf(txtFees.getText().toString());
                        TreatmentDTO treatmentDTO = new TreatmentDTO();
                        treatmentDTO.setId(appointment.getAppointmentID());
                        treatmentDTO.setFees(fee);
                        treatmentDTO.setDetails(comment);
                        service.createTreatment(treatmentDTO);
                        finish();
                    }
                }
            }
        });

        if (isEdit && appointment.getTreatment() != null) {
            txtDetails.setText(appointment.getTreatment().getDetails());
            txtFees.setText(appointment.getTreatment().getFees() + "");
            btnSave.setText("Update Treatment");
            int pos = txtDetails.getText().length();
            txtDetails.setSelection(pos);

            int pos2 = txtFees.getText().length();
            txtFees.setSelection(pos2);
        }
    }
}
