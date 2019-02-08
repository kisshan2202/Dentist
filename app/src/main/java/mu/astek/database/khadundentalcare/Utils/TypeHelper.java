package mu.astek.database.khadundentalcare.Utils;

import java.util.Calendar;
import java.util.Date;

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
}
