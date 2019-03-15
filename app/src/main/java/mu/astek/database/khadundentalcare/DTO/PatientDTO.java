package mu.astek.database.khadundentalcare.DTO;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.sql.Timestamp;

@IgnoreExtraProperties
public class PatientDTO implements Serializable {
    Integer patientId;
    String firstname;
    String lastname;
    Integer age;
    String address;
    String phone;
    String gender;
    Boolean savedOffline;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    @Override
    public String toString() {
        return firstname+ " "+lastname;
    }

    public Boolean getSavedOffline() {
        return savedOffline;
    }

    public void setSavedOffline(Boolean savedOffline) {
        this.savedOffline = savedOffline;
    }
}
