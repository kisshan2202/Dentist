package mu.astek.database.khadundentalcare.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import mu.astek.database.khadundentalcare.DTO.PatientDTO;


public final class PatientDAO {

    private final String TABLE_NAME = "patient";
    private final DatabaseHelper dbHelper;

    public PatientDAO(final Context context) {
        dbHelper = DatabaseHelper.getInstance(context);
    }

    public void createPatient(PatientDTO patientDTO) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.open();
        db.insert(TABLE_NAME, null, getContentValues(patientDTO));
        dbHelper.close();
    }

    public void updatePatient(PatientDTO patientDTO) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.open();
        db.update(TABLE_NAME, getContentValues(patientDTO), "patientId =" + patientDTO.getPatientId(), null);
        dbHelper.close();
    }

    public void deletePatient(PatientDTO patientDTO) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.open();
        db.delete(TABLE_NAME, "patientId =" + patientDTO.getPatientId(), null);
        dbHelper.close();
    }

    public ContentValues getContentValues(PatientDTO dto) {
        final ContentValues values = new ContentValues();

        if (dto.getPatientId() != null) {
            values.put("patientId", dto.getPatientId());
        }

        values.put("firstname", dto.getFirstname());
        values.put("lastname", dto.getLastname());
        values.put("address", dto.getAddress());
        values.put("phone", dto.getPhone());
        values.put("gender", dto.getGender());

        if (dto.getAge() != null)
            values.put("age", dto.getAge());

        return values;
    }

    public List<PatientDTO> getlistPatient() {

        final String query =
                "SELECT * " +
                        " FROM " + TABLE_NAME;

        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        final Cursor res = db.rawQuery(query, null);
        res.moveToFirst();

        final List<PatientDTO> list = new ArrayList<>();
        PatientDTO dto;

        while (!res.isAfterLast()) {

            dto = new PatientDTO();
            dto.setPatientId(res.getInt(res.getColumnIndex("patientId")));
            dto.setAge(res.getInt(res.getColumnIndexOrThrow("age")));
            dto.setFirstname(res.getString(res.getColumnIndexOrThrow("firstname")));
            dto.setLastname(res.getString(res.getColumnIndexOrThrow("lastname")));
            dto.setAddress(res.getString(res.getColumnIndexOrThrow("address")));
            dto.setPhone(res.getString(res.getColumnIndexOrThrow("phone")));
            dto.setGender(res.getString(res.getColumnIndexOrThrow("gender")));

            list.add(dto);

            res.moveToNext();
        }
        res.close();

        return list;
    }

    public PatientDTO getPatientById(Integer patientID) {

        final String query =
                "SELECT * " +
                        " FROM " + TABLE_NAME +
                        " WHERE patientId = " + patientID;

        PatientDTO patientDTO = null;
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        final Cursor res = db.rawQuery(query, null);
        int count = res.getCount();
        if (count == 1) {
            res.moveToFirst();
            patientDTO = new PatientDTO();
            patientDTO.setPatientId(res.getInt(res.getColumnIndex("patientId")));
            patientDTO.setAge(res.getInt(res.getColumnIndexOrThrow("age")));
            patientDTO.setFirstname(res.getString(res.getColumnIndexOrThrow("firstname")));
            patientDTO.setLastname(res.getString(res.getColumnIndexOrThrow("lastname")));
            patientDTO.setAddress(res.getString(res.getColumnIndexOrThrow("address")));
            patientDTO.setPhone(res.getString(res.getColumnIndexOrThrow("phone")));
            patientDTO.setGender(res.getString(res.getColumnIndexOrThrow("gender")));

            res.moveToNext();
        }
        res.close();

        return patientDTO;
    }
}
