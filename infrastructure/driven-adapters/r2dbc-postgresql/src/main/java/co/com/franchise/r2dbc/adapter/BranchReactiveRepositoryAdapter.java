package co.com.franchise.r2dbc.adapter;

import co.com.franchise.model.branch.Branch;
import co.com.franchise.model.error.ErrorCode;
import co.com.franchise.model.exception.ConstraintException;
import co.com.franchise.model.gateways.BranchRepository;
import co.com.franchise.r2dbc.entity.BranchEntity;
import co.com.franchise.r2dbc.helper.ReactiveAdapterOperations;
import co.com.franchise.r2dbc.repository.BranchReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
public class BranchReactiveRepositoryAdapter extends
    ReactiveAdapterOperations<Branch, BranchEntity, String, BranchReactiveRepository> implements
    BranchRepository {

  private final TransactionalOperator transactionalOperator;

  public BranchReactiveRepositoryAdapter(BranchReactiveRepository repository, ObjectMapper mapper,
      TransactionalOperator transactionalOperator) {
    super(repository, mapper, d -> mapper.map(d, Branch.class));
    this.transactionalOperator = transactionalOperator;
  }

  @Override
  public Mono<Branch> save(Branch branch) {
    log.info("Saving branch with name: {}", branch.getName());
    return super
        .save(branch)
        .as(transactionalOperator::transactional)
        .onErrorMap(DataIntegrityViolationException.class, e -> {
              log.error("Data integrity violation: {}", e.getMessage());
              String message = e.getMessage();
              if (message.contains("branch_name_unique_constraint")) {
                return new ConstraintException(ErrorCode.BRANCH_NAME_ALREADY_EXISTS);
              }
              return new ConstraintException(ErrorCode.CONSTRAINT_VIOLATION);
            }
        )
        .doOnSuccess(branchSaved -> log.debug("Branch saved: {}", branchSaved));
  }

  @Override
  public Mono<Boolean> existById(String id) {
    log.info("Validating existence of the branch by id: {}", id);
    return super.repository.existsById(id);
  }

  @Override
  public Flux<Branch> findAllByIdFranchise(String idFranchise) {
    log.info("Retrieving branches by franchise with id: {}", idFranchise);
    return super.repository
        .findAllByIdFranchise(idFranchise)
        .map(this::toEntity);
  }
}
