package mu.astek.database.khadundentalcare.Activities;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import mu.astek.database.khadundentalcare.DTO.AppointmentDTO;
import mu.astek.database.khadundentalcare.DTO.PatientDTO;
import mu.astek.database.khadundentalcare.Database.DatabaseService;
import mu.astek.database.khadundentalcare.PatientRecyclerAdapter;
import mu.astek.database.khadundentalcare.R;


public class PatientFragment extends Fragment {

    private Context context;
    RecyclerView recyclerView;
    DatabaseService databaseService;
    PatientRecyclerAdapter adapter;
    private LinearLayoutManager layoutManager;
    boolean fromCreateView = true;
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<PatientDTO> stringArrayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_patient, container, false);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder(); StrictMode.setVmPolicy(builder.build());

        //recyclerView.setNestedScrollingEnabled(false);
        this.context = getActivity();
        this.databaseService = new DatabaseService(context);
        this.recyclerView = view.findViewById(R.id.recyclerview);
        int orientation = getLayoutManagerOrientation(getResources().getConfiguration().orientation);
        layoutManager = new LinearLayoutManager(context, orientation, false);
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.hide();
        autoCompleteTextView = view.findViewById(R.id.autoCompleteTextView);
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //add call to method here
                if (TextUtils.isEmpty(s)) {
                    List<PatientDTO> list = databaseService.getPatientList();
                    if (!list.isEmpty()) {
                        list = sortList(list);
                        stringArrayAdapter = getAdapter(list);
                        autoCompleteTextView.setThreshold(1);//will start working from first character
                        autoCompleteTextView.setAdapter(stringArrayAdapter);//setting the adapter data into the AutoCompleteTextView
                        autoCompleteTextView.setTextColor(Color.BLUE);
                        if (adapter == null) {
                            adapter = new PatientRecyclerAdapter(PatientFragment.this, list);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setDrawingCacheEnabled(true);
                            recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                        } else {
                            adapter.setList(list);
                            adapter.notifyDataSetChanged();
                        }

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
                PatientDTO str = stringArrayAdapter.getItem(position);
                List<PatientDTO> list = new ArrayList<>();
                list.add(str);
                if (adapter == null) {
                    adapter = new PatientRecyclerAdapter(PatientFragment.this, list);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setDrawingCacheEnabled(true);
                    recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                } else {
                    adapter.setList(list);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        List<PatientDTO> list = databaseService.getPatientList();
        if (!list.isEmpty()) {
            list = sortList(list);
            stringArrayAdapter = getAdapter(list);
            autoCompleteTextView.setThreshold(1);//will start working from first character
            autoCompleteTextView.setAdapter(stringArrayAdapter);//setting the adapter data into the AutoCompleteTextView
            autoCompleteTextView.setTextColor(Color.BLUE);
            fromCreateView = false;
            adapter = new PatientRecyclerAdapter(this, list);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setDrawingCacheEnabled(true);
            recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);


        }

        return view;
    }

    private ArrayAdapter<PatientDTO> getAdapter(List<PatientDTO> list) {


        return new ArrayAdapter<>
                (getContext(), android.R.layout.select_dialog_item, list);

    }

    private List<PatientDTO> sortList(List<PatientDTO> list) {
        Collections.sort(list, new Comparator<PatientDTO>() {
            public int compare(PatientDTO t, PatientDTO t1) {
                return t.getFirstname().compareTo(t1.getFirstname());
            }
        });

        return list;
    }


    @Override
    public void onResume() {
        super.onResume();

        if (!fromCreateView) {
            autoCompleteTextView.setText("");
            List<PatientDTO> list = databaseService.getPatientList();
            list = sortList(list);
            stringArrayAdapter = getAdapter(list);
            autoCompleteTextView.setThreshold(1);//will start working from first character
            autoCompleteTextView.setAdapter(stringArrayAdapter);//setting the adapter data into the AutoCompleteTextView
            autoCompleteTextView.setTextColor(Color.BLUE);
            adapter.setList(list);
            adapter.notifyDataSetChanged();
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

    public void openfragment(List<AppointmentDTO> appointmentDTOS, Integer patientId) {
        Fragment fragment = new PatientDetailsAppointmentFragment();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();

        Bundle bundle = new Bundle();
        bundle.putInt("Id", patientId);
        bundle.putSerializable("list", (Serializable) appointmentDTOS);
        fragment.setArguments(bundle);
    }
}
