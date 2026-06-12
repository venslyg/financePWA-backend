package com.gvsolutions.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AssetCategoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AssetCategory getAssetCategorySample1() {
        return new AssetCategory()
            .id(1L)
            .branchCode("branchCode1")
            .branchId("branchId1")
            .assetCategoryCode("assetCategoryCode1")
            .assetCategoryName("assetCategoryName1")
            .description("description1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static AssetCategory getAssetCategorySample2() {
        return new AssetCategory()
            .id(2L)
            .branchCode("branchCode2")
            .branchId("branchId2")
            .assetCategoryCode("assetCategoryCode2")
            .assetCategoryName("assetCategoryName2")
            .description("description2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static AssetCategory getAssetCategoryRandomSampleGenerator() {
        return new AssetCategory()
            .id(longCount.incrementAndGet())
            .branchCode(UUID.randomUUID().toString())
            .branchId(UUID.randomUUID().toString())
            .assetCategoryCode(UUID.randomUUID().toString())
            .assetCategoryName(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
