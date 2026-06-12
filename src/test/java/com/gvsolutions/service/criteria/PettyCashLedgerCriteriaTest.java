package com.gvsolutions.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PettyCashLedgerCriteriaTest {

    @Test
    void newPettyCashLedgerCriteriaHasAllFiltersNullTest() {
        var pettyCashLedgerCriteria = new PettyCashLedgerCriteria();
        assertThat(pettyCashLedgerCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void pettyCashLedgerCriteriaFluentMethodsCreatesFiltersTest() {
        var pettyCashLedgerCriteria = new PettyCashLedgerCriteria();

        setAllFilters(pettyCashLedgerCriteria);

        assertThat(pettyCashLedgerCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void pettyCashLedgerCriteriaCopyCreatesNullFilterTest() {
        var pettyCashLedgerCriteria = new PettyCashLedgerCriteria();
        var copy = pettyCashLedgerCriteria.copy();

        assertThat(pettyCashLedgerCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(pettyCashLedgerCriteria)
        );
    }

    @Test
    void pettyCashLedgerCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var pettyCashLedgerCriteria = new PettyCashLedgerCriteria();
        setAllFilters(pettyCashLedgerCriteria);

        var copy = pettyCashLedgerCriteria.copy();

        assertThat(pettyCashLedgerCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(pettyCashLedgerCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var pettyCashLedgerCriteria = new PettyCashLedgerCriteria();

        assertThat(pettyCashLedgerCriteria).hasToString("PettyCashLedgerCriteria{}");
    }

    private static void setAllFilters(PettyCashLedgerCriteria pettyCashLedgerCriteria) {
        pettyCashLedgerCriteria.id();
        pettyCashLedgerCriteria.branchCode();
        pettyCashLedgerCriteria.pettyCashCode();
        pettyCashLedgerCriteria.date();
        pettyCashLedgerCriteria.pettyCashVoucherNo();
        pettyCashLedgerCriteria.description();
        pettyCashLedgerCriteria.cashIn();
        pettyCashLedgerCriteria.cashOut();
        pettyCashLedgerCriteria.runningBalance();
        pettyCashLedgerCriteria.linkedAccountCode();
        pettyCashLedgerCriteria.referenceNo();
        pettyCashLedgerCriteria.createdBy();
        pettyCashLedgerCriteria.createdDate();
        pettyCashLedgerCriteria.lastModifiedBy();
        pettyCashLedgerCriteria.lastModifiedDate();
        pettyCashLedgerCriteria.distinct();
    }

    private static Condition<PettyCashLedgerCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getBranchCode()) &&
                condition.apply(criteria.getPettyCashCode()) &&
                condition.apply(criteria.getDate()) &&
                condition.apply(criteria.getPettyCashVoucherNo()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getCashIn()) &&
                condition.apply(criteria.getCashOut()) &&
                condition.apply(criteria.getRunningBalance()) &&
                condition.apply(criteria.getLinkedAccountCode()) &&
                condition.apply(criteria.getReferenceNo()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PettyCashLedgerCriteria> copyFiltersAre(
        PettyCashLedgerCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getBranchCode(), copy.getBranchCode()) &&
                condition.apply(criteria.getPettyCashCode(), copy.getPettyCashCode()) &&
                condition.apply(criteria.getDate(), copy.getDate()) &&
                condition.apply(criteria.getPettyCashVoucherNo(), copy.getPettyCashVoucherNo()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getCashIn(), copy.getCashIn()) &&
                condition.apply(criteria.getCashOut(), copy.getCashOut()) &&
                condition.apply(criteria.getRunningBalance(), copy.getRunningBalance()) &&
                condition.apply(criteria.getLinkedAccountCode(), copy.getLinkedAccountCode()) &&
                condition.apply(criteria.getReferenceNo(), copy.getReferenceNo()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
