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
public class FranchiseCreateRequest {

  @NotBlank(message = "El nombre es obligatorio")
  @Size(max = 100, message = "El nombre debe tener menos de 100 caracteres")
  private String name;
}
