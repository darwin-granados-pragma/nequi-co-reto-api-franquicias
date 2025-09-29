package co.com.franchise.api.mapper;

import co.com.franchise.api.model.request.BranchCreateRequest;
import co.com.franchise.api.model.response.BranchResponse;
import co.com.franchise.model.branch.Branch;
import co.com.franchise.model.branch.BranchCreate;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface BranchRestMapper {

  BranchCreate toBranchCreate(BranchCreateRequest request);

  BranchResponse toBranchResponse(Branch franchise);
}
