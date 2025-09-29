package co.com.franchise.api.handler;

import co.com.franchise.api.mapper.BranchRestMapper;
import co.com.franchise.api.model.request.BranchCreateRequest;
import co.com.franchise.model.branch.BranchCreate;
import co.com.franchise.usecase.branch.BranchUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class BranchHandler {

  private final BranchUseCase useCase;
  private final BranchRestMapper mapper;
  private final RequestValidator requestValidator;

  public Mono<ServerResponse> createBranch(ServerRequest serverRequest) {
    log.info("Received request to create a branch at path={} method={}",
        serverRequest.path(),
        serverRequest.method()
    );
    return serverRequest
        .bodyToMono(BranchCreateRequest.class)
        .flatMap(request -> requestValidator
            .validate(request)
            .then(Mono.defer(() -> {
              BranchCreate branchCreate = mapper.toBranchCreate(request);
              return useCase
                  .createBranch(branchCreate)
                  .map(mapper::toBranchResponse)
                  .flatMap(response -> ServerResponse
                      .status(HttpStatus.CREATED)
                      .contentType(MediaType.APPLICATION_JSON)
                      .bodyValue(response));
            })));
  }

}
