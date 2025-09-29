package co.com.franchise.usecase.product;

import co.com.franchise.model.gateways.ProductRepository;
import co.com.franchise.model.product.Product;
import co.com.franchise.model.product.ProductCreate;
import co.com.franchise.usecase.branch.BranchUseCase;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ProductUseCase {

  private final ProductRepository repository;
  private final BranchUseCase branchUseCase;

  public Mono<Product> createProduct(ProductCreate data) {
    return branchUseCase
        .validateBranchById(data.idBranch())
        .then(buildAndSave(data));
  }

  private Mono<Product> buildAndSave(ProductCreate data) {
    return Mono.defer(() -> {
      Product branch = Product
          .builder()
          .id(UUID
              .randomUUID()
              .toString())
          .name(data.name())
          .idBranch(data.idBranch())
          .stock(data.stock())
          .isNew(true)
          .build();
      return repository.save(branch);
    });
  }
}
