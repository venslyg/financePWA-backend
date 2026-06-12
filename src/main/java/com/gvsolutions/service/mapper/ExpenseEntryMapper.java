package com.gvsolutions.service.mapper;

import com.gvsolutions.domain.ExpenseEntry;
import com.gvsolutions.service.dto.ExpenseEntryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ExpenseEntry} and its DTO {@link ExpenseEntryDTO}.
 */
@Mapper(componentModel = "spring")
public interface ExpenseEntryMapper extends EntityMapper<ExpenseEntryDTO, ExpenseEntry> {}
