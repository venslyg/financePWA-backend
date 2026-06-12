package com.gvsolutions.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DonationTrackerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static DonationTracker getDonationTrackerSample1() {
        return new DonationTracker()
            .id(1L)
            .branchCode("branchCode1")
            .donationIdCode("donationIdCode1")
            .donorNameOrOrg("donorNameOrOrg1")
            .contactDetails("contactDetails1")
            .purpose("purpose1")
            .notes("notes1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static DonationTracker getDonationTrackerSample2() {
        return new DonationTracker()
            .id(2L)
            .branchCode("branchCode2")
            .donationIdCode("donationIdCode2")
            .donorNameOrOrg("donorNameOrOrg2")
            .contactDetails("contactDetails2")
            .purpose("purpose2")
            .notes("notes2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static DonationTracker getDonationTrackerRandomSampleGenerator() {
        return new DonationTracker()
            .id(longCount.incrementAndGet())
            .branchCode(UUID.randomUUID().toString())
            .donationIdCode(UUID.randomUUID().toString())
            .donorNameOrOrg(UUID.randomUUID().toString())
            .contactDetails(UUID.randomUUID().toString())
            .purpose(UUID.randomUUID().toString())
            .notes(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
