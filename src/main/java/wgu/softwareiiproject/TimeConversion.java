package wgu.softwareiiproject;

import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class TimeConversion {

    public static LocalDateTime convertToUTC(LocalDateTime lcd) {
        Date dt = java.util.Date.from(lcd.atZone(ZoneId.systemDefault()).toInstant());
        Calendar cal = new GregorianCalendar();
        cal.setTime(dt);

        TimeZone utcTimeZone = TimeZone.getTimeZone("UTC");

        SimpleDateFormat newTimeZoneFormat = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm");
        newTimeZoneFormat.setTimeZone(utcTimeZone);
        cal.setTimeZone(utcTimeZone);

        return LocalDateTime.ofInstant(cal.toInstant(), ZoneId.of(utcTimeZone.getID()));
    }

    public static LocalDateTime convertFromUTC(LocalDateTime lcd) {
        Date dt = java.util.Date.from(lcd.toInstant(ZoneOffset.UTC));
        Calendar cal = new GregorianCalendar();
        cal.setTime(dt);

        TimeZone userTimeZone = TimeZone.getTimeZone(TimeZone.getDefault().toZoneId());

        SimpleDateFormat newTimeZoneFormat = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm");
        newTimeZoneFormat.setTimeZone(userTimeZone);
        cal.setTimeZone(userTimeZone);

        return LocalDateTime.ofInstant(cal.toInstant(), ZoneId.systemDefault());
    }

}
