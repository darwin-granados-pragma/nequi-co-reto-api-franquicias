package co.com.franchise.api.router;

import static org.springframework.web.reactive.function.server.RequestPredicates.PATCH;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import co.com.franchise.api.error.ErrorResponse;
import co.com.franchise.api.error.GlobalErrorWebFilter;
import co.com.franchise.api.handler.FranchiseHandler;
import co.com.franchise.api.model.request.FranchiseCreateRequest;
import co.com.franchise.api.model.request.FranchiseUpdateNameRequest;
import co.com.franchise.api.model.response.FranchiseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@RequiredArgsConstructor
public class FranchiseRouterRest {

  private static final String PATH = "/api/v1/franchise";
  private static final String PATH_ID_FRANCHISE = PATH + "/{idFranchise}";

  private final FranchiseHandler franchiseHandler;
  private final GlobalErrorWebFilter globalErrorWebFilter;

  @Bean
  @RouterOperations({@RouterOperation(method = RequestMethod.POST,
      path = PATH,
      beanClass = FranchiseHandler.class,
      beanMethod = "createFranchise",
      operation = @Operation(operationId = "createFranchise",
          summary = "Create franchise",
          description = "Receives data of the franchise and return the created object.",
          requestBody = @RequestBody(required = true,
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = FranchiseCreateRequest.class)
              )
          ),
          responses = {@ApiResponse(responseCode = "200",
              description = "Franchise created successfully.",
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = FranchiseResponse.class)
              )
          ), @ApiResponse(responseCode = "400",
              description = "Parameters invalid or missing.",
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class)
              )
          ), @ApiResponse(responseCode = "409",
              description = "Franchise with that name already exists.",
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class)
              )
          )}
      )
  ), @RouterOperation(method = RequestMethod.PATCH,
      path = PATH_ID_FRANCHISE,
      beanClass = FranchiseHandler.class,
      beanMethod = "updateNameByIdFranchise",
      operation = @Operation(operationId = "updateNameByIdFranchise",
          summary = "Update name of the franchise",
          description = "Receives data of the franchise and return the updated object.",
          parameters = {@Parameter(name = "idFranchise",
              in = ParameterIn.PATH,
              description = "Identifier of the franchise",
              required = true,
              schema = @Schema(type = "String")
          )},
          requestBody = @RequestBody(required = true,
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = FranchiseUpdateNameRequest.class)
              )
          ),
          responses = {@ApiResponse(responseCode = "200",
              description = "Name of the franchise updated successfully.",
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = FranchiseResponse.class)
              )
          ), @ApiResponse(responseCode = "400",
              description = "Parameters invalid or missing.",
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class)
              )
          ), @ApiResponse(responseCode = "404",
              description = "Franchise not found.",
              content = @Content(mediaType = "application/json",
                  schema = @Schema(implementation = ErrorResponse.class)
              )
          )}
      )
  )}
  )
  public RouterFunction<ServerResponse> franchiseRouterFunction() {
    return route(POST(PATH), franchiseHandler::createFranchise)
        .andRoute(PATCH(PATH_ID_FRANCHISE), franchiseHandler::updateNameByIdFranchise)
        .filter(globalErrorWebFilter);
  }
}
