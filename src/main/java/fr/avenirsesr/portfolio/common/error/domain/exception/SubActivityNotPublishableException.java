package fr.avenirsesr.portfolio.common.error.domain.exception;


import fr.avenirsesr.portfolio.common.error.domain.model.enums.EErrorCode;

public class SubActivityNotPublishableException extends BusinessException {

    public SubActivityNotPublishableException() {super(EErrorCode.SUB_ACTIVITY_NOT_PUBLISHABLE);}
}
