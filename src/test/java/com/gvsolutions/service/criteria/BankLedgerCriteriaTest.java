package com.gvsolutions.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class BankLedgerCriteriaTest {

    @Test
    void newBankLedgerCriteriaHasAllFiltersNullTest() {
        var bankLedgerCriteria = new BankLedgerCriteria();
        assertThat(bankLedgerCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void bankLedgerCriteriaFluentMethodsCreatesFiltersTest() {
        var bankLedgerCriteria = new BankLedgerCriteria();

        setAllFilters(bankLedgerCriteria);

        assertThat(bankLedgerCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void bankLedgerCriteriaCopyCreatesNullFilterTest() {
        var bankLedgerCriteria = new BankLedgerCriteria();
        var copy = bankLedgerCriteria.copy();

        assertThat(bankLedgerCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(bankLedgerCriteria)
        );
    }

    @Test
    void bankLedgerCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var bankLedgerCriteria = new BankLedgerCriteria();
        setAllFilters(bankLedgerCriteria);

        var copy = bankLedgerCriteria.copy();

        assertThat(bankLedgerCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(bankLedgerCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var bankLedgerCriteria = new BankLedgerCriteria();

        assertThat(bankLedgerCriteria).hasToString("BankLedgerCriteria{}");
    }

    private static void setAllFilters(BankLedgerCriteria bankLedgerCriteria) {
        bankLedgerCriteria.id();
        bankLedgerCriteria.branchCode();
        bankLedgerCriteria.branchId();
        bankLedgerCriteria.bankLedgerCode();
        bankLedgerCriteria.date();
        bankLedgerCriteria.referenceNo();
        bankLedgerCriteria.description();
        bankLedgerCriteria.depositAmount();
        bankLedgerCriteria.withdrawalAmount();
        bankLedgerCriteria.runningBalance();
        bankLedgerCriteria.remark();
        bankLedgerCriteria.createdBy();
        bankLedgerCriteria.createdDate();
        bankLedgerCriteria.lastModifiedBy();
        bankLedgerCriteria.lastModifiedDate();
        bankLedgerCriteria.distinct();
    }

    private static Condition<BankLedgerCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getBranchCode()) &&
                condition.apply(criteria.getBranchId()) &&
                condition.apply(criteria.getBankLedgerCode()) &&
                condition.apply(criteria.getDate()) &&
                condition.apply(criteria.getReferenceNo()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getDepositAmount()) &&
                condition.apply(criteria.getWithdrawalAmount()) &&
                condition.apply(criteria.getRunningBalance()) &&
                condition.apply(criteria.getRemark()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<BankLedgerCriteria> copyFiltersAre(BankLedgerCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getBranchCode(), copy.getBranchCode()) &&
                condition.apply(criteria.getBranchId(), copy.getBranchId()) &&
                condition.apply(criteria.getBankLedgerCode(), copy.getBankLedgerCode()) &&
                condition.apply(criteria.getDate(), copy.getDate()) &&
                condition.apply(criteria.getReferenceNo(), copy.getReferenceNo()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getDepositAmount(), copy.getDepositAmount()) &&
                condition.apply(criteria.getWithdrawalAmount(), copy.getWithdrawalAmount()) &&
                condition.apply(criteria.getRunningBalance(), copy.getRunningBalance()) &&
                condition.apply(criteria.getRemark(), copy.getRemark()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
