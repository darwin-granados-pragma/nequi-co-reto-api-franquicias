package co.com.franchise.model.gateways;

import co.com.franchise.model.branch.Branch;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BranchRepository {

  Mono<Branch> save(Branch branch);

  Mono<Boolean> existById(String id);

  Flux<Branch> findAllByIdFranchise(String idFranchise);

  Mono<Branch> findById(String id);

}
