package com.gvsolutions.service.mapper;

import static com.gvsolutions.domain.PettyCashLedgerAsserts.*;
import static com.gvsolutions.domain.PettyCashLedgerTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PettyCashLedgerMapperTest {

    private PettyCashLedgerMapper pettyCashLedgerMapper;

    @BeforeEach
    void setUp() {
        pettyCashLedgerMapper = new PettyCashLedgerMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPettyCashLedgerSample1();
        var actual = pettyCashLedgerMapper.toEntity(pettyCashLedgerMapper.toDto(expected));
        assertPettyCashLedgerAllPropertiesEquals(expected, actual);
    }
}
