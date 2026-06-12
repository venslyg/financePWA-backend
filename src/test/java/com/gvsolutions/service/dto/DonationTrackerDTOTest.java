package com.gvsolutions.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gvsolutions.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DonationTrackerDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DonationTrackerDTO.class);
        DonationTrackerDTO donationTrackerDTO1 = new DonationTrackerDTO();
        donationTrackerDTO1.setId(1L);
        DonationTrackerDTO donationTrackerDTO2 = new DonationTrackerDTO();
        assertThat(donationTrackerDTO1).isNotEqualTo(donationTrackerDTO2);
        donationTrackerDTO2.setId(donationTrackerDTO1.getId());
        assertThat(donationTrackerDTO1).isEqualTo(donationTrackerDTO2);
        donationTrackerDTO2.setId(2L);
        assertThat(donationTrackerDTO1).isNotEqualTo(donationTrackerDTO2);
        donationTrackerDTO1.setId(null);
        assertThat(donationTrackerDTO1).isNotEqualTo(donationTrackerDTO2);
    }
}
