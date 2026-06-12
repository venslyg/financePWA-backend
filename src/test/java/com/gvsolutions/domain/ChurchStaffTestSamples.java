package com.gvsolutions.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ChurchStaffTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ChurchStaff getChurchStaffSample1() {
        return new ChurchStaff()
            .id(1L)
            .staffCode("staffCode1")
            .branchCode("branchCode1")
            .fullName("fullName1")
            .position("position1")
            .contactNumber("contactNumber1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static ChurchStaff getChurchStaffSample2() {
        return new ChurchStaff()
            .id(2L)
            .staffCode("staffCode2")
            .branchCode("branchCode2")
            .fullName("fullName2")
            .position("position2")
            .contactNumber("contactNumber2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static ChurchStaff getChurchStaffRandomSampleGenerator() {
        return new ChurchStaff()
            .id(longCount.incrementAndGet())
            .staffCode(UUID.randomUUID().toString())
            .branchCode(UUID.randomUUID().toString())
            .fullName(UUID.randomUUID().toString())
            .position(UUID.randomUUID().toString())
            .contactNumber(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
