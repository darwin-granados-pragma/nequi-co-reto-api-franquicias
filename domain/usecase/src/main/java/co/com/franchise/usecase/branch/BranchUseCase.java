package co.com.franchise.usecase.branch;

import co.com.franchise.model.branch.Branch;
import co.com.franchise.model.branch.BranchCreate;
import co.com.franchise.model.error.ErrorCode;
import co.com.franchise.model.exception.ObjectNotFoundException;
import co.com.franchise.model.gateways.BranchRepository;
import co.com.franchise.usecase.franchise.FranchiseUseCase;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class BranchUseCase {

  private final BranchRepository repository;
  private final FranchiseUseCase franchiseUseCase;

  public Mono<Branch> createBranch(BranchCreate data) {
    return franchiseUseCase
        .validateFranchiseById(data.idFranchise())
        .then(buildAndSave(data));
  }

  public Mono<Void> validateBranchById(String id) {
    return repository
        .existById(id)
        .flatMap(exists -> Boolean.TRUE.equals(exists) ? Mono.empty()
            : Mono.error(new ObjectNotFoundException(ErrorCode.BRANCH_NOT_FOUND, id)));
  }

  private Mono<Branch> buildAndSave(BranchCreate data) {
    return Mono.defer(() -> {
      Branch branch = Branch
          .builder()
          .id(UUID
              .randomUUID()
              .toString())
          .name(data.name())
          .idFranchise(data.idFranchise())
          .isNew(true)
          .build();
      return repository.save(branch);
    });
  }
}
