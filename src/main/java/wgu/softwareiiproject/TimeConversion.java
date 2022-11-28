package wgu.softwareiiproject;

import java.time.*;

public class TimeConversion {

//    public static LocalDateTime convertToUTC(LocalDateTime ldt) {
//        ZonedDateTime utcZdt = ZonedDateTime.ofInstant(ZonedDateTime.of(ldt, ZoneId.systemDefault()).toInstant(), ZoneId.of("UTC"));
//        return LocalDateTime.of(utcZdt.getYear(), utcZdt.getMonth(), utcZdt.getDayOfMonth(), utcZdt.getHour(), utcZdt.getMinute());
//    }

    public static LocalDateTime convertToLocal(LocalDateTime ldt) {
        ZonedDateTime myZdt;
        ZonedDateTime utcZdt = ZonedDateTime.ofInstant(ZonedDateTime.of(ldt, ZoneId.systemDefault()).toInstant(), ZoneId.of("UTC"));
        myZdt = ZonedDateTime.ofInstant(utcZdt.toInstant(), ZoneId.systemDefault());
        return LocalDateTime.of(myZdt.getYear(), myZdt.getMonth(), myZdt.getDayOfMonth(), myZdt.getHour(), myZdt.getMinute());
    }

    public static LocalDateTime convertLocalToET(LocalDateTime ldt) {
        ZonedDateTime myZdt;
        ZonedDateTime utcZdt = ZonedDateTime.ofInstant(ZonedDateTime.of(ldt, ZoneId.systemDefault()).toInstant(), ZoneId.of("UTC"));
        myZdt = ZonedDateTime.ofInstant(utcZdt.toInstant(), ZoneId.of("America/New_York"));
        return LocalDateTime.of(myZdt.getYear(), myZdt.getMonth(), myZdt.getDayOfMonth(), myZdt.getHour(), myZdt.getMinute());
    }
}
