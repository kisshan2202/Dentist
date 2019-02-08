package mu.astek.database.khadundentalcare.DTO;

import java.io.Serializable;
import java.sql.Timestamp;

public class TreatmentDTO implements Serializable {
    Integer id;
    String details;
    Integer fees;

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Integer getFees() {
        return fees;
    }

    public void setFees(Integer fees) {
        this.fees = fees;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
