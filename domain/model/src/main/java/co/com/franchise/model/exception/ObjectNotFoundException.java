package co.com.franchise.model.exception;

import co.com.franchise.model.error.ErrorCode;

public class ObjectNotFoundException extends ApplicationException {
  public ObjectNotFoundException(ErrorCode errorCode, String value) {
    super(errorCode, value);
  }

}
