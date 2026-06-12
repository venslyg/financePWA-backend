package com.gvsolutions.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class InventoryItemTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static InventoryItem getInventoryItemSample1() {
        return new InventoryItem()
            .id(1L)
            .branchCode("branchCode1")
            .inventoryItemCode("inventoryItemCode1")
            .itemName("itemName1")
            .category("category1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static InventoryItem getInventoryItemSample2() {
        return new InventoryItem()
            .id(2L)
            .branchCode("branchCode2")
            .inventoryItemCode("inventoryItemCode2")
            .itemName("itemName2")
            .category("category2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static InventoryItem getInventoryItemRandomSampleGenerator() {
        return new InventoryItem()
            .id(longCount.incrementAndGet())
            .branchCode(UUID.randomUUID().toString())
            .inventoryItemCode(UUID.randomUUID().toString())
            .itemName(UUID.randomUUID().toString())
            .category(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
