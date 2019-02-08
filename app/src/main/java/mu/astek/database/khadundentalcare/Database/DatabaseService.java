package mu.astek.database.khadundentalcare.Database;

import android.content.Context;

import java.util.Date;
import java.util.List;

import mu.astek.database.khadundentalcare.DTO.AppointmentDTO;
import mu.astek.database.khadundentalcare.DTO.PatientDTO;
import mu.astek.database.khadundentalcare.DTO.TreatmentDTO;

public class DatabaseService {

    private Context context;
    private PatientDAO patientDAO;
    private TreatmentDAO treatmentDAO;
    private AppointmentDAO appointmentDAO;

    public DatabaseService(Context context) {
        this.context = context;
        this.patientDAO = new PatientDAO(context);
        this.appointmentDAO = new AppointmentDAO(context);
        this.treatmentDAO = new TreatmentDAO(context);
    }


   public void createPatient(PatientDTO patientDTO) {
        patientDAO.createPatient(patientDTO);
    }


   public void updatePatient(PatientDTO patientDTO) {
        patientDAO.updatePatient(patientDTO);
    }

   public void deletePatient(PatientDTO patientDTO) {
        patientDAO.deletePatient(patientDTO);
    }

    public List<PatientDTO> getPatientList(){
        return patientDAO.getlistPatient();
    }

   public void createAppointment(AppointmentDTO appointmentDTO) {
        appointmentDAO.createAppointment(appointmentDTO);
    }

   public void updateAppointment(AppointmentDTO appointmentDTO) {
        appointmentDAO.updateAppointment(appointmentDTO);
    }

   public void deleteAppointment(AppointmentDTO appointmentDTO) {
        appointmentDAO.deleteAppointment(appointmentDTO);
    }

    public List<AppointmentDTO> getAppointmentsByDate(Date date) {
        return appointmentDAO.getAppointmentListByDate(date);
    }
    
    public List<AppointmentDTO> getAppointmentListByPatientID(Integer patientId) {
        return appointmentDAO.getAppointmentListByPatientID(patientId);
    }

    public List<AppointmentDTO> getAppointmentList() {
        return appointmentDAO.getAppointmentList();
    }

   public void createTreatment(TreatmentDTO treatmentDTO){
        treatmentDAO.createTreatment(treatmentDTO);
    }

   public void updateTreatment(TreatmentDTO treatmentDTO){
        treatmentDAO.updateTreatment(treatmentDTO);
    }

   public void deleteTreatment(TreatmentDTO treatmentDTO){
        treatmentDAO.deleteTreatment(treatmentDTO);
    }

   public void getTreatmentByAppointmentID(Integer id){
        treatmentDAO.getTreatmentByAppointmentID(id);
    }
}
