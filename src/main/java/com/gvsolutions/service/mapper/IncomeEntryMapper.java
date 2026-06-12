package com.gvsolutions.service.mapper;

import com.gvsolutions.domain.IncomeEntry;
import com.gvsolutions.service.dto.IncomeEntryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link IncomeEntry} and its DTO {@link IncomeEntryDTO}.
 */
@Mapper(componentModel = "spring")
public interface IncomeEntryMapper extends EntityMapper<IncomeEntryDTO, IncomeEntry> {}
