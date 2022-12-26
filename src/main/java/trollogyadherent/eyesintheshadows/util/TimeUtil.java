package trollogyadherent.eyesintheshadows.util;

import java.time.temporal.ChronoUnit;
import java.util.Calendar;

public class TimeUtil {
    public static int getDaysUntilNextHalloween() {
        Calendar now = Calendar.getInstance();
        Calendar nextHalloween = new Calendar.Builder()
                .setDate(now.get(Calendar.YEAR), 9, 31)
                .setTimeOfDay(23, 59, 59, 999).build();
        if (now.after(nextHalloween)) {
            nextHalloween.add(Calendar.YEAR, 1);
        }
        return (int) Math.min(ChronoUnit.DAYS.between(now.toInstant(), nextHalloween.toInstant()), 30);
    }

    public static int getMinutesToMidnight() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        if (hour > 12) hour -= 24;
        return Math.abs(hour * 24 + minute);
    }
}
