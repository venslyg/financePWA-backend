package com.gvsolutions.service.mapper;

import com.gvsolutions.domain.LiabilityLog;
import com.gvsolutions.service.dto.LiabilityLogDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LiabilityLog} and its DTO {@link LiabilityLogDTO}.
 */
@Mapper(componentModel = "spring")
public interface LiabilityLogMapper extends EntityMapper<LiabilityLogDTO, LiabilityLog> {}
