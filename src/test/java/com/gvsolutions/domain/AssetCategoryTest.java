package com.gvsolutions.domain;

import static com.gvsolutions.domain.AssetCategoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.gvsolutions.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AssetCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssetCategory.class);
        AssetCategory assetCategory1 = getAssetCategorySample1();
        AssetCategory assetCategory2 = new AssetCategory();
        assertThat(assetCategory1).isNotEqualTo(assetCategory2);

        assetCategory2.setId(assetCategory1.getId());
        assertThat(assetCategory1).isEqualTo(assetCategory2);

        assetCategory2 = getAssetCategorySample2();
        assertThat(assetCategory1).isNotEqualTo(assetCategory2);
    }
}
