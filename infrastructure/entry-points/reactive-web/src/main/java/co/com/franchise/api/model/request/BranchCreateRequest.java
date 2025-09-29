package co.com.franchise.api.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BranchCreateRequest {

  @NotBlank(message = "The name is mandatory.")
  @Size(max = 100, message = "The name should under 100 characters.")
  private String name;

  @NotBlank(message = "The identifier of the franchise is mandatory.")
  @Size(max = 50, message = "The identifier of the franchise should under 50 characters.")
  private String idFranchise;
}
