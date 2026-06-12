package com.gvsolutions.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class IncomeEntryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static IncomeEntry getIncomeEntrySample1() {
        return new IncomeEntry()
            .id(1L)
            .branchCode("branchCode1")
            .branchId("branchId1")
            .accountCode("accountCode1")
            .incomeCode("incomeCode1")
            .createdByUsername("createdByUsername1")
            .receiptNo("receiptNo1")
            .description("description1")
            .receivablePerson("receivablePerson1")
            .receivedBy("receivedBy1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static IncomeEntry getIncomeEntrySample2() {
        return new IncomeEntry()
            .id(2L)
            .branchCode("branchCode2")
            .branchId("branchId2")
            .accountCode("accountCode2")
            .incomeCode("incomeCode2")
            .createdByUsername("createdByUsername2")
            .receiptNo("receiptNo2")
            .description("description2")
            .receivablePerson("receivablePerson2")
            .receivedBy("receivedBy2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static IncomeEntry getIncomeEntryRandomSampleGenerator() {
        return new IncomeEntry()
            .id(longCount.incrementAndGet())
            .branchCode(UUID.randomUUID().toString())
            .branchId(UUID.randomUUID().toString())
            .accountCode(UUID.randomUUID().toString())
            .incomeCode(UUID.randomUUID().toString())
            .createdByUsername(UUID.randomUUID().toString())
            .receiptNo(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .receivablePerson(UUID.randomUUID().toString())
            .receivedBy(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
