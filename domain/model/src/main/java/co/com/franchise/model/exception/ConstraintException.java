package co.com.franchise.model.exception;

import co.com.franchise.model.error.ErrorCode;

public class ConstraintException extends ApplicationException {

  public ConstraintException(ErrorCode errorCode) {
    super(errorCode);
  }
}
