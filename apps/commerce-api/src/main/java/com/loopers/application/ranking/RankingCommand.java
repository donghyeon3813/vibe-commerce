package com.loopers.application.ranking;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class RankingCommand {
    @Getter
    public static class Get{
        private String date;
        private int size;
        private int page;
        private Period period;

        public Get(String date, int size, int page, Period period) {
            if (!isValidDate(date)) {
                throw new CoreException(ErrorType.BAD_REQUEST, ErrorType.BAD_REQUEST.getMessage());
            }
            if (size < 1) {
                throw new CoreException(ErrorType.BAD_REQUEST, ErrorType.BAD_REQUEST.getMessage());
            }
            if (page < 1) {
                throw new CoreException(ErrorType.BAD_REQUEST, ErrorType.BAD_REQUEST.getMessage());
            }
            this.date = date;
            this.size = size;
            this.page = page;
            this.period = period;

        }

        public static Get of(String date, int size, int page, String period) {
            return new Get(date, size, page, Period.getPeriod(period));
        }
        public static boolean isValidDate(String date) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                LocalDate.parse(date, formatter); // 파싱 가능 여부 확인
                return true;
            } catch (DateTimeParseException e) {
                return false;
            }
        }
        public enum Period {
            DAILY, WEEKLY, MONTHLY;

            public static Period getPeriod(String period) {
                return Period.valueOf(period);
            }
        }
    }
}
