package app_sched_sys;

import java.time.*;

/**
 * The Time Conversion class.
 * This class holds all the methods for converting UTC time in the database.
 */
public class TimeConversion {

    /**
     * Converts UTC to the user's local time.
     * This method takes the UTC date time in the database and converts it to the user's system default time zone.
     * @param ldt The local date time that is being converted.
     * @return The local date time that has been converted to the user's system default time zone.
     */
    public static LocalDateTime convertToLocal(LocalDateTime ldt) {
        ZonedDateTime myZdt;
        ZonedDateTime utcZdt = ZonedDateTime.ofInstant(ZonedDateTime.of(ldt, ZoneId.systemDefault()).toInstant(), ZoneId.of("UTC"));
        myZdt = ZonedDateTime.ofInstant(utcZdt.toInstant(), ZoneId.systemDefault());
        return LocalDateTime.of(myZdt.getYear(), myZdt.getMonth(), myZdt.getDayOfMonth(), myZdt.getHour(), myZdt.getMinute());
    }

    /**
     * Converts a given time to ET for business hours logic.
     * This method takes the date time parameter and converts it to ET.
     * @param ldt The local date time that is being converted.
     * @return The local date time that has been converted to ET.
     */
    public static LocalDateTime convertLocalToET(LocalDateTime ldt) {
        ZonedDateTime myZdt;
        ZonedDateTime utcZdt = ZonedDateTime.ofInstant(ZonedDateTime.of(ldt, ZoneId.systemDefault()).toInstant(), ZoneId.of("UTC"));
        myZdt = ZonedDateTime.ofInstant(utcZdt.toInstant(), ZoneId.of("America/New_York"));
        return LocalDateTime.of(myZdt.getYear(), myZdt.getMonth(), myZdt.getDayOfMonth(), myZdt.getHour(), myZdt.getMinute());
    }
}
