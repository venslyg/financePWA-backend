package com.gvsolutions.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gvsolutions.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExpenseSubCategoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExpenseSubCategoryDTO.class);
        ExpenseSubCategoryDTO expenseSubCategoryDTO1 = new ExpenseSubCategoryDTO();
        expenseSubCategoryDTO1.setId(1L);
        ExpenseSubCategoryDTO expenseSubCategoryDTO2 = new ExpenseSubCategoryDTO();
        assertThat(expenseSubCategoryDTO1).isNotEqualTo(expenseSubCategoryDTO2);
        expenseSubCategoryDTO2.setId(expenseSubCategoryDTO1.getId());
        assertThat(expenseSubCategoryDTO1).isEqualTo(expenseSubCategoryDTO2);
        expenseSubCategoryDTO2.setId(2L);
        assertThat(expenseSubCategoryDTO1).isNotEqualTo(expenseSubCategoryDTO2);
        expenseSubCategoryDTO1.setId(null);
        assertThat(expenseSubCategoryDTO1).isNotEqualTo(expenseSubCategoryDTO2);
    }
}
