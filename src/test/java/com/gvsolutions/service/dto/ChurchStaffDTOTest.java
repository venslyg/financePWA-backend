package com.gvsolutions.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gvsolutions.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ChurchStaffDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChurchStaffDTO.class);
        ChurchStaffDTO churchStaffDTO1 = new ChurchStaffDTO();
        churchStaffDTO1.setId(1L);
        ChurchStaffDTO churchStaffDTO2 = new ChurchStaffDTO();
        assertThat(churchStaffDTO1).isNotEqualTo(churchStaffDTO2);
        churchStaffDTO2.setId(churchStaffDTO1.getId());
        assertThat(churchStaffDTO1).isEqualTo(churchStaffDTO2);
        churchStaffDTO2.setId(2L);
        assertThat(churchStaffDTO1).isNotEqualTo(churchStaffDTO2);
        churchStaffDTO1.setId(null);
        assertThat(churchStaffDTO1).isNotEqualTo(churchStaffDTO2);
    }
}
