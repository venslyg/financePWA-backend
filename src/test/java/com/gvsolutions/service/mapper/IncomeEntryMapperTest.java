package com.gvsolutions.service.mapper;

import static com.gvsolutions.domain.IncomeEntryAsserts.*;
import static com.gvsolutions.domain.IncomeEntryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IncomeEntryMapperTest {

    private IncomeEntryMapper incomeEntryMapper;

    @BeforeEach
    void setUp() {
        incomeEntryMapper = new IncomeEntryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getIncomeEntrySample1();
        var actual = incomeEntryMapper.toEntity(incomeEntryMapper.toDto(expected));
        assertIncomeEntryAllPropertiesEquals(expected, actual);
    }
}
