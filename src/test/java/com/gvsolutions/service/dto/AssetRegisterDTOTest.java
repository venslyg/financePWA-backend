package com.gvsolutions.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gvsolutions.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AssetRegisterDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssetRegisterDTO.class);
        AssetRegisterDTO assetRegisterDTO1 = new AssetRegisterDTO();
        assetRegisterDTO1.setId(1L);
        AssetRegisterDTO assetRegisterDTO2 = new AssetRegisterDTO();
        assertThat(assetRegisterDTO1).isNotEqualTo(assetRegisterDTO2);
        assetRegisterDTO2.setId(assetRegisterDTO1.getId());
        assertThat(assetRegisterDTO1).isEqualTo(assetRegisterDTO2);
        assetRegisterDTO2.setId(2L);
        assertThat(assetRegisterDTO1).isNotEqualTo(assetRegisterDTO2);
        assetRegisterDTO1.setId(null);
        assertThat(assetRegisterDTO1).isNotEqualTo(assetRegisterDTO2);
    }
}
