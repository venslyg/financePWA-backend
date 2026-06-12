package com.gvsolutions.service.mapper;

import static com.gvsolutions.domain.BudgetPlanAsserts.*;
import static com.gvsolutions.domain.BudgetPlanTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BudgetPlanMapperTest {

    private BudgetPlanMapper budgetPlanMapper;

    @BeforeEach
    void setUp() {
        budgetPlanMapper = new BudgetPlanMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBudgetPlanSample1();
        var actual = budgetPlanMapper.toEntity(budgetPlanMapper.toDto(expected));
        assertBudgetPlanAllPropertiesEquals(expected, actual);
    }
}
