package co.com.franchise.usecase.franchise;

import co.com.franchise.model.error.ErrorCode;
import co.com.franchise.model.exception.ObjectNotFoundException;
import co.com.franchise.model.franchise.Franchise;
import co.com.franchise.model.franchise.FranchiseCreate;
import co.com.franchise.model.franchise.FranchiseUpdateName;
import co.com.franchise.model.gateways.FranchiseRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class FranchiseUseCase {

  private final FranchiseRepository repository;

  public Mono<Franchise> createFranchise(FranchiseCreate data) {
    return Mono
        .fromCallable(() -> Franchise
            .builder()
            .id(UUID
                .randomUUID()
                .toString())
            .name(data.name())
            .isNew(true)
            .build())
        .flatMap(repository::save);
  }

  public Mono<Franchise> updateNameByIdFranchise(String idFranchise, FranchiseUpdateName data) {
    return getFranchiseById(idFranchise).flatMap(franchise -> {
      franchise.setName(data.name());
      return repository.save(franchise);
    });
  }

  public Mono<Void> validateFranchiseById(String id) {
    return repository
        .existById(id)
        .flatMap(exists -> Boolean.TRUE.equals(exists) ? Mono.empty()
            : Mono.error(new ObjectNotFoundException(ErrorCode.FRANCHISE_NOT_FOUND, id)));
  }

  private Mono<Franchise> getFranchiseById(String id) {
    return repository
        .findById(id)
        .switchIfEmpty(Mono.error(new ObjectNotFoundException(ErrorCode.FRANCHISE_NOT_FOUND, id)));
  }
}
