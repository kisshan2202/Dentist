package mu.astek.database.khadundentalcare.Activities;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mu.astek.database.khadundentalcare.AppointmentRecyclerAdapter;
import mu.astek.database.khadundentalcare.DTO.AppointmentDTO;
import mu.astek.database.khadundentalcare.Database.DatabaseService;
import mu.astek.database.khadundentalcare.PatientAppointmentDetailsRecyclerAdapter;
import mu.astek.database.khadundentalcare.R;


public class PatientDetailsAppointmentFragment extends Fragment  {

    private Context context;
    RecyclerView recyclerView;
    DatabaseService databaseService;
    PatientAppointmentDetailsRecyclerAdapter adapter;
    private LinearLayoutManager layoutManager;
    boolean fromCreateView = true;
    List<AppointmentDTO> list;
    Integer patientId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_patient_details, container, false);

        //recyclerView.setNestedScrollingEnabled(false);
        this.context = getActivity();
        this.databaseService = new DatabaseService(context);
        this.recyclerView = view.findViewById(R.id.recyclerview);
        int orientation = getLayoutManagerOrientation(getResources().getConfiguration().orientation);
        layoutManager = new LinearLayoutManager(context, orientation, false);


        final Bundle bundle = this.getArguments();
        if (bundle != null) {
            list  = (List<AppointmentDTO>) bundle.getSerializable("list");
            patientId  = bundle.getInt("Id");
            if (list !=null && !list.isEmpty()) {
                fromCreateView = false;
                adapter = new PatientAppointmentDetailsRecyclerAdapter(context, list);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setDrawingCacheEnabled(true);
                recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

            } else {
                Toast.makeText(getActivity(), "You have no appointments for this particular date", Toast.LENGTH_SHORT).show();

            }
        }




        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        if(!fromCreateView){
            list  = databaseService.getAppointmentListByPatientID(patientId);
            if(!list.isEmpty()){
                adapter.setList(list);
                adapter.notifyDataSetChanged();
            }
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    private int getLayoutManagerOrientation(int activityOrientation) {
        if (activityOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            return LinearLayoutManager.VERTICAL;
        } else {
            return LinearLayoutManager.HORIZONTAL;
        }
    }
}
