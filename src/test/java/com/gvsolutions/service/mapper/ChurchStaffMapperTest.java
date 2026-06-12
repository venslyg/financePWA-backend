package com.gvsolutions.service.mapper;

import static com.gvsolutions.domain.ChurchStaffAsserts.*;
import static com.gvsolutions.domain.ChurchStaffTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChurchStaffMapperTest {

    private ChurchStaffMapper churchStaffMapper;

    @BeforeEach
    void setUp() {
        churchStaffMapper = new ChurchStaffMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getChurchStaffSample1();
        var actual = churchStaffMapper.toEntity(churchStaffMapper.toDto(expected));
        assertChurchStaffAllPropertiesEquals(expected, actual);
    }
}
