package fr.avenirsesr.portfolio.common.temporal.domain.utils;

import fr.avenirsesr.portfolio.common.temporal.domain.model.enums.EPeriodStatus;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
public final class PeriodUtils {

    public static EPeriodStatus getPeriodStatus(LocalDate startDate, LocalDate endDate) {
        LocalDate today = LocalDate.now();

        if (today.isBefore(startDate)) {
            return EPeriodStatus.BEFORE;
        }

        if (endDate == null || !today.isAfter(endDate)) {
            return EPeriodStatus.DURING;
        }

        return EPeriodStatus.AFTER;
    }
}

