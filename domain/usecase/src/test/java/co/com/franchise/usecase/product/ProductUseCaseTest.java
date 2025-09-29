package co.com.franchise.usecase.product;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import co.com.franchise.model.gateways.ProductRepository;
import co.com.franchise.model.product.Product;
import co.com.franchise.model.product.ProductCreate;
import co.com.franchise.usecase.branch.BranchUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class ProductUseCaseTest {

  private final ProductCreate createData = new ProductCreate("Test-name", "test-id-branch", 20);
  private final Product product = Product
      .builder()
      .id("Test-id")
      .name(createData.name())
      .idBranch(createData.idBranch())
      .stock(createData.stock())
      .build();

  @Mock
  private ProductRepository repository;

  @Mock
  private BranchUseCase branchUseCase;

  @InjectMocks
  private ProductUseCase useCase;

  @Test
  void shouldReturnCreatedProductSuccessfully() {
    // Arrange
    when(repository.save(any(Product.class))).thenReturn(Mono.just(product));
    when(branchUseCase.validateBranchById(anyString())).thenReturn(Mono.empty());

    // Act
    var result = useCase.createProduct(createData);

    // Assert
    StepVerifier
        .create(result)
        .expectNextMatches(created -> created
            .getId()
            .equals(product.getId()) && created
            .getName()
            .equals(product.getName()) && created
            .getIdBranch()
            .equals(product.getIdBranch()) && created
            .getStock()
            .equals(product.getStock()))
        .verifyComplete();
    verify(repository, times(1)).save(any(Product.class));
    verify(branchUseCase, times(1)).validateBranchById(anyString());
  }
}