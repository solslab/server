package kr.sols.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FormatDate {
    public static String formatDate(LocalDateTime date) {
        if (date == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
    }
}
