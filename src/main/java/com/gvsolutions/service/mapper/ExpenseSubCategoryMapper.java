package com.gvsolutions.service.mapper;

import com.gvsolutions.domain.ExpenseCategory;
import com.gvsolutions.domain.ExpenseSubCategory;
import com.gvsolutions.service.dto.ExpenseCategoryDTO;
import com.gvsolutions.service.dto.ExpenseSubCategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ExpenseSubCategory} and its DTO {@link ExpenseSubCategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface ExpenseSubCategoryMapper extends EntityMapper<ExpenseSubCategoryDTO, ExpenseSubCategory> {
    @Mapping(target = "category", source = "category", qualifiedByName = "expenseCategoryCategoryCode")
    ExpenseSubCategoryDTO toDto(ExpenseSubCategory s);

    @Named("expenseCategoryCategoryCode")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "categoryCode", source = "categoryCode")
    ExpenseCategoryDTO toDtoExpenseCategoryCategoryCode(ExpenseCategory expenseCategory);
}
