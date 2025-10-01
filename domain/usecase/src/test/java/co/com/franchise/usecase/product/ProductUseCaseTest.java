package co.com.franchise.usecase.product;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import co.com.franchise.model.exception.ObjectNotFoundException;
import co.com.franchise.model.gateways.ProductRepository;
import co.com.franchise.model.product.Product;
import co.com.franchise.model.product.ProductCreate;
import co.com.franchise.model.product.ProductUpdateName;
import co.com.franchise.model.product.ProductUpdateStock;
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
  private final String idProduct = product.getId();
  private final ProductUpdateStock updateData = new ProductUpdateStock(36);
  private final ProductUpdateName updateNameData = new ProductUpdateName("test update name");

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

  @Test
  void shouldDeleteProductByIdSuccessfully() {
    // Arrange
    when(repository.existById(idProduct)).thenReturn(Mono.just(true));
    when(repository.deleteById(idProduct)).thenReturn(Mono.empty());

    // Act
    var result = useCase.deleteById(idProduct);

    // Arrange
    StepVerifier
        .create(result)
        .verifyComplete();
    verify(repository, times(1)).existById(idProduct);
    verify(repository, times(1)).deleteById(idProduct);
  }

  @Test
  void shouldThrowObjectNotFoundException_WhenProductByIdNotFound() {
    // Arrange
    when(repository.existById(idProduct)).thenReturn(Mono.just(false));

    // Act
    var result = useCase.deleteById(idProduct);

    // Arrange
    StepVerifier
        .create(result)
        .expectError(ObjectNotFoundException.class)
        .verify();
    verify(repository, times(1)).existById(idProduct);
    verify(repository, never()).deleteById(idProduct);
  }

  @Test
  void shouldUpdateProductStockSuccessfully() {
    // Arrange
    when(repository.findById(idProduct)).thenReturn(Mono.just(product));
    product.setStock(updateData.stock());
    when(repository.save(any(Product.class))).thenReturn(Mono.just(product));

    // Act
    var result = useCase.updateStockByIdProduct(idProduct, updateData);

    // Arrange
    StepVerifier
        .create(result)
        .expectNextMatches(updated -> updated
            .getId()
            .equals(product.getId()) && updated
            .getName()
            .equals(product.getName()) && updated
            .getIdBranch()
            .equals(product.getIdBranch()) && updated
            .getStock()
            .equals(product.getStock()))
        .verifyComplete();
    verify(repository, times(1)).findById(idProduct);
    verify(repository, times(1)).save(any(Product.class));
  }

  @Test
  void updateStockByIdProduct_ShouldThrowObjectNotFoundException_WhenProductByIdNotFound() {
    // Arrange
    when(repository.findById(idProduct)).thenReturn(Mono.empty());

    // Act
    var result = useCase.updateStockByIdProduct(idProduct, updateData);

    // Arrange
    StepVerifier
        .create(result)
        .expectError(ObjectNotFoundException.class)
        .verify();
    verify(repository, times(1)).findById(idProduct);
    verify(repository, never()).save(any(Product.class));
  }

  @Test
  void shouldUpdateProductNameSuccessfully() {
    // Arrange
    when(repository.findById(idProduct)).thenReturn(Mono.just(product));
    product.setName(updateNameData.name());
    when(repository.save(any(Product.class))).thenReturn(Mono.just(product));

    // Act
    var result = useCase.updateNameByIdProduct(idProduct, updateNameData);

    // Arrange
    StepVerifier
        .create(result)
        .expectNextMatches(updated -> updated
            .getId()
            .equals(product.getId()) && updated
            .getName()
            .equals(product.getName()))
        .verifyComplete();
    verify(repository, times(1)).findById(idProduct);
    verify(repository, times(1)).save(any(Product.class));
  }
}