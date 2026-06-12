package com.gvsolutions.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gvsolutions.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AssetSubCategoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssetSubCategoryDTO.class);
        AssetSubCategoryDTO assetSubCategoryDTO1 = new AssetSubCategoryDTO();
        assetSubCategoryDTO1.setId(1L);
        AssetSubCategoryDTO assetSubCategoryDTO2 = new AssetSubCategoryDTO();
        assertThat(assetSubCategoryDTO1).isNotEqualTo(assetSubCategoryDTO2);
        assetSubCategoryDTO2.setId(assetSubCategoryDTO1.getId());
        assertThat(assetSubCategoryDTO1).isEqualTo(assetSubCategoryDTO2);
        assetSubCategoryDTO2.setId(2L);
        assertThat(assetSubCategoryDTO1).isNotEqualTo(assetSubCategoryDTO2);
        assetSubCategoryDTO1.setId(null);
        assertThat(assetSubCategoryDTO1).isNotEqualTo(assetSubCategoryDTO2);
    }
}
