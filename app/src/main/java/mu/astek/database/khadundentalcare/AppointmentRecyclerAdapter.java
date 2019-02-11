package mu.astek.database.khadundentalcare;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import mu.astek.database.khadundentalcare.Activities.AddTreatmentActivity;
import mu.astek.database.khadundentalcare.DTO.AppointmentDTO;
import mu.astek.database.khadundentalcare.DTO.PatientDTO;
import mu.astek.database.khadundentalcare.DTO.TreatmentDTO;
import mu.astek.database.khadundentalcare.Utils.TypeHelper;

import static mu.astek.database.khadundentalcare.Utils.TypeHelper.getUriList;


public class AppointmentRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<AppointmentDTO> list;
    Context context;
    SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy HH:mm");

    public AppointmentRecyclerAdapter(Context context, List<AppointmentDTO> taskList) {
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
                .inflate(R.layout.viewholder_appointment, parent, false);
        return new TaskViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder myView, final int position) {
        final AppointmentDTO appointmentDTO = list.get(position);
        final TaskViewHolder holder = (TaskViewHolder) myView;
        if (appointmentDTO.getTreatment() != null) {
            TreatmentDTO treatmentDTO = appointmentDTO.getTreatment();
            holder.linearTreatment.setVisibility(View.VISIBLE);
            holder.txtComments.setText(appointmentDTO.getTreatment().getDetails());
            holder.txtFees.setText(appointmentDTO.getTreatment().getFees()+"");
            holder.btnAddTreatment.setVisibility(View.GONE);
            if (treatmentDTO.getImages() != null) {
                String imageUris = treatmentDTO.getImages();
                List<String> list = Arrays.asList(imageUris.split("--"));
                List<Uri> uriList = getUriList(list);
                if(!uriList.isEmpty()){
                    holder.linearImages.setVisibility(View.VISIBLE);
                    ImageAdapter imageAdapter = new ImageAdapter(uriList, context,true);
                    holder.recyclerviewPhoto.setAdapter(imageAdapter);
                    final LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                    holder.recyclerviewPhoto.setLayoutManager(layoutManager);
                }
            }
            if (treatmentDTO.getPdfs() != null) {
                String pdfs = treatmentDTO.getPdfs();
                List<String> pdfNames = Arrays.asList(pdfs.split("--"));
                List<Uri> pdfList = getUriList(pdfNames);
                if(!pdfList.isEmpty()){
                    holder.linearPdf.setVisibility(View.VISIBLE);
                    PdfAdapter pdfAdapter = new PdfAdapter(pdfList, context,true);
                    holder.recyclerPdf.setAdapter(pdfAdapter);
                    final LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                    holder.recyclerPdf.setLayoutManager(layoutManager);
                }
            }

        } else {
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
            int age = patientDTO.getAge();
            holder.txtAge.setText("" + age);
            holder.txtUsername.setText(patientDTO.getFirstname() + " " + patientDTO.getLastname());
            holder.txtPhone.setText(patientDTO.getPhone());
            holder.txtAddress.setText(patientDTO.getAddress());
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


    public class TaskViewHolder extends AppointmentRecyclerAdapter.ViewHolder {
        TextView txtUsername, txtAddress, txtAge, txtPhone, txtComments, txtFees, txtDate;
        Button btnAddTreatment, btnEditTreatment;
        LinearLayout linearTreatment, linearImages, linearPdf;
        RecyclerView recyclerviewPhoto, recyclerPdf;

        public TaskViewHolder(View v) {
            super(v);
            txtDate = v.findViewById(R.id.txtDate);
            txtUsername = v.findViewById(R.id.txtUsername);
            txtAddress = v.findViewById(R.id.txtAddress);
            txtAge = v.findViewById(R.id.txtAge);
            txtPhone = v.findViewById(R.id.txtPhone);
            txtComments = v.findViewById(R.id.txtComments);
            txtFees = v.findViewById(R.id.txtFees);
            btnAddTreatment = v.findViewById(R.id.btnAddTreatment);
            btnEditTreatment = v.findViewById(R.id.btnEditTreatment);
            linearTreatment = v.findViewById(R.id.linearTreatment);
            linearImages = v.findViewById(R.id.linearImages);
            linearPdf = v.findViewById(R.id.linearPdf);
            recyclerviewPhoto = v.findViewById(R.id.recyclerviewPhoto);
            recyclerPdf = v.findViewById(R.id.recyclerPdf);
        }
    }


}