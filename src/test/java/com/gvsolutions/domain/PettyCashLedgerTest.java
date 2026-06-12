package com.gvsolutions.domain;

import static com.gvsolutions.domain.PettyCashLedgerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.gvsolutions.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PettyCashLedgerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PettyCashLedger.class);
        PettyCashLedger pettyCashLedger1 = getPettyCashLedgerSample1();
        PettyCashLedger pettyCashLedger2 = new PettyCashLedger();
        assertThat(pettyCashLedger1).isNotEqualTo(pettyCashLedger2);

        pettyCashLedger2.setId(pettyCashLedger1.getId());
        assertThat(pettyCashLedger1).isEqualTo(pettyCashLedger2);

        pettyCashLedger2 = getPettyCashLedgerSample2();
        assertThat(pettyCashLedger1).isNotEqualTo(pettyCashLedger2);
    }
}
