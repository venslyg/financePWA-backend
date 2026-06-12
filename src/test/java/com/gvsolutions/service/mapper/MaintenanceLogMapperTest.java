package com.gvsolutions.service.mapper;

import static com.gvsolutions.domain.MaintenanceLogAsserts.*;
import static com.gvsolutions.domain.MaintenanceLogTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MaintenanceLogMapperTest {

    private MaintenanceLogMapper maintenanceLogMapper;

    @BeforeEach
    void setUp() {
        maintenanceLogMapper = new MaintenanceLogMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMaintenanceLogSample1();
        var actual = maintenanceLogMapper.toEntity(maintenanceLogMapper.toDto(expected));
        assertMaintenanceLogAllPropertiesEquals(expected, actual);
    }
}
