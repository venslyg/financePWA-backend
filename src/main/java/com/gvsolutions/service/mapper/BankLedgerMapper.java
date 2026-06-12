package com.gvsolutions.service.mapper;

import com.gvsolutions.domain.BankLedger;
import com.gvsolutions.service.dto.BankLedgerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BankLedger} and its DTO {@link BankLedgerDTO}.
 */
@Mapper(componentModel = "spring")
public interface BankLedgerMapper extends EntityMapper<BankLedgerDTO, BankLedger> {}
