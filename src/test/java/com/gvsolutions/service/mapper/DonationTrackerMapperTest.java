package com.gvsolutions.service.mapper;

import static com.gvsolutions.domain.DonationTrackerAsserts.*;
import static com.gvsolutions.domain.DonationTrackerTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DonationTrackerMapperTest {

    private DonationTrackerMapper donationTrackerMapper;

    @BeforeEach
    void setUp() {
        donationTrackerMapper = new DonationTrackerMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDonationTrackerSample1();
        var actual = donationTrackerMapper.toEntity(donationTrackerMapper.toDto(expected));
        assertDonationTrackerAllPropertiesEquals(expected, actual);
    }
}
