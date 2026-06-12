package com.gvsolutions.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class IncomeEntryCriteriaTest {

    @Test
    void newIncomeEntryCriteriaHasAllFiltersNullTest() {
        var incomeEntryCriteria = new IncomeEntryCriteria();
        assertThat(incomeEntryCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void incomeEntryCriteriaFluentMethodsCreatesFiltersTest() {
        var incomeEntryCriteria = new IncomeEntryCriteria();

        setAllFilters(incomeEntryCriteria);

        assertThat(incomeEntryCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void incomeEntryCriteriaCopyCreatesNullFilterTest() {
        var incomeEntryCriteria = new IncomeEntryCriteria();
        var copy = incomeEntryCriteria.copy();

        assertThat(incomeEntryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(incomeEntryCriteria)
        );
    }

    @Test
    void incomeEntryCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var incomeEntryCriteria = new IncomeEntryCriteria();
        setAllFilters(incomeEntryCriteria);

        var copy = incomeEntryCriteria.copy();

        assertThat(incomeEntryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(incomeEntryCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var incomeEntryCriteria = new IncomeEntryCriteria();

        assertThat(incomeEntryCriteria).hasToString("IncomeEntryCriteria{}");
    }

    private static void setAllFilters(IncomeEntryCriteria incomeEntryCriteria) {
        incomeEntryCriteria.id();
        incomeEntryCriteria.branchCode();
        incomeEntryCriteria.branchId();
        incomeEntryCriteria.accountCode();
        incomeEntryCriteria.incomeCode();
        incomeEntryCriteria.createdByUsername();
        incomeEntryCriteria.date();
        incomeEntryCriteria.receiptNo();
        incomeEntryCriteria.description();
        incomeEntryCriteria.incomeType();
        incomeEntryCriteria.amount();
        incomeEntryCriteria.paymentMethod();
        incomeEntryCriteria.receivablePerson();
        incomeEntryCriteria.receivedBy();
        incomeEntryCriteria.syncStatus();
        incomeEntryCriteria.createdBy();
        incomeEntryCriteria.createdDate();
        incomeEntryCriteria.lastModifiedBy();
        incomeEntryCriteria.lastModifiedDate();
        incomeEntryCriteria.distinct();
    }

    private static Condition<IncomeEntryCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getBranchCode()) &&
                condition.apply(criteria.getBranchId()) &&
                condition.apply(criteria.getAccountCode()) &&
                condition.apply(criteria.getIncomeCode()) &&
                condition.apply(criteria.getCreatedByUsername()) &&
                condition.apply(criteria.getDate()) &&
                condition.apply(criteria.getReceiptNo()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getIncomeType()) &&
                condition.apply(criteria.getAmount()) &&
                condition.apply(criteria.getPaymentMethod()) &&
                condition.apply(criteria.getReceivablePerson()) &&
                condition.apply(criteria.getReceivedBy()) &&
                condition.apply(criteria.getSyncStatus()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<IncomeEntryCriteria> copyFiltersAre(IncomeEntryCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getBranchCode(), copy.getBranchCode()) &&
                condition.apply(criteria.getBranchId(), copy.getBranchId()) &&
                condition.apply(criteria.getAccountCode(), copy.getAccountCode()) &&
                condition.apply(criteria.getIncomeCode(), copy.getIncomeCode()) &&
                condition.apply(criteria.getCreatedByUsername(), copy.getCreatedByUsername()) &&
                condition.apply(criteria.getDate(), copy.getDate()) &&
                condition.apply(criteria.getReceiptNo(), copy.getReceiptNo()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getIncomeType(), copy.getIncomeType()) &&
                condition.apply(criteria.getAmount(), copy.getAmount()) &&
                condition.apply(criteria.getPaymentMethod(), copy.getPaymentMethod()) &&
                condition.apply(criteria.getReceivablePerson(), copy.getReceivablePerson()) &&
                condition.apply(criteria.getReceivedBy(), copy.getReceivedBy()) &&
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
