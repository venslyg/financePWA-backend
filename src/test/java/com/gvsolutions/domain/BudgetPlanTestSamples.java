package com.gvsolutions.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class BudgetPlanTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static BudgetPlan getBudgetPlanSample1() {
        return new BudgetPlan()
            .id(1L)
            .branchCode("branchCode1")
            .branchId("branchId1")
            .accountCode("accountCode1")
            .budgetPlanCode("budgetPlanCode1")
            .departmentName("departmentName1")
            .year(1)
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static BudgetPlan getBudgetPlanSample2() {
        return new BudgetPlan()
            .id(2L)
            .branchCode("branchCode2")
            .branchId("branchId2")
            .accountCode("accountCode2")
            .budgetPlanCode("budgetPlanCode2")
            .departmentName("departmentName2")
            .year(2)
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static BudgetPlan getBudgetPlanRandomSampleGenerator() {
        return new BudgetPlan()
            .id(longCount.incrementAndGet())
            .branchCode(UUID.randomUUID().toString())
            .branchId(UUID.randomUUID().toString())
            .accountCode(UUID.randomUUID().toString())
            .budgetPlanCode(UUID.randomUUID().toString())
            .departmentName(UUID.randomUUID().toString())
            .year(intCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
