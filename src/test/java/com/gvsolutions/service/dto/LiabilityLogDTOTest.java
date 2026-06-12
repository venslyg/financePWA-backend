package com.gvsolutions.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gvsolutions.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LiabilityLogDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LiabilityLogDTO.class);
        LiabilityLogDTO liabilityLogDTO1 = new LiabilityLogDTO();
        liabilityLogDTO1.setId(1L);
        LiabilityLogDTO liabilityLogDTO2 = new LiabilityLogDTO();
        assertThat(liabilityLogDTO1).isNotEqualTo(liabilityLogDTO2);
        liabilityLogDTO2.setId(liabilityLogDTO1.getId());
        assertThat(liabilityLogDTO1).isEqualTo(liabilityLogDTO2);
        liabilityLogDTO2.setId(2L);
        assertThat(liabilityLogDTO1).isNotEqualTo(liabilityLogDTO2);
        liabilityLogDTO1.setId(null);
        assertThat(liabilityLogDTO1).isNotEqualTo(liabilityLogDTO2);
    }
}
