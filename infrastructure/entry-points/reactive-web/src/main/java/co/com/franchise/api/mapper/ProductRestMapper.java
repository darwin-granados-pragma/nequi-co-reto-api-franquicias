package co.com.franchise.api.mapper;

import co.com.franchise.api.model.request.ProductCreateRequest;
import co.com.franchise.api.model.response.ProductResponse;
import co.com.franchise.model.product.Product;
import co.com.franchise.model.product.ProductCreate;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface ProductRestMapper {

  ProductCreate toProductCreate(ProductCreateRequest request);

  ProductResponse toProductResponse(Product product);
}
