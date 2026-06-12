package com.gvsolutions.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BranchTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Branch getBranchSample1() {
        return new Branch()
            .id(1L)
            .branchCode("branchCode1")
            .branchName("branchName1")
            .location("location1")
            .phoneNumber("phoneNumber1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static Branch getBranchSample2() {
        return new Branch()
            .id(2L)
            .branchCode("branchCode2")
            .branchName("branchName2")
            .location("location2")
            .phoneNumber("phoneNumber2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static Branch getBranchRandomSampleGenerator() {
        return new Branch()
            .id(longCount.incrementAndGet())
            .branchCode(UUID.randomUUID().toString())
            .branchName(UUID.randomUUID().toString())
            .location(UUID.randomUUID().toString())
            .phoneNumber(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
