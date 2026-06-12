package com.gvsolutions.service.mapper;

import static com.gvsolutions.domain.BinCardLineAsserts.*;
import static com.gvsolutions.domain.BinCardLineTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BinCardLineMapperTest {

    private BinCardLineMapper binCardLineMapper;

    @BeforeEach
    void setUp() {
        binCardLineMapper = new BinCardLineMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBinCardLineSample1();
        var actual = binCardLineMapper.toEntity(binCardLineMapper.toDto(expected));
        assertBinCardLineAllPropertiesEquals(expected, actual);
    }
}
