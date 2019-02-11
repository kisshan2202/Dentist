package mu.astek.database.khadundentalcare.Activities;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import mu.astek.database.khadundentalcare.DTO.AppointmentDTO;
import mu.astek.database.khadundentalcare.DTO.TreatmentDTO;
import mu.astek.database.khadundentalcare.Database.DatabaseService;
import mu.astek.database.khadundentalcare.ImageAdapter;
import mu.astek.database.khadundentalcare.PdfAdapter;
import mu.astek.database.khadundentalcare.R;

public class AddTreatmentActivity extends AppCompatActivity {

    AppointmentDTO appointment;
    EditText txtDetails, txtFees;
    Button btnSave;
    Boolean isEdit = false;
    DatabaseService service;
    LinearLayout linearImage, linearPdf;
    int SELECT_PICTURES = 5;
    public static final String TAG = "PhotoUtils";
    List<Uri> uriList = new ArrayList<>();
    List<Uri> pdfList = new ArrayList<>();
    RecyclerView recyclerviewPhoto, recyclerPdf;
    ImageAdapter imageAdapter;
    PdfAdapter pdfAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder(); StrictMode.setVmPolicy(builder.build());

        setContentView(R.layout.activity_add_treatment);
        service = new DatabaseService(this);
        btnSave = findViewById(R.id.btnSave);
        txtDetails = findViewById(R.id.txtDetails);
        txtFees = findViewById(R.id.txtFees);
        linearImage = findViewById(R.id.linearImage);
        recyclerviewPhoto = findViewById(R.id.recyclerviewPhoto);
        linearPdf = findViewById(R.id.linearPdf);
        recyclerPdf = findViewById(R.id.recyclerPdf);

        appointment = (AppointmentDTO) getIntent().getSerializableExtra("appointment");
        isEdit = getIntent().getBooleanExtra("edit", false);
        if (isEdit) {
            linearImage.setVisibility(View.GONE);
            linearPdf.setVisibility(View.GONE);
        }
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
                        pdfList = pdfAdapter.getList();
                        uriList = imageAdapter.getList();

                        String pdf = null;
                        if(!pdfList.isEmpty()){
                            pdf = getNamePdf(pdfList);
                        }
                        String img = null;
                        if(!uriList.isEmpty()){
                            img = getNameImages(uriList);
                        }

                        String comment = txtDetails.getText().toString();
                        Integer fee = Integer.valueOf(txtFees.getText().toString());
                        TreatmentDTO treatmentDTO = new TreatmentDTO();
                        treatmentDTO.setId(appointment.getAppointmentID());
                        treatmentDTO.setFees(fee);
                        treatmentDTO.setDetails(comment);
                        treatmentDTO.setPdfs(pdf);
                        treatmentDTO.setImages(img);
                        service.createTreatment(treatmentDTO);
                        finish();
                    }
                }
            }
        });

        linearImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*"); //allows any image file type. Change * to specific extension to limit it
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURES);
            }
        });

        linearPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPDF = new Intent(Intent.ACTION_GET_CONTENT);
                intentPDF.setType("image/jpg");
                intentPDF.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intentPDF, 1212);
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

    private String getNameImages(List<Uri> imageList) {
        List<String> names = new ArrayList<>();

        for(int i = 0;i< imageList.size();i++){
            String name = Calendar.getInstance().getTimeInMillis()+".jpg";
            savePhotoProfile(name,getBytes(imageList.get(i)));
            names.add(name);
        }

        return TextUtils.join("--", names);
    }

    private String getNamePdf(List<Uri> pdfList) {
        List<String> names = new ArrayList<>();
        for(int i = 0;i< pdfList.size();i++){
            String name = Calendar.getInstance().getTimeInMillis()+".pdf";
            saveFile(pdfList.get(i),name);
            names.add(name);
        }

        return TextUtils.join("--", names);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PICTURES) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                    Uri imageUri = null;
                    for (int i = 0; i < count; i++) {
                        imageUri = data.getClipData().getItemAt(i).getUri();
                        recyclerviewPhoto.setVisibility(View.VISIBLE);
                        uriList.add(imageUri);

                        //savePhotoProfile("File" + i + ".jpg", getBytes(imageUri));
                    }
                    imageAdapter = new ImageAdapter(uriList, AddTreatmentActivity.this,false);
                    recyclerviewPhoto.setAdapter(imageAdapter);
                    final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                    recyclerviewPhoto.setLayoutManager(layoutManager);
                    //do something with the image (save it to some directory or whatever you need to do with it here)

                }
            }
        }

        if (requestCode == 1212 && data != null) {
            Uri uri = data.getData();
            pdfList.add(uri);
            recyclerPdf.setVisibility(View.VISIBLE);
            pdfAdapter = new PdfAdapter(pdfList, AddTreatmentActivity.this,false);
            recyclerPdf.setAdapter(pdfAdapter);
            final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            recyclerPdf.setLayoutManager(layoutManager);
            /*try {

               // saveFile(uri, "File.pdf");
            } /*catch (IOException e) {
                e.printStackTrace();
            }*/
        }
    }


    private void saveFile(Uri sourceUri, String photoName)  {

        InputStream in = null;
        try {
            in = getContentResolver().openInputStream(sourceUri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        OutputStream out = null;

        try{
            out =  new FileOutputStream(new File(getOutputMediaDirectoryProfilePic() + "/" + photoName));
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();

        }catch (Exception e){

        }finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                }
            }
        }



    }

    public final void savePhotoProfile(String photoName, byte[] photo) {
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

    public final File getOutputMediaDirectoryProfilePic() {
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


    private String getRealPathFromURI(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = getContentResolver().query(contentUri, proj, null, null, null);
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

    public byte[] getBytes(Uri data) {
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


    public String getImagePath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
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

