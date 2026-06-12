package com.gvsolutions.service.mapper;

import static com.gvsolutions.domain.AssetRegisterAsserts.*;
import static com.gvsolutions.domain.AssetRegisterTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AssetRegisterMapperTest {

    private AssetRegisterMapper assetRegisterMapper;

    @BeforeEach
    void setUp() {
        assetRegisterMapper = new AssetRegisterMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAssetRegisterSample1();
        var actual = assetRegisterMapper.toEntity(assetRegisterMapper.toDto(expected));
        assertAssetRegisterAllPropertiesEquals(expected, actual);
    }
}
