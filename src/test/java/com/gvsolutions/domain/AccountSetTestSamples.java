package com.gvsolutions.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AccountSetTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AccountSet getAccountSetSample1() {
        return new AccountSet()
            .id(1L)
            .branchCode("branchCode1")
            .branchId("branchId1")
            .accountCode("accountCode1")
            .accountName("accountName1")
            .subCategory("subCategory1")
            .remark("remark1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static AccountSet getAccountSetSample2() {
        return new AccountSet()
            .id(2L)
            .branchCode("branchCode2")
            .branchId("branchId2")
            .accountCode("accountCode2")
            .accountName("accountName2")
            .subCategory("subCategory2")
            .remark("remark2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static AccountSet getAccountSetRandomSampleGenerator() {
        return new AccountSet()
            .id(longCount.incrementAndGet())
            .branchCode(UUID.randomUUID().toString())
            .branchId(UUID.randomUUID().toString())
            .accountCode(UUID.randomUUID().toString())
            .accountName(UUID.randomUUID().toString())
            .subCategory(UUID.randomUUID().toString())
            .remark(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
