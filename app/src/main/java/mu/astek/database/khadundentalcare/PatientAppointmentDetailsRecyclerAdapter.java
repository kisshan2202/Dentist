package mu.astek.database.khadundentalcare;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import mu.astek.database.khadundentalcare.Activities.AddTreatmentActivity;
import mu.astek.database.khadundentalcare.DTO.AppointmentDTO;
import mu.astek.database.khadundentalcare.DTO.PatientDTO;
import mu.astek.database.khadundentalcare.Utils.TypeHelper;


public class PatientAppointmentDetailsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<AppointmentDTO> list;
    Context context;
    SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy HH:mm");

    public PatientAppointmentDetailsRecyclerAdapter(Context context, List<AppointmentDTO> taskList) {
        this.context = context;
        this.list = taskList;

    }
    public List<AppointmentDTO> getList() {
        return list;
    }

    public void setList(List<AppointmentDTO> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewholder_history, parent, false);
        return new TaskViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder myView, final int position) {
        final AppointmentDTO appointmentDTO = list.get(position);
        final TaskViewHolder holder = (TaskViewHolder) myView;
        if (appointmentDTO.getTreatment() != null) {
            holder.linearTreatment.setVisibility(View.VISIBLE);
            holder.txtComments.setText(appointmentDTO.getTreatment().getDetails());
            holder.txtFees.setText(appointmentDTO.getTreatment().getFees()+"");
            holder.btnAddTreatment.setVisibility(View.GONE);
            holder.txtTreatment.setVisibility(View.GONE);

        } else {
            holder.txtTreatment.setVisibility(View.VISIBLE);
            holder.linearTreatment.setVisibility(View.GONE);
            holder.btnAddTreatment.setVisibility(View.VISIBLE);

        }

        holder.btnAddTreatment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddTreatmentActivity.class);
                intent.putExtra("appointment",appointmentDTO);
                context.startActivity(intent);
            }
        });

        holder.btnEditTreatment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddTreatmentActivity.class);
                intent.putExtra("appointment",appointmentDTO);
                intent.putExtra("edit",true);
                context.startActivity(intent);
            }
        });

        if (appointmentDTO != null && appointmentDTO.getPatientDTO() != null) {

            PatientDTO patientDTO = appointmentDTO.getPatientDTO();
            int age = TypeHelper.getAge(patientDTO.getDateOfBirth());
        }
        holder.txtDate.setText(sdf.format(new Date(appointmentDTO.getDate())));

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


    public class TaskViewHolder extends PatientAppointmentDetailsRecyclerAdapter.ViewHolder {
        TextView  txtComments, txtFees, txtDate,txtTreatment;
        Button btnAddTreatment, btnEditTreatment;
        LinearLayout linearTreatment;

        public TaskViewHolder(View v) {
            super(v);
            txtDate = v.findViewById(R.id.txtDate);
            txtTreatment = v.findViewById(R.id.txtTreatment);
            txtComments = v.findViewById(R.id.txtComments);
            txtFees = v.findViewById(R.id.txtFees);
            btnAddTreatment = v.findViewById(R.id.btnAddTreatment);
            btnEditTreatment = v.findViewById(R.id.btnEditTreatment);
            linearTreatment = v.findViewById(R.id.linearTreatment);

        }
    }


}