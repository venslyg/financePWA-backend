package com.gvsolutions.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AssetSubCategoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AssetSubCategory getAssetSubCategorySample1() {
        return new AssetSubCategory()
            .id(1L)
            .assetCategoryCode("assetCategoryCode1")
            .assetSubCategoryCode("assetSubCategoryCode1")
            .assetSubCategoryName("assetSubCategoryName1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static AssetSubCategory getAssetSubCategorySample2() {
        return new AssetSubCategory()
            .id(2L)
            .assetCategoryCode("assetCategoryCode2")
            .assetSubCategoryCode("assetSubCategoryCode2")
            .assetSubCategoryName("assetSubCategoryName2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static AssetSubCategory getAssetSubCategoryRandomSampleGenerator() {
        return new AssetSubCategory()
            .id(longCount.incrementAndGet())
            .assetCategoryCode(UUID.randomUUID().toString())
            .assetSubCategoryCode(UUID.randomUUID().toString())
            .assetSubCategoryName(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
