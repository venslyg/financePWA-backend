package com.gvsolutions.domain;

import static com.gvsolutions.domain.BudgetPlanTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.gvsolutions.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BudgetPlanTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BudgetPlan.class);
        BudgetPlan budgetPlan1 = getBudgetPlanSample1();
        BudgetPlan budgetPlan2 = new BudgetPlan();
        assertThat(budgetPlan1).isNotEqualTo(budgetPlan2);

        budgetPlan2.setId(budgetPlan1.getId());
        assertThat(budgetPlan1).isEqualTo(budgetPlan2);

        budgetPlan2 = getBudgetPlanSample2();
        assertThat(budgetPlan1).isNotEqualTo(budgetPlan2);
    }
}
