package com.gvsolutions.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ExpenseCategoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ExpenseCategory getExpenseCategorySample1() {
        return new ExpenseCategory()
            .id(1L)
            .categoryCode("categoryCode1")
            .categoryName("categoryName1")
            .description("description1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static ExpenseCategory getExpenseCategorySample2() {
        return new ExpenseCategory()
            .id(2L)
            .categoryCode("categoryCode2")
            .categoryName("categoryName2")
            .description("description2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static ExpenseCategory getExpenseCategoryRandomSampleGenerator() {
        return new ExpenseCategory()
            .id(longCount.incrementAndGet())
            .categoryCode(UUID.randomUUID().toString())
            .categoryName(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
