package co.com.franchise.r2dbc.repository;

import co.com.franchise.r2dbc.entity.ProductEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ProductReactiveRepository extends ReactiveCrudRepository<ProductEntity, String>,
    ReactiveQueryByExampleExecutor<ProductEntity> {

  Mono<ProductEntity> findTopByIdBranchOrderByStockDesc(String idBranch);
}
