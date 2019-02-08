package mu.astek.database.khadundentalcare.Activities;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.channels.FileChannel;

import mu.astek.database.khadundentalcare.DTO.AppointmentDTO;
import mu.astek.database.khadundentalcare.DTO.TreatmentDTO;
import mu.astek.database.khadundentalcare.Database.DatabaseService;
import mu.astek.database.khadundentalcare.R;

public class AddTreatmentActivity extends AppCompatActivity {

    AppointmentDTO appointment;
    EditText txtDetails, txtFees;
    Button btnSave;
    Boolean isEdit = false;
    DatabaseService service;
    TextView txtFiles;
    int SELECT_PICTURES = 5;
    public static final String TAG = "PhotoUtils";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_treatment);
        service = new DatabaseService(this);
        btnSave = findViewById(R.id.btnSave);
        txtDetails = findViewById(R.id.txtDetails);
        txtFees = findViewById(R.id.txtFees);
        txtFiles = findViewById(R.id.txtFiles);

        appointment = (AppointmentDTO) getIntent().getSerializableExtra("appointment");
        isEdit = getIntent().getBooleanExtra("edit", false);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEdit) {
                    String comment = txtDetails.getText().toString();
                    Integer fee = Integer.valueOf(txtFees.getText().toString());
                    TreatmentDTO treatmentDTO = appointment.getTreatment();
                    treatmentDTO.setFees(fee);
                    treatmentDTO.setDetails(comment);
                    service.updateTreatment(treatmentDTO);
                    finish();
                } else {
                    if (!TextUtils.isEmpty(txtDetails.getText()) && !TextUtils.isEmpty(txtFees.getText())) {
                        String comment = txtDetails.getText().toString();
                        Integer fee = Integer.valueOf(txtFees.getText().toString());
                        TreatmentDTO treatmentDTO = new TreatmentDTO();
                        treatmentDTO.setId(appointment.getAppointmentID());
                        treatmentDTO.setFees(fee);
                        treatmentDTO.setDetails(comment);
                        service.createTreatment(treatmentDTO);
                        finish();
                    }
                }
            }
        });

        txtFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*"); //allows any image file type. Change * to specific extension to limit it
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURES);*/

                Intent intentPDF = new Intent(Intent.ACTION_GET_CONTENT);
                intentPDF.setType("application/pdf");
                intentPDF.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intentPDF,1212);
            }
        });

        if (isEdit && appointment.getTreatment() != null) {
            txtDetails.setText(appointment.getTreatment().getDetails());
            txtFees.setText(appointment.getTreatment().getFees() + "");
            btnSave.setText("Update Treatment");
            int pos = txtDetails.getText().length();
            txtDetails.setSelection(pos);

            int pos2 = txtFees.getText().length();
            txtFees.setSelection(pos2);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PICTURES) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                    Uri imageUri = null;
                    for (int i = 0; i < count; i++){
                        imageUri = data.getClipData().getItemAt(i).getUri();
                        savePhotoProfile("File"+i+".jpg",getBytes(imageUri));
                    }

                    //do something with the image (save it to some directory or whatever you need to do with it here)

                }
            } else if (data.getData() != null) {
                String imagePath = data.getData().getPath();
                //do something with the image (save it to some directory or whatever you need to do with it here)
            }
        }

        if(requestCode == 1212){
            Uri uri = data.getData();
            try {
                saveFile(uri,"File.pdf");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void saveFile(Uri sourceUri, String photoName) throws IOException {

        InputStream in = null;
        try {
            in = getContentResolver().openInputStream(sourceUri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        OutputStream out = new FileOutputStream(new File(getOutputMediaDirectoryProfilePic()+"/"+photoName));
        byte[] buf = new byte[1024];
        int len;
        while((len=in.read(buf))>0){
            out.write(buf,0,len);
        }
        out.close();
        in.close();
}
    public final static void savePhotoProfile(String photoName, byte[] photo) {
        Log.i(TAG, "Saving photo:" + photoName);
        File dir = getOutputMediaDirectoryProfilePic();
        if (dir != null && photo != null) {
            File photoFile = new File(dir, photoName);
            FileOutputStream fos = null;
            try {

                fos = new FileOutputStream(photoFile);
                fos.write(photo);

            } catch (Exception e) {
                Log.e(TAG, "Error writing profile photo with photoName: " + photoName);
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (Exception e) {
                    }
                }
            }
        }
    }
    public final static File getOutputMediaDirectoryProfilePic() {
        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), ".Dentist");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e(TAG, "Error creating media storage directory");
                return null;
            }
        }
        return mediaStorageDir;
    }


    private String getRealPathFromURI( Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            Log.e(TAG, "getRealPathFromURI Exception : " + e.toString());
            return "";
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public byte[] getBytes(Uri data)  {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileInputStream fis;
        try {
            fis = new FileInputStream(new File(getImagePath(data)));
            byte[] buf = new byte[1024];
            int n;
            while (-1 != (n = fis.read(buf)))
                baos.write(buf, 0, n);
        } catch (Exception e) {
            e.printStackTrace();
        }

        byte[] bbytes = baos.toByteArray();
        return bbytes;
    }



    public  String getImagePath(Uri uri){
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":")+1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

}

