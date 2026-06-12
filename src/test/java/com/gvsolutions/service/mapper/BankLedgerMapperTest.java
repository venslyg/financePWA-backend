package com.gvsolutions.service.mapper;

import static com.gvsolutions.domain.BankLedgerAsserts.*;
import static com.gvsolutions.domain.BankLedgerTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BankLedgerMapperTest {

    private BankLedgerMapper bankLedgerMapper;

    @BeforeEach
    void setUp() {
        bankLedgerMapper = new BankLedgerMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBankLedgerSample1();
        var actual = bankLedgerMapper.toEntity(bankLedgerMapper.toDto(expected));
        assertBankLedgerAllPropertiesEquals(expected, actual);
    }
}
