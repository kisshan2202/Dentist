package mu.astek.database.khadundentalcare.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import mu.astek.database.khadundentalcare.DTO.TreatmentDTO;


public final class TreatmentDAO {

    private final String TABLE_NAME = "treatment";
    private final DatabaseHelper dbHelper;
    private Context context;

    public TreatmentDAO(final Context context) {
        dbHelper = DatabaseHelper.getInstance(context);
        this.context = context;
    }

    public void createTreatment(TreatmentDTO TreatmentDTO) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.open();
        db.insert(TABLE_NAME, null, getContentValues(TreatmentDTO));
        dbHelper.close();
    }

    public void updateTreatment(TreatmentDTO treatmentDTO) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.open();
        db.update(TABLE_NAME, getContentValues(treatmentDTO), "id =" + treatmentDTO.getId(), null);
        dbHelper.close();
    }

    public void deleteTreatment(TreatmentDTO treatmentDTO) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.open();
        db.delete(TABLE_NAME, "id =" + treatmentDTO.getId(), null);
        dbHelper.close();
    }

    public ContentValues getContentValues(TreatmentDTO dto) {
        final ContentValues values = new ContentValues();

        if (dto.getId() != null) {
            values.put("id", dto.getId());
        }
        if(dto.getPdfs()!=null){
            values.put("pdfs", dto.getPdfs());
        }
        if(dto.getImages()!=null){
            values.put("images", dto.getImages());
        }

        values.put("details", dto.getDetails());
        values.put("fees", dto.getFees());


        return values;
    }

    public TreatmentDTO getTreatmentByAppointmentID(Integer id) {

        final String query =
                "SELECT * " +
                        " FROM " + TABLE_NAME +
                        " WHERE id = " + id;

        TreatmentDTO treatmentDTO = null;
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        final Cursor res = db.rawQuery(query, null);
        int count = res.getCount();
        if(count == 1){
            res.moveToFirst();
            treatmentDTO = new TreatmentDTO();
            treatmentDTO.setId(id);
            treatmentDTO.setDetails(res.getString(res.getColumnIndexOrThrow("details")));
            treatmentDTO.setImages(res.getString(res.getColumnIndexOrThrow("images")));
            treatmentDTO.setPdfs(res.getString(res.getColumnIndexOrThrow("pdfs")));
            treatmentDTO.setFees(res.getInt(res.getColumnIndexOrThrow("fees")));

            res.moveToNext();
        }
        res.close();

        return treatmentDTO;
    }

    public void deleteData(){
        String query = "delete  from treatment";
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(query);
    }
}
