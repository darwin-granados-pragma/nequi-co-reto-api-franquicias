package co.com.franchise.r2dbc.adapter;

import co.com.franchise.model.gateways.ProductRepository;
import co.com.franchise.model.product.Product;
import co.com.franchise.r2dbc.entity.ProductEntity;
import co.com.franchise.r2dbc.helper.ReactiveAdapterOperations;
import co.com.franchise.r2dbc.repository.ProductReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
public class ProductReactiveRepositoryAdapter extends
    ReactiveAdapterOperations<Product, ProductEntity, String, ProductReactiveRepository> implements
    ProductRepository {

  private final TransactionalOperator transactionalOperator;

  public ProductReactiveRepositoryAdapter(ProductReactiveRepository repository, ObjectMapper mapper,
      TransactionalOperator transactionalOperator) {
    super(repository, mapper, d -> mapper.map(d, Product.class));
    this.transactionalOperator = transactionalOperator;
  }

  @Override
  public Mono<Product> save(Product product) {
    log.info("Saving product with name: {}", product.getName());
    return super
        .save(product)
        .as(transactionalOperator::transactional)
        .doOnSuccess(productSaved -> log.debug("Product saved: {}", productSaved));
  }

  @Override
  public Mono<Boolean> existById(String id) {
    log.info("Validating existence of the product by id: {}", id);
    return super.repository.existsById(id);
  }

  @Override
  public Mono<Void> deleteById(String id) {
    log.info("Deleting product by id: {}", id);
    return super.repository.deleteById(id);
  }
}
