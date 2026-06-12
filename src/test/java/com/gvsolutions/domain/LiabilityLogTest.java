package com.gvsolutions.domain;

import static com.gvsolutions.domain.LiabilityLogTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.gvsolutions.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LiabilityLogTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LiabilityLog.class);
        LiabilityLog liabilityLog1 = getLiabilityLogSample1();
        LiabilityLog liabilityLog2 = new LiabilityLog();
        assertThat(liabilityLog1).isNotEqualTo(liabilityLog2);

        liabilityLog2.setId(liabilityLog1.getId());
        assertThat(liabilityLog1).isEqualTo(liabilityLog2);

        liabilityLog2 = getLiabilityLogSample2();
        assertThat(liabilityLog1).isNotEqualTo(liabilityLog2);
    }
}
