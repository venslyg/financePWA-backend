package com.gvsolutions.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AssetDepreciationHistoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AssetDepreciationHistory getAssetDepreciationHistorySample1() {
        return new AssetDepreciationHistory()
            .id(1L)
            .assetRegisterCode("assetRegisterCode1")
            .processedBy("processedBy1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static AssetDepreciationHistory getAssetDepreciationHistorySample2() {
        return new AssetDepreciationHistory()
            .id(2L)
            .assetRegisterCode("assetRegisterCode2")
            .processedBy("processedBy2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static AssetDepreciationHistory getAssetDepreciationHistoryRandomSampleGenerator() {
        return new AssetDepreciationHistory()
            .id(longCount.incrementAndGet())
            .assetRegisterCode(UUID.randomUUID().toString())
            .processedBy(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
