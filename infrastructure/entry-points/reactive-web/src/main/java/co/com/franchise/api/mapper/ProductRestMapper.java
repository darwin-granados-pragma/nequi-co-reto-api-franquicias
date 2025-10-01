package co.com.franchise.api.mapper;

import co.com.franchise.api.model.request.ProductCreateRequest;
import co.com.franchise.api.model.request.ProductUpdateNameRequest;
import co.com.franchise.api.model.request.ProductUpdateStockRequest;
import co.com.franchise.api.model.response.ProductResponse;
import co.com.franchise.api.model.response.ProductRestResponse;
import co.com.franchise.model.product.Product;
import co.com.franchise.model.product.ProductCreate;
import co.com.franchise.model.product.ProductDomainResponse;
import co.com.franchise.model.product.ProductUpdateName;
import co.com.franchise.model.product.ProductUpdateStock;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface ProductRestMapper {

  ProductCreate toProductCreate(ProductCreateRequest request);

  ProductUpdateStock toProductUpdateStock(ProductUpdateStockRequest request);

  ProductResponse toProductResponse(Product product);

  ProductRestResponse toProductRestResponse(ProductDomainResponse response);

  ProductUpdateName toProductUpdateName(ProductUpdateNameRequest request);
}
