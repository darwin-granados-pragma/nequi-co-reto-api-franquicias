package co.com.franchise.r2dbc.repository;

import co.com.franchise.r2dbc.entity.FranchiseEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface FranchiseReactiveRepository extends
    ReactiveCrudRepository<FranchiseEntity, String>,
    ReactiveQueryByExampleExecutor<FranchiseEntity> {

}
