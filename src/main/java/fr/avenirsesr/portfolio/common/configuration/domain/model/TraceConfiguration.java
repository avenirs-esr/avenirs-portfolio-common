package fr.avenirsesr.portfolio.common.configuration.domain.model;

public record TraceConfiguration(
    int maxRemainingDays, int maxRemainingDaysBeforeWarning, int maxRemainingDaysBeforeCritical) {}
