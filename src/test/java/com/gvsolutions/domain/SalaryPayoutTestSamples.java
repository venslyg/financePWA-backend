package com.gvsolutions.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SalaryPayoutTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SalaryPayout getSalaryPayoutSample1() {
        return new SalaryPayout()
            .id(1L)
            .branchCode("branchCode1")
            .salaryPayoutCode("salaryPayoutCode1")
            .staffCode("staffCode1")
            .payPeriod("payPeriod1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static SalaryPayout getSalaryPayoutSample2() {
        return new SalaryPayout()
            .id(2L)
            .branchCode("branchCode2")
            .salaryPayoutCode("salaryPayoutCode2")
            .staffCode("staffCode2")
            .payPeriod("payPeriod2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static SalaryPayout getSalaryPayoutRandomSampleGenerator() {
        return new SalaryPayout()
            .id(longCount.incrementAndGet())
            .branchCode(UUID.randomUUID().toString())
            .salaryPayoutCode(UUID.randomUUID().toString())
            .staffCode(UUID.randomUUID().toString())
            .payPeriod(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
