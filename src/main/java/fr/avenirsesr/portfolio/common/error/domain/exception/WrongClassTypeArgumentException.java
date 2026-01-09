package fr.avenirsesr.portfolio.common.error.domain.exception;

import fr.avenirsesr.portfolio.common.error.domain.model.enums.EErrorCode;

public class WrongClassTypeArgumentException extends BusinessException {

    public WrongClassTypeArgumentException() {
        super(EErrorCode.WRONG_CLASS_TYPE);
    }

    public WrongClassTypeArgumentException(String customMessage) {
        super(EErrorCode.WRONG_CLASS_TYPE, customMessage);
    }
}
