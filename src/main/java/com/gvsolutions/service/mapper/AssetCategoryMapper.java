package com.gvsolutions.service.mapper;

import com.gvsolutions.domain.AssetCategory;
import com.gvsolutions.service.dto.AssetCategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AssetCategory} and its DTO {@link AssetCategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface AssetCategoryMapper extends EntityMapper<AssetCategoryDTO, AssetCategory> {}
