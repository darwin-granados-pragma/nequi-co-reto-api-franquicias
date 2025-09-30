package co.com.franchise.api.handler;

import co.com.franchise.api.mapper.ProductRestMapper;
import co.com.franchise.api.model.request.ProductCreateRequest;
import co.com.franchise.api.model.request.ProductUpdateNameRequest;
import co.com.franchise.api.model.request.ProductUpdateStockRequest;
import co.com.franchise.model.product.ProductCreate;
import co.com.franchise.model.product.ProductUpdateName;
import co.com.franchise.model.product.ProductUpdateStock;
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

  private static final String ID_PRODUCT = "idProduct";

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
      String idProduct = serverRequest.pathVariable(ID_PRODUCT);
      return useCase
          .deleteById(idProduct)
          .then(ServerResponse
              .noContent()
              .build());
    });
  }

  public Mono<ServerResponse> updateStockByIdProduct(ServerRequest serverRequest) {
    log.info("Received request to update stock of the product at path={} method={}",
        serverRequest.path(),
        serverRequest.method()
    );
    return Mono.defer(() -> {
      String idProduct = serverRequest.pathVariable(ID_PRODUCT);
      return serverRequest
          .bodyToMono(ProductUpdateStockRequest.class)
          .flatMap(request -> requestValidator
              .validate(request)
              .then(Mono.defer(() -> {
                ProductUpdateStock updateStock = mapper.toProductUpdateStock(request);
                return useCase
                    .updateStockByIdProduct(idProduct, updateStock)
                    .map(mapper::toProductResponse)
                    .flatMap(response -> ServerResponse
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response));
              })));
    });
  }

  public Mono<ServerResponse> updateNameByIdProduct(ServerRequest serverRequest) {
    log.info("Received request to update name of the product at path={} method={}",
        serverRequest.path(),
        serverRequest.method()
    );
    return Mono.defer(() -> {
      String idProduct = serverRequest.pathVariable(ID_PRODUCT);
      return serverRequest
          .bodyToMono(ProductUpdateNameRequest.class)
          .flatMap(request -> requestValidator
              .validate(request)
              .then(Mono.defer(() -> {
                ProductUpdateName updatedData = mapper.toProductUpdateName(request);
                return useCase
                    .updateNameByIdProduct(idProduct, updatedData)
                    .map(mapper::toProductResponse)
                    .flatMap(response -> ServerResponse
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response));
              })));
    });
  }
}
