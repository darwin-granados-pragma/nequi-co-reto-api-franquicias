package co.com.franchise.api.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class ProductCreateRequest {

  @NotBlank(message = "The name is mandatory.")
  @Size(max = 100, message = "The name should under 100 characters.")
  private String name;

  @NotBlank(message = "The identifier of the branch is mandatory.")
  @Size(max = 50, message = "The identifier of the branch should under 50 characters.")
  private String idBranch;

  @NotNull(message = "The stock is mandatory.")
  private Integer stock;
}
