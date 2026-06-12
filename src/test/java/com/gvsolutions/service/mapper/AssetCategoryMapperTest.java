package com.gvsolutions.service.mapper;

import static com.gvsolutions.domain.AssetCategoryAsserts.*;
import static com.gvsolutions.domain.AssetCategoryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AssetCategoryMapperTest {

    private AssetCategoryMapper assetCategoryMapper;

    @BeforeEach
    void setUp() {
        assetCategoryMapper = new AssetCategoryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAssetCategorySample1();
        var actual = assetCategoryMapper.toEntity(assetCategoryMapper.toDto(expected));
        assertAssetCategoryAllPropertiesEquals(expected, actual);
    }
}
