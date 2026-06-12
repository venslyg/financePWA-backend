package com.gvsolutions.service.mapper;

import static com.gvsolutions.domain.AssetSubCategoryAsserts.*;
import static com.gvsolutions.domain.AssetSubCategoryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AssetSubCategoryMapperTest {

    private AssetSubCategoryMapper assetSubCategoryMapper;

    @BeforeEach
    void setUp() {
        assetSubCategoryMapper = new AssetSubCategoryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAssetSubCategorySample1();
        var actual = assetSubCategoryMapper.toEntity(assetSubCategoryMapper.toDto(expected));
        assertAssetSubCategoryAllPropertiesEquals(expected, actual);
    }
}
