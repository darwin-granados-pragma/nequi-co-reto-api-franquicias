package co.com.franchise.model.exception;

import java.util.List;
import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {

  private final List<String> details;

  public ValidationException(List<String> details) {
    super("Validation data failed");
    this.details = details;
  }
}
