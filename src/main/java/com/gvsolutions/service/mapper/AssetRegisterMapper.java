package com.gvsolutions.service.mapper;

import com.gvsolutions.domain.AssetRegister;
import com.gvsolutions.service.dto.AssetRegisterDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AssetRegister} and its DTO {@link AssetRegisterDTO}.
 */
@Mapper(componentModel = "spring")
public interface AssetRegisterMapper extends EntityMapper<AssetRegisterDTO, AssetRegister> {}
