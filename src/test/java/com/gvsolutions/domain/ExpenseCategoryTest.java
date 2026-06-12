package com.gvsolutions.domain;

import static com.gvsolutions.domain.ExpenseCategoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.gvsolutions.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExpenseCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExpenseCategory.class);
        ExpenseCategory expenseCategory1 = getExpenseCategorySample1();
        ExpenseCategory expenseCategory2 = new ExpenseCategory();
        assertThat(expenseCategory1).isNotEqualTo(expenseCategory2);

        expenseCategory2.setId(expenseCategory1.getId());
        assertThat(expenseCategory1).isEqualTo(expenseCategory2);

        expenseCategory2 = getExpenseCategorySample2();
        assertThat(expenseCategory1).isNotEqualTo(expenseCategory2);
    }
}
