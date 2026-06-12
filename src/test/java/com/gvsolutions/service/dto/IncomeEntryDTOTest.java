package com.gvsolutions.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gvsolutions.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IncomeEntryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IncomeEntryDTO.class);
        IncomeEntryDTO incomeEntryDTO1 = new IncomeEntryDTO();
        incomeEntryDTO1.setId(1L);
        IncomeEntryDTO incomeEntryDTO2 = new IncomeEntryDTO();
        assertThat(incomeEntryDTO1).isNotEqualTo(incomeEntryDTO2);
        incomeEntryDTO2.setId(incomeEntryDTO1.getId());
        assertThat(incomeEntryDTO1).isEqualTo(incomeEntryDTO2);
        incomeEntryDTO2.setId(2L);
        assertThat(incomeEntryDTO1).isNotEqualTo(incomeEntryDTO2);
        incomeEntryDTO1.setId(null);
        assertThat(incomeEntryDTO1).isNotEqualTo(incomeEntryDTO2);
    }
}
