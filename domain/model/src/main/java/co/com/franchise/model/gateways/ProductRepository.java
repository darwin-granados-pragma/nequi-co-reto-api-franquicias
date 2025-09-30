package co.com.franchise.model.gateways;

import co.com.franchise.model.product.Product;
import reactor.core.publisher.Mono;

public interface ProductRepository {

  Mono<Product> save(Product product);

  Mono<Boolean> existById(String id);

  Mono<Product> findById(String id);

  Mono<Void> deleteById(String id);

  Mono<Product> findTopByIdBranch(String idBranch);
}
