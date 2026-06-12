package com.gvsolutions.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AssetRegisterTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AssetRegister getAssetRegisterSample1() {
        return new AssetRegister()
            .id(1L)
            .branchCode("branchCode1")
            .branchId("branchId1")
            .assetRegisterCode("assetRegisterCode1")
            .assetCategoryCode("assetCategoryCode1")
            .assetSubCategoryCode("assetSubCategoryCode1")
            .assetName("assetName1")
            .category("category1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static AssetRegister getAssetRegisterSample2() {
        return new AssetRegister()
            .id(2L)
            .branchCode("branchCode2")
            .branchId("branchId2")
            .assetRegisterCode("assetRegisterCode2")
            .assetCategoryCode("assetCategoryCode2")
            .assetSubCategoryCode("assetSubCategoryCode2")
            .assetName("assetName2")
            .category("category2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static AssetRegister getAssetRegisterRandomSampleGenerator() {
        return new AssetRegister()
            .id(longCount.incrementAndGet())
            .branchCode(UUID.randomUUID().toString())
            .branchId(UUID.randomUUID().toString())
            .assetRegisterCode(UUID.randomUUID().toString())
            .assetCategoryCode(UUID.randomUUID().toString())
            .assetSubCategoryCode(UUID.randomUUID().toString())
            .assetName(UUID.randomUUID().toString())
            .category(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
