package com.gvsolutions.service.mapper;

import static com.gvsolutions.domain.ExpenseSubCategoryAsserts.*;
import static com.gvsolutions.domain.ExpenseSubCategoryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExpenseSubCategoryMapperTest {

    private ExpenseSubCategoryMapper expenseSubCategoryMapper;

    @BeforeEach
    void setUp() {
        expenseSubCategoryMapper = new ExpenseSubCategoryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getExpenseSubCategorySample1();
        var actual = expenseSubCategoryMapper.toEntity(expenseSubCategoryMapper.toDto(expected));
        assertExpenseSubCategoryAllPropertiesEquals(expected, actual);
    }
}
