package com.gvsolutions.domain;

import static com.gvsolutions.domain.IncomeEntryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.gvsolutions.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IncomeEntryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IncomeEntry.class);
        IncomeEntry incomeEntry1 = getIncomeEntrySample1();
        IncomeEntry incomeEntry2 = new IncomeEntry();
        assertThat(incomeEntry1).isNotEqualTo(incomeEntry2);

        incomeEntry2.setId(incomeEntry1.getId());
        assertThat(incomeEntry1).isEqualTo(incomeEntry2);

        incomeEntry2 = getIncomeEntrySample2();
        assertThat(incomeEntry1).isNotEqualTo(incomeEntry2);
    }
}
