package mu.astek.database.khadundentalcare.DTO;


import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class AppointmentDTO implements Serializable {
    PatientDTO patientDTO;
    Integer appointmentID;
    Long date;
    Boolean presence;
    Boolean savedOffline;
    TreatmentDTO treatment;

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Boolean getPresence() {
        return presence;
    }

    public void setPresence(Boolean presence) {
        this.presence = presence;
    }

    public TreatmentDTO getTreatment() {
        return treatment;
    }

    public void setTreatment(TreatmentDTO treatment) {
        this.treatment = treatment;
    }

    public Integer getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(Integer appointmentID) {
        this.appointmentID = appointmentID;
    }

    public PatientDTO getPatientDTO() {
        return patientDTO;
    }

    public void setPatientDTO(PatientDTO patientDTO) {
        this.patientDTO = patientDTO;
    }

    public Boolean getSavedOffline() {
        return savedOffline;
    }

    public void setSavedOffline(Boolean savedOffline) {
        this.savedOffline = savedOffline;
    }
}
