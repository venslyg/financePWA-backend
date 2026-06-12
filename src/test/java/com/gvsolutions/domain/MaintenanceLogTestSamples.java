package com.gvsolutions.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MaintenanceLogTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MaintenanceLog getMaintenanceLogSample1() {
        return new MaintenanceLog()
            .id(1L)
            .maintenanceLogCode("maintenanceLogCode1")
            .description("description1")
            .vendor("vendor1")
            .note("note1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static MaintenanceLog getMaintenanceLogSample2() {
        return new MaintenanceLog()
            .id(2L)
            .maintenanceLogCode("maintenanceLogCode2")
            .description("description2")
            .vendor("vendor2")
            .note("note2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static MaintenanceLog getMaintenanceLogRandomSampleGenerator() {
        return new MaintenanceLog()
            .id(longCount.incrementAndGet())
            .maintenanceLogCode(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .vendor(UUID.randomUUID().toString())
            .note(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
