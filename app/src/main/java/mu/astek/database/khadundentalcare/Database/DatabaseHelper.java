package mu.astek.database.khadundentalcare.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;

/**
 * Manages the SQLite database.
 * Hold wrappers around the SQLiteDatabase object.
 * Responsible for database & table creation.
 */
public class DatabaseHelper extends SQLiteOpenHelper {


    private static String TAG = SQLiteOpenHelper.class.getName();

    private static final int DATABASE_VERSION = 26;
    private static final String DATABASE_NAME = "Dentist.db";
    private static DatabaseHelper mInstance = null;
    private SQLiteDatabase db;

    private Context context;


    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public static DatabaseHelper getInstance(Context ctx) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (mInstance == null) {
            mInstance = new DatabaseHelper(ctx.getApplicationContext());
        }

        return mInstance;
    }

    public static void logPRAGMA_foreign_keys(SQLiteDatabase db) {

        Cursor res = db.rawQuery("PRAGMA foreign_keys", null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            Log.e("PRAGMA foreign_keys", res.getString(res.getColumnIndex("foreign_keys")));
            res.moveToNext();
        }
        res.close();
    }

    public DatabaseHelper open() throws SQLException {
        db = this.getWritableDatabase();
        return this;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void runUpdateQueries(List<String> newColumns, SQLiteDatabase db) {
        for (String sql : newColumns) {
            try {
                db.execSQL(sql);
            } catch (SQLiteException e) {
                Log.e(TAG, "column already exists:" + e.getMessage());
            }
        }
    }


    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys = \"1\"; commit;");
    }


    private void createTables(SQLiteDatabase db) {
        createTablePatient(db);
        createTableAppointment(db);
        createTableTreatmentDTO(db);


    }

    private void createTablePatient(SQLiteDatabase db) {
        String qb = "CREATE TABLE IF NOT EXISTS patient (" +
                " patientId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                " firstname TEXT NOT NULL, " +
                " lastname TEXT NOT NULL, " +
                " address TEXT NOT NULL, " +
                " phone TEXT NOT NULL, " +
                " gender TEXT NOT NULL, " +
                " age NUMERIC DEFAULT NULL ); ";

        db.execSQL(qb);
    }


    private void createTableAppointment(SQLiteDatabase db) {
        String qb = "CREATE TABLE IF NOT EXISTS appointment (" +
                " appointmentId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                " patientId INTEGER NOT NULL, " +
                " presence INTEGER DEFAULT 0, " +
                " dateAppointment NUMERIC DEFAULT NULL , " +
                "     FOREIGN KEY(patientId) REFERENCES patient(patientId) ON DELETE SET NULL ON UPDATE SET NULL ); ";

        db.execSQL(qb);
    }

    private void createTableTreatmentDTO(SQLiteDatabase db) {
        String qb = "CREATE TABLE IF NOT EXISTS treatment (" +
                " id INTEGER PRIMARY KEY NOT NULL, " +
                " details TEXT NOT NULL, " +
                " fees NUMERIC DEFAULT NULL , " +
                "     FOREIGN KEY(id) REFERENCES appointment(appointmentId) ON DELETE SET NULL ON UPDATE SET NULL ); ";

        db.execSQL(qb);
    }


}
