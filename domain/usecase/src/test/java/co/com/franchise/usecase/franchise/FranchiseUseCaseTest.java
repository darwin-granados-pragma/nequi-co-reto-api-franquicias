package co.com.franchise.usecase.franchise;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import co.com.franchise.model.exception.ObjectNotFoundException;
import co.com.franchise.model.franchise.Franchise;
import co.com.franchise.model.franchise.FranchiseCreate;
import co.com.franchise.model.gateways.FranchiseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class FranchiseUseCaseTest {

  private final FranchiseCreate createData = new FranchiseCreate("Test-name");
  private final Franchise franchise = Franchise
      .builder()
      .id("Test-id")
      .name(createData.name())
      .build();
  private final String idFranchise = franchise.getId();

  @Mock
  private FranchiseRepository repository;

  @InjectMocks
  private FranchiseUseCase useCase;

  @Test
  void shouldReturnCreatedFranchise() {
    // Arrange
    when(repository.save(any(Franchise.class))).thenReturn(Mono.just(franchise));

    // Act
    var result = useCase.createFranchise(createData);

    // Assert
    StepVerifier
        .create(result)
        .expectNextMatches(created -> created
            .getId()
            .equals(franchise.getId()) && created
            .getName()
            .equals(franchise.getName()))
        .verifyComplete();
    verify(repository, times(1)).save(any(Franchise.class));
  }

  @Test
  void shouldValidateFranchiseById() {
    // Arrange
    when(repository.existById(idFranchise)).thenReturn(Mono.just(true));

    // Act
    var result = useCase.validateFranchiseById(idFranchise);

    // Assert
    StepVerifier
        .create(result)
        .verifyComplete();
    verify(repository, times(1)).existById(idFranchise);
  }

  @Test
  void shouldThrowObjectNotFoundException_WhenFranchiseNotExist() {
    // Arrange
    when(repository.existById(idFranchise)).thenReturn(Mono.just(false));

    // Act
    var result = useCase.validateFranchiseById(idFranchise);

    // Assert
    StepVerifier
        .create(result)
        .expectError(ObjectNotFoundException.class)
        .verify();
    verify(repository, times(1)).existById(idFranchise);
  }
}