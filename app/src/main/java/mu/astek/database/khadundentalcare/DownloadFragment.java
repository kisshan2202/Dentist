package mu.astek.database.khadundentalcare;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_dwl, container, false);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        //recyclerView.setNestedScrollingEnabled(false);
        this.context = getActivity();
        this.databaseService = new DatabaseService(context);
        buttonSync = view.findViewById(R.id.buttonSync);
        buttonSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(!TypeHelper.isConnected(context)){
                    Toast.makeText(context,"No network connection available",Toast.LENGTH_SHORT).show();
                }else {
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
                               Toast.makeText(context,"Patients Saved",Toast.LENGTH_SHORT).show();
                           }
                       }

                       @Override
                       public void onCancelled(DatabaseError databaseError) {

                       }
                   });

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
                               Toast.makeText(context,"Appointments Saved",Toast.LENGTH_SHORT).show();
                           }
                       }

                       @Override
                       public void onCancelled(DatabaseError databaseError) {

                       }
                   });

                }


            }
        });


        return view;
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
}
