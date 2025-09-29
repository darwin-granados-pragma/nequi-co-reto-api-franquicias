package co.com.franchise.usecase.branch;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import co.com.franchise.model.branch.Branch;
import co.com.franchise.model.branch.BranchCreate;
import co.com.franchise.model.gateways.BranchRepository;
import co.com.franchise.usecase.franchise.FranchiseUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

  @Mock
  private BranchRepository repository;

  @Mock
  private FranchiseUseCase franchiseUseCase;

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
}