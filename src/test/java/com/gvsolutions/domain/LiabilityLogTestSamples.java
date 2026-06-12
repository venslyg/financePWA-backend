package com.gvsolutions.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class LiabilityLogTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static LiabilityLog getLiabilityLogSample1() {
        return new LiabilityLog()
            .id(1L)
            .branchCode("branchCode1")
            .branchId("branchId1")
            .liabilityCode("liabilityCode1")
            .loanFrom("loanFrom1")
            .description("description1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static LiabilityLog getLiabilityLogSample2() {
        return new LiabilityLog()
            .id(2L)
            .branchCode("branchCode2")
            .branchId("branchId2")
            .liabilityCode("liabilityCode2")
            .loanFrom("loanFrom2")
            .description("description2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static LiabilityLog getLiabilityLogRandomSampleGenerator() {
        return new LiabilityLog()
            .id(longCount.incrementAndGet())
            .branchCode(UUID.randomUUID().toString())
            .branchId(UUID.randomUUID().toString())
            .liabilityCode(UUID.randomUUID().toString())
            .loanFrom(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
