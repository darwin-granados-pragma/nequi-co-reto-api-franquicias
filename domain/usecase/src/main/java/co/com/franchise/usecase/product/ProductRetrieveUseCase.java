package co.com.franchise.usecase.product;

import co.com.franchise.model.gateways.ProductRepository;
import co.com.franchise.model.product.ProductDomainResponse;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ProductRetrieveUseCase {

  private final ProductRepository repository;

  public Mono<ProductDomainResponse> getTopProductStockByIdBranch(String idBranch) {
    return repository
        .findTopByIdBranch(idBranch)
        .map(product -> ProductDomainResponse
            .builder()
            .id(product.getId())
            .name(product.getName())
            .stock(product.getStock())
            .build());
  }
}
