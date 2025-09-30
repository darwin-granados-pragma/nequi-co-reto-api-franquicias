package co.com.franchise.api.mapper;

import co.com.franchise.api.model.request.BranchCreateRequest;
import co.com.franchise.api.model.request.BranchUpdateNameRequest;
import co.com.franchise.api.model.response.BranchProductRestResponse;
import co.com.franchise.api.model.response.BranchResponse;
import co.com.franchise.model.branch.Branch;
import co.com.franchise.model.branch.BranchCreate;
import co.com.franchise.model.branch.BranchDomainResponse;
import co.com.franchise.model.branch.BranchUpdateName;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING, uses = {ProductRestMapper.class})
public interface BranchRestMapper {

  BranchCreate toBranchCreate(BranchCreateRequest request);

  BranchResponse toBranchResponse(Branch franchise);

  BranchProductRestResponse toBranchProductRestResponse(BranchDomainResponse response);

  BranchUpdateName toBranchUpdateName(BranchUpdateNameRequest request);

}
