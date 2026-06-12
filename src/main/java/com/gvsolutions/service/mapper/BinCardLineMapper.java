package com.gvsolutions.service.mapper;

import com.gvsolutions.domain.BinCardLine;
import com.gvsolutions.service.dto.BinCardLineDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BinCardLine} and its DTO {@link BinCardLineDTO}.
 */
@Mapper(componentModel = "spring")
public interface BinCardLineMapper extends EntityMapper<BinCardLineDTO, BinCardLine> {}
