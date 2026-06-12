package com.gvsolutions.service.mapper;

import static com.gvsolutions.domain.LiabilityLogAsserts.*;
import static com.gvsolutions.domain.LiabilityLogTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LiabilityLogMapperTest {

    private LiabilityLogMapper liabilityLogMapper;

    @BeforeEach
    void setUp() {
        liabilityLogMapper = new LiabilityLogMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getLiabilityLogSample1();
        var actual = liabilityLogMapper.toEntity(liabilityLogMapper.toDto(expected));
        assertLiabilityLogAllPropertiesEquals(expected, actual);
    }
}
