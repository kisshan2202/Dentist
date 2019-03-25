package mu.astek.database.khadundentalcare;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import mu.astek.database.khadundentalcare.DTO.AppointmentDTO;
import mu.astek.database.khadundentalcare.DTO.PatientDTO;
import mu.astek.database.khadundentalcare.Database.DatabaseService;
import mu.astek.database.khadundentalcare.Utils.TypeHelper;


public class DownloadFragment extends Fragment {

    private Context context;
    DatabaseService databaseService;
    Button buttonSync;
    ValueEventListener valueEventListener;
    DatabaseReference reference;

    ValueEventListener valueEventListener2;
    DatabaseReference reference2;
    int totalPatient = 0;
    int appointments = 0;
    ProgressBar progressBar;
    Boolean hascompletedPatient = false;
    Boolean hascompletedAppointment = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_dwl, container, false);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        //recyclerView.setNestedScrollingEnabled(false);
        this.context = getActivity();
        this.databaseService = new DatabaseService(context);
        buttonSync = view.findViewById(R.id.buttonSync);
        progressBar = view.findViewById(R.id.progressBar);
        buttonSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(!TypeHelper.isConnected(context)){
                    Toast.makeText(context,"No network connection available",Toast.LENGTH_SHORT).show();
                }else {
                   openPopup();
                }


            }
        });


        return view;
    }

    private void openPopup() {
        final Dialog dialog = new Dialog(getActivity(), R.style.WalkthroughTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(false);

        dialog.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                }
                return true;
            }
        });

        final EditText txtPassword;
        txtPassword = dialog.findViewById(R.id.txtPassword);

        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnContinue = dialog.findViewById(R.id.btnContinue);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(txtPassword.getText()) || !TextUtils.equals(txtPassword.getText(),"lallmatie")){
                    txtPassword.setError("Invalid Password");
                }else{
                    downloadData();
                    dialog.dismiss();
                }
            }
        });


        if (!getActivity().isFinishing()) {
            dialog.show();
        }
    }

    private void handleProgressBar() {
        if(hascompletedAppointment && hascompletedPatient){
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(context,"Data has been downloaded from server",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStop() {
        try{
            reference.removeEventListener(valueEventListener);
            reference2.removeEventListener(valueEventListener2);
        }catch (Exception ex){

        }
        super.onStop();

    }

    public void downloadData(){
        progressBar.setVisibility(View.VISIBLE);
        databaseService.deleteAllData();
        reference  = FirebaseDatabase.getInstance().getReference("Patient");
        valueEventListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                int x = (int)snapshot.getChildrenCount();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    PatientDTO patientDTO = dataSnapshot.getValue(PatientDTO.class);
                    databaseService.createPatient(patientDTO);
                    totalPatient++;
                }


                if(totalPatient == x){
                    hascompletedPatient = true;
                    handleProgressBar();

                    reference2  = FirebaseDatabase.getInstance().getReference("Appointments");
                    valueEventListener2 = reference2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            int x = (int)snapshot.getChildrenCount();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                AppointmentDTO appointmentDTO = dataSnapshot.getValue(AppointmentDTO.class);
                                databaseService.createAppointment(appointmentDTO);

                                if(appointmentDTO.getTreatment()!= null){
                                    databaseService.createTreatment(appointmentDTO.getTreatment());
                                }
                                appointments++;
                            }


                            if(appointments == x){
                                hascompletedAppointment = true;
                                handleProgressBar();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(context,"An error has occured, please try again!",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(context,"An error has occured, please try again!",Toast.LENGTH_LONG).show();
            }
        });


    }
}
