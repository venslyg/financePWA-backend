package com.gvsolutions.service.mapper;

import static com.gvsolutions.domain.ExpenseEntryAsserts.*;
import static com.gvsolutions.domain.ExpenseEntryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExpenseEntryMapperTest {

    private ExpenseEntryMapper expenseEntryMapper;

    @BeforeEach
    void setUp() {
        expenseEntryMapper = new ExpenseEntryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getExpenseEntrySample1();
        var actual = expenseEntryMapper.toEntity(expenseEntryMapper.toDto(expected));
        assertExpenseEntryAllPropertiesEquals(expected, actual);
    }
}
