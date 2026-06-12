package com.gvsolutions.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gvsolutions.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PettyCashLedgerDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PettyCashLedgerDTO.class);
        PettyCashLedgerDTO pettyCashLedgerDTO1 = new PettyCashLedgerDTO();
        pettyCashLedgerDTO1.setId(1L);
        PettyCashLedgerDTO pettyCashLedgerDTO2 = new PettyCashLedgerDTO();
        assertThat(pettyCashLedgerDTO1).isNotEqualTo(pettyCashLedgerDTO2);
        pettyCashLedgerDTO2.setId(pettyCashLedgerDTO1.getId());
        assertThat(pettyCashLedgerDTO1).isEqualTo(pettyCashLedgerDTO2);
        pettyCashLedgerDTO2.setId(2L);
        assertThat(pettyCashLedgerDTO1).isNotEqualTo(pettyCashLedgerDTO2);
        pettyCashLedgerDTO1.setId(null);
        assertThat(pettyCashLedgerDTO1).isNotEqualTo(pettyCashLedgerDTO2);
    }
}
