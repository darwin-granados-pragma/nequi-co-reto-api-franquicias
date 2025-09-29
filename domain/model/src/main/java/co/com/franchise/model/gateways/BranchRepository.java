package co.com.franchise.model.gateways;

import co.com.franchise.model.branch.Branch;
import reactor.core.publisher.Mono;

public interface BranchRepository {

  Mono<Branch> save(Branch branch);
}
