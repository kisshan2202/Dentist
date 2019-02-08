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
import mu.astek.database.khadundentalcare.R;


public class AppointmentFragment extends Fragment implements CalendarDatePickerDialogFragment.OnDateSetListener {

    private Context context;
    RecyclerView recyclerView;
    DatabaseService databaseService;
    AppointmentRecyclerAdapter adapter;
    private LinearLayoutManager layoutManager;
    boolean fromCreateView = true;
    ImageView imgDate;
    Calendar calendar = Calendar.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_appointment, container, false);

        //recyclerView.setNestedScrollingEnabled(false);
        this.context = getActivity();
        this.databaseService = new DatabaseService(context);
        this.recyclerView = view.findViewById(R.id.recyclerview);
        int orientation = getLayoutManagerOrientation(getResources().getConfiguration().orientation);
        layoutManager = new LinearLayoutManager(context, orientation, false);
        FloatingActionButton fab = view.findViewById(R.id.fab);
        imgDate = view.findViewById(R.id.imgDate);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddAppointmentActivity.class));
            }
        });
        List<AppointmentDTO> list = databaseService.getAppointmentsByDate(new Date());
        if (!list.isEmpty()) {
            fromCreateView = false;
            adapter = new AppointmentRecyclerAdapter(context, list);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setDrawingCacheEnabled(true);
            recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        } else {
            Toast.makeText(getActivity(), "You have no appointments for this particular date", Toast.LENGTH_SHORT).show();

        }

        imgDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                        .setOnDateSetListener(AppointmentFragment.this)
                        .setDoneText("Ok")
                        .setCancelText("Cancel");

                cdp.show(getChildFragmentManager(), "Show date");
            }
        });


        return view;
    }


    @Override
    public void onResume() {
        super.onResume();


        List<AppointmentDTO> list = databaseService.getAppointmentsByDate(calendar.getTime());
        if (adapter != null) {
            adapter.setList(list);
            adapter.notifyDataSetChanged();
        } else {
            if (!list.isEmpty()) {
                adapter = new AppointmentRecyclerAdapter(context, list);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setDrawingCacheEnabled(true);
                recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
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

    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        calendar.set(year, monthOfYear, dayOfMonth);
        List<AppointmentDTO> list = databaseService.getAppointmentsByDate(calendar.getTime());
        if (list.isEmpty()) {
            Toast.makeText(getActivity(), "You have no appointments for this particular date", Toast.LENGTH_SHORT).show();
        }
        if (adapter != null) {
            adapter.setList(list);
            adapter.notifyDataSetChanged();
        } else {
            if (!list.isEmpty()) {
                adapter = new AppointmentRecyclerAdapter(context, list);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setDrawingCacheEnabled(true);
                recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
            }
        }

    }
}
