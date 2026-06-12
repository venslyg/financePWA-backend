package com.gvsolutions.service.mapper;

import com.gvsolutions.domain.ExpenseCategory;
import com.gvsolutions.service.dto.ExpenseCategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ExpenseCategory} and its DTO {@link ExpenseCategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface ExpenseCategoryMapper extends EntityMapper<ExpenseCategoryDTO, ExpenseCategory> {}
