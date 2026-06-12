package com.gvsolutions.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gvsolutions.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BinCardLineDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BinCardLineDTO.class);
        BinCardLineDTO binCardLineDTO1 = new BinCardLineDTO();
        binCardLineDTO1.setId(1L);
        BinCardLineDTO binCardLineDTO2 = new BinCardLineDTO();
        assertThat(binCardLineDTO1).isNotEqualTo(binCardLineDTO2);
        binCardLineDTO2.setId(binCardLineDTO1.getId());
        assertThat(binCardLineDTO1).isEqualTo(binCardLineDTO2);
        binCardLineDTO2.setId(2L);
        assertThat(binCardLineDTO1).isNotEqualTo(binCardLineDTO2);
        binCardLineDTO1.setId(null);
        assertThat(binCardLineDTO1).isNotEqualTo(binCardLineDTO2);
    }
}
