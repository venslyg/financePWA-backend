package com.gvsolutions.domain;

import static com.gvsolutions.domain.SalaryPayoutTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.gvsolutions.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SalaryPayoutTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SalaryPayout.class);
        SalaryPayout salaryPayout1 = getSalaryPayoutSample1();
        SalaryPayout salaryPayout2 = new SalaryPayout();
        assertThat(salaryPayout1).isNotEqualTo(salaryPayout2);

        salaryPayout2.setId(salaryPayout1.getId());
        assertThat(salaryPayout1).isEqualTo(salaryPayout2);

        salaryPayout2 = getSalaryPayoutSample2();
        assertThat(salaryPayout1).isNotEqualTo(salaryPayout2);
    }
}
