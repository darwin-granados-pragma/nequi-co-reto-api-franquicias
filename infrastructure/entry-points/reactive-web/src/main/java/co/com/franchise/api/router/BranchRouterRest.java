package co.com.franchise.api.router;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import co.com.franchise.api.error.ErrorResponse;
import co.com.franchise.api.error.GlobalErrorWebFilter;
import co.com.franchise.api.handler.BranchHandler;
import co.com.franchise.api.model.request.BranchCreateRequest;
import co.com.franchise.api.model.response.BranchResponse;
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
public class BranchRouterRest {

  private static final String PATH = "/api/v1/branch";

  private final BranchHandler branchHandler;
  private final GlobalErrorWebFilter globalErrorWebFilter;

  @Bean
  @RouterOperation(method = RequestMethod.POST,
      path = PATH,
      beanClass = BranchHandler.class,
      beanMethod = "createBranch",
      operation = @Operation(operationId = "createBranch",
          summary = "Create branch",
          description = "Receives data of the branch and return the created object.",
          requestBody = @RequestBody(required = true,
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = BranchCreateRequest.class)
              )
          ),
          responses = {@ApiResponse(responseCode = "200",
              description = "Branch created successfully.",
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = BranchResponse.class)
              )
          ), @ApiResponse(responseCode = "400",
              description = "Parameters invalid or missing.",
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class)
              )
          ), @ApiResponse(responseCode = "409",
              description = "Branch with that name already exists.",
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class)
              )
          )}
      )
  )
  public RouterFunction<ServerResponse> branchRouterFunction() {
    return route(POST(PATH), branchHandler::createBranch).filter(globalErrorWebFilter);
  }
}
