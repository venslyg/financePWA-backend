package com.gvsolutions.service.mapper;

import com.gvsolutions.domain.AssetDepreciationHistory;
import com.gvsolutions.service.dto.AssetDepreciationHistoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AssetDepreciationHistory} and its DTO {@link AssetDepreciationHistoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface AssetDepreciationHistoryMapper extends EntityMapper<AssetDepreciationHistoryDTO, AssetDepreciationHistory> {}
