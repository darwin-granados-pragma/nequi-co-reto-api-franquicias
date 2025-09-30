package co.com.franchise.usecase.branch;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import co.com.franchise.model.branch.Branch;
import co.com.franchise.model.branch.BranchCreate;
import co.com.franchise.model.exception.ObjectNotFoundException;
import co.com.franchise.model.gateways.BranchRepository;
import co.com.franchise.model.product.ProductDomainResponse;
import co.com.franchise.usecase.franchise.FranchiseUseCase;
import co.com.franchise.usecase.product.ProductRetrieveUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class BranchUseCaseTest {

  private final BranchCreate createData = new BranchCreate("Test-name", "test-id-franchise");
  private final Branch branch = Branch
      .builder()
      .id("Test-id")
      .name(createData.name())
      .idFranchise(createData.idFranchise())
      .build();
  private final String idBranch = branch.getId();

  @Mock
  private BranchRepository repository;

  @Mock
  private FranchiseUseCase franchiseUseCase;

  @Mock
  private ProductRetrieveUseCase productRetrieveUseCase;

  @InjectMocks
  private BranchUseCase useCase;

  @Test
  void shouldReturnCreatedBranchSuccessfully() {
    // Arrange
    when(repository.save(any(Branch.class))).thenReturn(Mono.just(branch));
    when(franchiseUseCase.validateFranchiseById(anyString())).thenReturn(Mono.empty());

    // Act
    var result = useCase.createBranch(createData);

    // Assert
    StepVerifier
        .create(result)
        .expectNextMatches(created -> created
            .getId()
            .equals(branch.getId()) && created
            .getName()
            .equals(branch.getName()) && created
            .getIdFranchise()
            .equals(branch.getIdFranchise()))
        .verifyComplete();
    verify(repository, times(1)).save(any(Branch.class));
    verify(franchiseUseCase, times(1)).validateFranchiseById(anyString());
  }

  @Test
  void shouldValidateBranchById() {
    // Arrange
    when(repository.existById(idBranch)).thenReturn(Mono.just(true));

    // Act
    var result = useCase.validateBranchById(idBranch);

    // Assert
    StepVerifier
        .create(result)
        .verifyComplete();
    verify(repository, times(1)).existById(idBranch);
  }

  @Test
  void shouldThrowObjectNotFoundException_WhenBranchNotExist() {
    // Arrange
    when(repository.existById(idBranch)).thenReturn(Mono.just(false));

    // Act
    var result = useCase.validateBranchById(idBranch);

    // Assert
    StepVerifier
        .create(result)
        .expectError(ObjectNotFoundException.class)
        .verify();
    verify(repository, times(1)).existById(idBranch);
  }

  @Test
  void shouldGetTopProductStockByIdFranchise() {
    // Arrange
    String idFranchise = "franchise-123";
    var branch1 = Branch.builder().id("branch-1").idFranchise(idFranchise).name("test name").build();
    var branch2 = Branch.builder().id("branch-2").idFranchise(idFranchise).name("test name 2").build();
    var topProduct1 = new ProductDomainResponse("prod-A", "Producto Top A", 100);
    var topProduct2 = new ProductDomainResponse("prod-B", "Producto Top B", 150);

    when(franchiseUseCase.validateFranchiseById(idFranchise)).thenReturn(Mono.empty());
    when(repository.findAllByIdFranchise(idFranchise)).thenReturn(Flux.just(branch1, branch2));
    when(productRetrieveUseCase.getTopProductStockByIdBranch(branch1.getId())).thenReturn(Mono.just(topProduct1));
    when(productRetrieveUseCase.getTopProductStockByIdBranch(branch2.getId())).thenReturn(Mono.just(topProduct2));

    // Act
    var resultFlux = useCase.getTopProductStockByIdFranchise(idFranchise);

    // Assert
    StepVerifier.create(resultFlux)
        .expectNextMatches(response ->
            response.getId().equals(branch1.getId()) &&
                response.getName().equals(branch1.getName()) &&
                response.getProductResponse().getId().equals(topProduct1.getId())
        )
        .expectNextMatches(response ->
            response.getId().equals(branch2.getId()) &&
                response.getName().equals(branch2.getName()) &&
                response.getProductResponse().getId().equals(topProduct2.getId())
        )
        .verifyComplete();

    verify(franchiseUseCase, times(1)).validateFranchiseById(idFranchise);
    verify(repository, times(1)).findAllByIdFranchise(idFranchise);
    verify(productRetrieveUseCase, times(2)).getTopProductStockByIdBranch(anyString());
  }
}