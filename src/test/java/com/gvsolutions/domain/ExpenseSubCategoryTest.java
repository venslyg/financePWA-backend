package com.gvsolutions.domain;

import static com.gvsolutions.domain.ExpenseCategoryTestSamples.*;
import static com.gvsolutions.domain.ExpenseSubCategoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.gvsolutions.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExpenseSubCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExpenseSubCategory.class);
        ExpenseSubCategory expenseSubCategory1 = getExpenseSubCategorySample1();
        ExpenseSubCategory expenseSubCategory2 = new ExpenseSubCategory();
        assertThat(expenseSubCategory1).isNotEqualTo(expenseSubCategory2);

        expenseSubCategory2.setId(expenseSubCategory1.getId());
        assertThat(expenseSubCategory1).isEqualTo(expenseSubCategory2);

        expenseSubCategory2 = getExpenseSubCategorySample2();
        assertThat(expenseSubCategory1).isNotEqualTo(expenseSubCategory2);
    }

    @Test
    void categoryTest() {
        ExpenseSubCategory expenseSubCategory = getExpenseSubCategoryRandomSampleGenerator();
        ExpenseCategory expenseCategoryBack = getExpenseCategoryRandomSampleGenerator();

        expenseSubCategory.setCategory(expenseCategoryBack);
        assertThat(expenseSubCategory.getCategory()).isEqualTo(expenseCategoryBack);

        expenseSubCategory.category(null);
        assertThat(expenseSubCategory.getCategory()).isNull();
    }
}
