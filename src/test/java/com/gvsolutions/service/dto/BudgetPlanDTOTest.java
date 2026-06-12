package com.gvsolutions.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gvsolutions.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BudgetPlanDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BudgetPlanDTO.class);
        BudgetPlanDTO budgetPlanDTO1 = new BudgetPlanDTO();
        budgetPlanDTO1.setId(1L);
        BudgetPlanDTO budgetPlanDTO2 = new BudgetPlanDTO();
        assertThat(budgetPlanDTO1).isNotEqualTo(budgetPlanDTO2);
        budgetPlanDTO2.setId(budgetPlanDTO1.getId());
        assertThat(budgetPlanDTO1).isEqualTo(budgetPlanDTO2);
        budgetPlanDTO2.setId(2L);
        assertThat(budgetPlanDTO1).isNotEqualTo(budgetPlanDTO2);
        budgetPlanDTO1.setId(null);
        assertThat(budgetPlanDTO1).isNotEqualTo(budgetPlanDTO2);
    }
}
