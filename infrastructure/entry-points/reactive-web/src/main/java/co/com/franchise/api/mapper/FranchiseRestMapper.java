package co.com.franchise.api.mapper;

import co.com.franchise.api.model.request.FranchiseCreateRequest;
import co.com.franchise.api.model.response.FranchiseResponse;
import co.com.franchise.model.franchise.Franchise;
import co.com.franchise.model.franchise.FranchiseCreate;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface FranchiseRestMapper {

  FranchiseCreate toFranchiseCreate(FranchiseCreateRequest request);

  FranchiseResponse toFranchiseResponse(Franchise franchise);
}
