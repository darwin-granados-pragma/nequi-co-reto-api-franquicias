package co.com.franchise.model.product;

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
public class Product {

  private String id;
  private String name;
  private String idBranch;
  private Integer stock;
  private boolean isNew;
}
