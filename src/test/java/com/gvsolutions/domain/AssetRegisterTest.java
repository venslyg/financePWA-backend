package com.gvsolutions.domain;

import static com.gvsolutions.domain.AssetRegisterTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.gvsolutions.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AssetRegisterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssetRegister.class);
        AssetRegister assetRegister1 = getAssetRegisterSample1();
        AssetRegister assetRegister2 = new AssetRegister();
        assertThat(assetRegister1).isNotEqualTo(assetRegister2);

        assetRegister2.setId(assetRegister1.getId());
        assertThat(assetRegister1).isEqualTo(assetRegister2);

        assetRegister2 = getAssetRegisterSample2();
        assertThat(assetRegister1).isNotEqualTo(assetRegister2);
    }
}
