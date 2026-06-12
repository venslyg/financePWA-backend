package com.gvsolutions.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PettyCashLedgerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PettyCashLedger getPettyCashLedgerSample1() {
        return new PettyCashLedger()
            .id(1L)
            .branchCode("branchCode1")
            .pettyCashCode("pettyCashCode1")
            .pettyCashVoucherNo("pettyCashVoucherNo1")
            .description("description1")
            .linkedAccountCode("linkedAccountCode1")
            .referenceNo("referenceNo1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static PettyCashLedger getPettyCashLedgerSample2() {
        return new PettyCashLedger()
            .id(2L)
            .branchCode("branchCode2")
            .pettyCashCode("pettyCashCode2")
            .pettyCashVoucherNo("pettyCashVoucherNo2")
            .description("description2")
            .linkedAccountCode("linkedAccountCode2")
            .referenceNo("referenceNo2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static PettyCashLedger getPettyCashLedgerRandomSampleGenerator() {
        return new PettyCashLedger()
            .id(longCount.incrementAndGet())
            .branchCode(UUID.randomUUID().toString())
            .pettyCashCode(UUID.randomUUID().toString())
            .pettyCashVoucherNo(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .linkedAccountCode(UUID.randomUUID().toString())
            .referenceNo(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
