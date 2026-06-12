package com.gvsolutions.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gvsolutions.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExpenseEntryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExpenseEntryDTO.class);
        ExpenseEntryDTO expenseEntryDTO1 = new ExpenseEntryDTO();
        expenseEntryDTO1.setId(1L);
        ExpenseEntryDTO expenseEntryDTO2 = new ExpenseEntryDTO();
        assertThat(expenseEntryDTO1).isNotEqualTo(expenseEntryDTO2);
        expenseEntryDTO2.setId(expenseEntryDTO1.getId());
        assertThat(expenseEntryDTO1).isEqualTo(expenseEntryDTO2);
        expenseEntryDTO2.setId(2L);
        assertThat(expenseEntryDTO1).isNotEqualTo(expenseEntryDTO2);
        expenseEntryDTO1.setId(null);
        assertThat(expenseEntryDTO1).isNotEqualTo(expenseEntryDTO2);
    }
}
