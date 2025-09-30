package co.com.franchise.model.branch;

import co.com.franchise.model.product.ProductDomainResponse;
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
public class BranchDomainResponse {

  private String id;
  private String name;
  private ProductDomainResponse productResponse;
}
