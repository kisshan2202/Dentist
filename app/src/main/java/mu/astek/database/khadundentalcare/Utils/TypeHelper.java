package mu.astek.database.khadundentalcare.Utils;

import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TypeHelper {
    public static boolean getBooleanFromInt(int b) {

        return (b != 0);
    }
    public static int getAge(Long time) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.setTimeInMillis(time);
        today.setTime(new Date());

        int yearDOB = dob.get(Calendar.YEAR), yearToday = today.get(Calendar.YEAR);

        int age = yearToday - yearDOB;

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        return age;
    }

    public static List<Uri> getUriList(List<String> list) {
        List<Uri> uriList = new ArrayList<>();
        if (!list.isEmpty()) {
            for (String fileName : list) {
                File dir = getOutputMediaDirectoryProfilePic();
                if (dir != null) {
                    File photoFile = new File(dir, fileName);
                    if (photoFile.exists()) {
                        uriList.add(Uri.fromFile(photoFile));
                    }
                }

            }
        }

        return uriList;
    }
    public final static File getOutputMediaDirectoryProfilePic() {
        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), ".Dentist");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        return mediaStorageDir;
    }
}
