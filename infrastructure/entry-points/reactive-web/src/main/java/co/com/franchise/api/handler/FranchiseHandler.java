package co.com.franchise.api.handler;

import co.com.franchise.api.mapper.FranchiseRestMapper;
import co.com.franchise.api.model.request.FranchiseCreateRequest;
import co.com.franchise.api.model.request.FranchiseUpdateNameRequest;
import co.com.franchise.model.franchise.FranchiseCreate;
import co.com.franchise.model.franchise.FranchiseUpdateName;
import co.com.franchise.usecase.franchise.FranchiseUseCase;
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
public class FranchiseHandler {

  private final FranchiseUseCase useCase;
  private final FranchiseRestMapper mapper;
  private final RequestValidator requestValidator;

  public Mono<ServerResponse> createFranchise(ServerRequest serverRequest) {
    log.info("Received request to create a franchise at path={} method={}",
        serverRequest.path(),
        serverRequest.method()
    );
    return serverRequest
        .bodyToMono(FranchiseCreateRequest.class)
        .flatMap(request -> requestValidator
            .validate(request)
            .then(Mono.defer(() -> {
              FranchiseCreate franchiseCreate = mapper.toFranchiseCreate(request);
              return useCase
                  .createFranchise(franchiseCreate)
                  .map(mapper::toFranchiseResponse)
                  .flatMap(response -> ServerResponse
                      .status(HttpStatus.CREATED)
                      .contentType(MediaType.APPLICATION_JSON)
                      .bodyValue(response));
            })));
  }

  public Mono<ServerResponse> updateNameByIdFranchise(ServerRequest serverRequest) {
    log.info("Received request to update name of the franchise at path={} method={}",
        serverRequest.path(),
        serverRequest.method()
    );
    return Mono.defer(() -> {
      String idFranchise = serverRequest.pathVariable("idFranchise");
      return serverRequest
          .bodyToMono(FranchiseUpdateNameRequest.class)
          .flatMap(request -> requestValidator
              .validate(request)
              .then(Mono.defer(() -> {
                FranchiseUpdateName updatedData = mapper.toFranchiseUpdateName(request);
                return useCase
                    .updateNameByIdFranchise(idFranchise, updatedData)
                    .map(mapper::toFranchiseResponse)
                    .flatMap(response -> ServerResponse
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response));
              })));
    });
  }
}
