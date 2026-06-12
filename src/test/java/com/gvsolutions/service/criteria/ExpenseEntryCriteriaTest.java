package com.gvsolutions.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ExpenseEntryCriteriaTest {

    @Test
    void newExpenseEntryCriteriaHasAllFiltersNullTest() {
        var expenseEntryCriteria = new ExpenseEntryCriteria();
        assertThat(expenseEntryCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void expenseEntryCriteriaFluentMethodsCreatesFiltersTest() {
        var expenseEntryCriteria = new ExpenseEntryCriteria();

        setAllFilters(expenseEntryCriteria);

        assertThat(expenseEntryCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void expenseEntryCriteriaCopyCreatesNullFilterTest() {
        var expenseEntryCriteria = new ExpenseEntryCriteria();
        var copy = expenseEntryCriteria.copy();

        assertThat(expenseEntryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(expenseEntryCriteria)
        );
    }

    @Test
    void expenseEntryCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var expenseEntryCriteria = new ExpenseEntryCriteria();
        setAllFilters(expenseEntryCriteria);

        var copy = expenseEntryCriteria.copy();

        assertThat(expenseEntryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(expenseEntryCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var expenseEntryCriteria = new ExpenseEntryCriteria();

        assertThat(expenseEntryCriteria).hasToString("ExpenseEntryCriteria{}");
    }

    private static void setAllFilters(ExpenseEntryCriteria expenseEntryCriteria) {
        expenseEntryCriteria.id();
        expenseEntryCriteria.branchCode();
        expenseEntryCriteria.branchId();
        expenseEntryCriteria.accountCode();
        expenseEntryCriteria.expenseCode();
        expenseEntryCriteria.expenseCategoryCode();
        expenseEntryCriteria.expenseSubCategoryCode();
        expenseEntryCriteria.createdByUsername();
        expenseEntryCriteria.date();
        expenseEntryCriteria.voucherNo();
        expenseEntryCriteria.description();
        expenseEntryCriteria.amount();
        expenseEntryCriteria.paymentMode();
        expenseEntryCriteria.approvalStatus();
        expenseEntryCriteria.approvedBy();
        expenseEntryCriteria.vendor();
        expenseEntryCriteria.syncStatus();
        expenseEntryCriteria.createdBy();
        expenseEntryCriteria.createdDate();
        expenseEntryCriteria.lastModifiedBy();
        expenseEntryCriteria.lastModifiedDate();
        expenseEntryCriteria.distinct();
    }

    private static Condition<ExpenseEntryCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getBranchCode()) &&
                condition.apply(criteria.getBranchId()) &&
                condition.apply(criteria.getAccountCode()) &&
                condition.apply(criteria.getExpenseCode()) &&
                condition.apply(criteria.getExpenseCategoryCode()) &&
                condition.apply(criteria.getExpenseSubCategoryCode()) &&
                condition.apply(criteria.getCreatedByUsername()) &&
                condition.apply(criteria.getDate()) &&
                condition.apply(criteria.getVoucherNo()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getAmount()) &&
                condition.apply(criteria.getPaymentMode()) &&
                condition.apply(criteria.getApprovalStatus()) &&
                condition.apply(criteria.getApprovedBy()) &&
                condition.apply(criteria.getVendor()) &&
                condition.apply(criteria.getSyncStatus()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ExpenseEntryCriteria> copyFiltersAre(
        ExpenseEntryCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getBranchCode(), copy.getBranchCode()) &&
                condition.apply(criteria.getBranchId(), copy.getBranchId()) &&
                condition.apply(criteria.getAccountCode(), copy.getAccountCode()) &&
                condition.apply(criteria.getExpenseCode(), copy.getExpenseCode()) &&
                condition.apply(criteria.getExpenseCategoryCode(), copy.getExpenseCategoryCode()) &&
                condition.apply(criteria.getExpenseSubCategoryCode(), copy.getExpenseSubCategoryCode()) &&
                condition.apply(criteria.getCreatedByUsername(), copy.getCreatedByUsername()) &&
                condition.apply(criteria.getDate(), copy.getDate()) &&
                condition.apply(criteria.getVoucherNo(), copy.getVoucherNo()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getAmount(), copy.getAmount()) &&
                condition.apply(criteria.getPaymentMode(), copy.getPaymentMode()) &&
                condition.apply(criteria.getApprovalStatus(), copy.getApprovalStatus()) &&
                condition.apply(criteria.getApprovedBy(), copy.getApprovedBy()) &&
                condition.apply(criteria.getVendor(), copy.getVendor()) &&
                condition.apply(criteria.getSyncStatus(), copy.getSyncStatus()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
