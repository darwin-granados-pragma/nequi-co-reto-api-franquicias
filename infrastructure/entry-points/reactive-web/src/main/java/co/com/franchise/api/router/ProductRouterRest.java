package co.com.franchise.api.router;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import co.com.franchise.api.error.ErrorResponse;
import co.com.franchise.api.error.GlobalErrorWebFilter;
import co.com.franchise.api.handler.ProductHandler;
import co.com.franchise.api.model.request.ProductCreateRequest;
import co.com.franchise.api.model.response.ProductResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@RequiredArgsConstructor
public class ProductRouterRest {

  private static final String PATH = "/api/v1/product";

  private final ProductHandler productHandler;
  private final GlobalErrorWebFilter globalErrorWebFilter;

  @Bean
  @RouterOperation(method = RequestMethod.POST,
      path = PATH,
      beanClass = ProductHandler.class,
      beanMethod = "createProduct",
      operation = @Operation(operationId = "createProduct",
          summary = "Create product",
          description = "Receives data of the product and return the created object.",
          requestBody = @RequestBody(required = true,
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = ProductCreateRequest.class)
              )
          ),
          responses = {@ApiResponse(responseCode = "200",
              description = "Product created successfully.",
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = ProductResponse.class)
              )
          ), @ApiResponse(responseCode = "400",
              description = "Parameters invalid or missing.",
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class)
              )
          )}
      )
  )
  public RouterFunction<ServerResponse> productRouterFunction() {
    return route(POST(PATH), productHandler::createProduct).filter(globalErrorWebFilter);
  }
}
