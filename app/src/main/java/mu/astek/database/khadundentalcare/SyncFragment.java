package mu.astek.database.khadundentalcare;


import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import mu.astek.database.khadundentalcare.DTO.AppointmentDTO;
import mu.astek.database.khadundentalcare.DTO.PatientDTO;
import mu.astek.database.khadundentalcare.Database.DatabaseService;
import mu.astek.database.khadundentalcare.Utils.TypeHelper;


public class SyncFragment extends Fragment {

    private Context context;
    DatabaseService databaseService;
    Button buttonSync;
    private int count = 0;
    int counter = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_sync, container, false);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        //recyclerView.setNestedScrollingEnabled(false);
        this.context = getActivity();
        this.databaseService = new DatabaseService(context);
        buttonSync = view.findViewById(R.id.buttonSync);
        buttonSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<PatientDTO> patientDTOS = databaseService.getOfflinePatientList();
                List<AppointmentDTO> list = databaseService.getOfflineAppointments();
                count = patientDTOS.size()+list.size();
                if(count == 0){
                    Toast.makeText(context,"You have no data to be synced",Toast.LENGTH_SHORT).show();
                }else if(!TypeHelper.isConnected(context)){
                    Toast.makeText(context,"No network connection available",Toast.LENGTH_SHORT).show();
                }else {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Patient");
                    if(!patientDTOS.isEmpty()){
                        for(final PatientDTO patient:patientDTOS){
                            reference.child(patient.getPatientId()+"").setValue(patient).
                                    addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            counter++;
                                            Toast.makeText(context,counter+"/"+count+" items synced",Toast.LENGTH_SHORT).show();
                                            patient.setSavedOffline(true);
                                            databaseService.updatePatient(patient);
                                        }
                                    });
                        }
                    }

                    if(!list.isEmpty()){
                        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Appointments");
                        for(final AppointmentDTO appointmentDTO:list){
                            databaseReference.child(appointmentDTO.getAppointmentID()+"").setValue(appointmentDTO).
                                    addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            counter++;
                                            Toast.makeText(context,counter+"/"+count+" items synced",Toast.LENGTH_SHORT).show();
                                            appointmentDTO.setSavedOffline(true);
                                            databaseService.updateAppointment(appointmentDTO);
                                        }
                                    });
                        }
                    }
                }


            }
        });


        return view;
    }


}
