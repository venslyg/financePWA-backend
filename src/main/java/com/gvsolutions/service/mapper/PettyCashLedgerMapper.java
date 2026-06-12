package com.gvsolutions.service.mapper;

import com.gvsolutions.domain.PettyCashLedger;
import com.gvsolutions.service.dto.PettyCashLedgerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PettyCashLedger} and its DTO {@link PettyCashLedgerDTO}.
 */
@Mapper(componentModel = "spring")
public interface PettyCashLedgerMapper extends EntityMapper<PettyCashLedgerDTO, PettyCashLedger> {}
