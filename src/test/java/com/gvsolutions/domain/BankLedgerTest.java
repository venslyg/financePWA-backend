package com.gvsolutions.domain;

import static com.gvsolutions.domain.BankLedgerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.gvsolutions.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BankLedgerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BankLedger.class);
        BankLedger bankLedger1 = getBankLedgerSample1();
        BankLedger bankLedger2 = new BankLedger();
        assertThat(bankLedger1).isNotEqualTo(bankLedger2);

        bankLedger2.setId(bankLedger1.getId());
        assertThat(bankLedger1).isEqualTo(bankLedger2);

        bankLedger2 = getBankLedgerSample2();
        assertThat(bankLedger1).isNotEqualTo(bankLedger2);
    }
}
