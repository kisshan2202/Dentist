package mu.astek.database.khadundentalcare.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mu.astek.database.khadundentalcare.DTO.AppointmentDTO;
import mu.astek.database.khadundentalcare.DTO.TreatmentDTO;
import mu.astek.database.khadundentalcare.Utils.TypeHelper;


public final class AppointmentDAO {

    private final String TABLE_NAME = "appointment";
    private final DatabaseHelper dbHelper;
    private Context context;

    public AppointmentDAO(final Context context) {
        dbHelper = DatabaseHelper.getInstance(context);
        this.context = context;
    }

    public void createAppointment(AppointmentDTO patientDTO) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.open();
        db.insert(TABLE_NAME, null, getContentValues(patientDTO));
        dbHelper.close();
    }

    public void updateAppointment(AppointmentDTO appointmentDTO) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.open();
        db.update(TABLE_NAME, getContentValues(appointmentDTO), "appointmentId =" + appointmentDTO.getAppointmentID(), null);
        dbHelper.close();
    }

    public void deleteAppointment(AppointmentDTO appointmentDTO) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.open();
        db.delete(TABLE_NAME, "appointmentId =" + appointmentDTO.getAppointmentID(), null);
        dbHelper.close();
    }

    public ContentValues getContentValues(AppointmentDTO dto) {
        final ContentValues values = new ContentValues();

        if (dto.getAppointmentID() != null) {
            values.put("appointmentId", dto.getAppointmentID());
        }
        if (dto != null && dto.getPatientDTO() != null) {
            values.put("patientId", dto.getPatientDTO().getPatientId());
        }

        values.put("presence", dto.getPresence());

        if (dto.getDate() != null)
            values.put("dateAppointment", dto.getDate());

        return values;
    }

    public List<AppointmentDTO> getAppointmentListByPatientID(Integer patientID) {


        final String query =
                "SELECT *" +
                        " FROM " + TABLE_NAME +
                        " WHERE patientId = " + patientID +
                        " ORDER BY dateAppointment DESC";

        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        final Cursor res = db.rawQuery(query, null);
        res.moveToFirst();

        final List<AppointmentDTO> list = new ArrayList<>();
        AppointmentDTO dto;
        TreatmentDAO treatmentDAO = new TreatmentDAO(context);
        PatientDAO patientDAO = new PatientDAO(context);
        while (!res.isAfterLast()) {

            dto = new AppointmentDTO();
            dto.setAppointmentID(res.getInt(res.getColumnIndex("appointmentId")));
            dto.setDate(res.getLong(res.getColumnIndexOrThrow("dateAppointment")));
            dto.setPresence(TypeHelper.getBooleanFromInt(res.getInt(res.getColumnIndex("presence"))));
            dto.setPatientDTO(patientDAO.getPatientById(patientID));
            TreatmentDTO treatmentDTO = treatmentDAO.getTreatmentByAppointmentID(dto.getAppointmentID());
            if (treatmentDTO != null) {
                dto.setTreatment(treatmentDTO);
            }
            list.add(dto);

            res.moveToNext();
        }
        res.close();

        return list;
    }

    public List<AppointmentDTO> getAppointmentListByDate(Date date) {

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 1);

        Date date1 = calendar.getTime();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date date2 = calendar.getTime();

        final String query =
                "SELECT *" +
                        " FROM " + TABLE_NAME +
                        " WHERE dateAppointment > " + date1.getTime() +
                        " AND dateAppointment < " + date2.getTime() +
                        " ORDER BY dateAppointment ASC";

        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        final Cursor res = db.rawQuery(query, null);
        res.moveToFirst();

        final List<AppointmentDTO> list = new ArrayList<>();
        AppointmentDTO dto;
        TreatmentDAO treatmentDAO = new TreatmentDAO(context);
        PatientDAO patientDAO = new PatientDAO(context);
        while (!res.isAfterLast()) {

            dto = new AppointmentDTO();
            dto.setAppointmentID(res.getInt(res.getColumnIndex("appointmentId")));
            dto.setDate(res.getLong(res.getColumnIndexOrThrow("dateAppointment")));
            dto.setPresence(TypeHelper.getBooleanFromInt(res.getInt(res.getColumnIndex("presence"))));
            dto.setPatientDTO(patientDAO.getPatientById(res.getInt(res.getColumnIndexOrThrow("patientId"))));
            TreatmentDTO treatmentDTO = treatmentDAO.getTreatmentByAppointmentID(dto.getAppointmentID());
            if (treatmentDTO != null) {
                dto.setTreatment(treatmentDTO);
            }
            list.add(dto);

            res.moveToNext();
        }
        res.close();

        return list;
    }

    public List<AppointmentDTO> getAppointmentList() {


        final String query =
                "SELECT *" +
                        " FROM " + TABLE_NAME +
                        " ORDER BY dateAppointment ASC";

        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        final Cursor res = db.rawQuery(query, null);
        res.moveToFirst();

        final List<AppointmentDTO> list = new ArrayList<>();
        AppointmentDTO dto;

        PatientDAO patientDAO = new PatientDAO(context);
        TreatmentDAO treatmentDAO = new TreatmentDAO(context);
        while (!res.isAfterLast()) {

            dto = new AppointmentDTO();
            dto.setAppointmentID(res.getInt(res.getColumnIndex("appointmentId")));
            dto.setDate(res.getLong(res.getColumnIndexOrThrow("dateAppointment")));
            dto.setPresence(TypeHelper.getBooleanFromInt(res.getInt(res.getColumnIndex("presence"))));
            dto.setPatientDTO(patientDAO.getPatientById(res.getInt(res.getColumnIndexOrThrow("patientId"))));

            TreatmentDTO treatmentDTO = treatmentDAO.getTreatmentByAppointmentID(dto.getAppointmentID());
            if (treatmentDTO != null) {
                dto.setTreatment(treatmentDTO);
            }

            list.add(dto);

            res.moveToNext();
        }
        res.close();

        return list;
    }
}
