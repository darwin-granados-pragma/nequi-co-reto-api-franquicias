package co.com.franchise.usecase.branch;

import co.com.franchise.model.branch.Branch;
import co.com.franchise.model.branch.BranchCreate;
import co.com.franchise.model.branch.BranchDomainResponse;
import co.com.franchise.model.branch.BranchUpdateName;
import co.com.franchise.model.error.ErrorCode;
import co.com.franchise.model.exception.ObjectNotFoundException;
import co.com.franchise.model.gateways.BranchRepository;
import co.com.franchise.usecase.franchise.FranchiseUseCase;
import co.com.franchise.usecase.product.ProductRetrieveUseCase;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class BranchUseCase {

  private final BranchRepository repository;
  private final FranchiseUseCase franchiseUseCase;
  private final ProductRetrieveUseCase productRetrieveUseCase;

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

  public Flux<BranchDomainResponse> getTopProductStockByIdFranchise(String idFranchise) {
    return franchiseUseCase
        .validateFranchiseById(idFranchise)
        .thenMany(repository
            .findAllByIdFranchise(idFranchise)
            .flatMap(this::mapToBranchResponse));
  }

  public Mono<Branch> updateNameByIdBranch(String idBranch, BranchUpdateName data) {
    return getBranchById(idBranch).flatMap(branch -> {
      branch.setName(data.name());
      return repository.save(branch);
    });
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

  private Mono<BranchDomainResponse> mapToBranchResponse(Branch branch) {
    return productRetrieveUseCase
        .getTopProductStockByIdBranch(branch.getId())
        .map(productResponse -> BranchDomainResponse
            .builder()
            .id(branch.getId())
            .name(branch.getName())
            .productResponse(productResponse)
            .build());
  }

  private Mono<Branch> getBranchById(String id) {
    return repository
        .findById(id)
        .switchIfEmpty(Mono.error(new ObjectNotFoundException(ErrorCode.BRANCH_NOT_FOUND, id)));
  }
}
