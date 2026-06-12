package com.gvsolutions.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BankLedgerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static BankLedger getBankLedgerSample1() {
        return new BankLedger()
            .id(1L)
            .branchCode("branchCode1")
            .branchId("branchId1")
            .bankLedgerCode("bankLedgerCode1")
            .referenceNo("referenceNo1")
            .description("description1")
            .remark("remark1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static BankLedger getBankLedgerSample2() {
        return new BankLedger()
            .id(2L)
            .branchCode("branchCode2")
            .branchId("branchId2")
            .bankLedgerCode("bankLedgerCode2")
            .referenceNo("referenceNo2")
            .description("description2")
            .remark("remark2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static BankLedger getBankLedgerRandomSampleGenerator() {
        return new BankLedger()
            .id(longCount.incrementAndGet())
            .branchCode(UUID.randomUUID().toString())
            .branchId(UUID.randomUUID().toString())
            .bankLedgerCode(UUID.randomUUID().toString())
            .referenceNo(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .remark(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
