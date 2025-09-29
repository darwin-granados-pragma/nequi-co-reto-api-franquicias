package co.com.franchise.model.exception;

import co.com.franchise.model.error.ErrorCode;
import co.com.franchise.model.error.ExceptionCode;
import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {

  private final ExceptionCode exceptionCode;

  public ApplicationException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.exceptionCode = errorCode.getExceptionCode();
  }

  public ApplicationException(ErrorCode errorCode, String value) {
    super(errorCode
        .getMessage()
        .concat(value));
    this.exceptionCode = errorCode.getExceptionCode();
  }
}
