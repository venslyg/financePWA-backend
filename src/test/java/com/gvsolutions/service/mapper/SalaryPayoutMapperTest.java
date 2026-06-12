package com.gvsolutions.service.mapper;

import static com.gvsolutions.domain.SalaryPayoutAsserts.*;
import static com.gvsolutions.domain.SalaryPayoutTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SalaryPayoutMapperTest {

    private SalaryPayoutMapper salaryPayoutMapper;

    @BeforeEach
    void setUp() {
        salaryPayoutMapper = new SalaryPayoutMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSalaryPayoutSample1();
        var actual = salaryPayoutMapper.toEntity(salaryPayoutMapper.toDto(expected));
        assertSalaryPayoutAllPropertiesEquals(expected, actual);
    }
}
