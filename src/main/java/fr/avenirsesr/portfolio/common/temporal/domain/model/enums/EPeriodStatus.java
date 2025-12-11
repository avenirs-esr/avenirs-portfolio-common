package fr.avenirsesr.portfolio.common.temporal.domain.model.enums;

import java.time.LocalDate;

import static fr.avenirsesr.portfolio.common.validation.domain.utils.FieldValidationUtils.requireNotNull;
import static fr.avenirsesr.portfolio.common.validation.domain.utils.FieldValidationUtils.validateDateOrder;

public enum EPeriodStatus {
    BEFORE,
    DURING,
    AFTER;

    public static EPeriodStatus of(LocalDate startDate, LocalDate endDate) {
        requireNotNull("startDate", startDate);
        validateDateOrder(startDate, endDate);
        LocalDate today = LocalDate.now();

        if (today.isBefore(startDate)) {
            return BEFORE;
        }

        if (endDate == null || !today.isAfter(endDate)) {
            return DURING;
        }

        return AFTER;
    }
}
