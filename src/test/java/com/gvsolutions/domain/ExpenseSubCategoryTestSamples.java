package com.gvsolutions.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ExpenseSubCategoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ExpenseSubCategory getExpenseSubCategorySample1() {
        return new ExpenseSubCategory()
            .id(1L)
            .categoryCode("categoryCode1")
            .subCategoryCode("subCategoryCode1")
            .subCategoryName("subCategoryName1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static ExpenseSubCategory getExpenseSubCategorySample2() {
        return new ExpenseSubCategory()
            .id(2L)
            .categoryCode("categoryCode2")
            .subCategoryCode("subCategoryCode2")
            .subCategoryName("subCategoryName2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static ExpenseSubCategory getExpenseSubCategoryRandomSampleGenerator() {
        return new ExpenseSubCategory()
            .id(longCount.incrementAndGet())
            .categoryCode(UUID.randomUUID().toString())
            .subCategoryCode(UUID.randomUUID().toString())
            .subCategoryName(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
