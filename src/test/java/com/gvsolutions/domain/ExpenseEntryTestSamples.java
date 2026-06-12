package com.gvsolutions.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ExpenseEntryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ExpenseEntry getExpenseEntrySample1() {
        return new ExpenseEntry()
            .id(1L)
            .branchCode("branchCode1")
            .branchId("branchId1")
            .accountCode("accountCode1")
            .expenseCode("expenseCode1")
            .expenseCategoryCode("expenseCategoryCode1")
            .expenseSubCategoryCode("expenseSubCategoryCode1")
            .createdByUsername("createdByUsername1")
            .voucherNo("voucherNo1")
            .description("description1")
            .approvedBy("approvedBy1")
            .vendor("vendor1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static ExpenseEntry getExpenseEntrySample2() {
        return new ExpenseEntry()
            .id(2L)
            .branchCode("branchCode2")
            .branchId("branchId2")
            .accountCode("accountCode2")
            .expenseCode("expenseCode2")
            .expenseCategoryCode("expenseCategoryCode2")
            .expenseSubCategoryCode("expenseSubCategoryCode2")
            .createdByUsername("createdByUsername2")
            .voucherNo("voucherNo2")
            .description("description2")
            .approvedBy("approvedBy2")
            .vendor("vendor2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static ExpenseEntry getExpenseEntryRandomSampleGenerator() {
        return new ExpenseEntry()
            .id(longCount.incrementAndGet())
            .branchCode(UUID.randomUUID().toString())
            .branchId(UUID.randomUUID().toString())
            .accountCode(UUID.randomUUID().toString())
            .expenseCode(UUID.randomUUID().toString())
            .expenseCategoryCode(UUID.randomUUID().toString())
            .expenseSubCategoryCode(UUID.randomUUID().toString())
            .createdByUsername(UUID.randomUUID().toString())
            .voucherNo(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .approvedBy(UUID.randomUUID().toString())
            .vendor(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
