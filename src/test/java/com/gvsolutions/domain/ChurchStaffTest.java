package com.gvsolutions.domain;

import static com.gvsolutions.domain.ChurchStaffTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.gvsolutions.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ChurchStaffTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChurchStaff.class);
        ChurchStaff churchStaff1 = getChurchStaffSample1();
        ChurchStaff churchStaff2 = new ChurchStaff();
        assertThat(churchStaff1).isNotEqualTo(churchStaff2);

        churchStaff2.setId(churchStaff1.getId());
        assertThat(churchStaff1).isEqualTo(churchStaff2);

        churchStaff2 = getChurchStaffSample2();
        assertThat(churchStaff1).isNotEqualTo(churchStaff2);
    }
}
