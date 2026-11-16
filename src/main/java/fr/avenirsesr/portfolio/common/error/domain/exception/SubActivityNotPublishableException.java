package fr.avenirsesr.portfolio.common.error.domain.exception;

import fr.avenirsesr.portfolio.common.error.domain.model.enums.EErrorCode;

public class SubActivityNotPublishableException extends BusinessException  {

    public SubActivityNotPublishableException(EErrorCode errorCode) {
        super(errorCode);
    }

    public SubActivityNotPublishableException(EErrorCode errorCode, String message) {
        super(errorCode, message);
    }

}