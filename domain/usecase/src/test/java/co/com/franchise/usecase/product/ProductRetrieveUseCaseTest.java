package co.com.franchise.usecase.product;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import co.com.franchise.model.gateways.ProductRepository;
import co.com.franchise.model.product.Product;
import co.com.franchise.model.product.ProductDomainResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class ProductRetrieveUseCaseTest {

  private final String idBranch = "branch-123";

  @Mock
  private ProductRepository repository;

  @InjectMocks
  private ProductRetrieveUseCase useCase;

  @Test
  void shouldReturnTopProductWhenFound() {
    // Arrange
    var product = Product
        .builder()
        .id("prod-abc")
        .name("test product name")
        .stock(200)
        .idBranch(idBranch)
        .build();

    when(repository.findTopByIdBranch(idBranch)).thenReturn(Mono.just(product));

    // Act
    Mono<ProductDomainResponse> resultMono = useCase.getTopProductStockByIdBranch(idBranch);

    // Assert
    StepVerifier
        .create(resultMono)
        .expectNextMatches(response -> response
            .getId()
            .equals(product.getId()) && response
            .getName()
            .equals(product.getName()) && response
            .getStock()
            .equals(product.getStock()))
        .verifyComplete();

    verify(repository, times(1)).findTopByIdBranch(idBranch);
  }

  @Test
  void shouldReturnEmptyMonoWhenProductNotFound() {
    // Arrange
    when(repository.findTopByIdBranch(idBranch)).thenReturn(Mono.empty());
    // Act
    Mono<ProductDomainResponse> resultMono = useCase.getTopProductStockByIdBranch(idBranch);

    // Assert
    StepVerifier
        .create(resultMono)
        .verifyComplete();

    verify(repository, times(1)).findTopByIdBranch(idBranch);
  }
}