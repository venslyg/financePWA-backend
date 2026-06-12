package com.gvsolutions.service.mapper;

import static com.gvsolutions.domain.AssetDepreciationHistoryAsserts.*;
import static com.gvsolutions.domain.AssetDepreciationHistoryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AssetDepreciationHistoryMapperTest {

    private AssetDepreciationHistoryMapper assetDepreciationHistoryMapper;

    @BeforeEach
    void setUp() {
        assetDepreciationHistoryMapper = new AssetDepreciationHistoryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAssetDepreciationHistorySample1();
        var actual = assetDepreciationHistoryMapper.toEntity(assetDepreciationHistoryMapper.toDto(expected));
        assertAssetDepreciationHistoryAllPropertiesEquals(expected, actual);
    }
}
