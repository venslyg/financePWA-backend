package com.gvsolutions.service.mapper;

import com.gvsolutions.domain.AssetRegister;
import com.gvsolutions.domain.MaintenanceLog;
import com.gvsolutions.service.dto.AssetRegisterDTO;
import com.gvsolutions.service.dto.MaintenanceLogDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MaintenanceLog} and its DTO {@link MaintenanceLogDTO}.
 */
@Mapper(componentModel = "spring")
public interface MaintenanceLogMapper extends EntityMapper<MaintenanceLogDTO, MaintenanceLog> {
    @Mapping(target = "asset", source = "asset", qualifiedByName = "assetRegisterAssetRegisterCode")
    MaintenanceLogDTO toDto(MaintenanceLog s);

    @Named("assetRegisterAssetRegisterCode")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "assetRegisterCode", source = "assetRegisterCode")
    AssetRegisterDTO toDtoAssetRegisterAssetRegisterCode(AssetRegister assetRegister);
}
