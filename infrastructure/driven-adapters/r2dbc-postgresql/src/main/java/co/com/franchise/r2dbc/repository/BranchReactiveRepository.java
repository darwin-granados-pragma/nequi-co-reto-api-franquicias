package co.com.franchise.r2dbc.repository;

import co.com.franchise.r2dbc.entity.BranchEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface BranchReactiveRepository extends ReactiveCrudRepository<BranchEntity, String>,
    ReactiveQueryByExampleExecutor<BranchEntity> {

}
