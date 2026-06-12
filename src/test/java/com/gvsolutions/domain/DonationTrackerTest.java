package com.gvsolutions.domain;

import static com.gvsolutions.domain.DonationTrackerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.gvsolutions.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DonationTrackerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DonationTracker.class);
        DonationTracker donationTracker1 = getDonationTrackerSample1();
        DonationTracker donationTracker2 = new DonationTracker();
        assertThat(donationTracker1).isNotEqualTo(donationTracker2);

        donationTracker2.setId(donationTracker1.getId());
        assertThat(donationTracker1).isEqualTo(donationTracker2);

        donationTracker2 = getDonationTrackerSample2();
        assertThat(donationTracker1).isNotEqualTo(donationTracker2);
    }
}
