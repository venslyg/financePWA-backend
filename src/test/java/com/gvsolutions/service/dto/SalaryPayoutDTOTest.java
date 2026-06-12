package com.gvsolutions.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gvsolutions.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SalaryPayoutDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SalaryPayoutDTO.class);
        SalaryPayoutDTO salaryPayoutDTO1 = new SalaryPayoutDTO();
        salaryPayoutDTO1.setId(1L);
        SalaryPayoutDTO salaryPayoutDTO2 = new SalaryPayoutDTO();
        assertThat(salaryPayoutDTO1).isNotEqualTo(salaryPayoutDTO2);
        salaryPayoutDTO2.setId(salaryPayoutDTO1.getId());
        assertThat(salaryPayoutDTO1).isEqualTo(salaryPayoutDTO2);
        salaryPayoutDTO2.setId(2L);
        assertThat(salaryPayoutDTO1).isNotEqualTo(salaryPayoutDTO2);
        salaryPayoutDTO1.setId(null);
        assertThat(salaryPayoutDTO1).isNotEqualTo(salaryPayoutDTO2);
    }
}
