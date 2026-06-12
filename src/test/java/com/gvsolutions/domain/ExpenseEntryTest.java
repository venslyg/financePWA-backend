package com.gvsolutions.domain;

import static com.gvsolutions.domain.ExpenseEntryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.gvsolutions.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExpenseEntryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExpenseEntry.class);
        ExpenseEntry expenseEntry1 = getExpenseEntrySample1();
        ExpenseEntry expenseEntry2 = new ExpenseEntry();
        assertThat(expenseEntry1).isNotEqualTo(expenseEntry2);

        expenseEntry2.setId(expenseEntry1.getId());
        assertThat(expenseEntry1).isEqualTo(expenseEntry2);

        expenseEntry2 = getExpenseEntrySample2();
        assertThat(expenseEntry1).isNotEqualTo(expenseEntry2);
    }
}
