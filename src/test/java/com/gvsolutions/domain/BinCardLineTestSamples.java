package com.gvsolutions.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BinCardLineTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static BinCardLine getBinCardLineSample1() {
        return new BinCardLine()
            .id(1L)
            .inventoryItemCode("inventoryItemCode1")
            .referenceNo("referenceNo1")
            .description("description1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static BinCardLine getBinCardLineSample2() {
        return new BinCardLine()
            .id(2L)
            .inventoryItemCode("inventoryItemCode2")
            .referenceNo("referenceNo2")
            .description("description2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static BinCardLine getBinCardLineRandomSampleGenerator() {
        return new BinCardLine()
            .id(longCount.incrementAndGet())
            .inventoryItemCode(UUID.randomUUID().toString())
            .referenceNo(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
