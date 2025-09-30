package co.com.franchise.api.handler;

import co.com.franchise.api.mapper.ProductRestMapper;
import co.com.franchise.api.model.request.ProductCreateRequest;
import co.com.franchise.model.product.ProductCreate;
import co.com.franchise.usecase.product.ProductUseCase;
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
public class ProductHandler {

  private final ProductUseCase useCase;
  private final ProductRestMapper mapper;
  private final RequestValidator requestValidator;

  public Mono<ServerResponse> createProduct(ServerRequest serverRequest) {
    log.info("Received request to create a product at path={} method={}",
        serverRequest.path(),
        serverRequest.method()
    );
    return serverRequest
        .bodyToMono(ProductCreateRequest.class)
        .flatMap(request -> requestValidator
            .validate(request)
            .then(Mono.defer(() -> {
              ProductCreate productCreate = mapper.toProductCreate(request);
              return useCase
                  .createProduct(productCreate)
                  .map(mapper::toProductResponse)
                  .flatMap(response -> ServerResponse
                      .status(HttpStatus.CREATED)
                      .contentType(MediaType.APPLICATION_JSON)
                      .bodyValue(response));
            })));
  }

  public Mono<ServerResponse> deleteProduct(ServerRequest serverRequest) {
    log.info("Received request to delete a product at path={} method={}",
        serverRequest.path(),
        serverRequest.method()
    );
    return Mono.defer(() -> {
      String idProduct = serverRequest.pathVariable("idProduct");
      return useCase
          .deleteById(idProduct)
          .then(ServerResponse
              .noContent()
              .build());
    });
  }
}
