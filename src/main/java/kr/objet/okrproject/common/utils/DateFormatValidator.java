package kr.objet.okrproject.common.utils;

import kr.objet.okrproject.common.exception.ErrorCode;
import kr.objet.okrproject.common.exception.OkrApplicationException;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class DateFormatValidator {

    public static YearMonth validateYearMonth(String yearMonth) {
        try {
            return yearMonth == null ? YearMonth.now() :
                    YearMonth.parse(yearMonth, DateTimeFormatter.ofPattern("yyyy-MM"));
        } catch (Exception e) {
            throw new OkrApplicationException(ErrorCode.INVALID_YEARMONTH_FORMAT);
        }
    }

    public static LocalDate validateDate(String date) {
        try {
            return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyyMMdd"));
        } catch (Exception e) {
            throw new OkrApplicationException(ErrorCode.INVALID_SEARCH_DATE_FORM);
        }
    }

}
