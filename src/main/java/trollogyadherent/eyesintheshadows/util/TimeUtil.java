package trollogyadherent.eyesintheshadows.util;

import java.time.temporal.ChronoUnit;
import java.util.Calendar;

public class TimeUtil {
    /* source: https://stackoverflow.com/a/27338546 */
    public static int getDaysUntil(int month, int day) {
        /**
         * First get a properly formatted calendar representing right now. This
         * should include leap years and local. With this calendar, we get the
         * day of the year.
         */
        Calendar calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DAY_OF_YEAR);

        /**
         * Now change the day and month of the current calendar to the given day
         * and month.
         */
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        int desiredDay = calendar.get(Calendar.DAY_OF_YEAR);

        /**
         * Then we just get the difference between now and then.
         */
        int difference = desiredDay - today;

        /**
         * If the desiredDay has passed already, or it's currently the
         * desiredDay, we need to recalculate the difference.
         */
        if (difference <= 0) {
            /**
             * We start by getting the days until the end of the year.
             */
            calendar.set(Calendar.MONTH, 11);
            calendar.set(Calendar.DAY_OF_MONTH, 31);
            int daysUntilEnd = calendar.get(Calendar.DAY_OF_YEAR) - today;

            /**
             * Then, move the calendar forward a year and get the day of year
             * for the desired day again. We recalculate the number of days just
             * in case next year is a leap year.
             */
            calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            desiredDay = calendar.get(Calendar.DAY_OF_YEAR);

            /**
             * Finally, just add daysUntilEnd and desiredDay to get the updated
             * difference.
             */
            difference = daysUntilEnd + desiredDay;
        }

        return difference;
    }

    public static int getDaysUntilNextHalloween() {
        return (getDaysUntil(9, 31));
    }

    public static int getMinutesToMidnight() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        if (hour > 12) hour -= 24;
        return Math.abs(hour * 24 + minute);
    }
}
