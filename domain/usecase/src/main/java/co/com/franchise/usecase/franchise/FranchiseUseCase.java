package co.com.franchise.usecase.franchise;

import co.com.franchise.model.franchise.Franchise;
import co.com.franchise.model.franchise.FranchiseCreate;
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
}
