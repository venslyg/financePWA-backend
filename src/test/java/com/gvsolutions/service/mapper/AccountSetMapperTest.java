package com.gvsolutions.service.mapper;

import static com.gvsolutions.domain.AccountSetAsserts.*;
import static com.gvsolutions.domain.AccountSetTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AccountSetMapperTest {

    private AccountSetMapper accountSetMapper;

    @BeforeEach
    void setUp() {
        accountSetMapper = new AccountSetMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAccountSetSample1();
        var actual = accountSetMapper.toEntity(accountSetMapper.toDto(expected));
        assertAccountSetAllPropertiesEquals(expected, actual);
    }
}
