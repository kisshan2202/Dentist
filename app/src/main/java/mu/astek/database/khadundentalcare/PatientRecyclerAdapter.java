package mu.astek.database.khadundentalcare;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.List;

import mu.astek.database.khadundentalcare.Activities.AddPatientActivity;
import mu.astek.database.khadundentalcare.Activities.PatientFragment;
import mu.astek.database.khadundentalcare.DTO.AppointmentDTO;
import mu.astek.database.khadundentalcare.DTO.PatientDTO;
import mu.astek.database.khadundentalcare.Database.DatabaseService;
import mu.astek.database.khadundentalcare.Utils.TypeHelper;


public class PatientRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<PatientDTO> list;
    Context context;
    PatientFragment patientFragment;
    DatabaseService databaseService;
    SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy HH:mm");

    public PatientRecyclerAdapter(PatientFragment patientFragment, List<PatientDTO> taskList) {
        this.context = patientFragment.getContext();
        this.databaseService = new DatabaseService(context);
        this.patientFragment  = patientFragment;
        this.list = taskList;


    }

    public List<PatientDTO> getList() {
        return list;
    }

    public void setList(List<PatientDTO> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewholder_patient, parent, false);
        return new PatientViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder myView, final int position) {
        final PatientDTO patientDTO = list.get(position);
        final PatientViewHolder holder = (PatientViewHolder) myView;
        if (patientDTO != null ) {
            int age = patientDTO.getAge();
            holder.txtAge.setText("" + age);
            holder.txtUsername.setText(patientDTO.getFirstname() + " " + patientDTO.getLastname());
            holder.txtPhone.setText(patientDTO.getPhone()+"");
            holder.txtAddress.setText(patientDTO.getAddress());
        }


        holder.btnEditPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddPatientActivity.class);
                intent.putExtra("patient",patientDTO);
                intent.putExtra("edit",true);
                context.startActivity(intent);

            }
        });

        holder.btnPatientHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               List<AppointmentDTO > appointmentDTOS = databaseService.getAppointmentListByPatientID(patientDTO.getPatientId());
               if(!appointmentDTOS.isEmpty()){
                   patientFragment.openfragment(appointmentDTOS,patientDTO.getPatientId());
               }else {
                   Toast.makeText(context,"This user has no Appointments",Toast.LENGTH_SHORT).show();
               }
            }
        });


    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            super(v);
        }
    }


    public class PatientViewHolder extends PatientRecyclerAdapter.ViewHolder {
        TextView txtUsername, txtAddress, txtAge, txtPhone, txtFees;
        Button btnEditPatient, btnPatientHistory;


        public PatientViewHolder(View v) {
            super(v);
            txtUsername = v.findViewById(R.id.txtUsername);
            txtAddress = v.findViewById(R.id.txtAddress);
            txtAge = v.findViewById(R.id.txtAge);
            txtPhone = v.findViewById(R.id.txtPhone);

            txtFees = v.findViewById(R.id.txtFees);
            btnEditPatient = v.findViewById(R.id.btnEditPatient);
            btnPatientHistory = v.findViewById(R.id.btnPatientHistory);


        }
    }


}