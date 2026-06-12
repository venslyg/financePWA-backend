package com.gvsolutions.domain;

import static com.gvsolutions.domain.AssetCategoryTestSamples.*;
import static com.gvsolutions.domain.AssetSubCategoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.gvsolutions.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AssetSubCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssetSubCategory.class);
        AssetSubCategory assetSubCategory1 = getAssetSubCategorySample1();
        AssetSubCategory assetSubCategory2 = new AssetSubCategory();
        assertThat(assetSubCategory1).isNotEqualTo(assetSubCategory2);

        assetSubCategory2.setId(assetSubCategory1.getId());
        assertThat(assetSubCategory1).isEqualTo(assetSubCategory2);

        assetSubCategory2 = getAssetSubCategorySample2();
        assertThat(assetSubCategory1).isNotEqualTo(assetSubCategory2);
    }

    @Test
    void categoryTest() {
        AssetSubCategory assetSubCategory = getAssetSubCategoryRandomSampleGenerator();
        AssetCategory assetCategoryBack = getAssetCategoryRandomSampleGenerator();

        assetSubCategory.setCategory(assetCategoryBack);
        assertThat(assetSubCategory.getCategory()).isEqualTo(assetCategoryBack);

        assetSubCategory.category(null);
        assertThat(assetSubCategory.getCategory()).isNull();
    }
}
