package com.gvsolutions.domain;

import static com.gvsolutions.domain.AssetDepreciationHistoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.gvsolutions.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AssetDepreciationHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssetDepreciationHistory.class);
        AssetDepreciationHistory assetDepreciationHistory1 = getAssetDepreciationHistorySample1();
        AssetDepreciationHistory assetDepreciationHistory2 = new AssetDepreciationHistory();
        assertThat(assetDepreciationHistory1).isNotEqualTo(assetDepreciationHistory2);

        assetDepreciationHistory2.setId(assetDepreciationHistory1.getId());
        assertThat(assetDepreciationHistory1).isEqualTo(assetDepreciationHistory2);

        assetDepreciationHistory2 = getAssetDepreciationHistorySample2();
        assertThat(assetDepreciationHistory1).isNotEqualTo(assetDepreciationHistory2);
    }
}
