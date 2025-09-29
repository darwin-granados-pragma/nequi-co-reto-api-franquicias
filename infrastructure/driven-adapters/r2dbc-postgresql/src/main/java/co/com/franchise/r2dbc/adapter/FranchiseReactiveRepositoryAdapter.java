package co.com.franchise.r2dbc.adapter;

import co.com.franchise.model.error.ErrorCode;
import co.com.franchise.model.exception.ConstraintException;
import co.com.franchise.model.franchise.Franchise;
import co.com.franchise.model.gateways.FranchiseRepository;
import co.com.franchise.r2dbc.entity.FranchiseEntity;
import co.com.franchise.r2dbc.helper.ReactiveAdapterOperations;
import co.com.franchise.r2dbc.repository.FranchiseReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
public class FranchiseReactiveRepositoryAdapter extends
    ReactiveAdapterOperations<Franchise, FranchiseEntity, String, FranchiseReactiveRepository> implements
    FranchiseRepository {

  private final TransactionalOperator transactionalOperator;

  public FranchiseReactiveRepositoryAdapter(FranchiseReactiveRepository repository,
      ObjectMapper mapper, TransactionalOperator transactionalOperator) {
    super(repository, mapper, d -> mapper.map(d, Franchise.class));
    this.transactionalOperator = transactionalOperator;
  }

  @Override
  public Mono<Franchise> save(Franchise franchise) {
    log.info("Saving franchise with name: {}", franchise.getName());
    return super
        .save(franchise)
        .as(transactionalOperator::transactional)
        .onErrorMap(DataIntegrityViolationException.class, e -> {
              log.error("Data integrity violation: {}", e.getMessage());
              String message = e.getMessage();
              if (message.contains("name_unique_constraint")) {
                return new ConstraintException(ErrorCode.FRANCHISE_NAME_ALREADY_EXISTS);
              }
              return new ConstraintException(ErrorCode.CONSTRAINT_VIOLATION);
            }
        )
        .doOnSuccess(franchiseSaved -> log.debug("Franchise saved: {}", franchiseSaved));
  }

  @Override
  public Mono<Boolean> existById(String id) {
    log.info("Validating existence of the franchise by id: {}", id);
    return super.repository.existsById(id);
  }
}
