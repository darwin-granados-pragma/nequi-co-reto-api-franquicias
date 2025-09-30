package co.com.franchise.usecase.product;

import co.com.franchise.model.error.ErrorCode;
import co.com.franchise.model.exception.ObjectNotFoundException;
import co.com.franchise.model.gateways.ProductRepository;
import co.com.franchise.model.product.Product;
import co.com.franchise.model.product.ProductCreate;
import co.com.franchise.model.product.ProductUpdateName;
import co.com.franchise.model.product.ProductUpdateStock;
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

  public Mono<Void> deleteById(String id) {
    return repository
        .existById(id)
        .flatMap(exists -> Boolean.TRUE.equals(exists) ? repository.deleteById(id)
            : Mono.error(new ObjectNotFoundException(ErrorCode.PRODUCT_NOT_FOUND, id)));
  }

  public Mono<Product> updateStockByIdProduct(String idProduct, ProductUpdateStock data) {
    return getProductById(idProduct).flatMap(product -> {
      product.setStock(data.stock());
      return repository.save(product);
    });
  }

  public Mono<Product> updateNameByIdProduct(String idProduct, ProductUpdateName data) {
    return getProductById(idProduct).flatMap(product -> {
      product.setName(data.name());
      return repository.save(product);
    });
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

  private Mono<Product> getProductById(String id) {
    return repository
        .findById(id)
        .switchIfEmpty(Mono.error(new ObjectNotFoundException(ErrorCode.PRODUCT_NOT_FOUND, id)));
  }
}
