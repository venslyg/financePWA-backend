package com.gvsolutions.domain;

import static com.gvsolutions.domain.BinCardLineTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.gvsolutions.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BinCardLineTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BinCardLine.class);
        BinCardLine binCardLine1 = getBinCardLineSample1();
        BinCardLine binCardLine2 = new BinCardLine();
        assertThat(binCardLine1).isNotEqualTo(binCardLine2);

        binCardLine2.setId(binCardLine1.getId());
        assertThat(binCardLine1).isEqualTo(binCardLine2);

        binCardLine2 = getBinCardLineSample2();
        assertThat(binCardLine1).isNotEqualTo(binCardLine2);
    }
}
