package com.gvsolutions.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gvsolutions.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BankLedgerDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BankLedgerDTO.class);
        BankLedgerDTO bankLedgerDTO1 = new BankLedgerDTO();
        bankLedgerDTO1.setId(1L);
        BankLedgerDTO bankLedgerDTO2 = new BankLedgerDTO();
        assertThat(bankLedgerDTO1).isNotEqualTo(bankLedgerDTO2);
        bankLedgerDTO2.setId(bankLedgerDTO1.getId());
        assertThat(bankLedgerDTO1).isEqualTo(bankLedgerDTO2);
        bankLedgerDTO2.setId(2L);
        assertThat(bankLedgerDTO1).isNotEqualTo(bankLedgerDTO2);
        bankLedgerDTO1.setId(null);
        assertThat(bankLedgerDTO1).isNotEqualTo(bankLedgerDTO2);
    }
}
