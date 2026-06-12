package com.gvsolutions.service.mapper;

import com.gvsolutions.domain.AssetCategory;
import com.gvsolutions.domain.AssetSubCategory;
import com.gvsolutions.service.dto.AssetCategoryDTO;
import com.gvsolutions.service.dto.AssetSubCategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AssetSubCategory} and its DTO {@link AssetSubCategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface AssetSubCategoryMapper extends EntityMapper<AssetSubCategoryDTO, AssetSubCategory> {
    @Mapping(target = "category", source = "category", qualifiedByName = "assetCategoryAssetCategoryCode")
    AssetSubCategoryDTO toDto(AssetSubCategory s);

    @Named("assetCategoryAssetCategoryCode")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "assetCategoryCode", source = "assetCategoryCode")
    AssetCategoryDTO toDtoAssetCategoryAssetCategoryCode(AssetCategory assetCategory);
}
