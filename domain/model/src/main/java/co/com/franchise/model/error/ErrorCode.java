package co.com.franchise.model.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

  FRANCHISE_NAME_ALREADY_EXISTS(ExceptionCode.CONSTRAINT_VIOLATION,
      "Name of the franchise already exists."
  ),
  CONSTRAINT_VIOLATION(ExceptionCode.CONSTRAINT_VIOLATION, "Constraint violation."),
  ;
  private final ExceptionCode exceptionCode;
  private final String message;
}
