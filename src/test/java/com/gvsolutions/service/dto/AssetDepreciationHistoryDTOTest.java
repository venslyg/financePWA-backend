package com.gvsolutions.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gvsolutions.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AssetDepreciationHistoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssetDepreciationHistoryDTO.class);
        AssetDepreciationHistoryDTO assetDepreciationHistoryDTO1 = new AssetDepreciationHistoryDTO();
        assetDepreciationHistoryDTO1.setId(1L);
        AssetDepreciationHistoryDTO assetDepreciationHistoryDTO2 = new AssetDepreciationHistoryDTO();
        assertThat(assetDepreciationHistoryDTO1).isNotEqualTo(assetDepreciationHistoryDTO2);
        assetDepreciationHistoryDTO2.setId(assetDepreciationHistoryDTO1.getId());
        assertThat(assetDepreciationHistoryDTO1).isEqualTo(assetDepreciationHistoryDTO2);
        assetDepreciationHistoryDTO2.setId(2L);
        assertThat(assetDepreciationHistoryDTO1).isNotEqualTo(assetDepreciationHistoryDTO2);
        assetDepreciationHistoryDTO1.setId(null);
        assertThat(assetDepreciationHistoryDTO1).isNotEqualTo(assetDepreciationHistoryDTO2);
    }
}
