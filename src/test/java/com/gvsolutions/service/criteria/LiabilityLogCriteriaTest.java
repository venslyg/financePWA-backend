package com.gvsolutions.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class LiabilityLogCriteriaTest {

    @Test
    void newLiabilityLogCriteriaHasAllFiltersNullTest() {
        var liabilityLogCriteria = new LiabilityLogCriteria();
        assertThat(liabilityLogCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void liabilityLogCriteriaFluentMethodsCreatesFiltersTest() {
        var liabilityLogCriteria = new LiabilityLogCriteria();

        setAllFilters(liabilityLogCriteria);

        assertThat(liabilityLogCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void liabilityLogCriteriaCopyCreatesNullFilterTest() {
        var liabilityLogCriteria = new LiabilityLogCriteria();
        var copy = liabilityLogCriteria.copy();

        assertThat(liabilityLogCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(liabilityLogCriteria)
        );
    }

    @Test
    void liabilityLogCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var liabilityLogCriteria = new LiabilityLogCriteria();
        setAllFilters(liabilityLogCriteria);

        var copy = liabilityLogCriteria.copy();

        assertThat(liabilityLogCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(liabilityLogCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var liabilityLogCriteria = new LiabilityLogCriteria();

        assertThat(liabilityLogCriteria).hasToString("LiabilityLogCriteria{}");
    }

    private static void setAllFilters(LiabilityLogCriteria liabilityLogCriteria) {
        liabilityLogCriteria.id();
        liabilityLogCriteria.branchCode();
        liabilityLogCriteria.liabilityCode();
        liabilityLogCriteria.loanFrom();
        liabilityLogCriteria.description();
        liabilityLogCriteria.liabilityType();
        liabilityLogCriteria.totalLoanAmount();
        liabilityLogCriteria.startDate();
        liabilityLogCriteria.endDate();
        liabilityLogCriteria.interestPercentage();
        liabilityLogCriteria.monthlyPaymentAmount();
        liabilityLogCriteria.principalPaid();
        liabilityLogCriteria.balanceToPay();
        liabilityLogCriteria.status();
        liabilityLogCriteria.createdBy();
        liabilityLogCriteria.createdDate();
        liabilityLogCriteria.lastModifiedBy();
        liabilityLogCriteria.lastModifiedDate();
        liabilityLogCriteria.distinct();
    }

    private static Condition<LiabilityLogCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getBranchCode()) &&
                condition.apply(criteria.getLiabilityCode()) &&
                condition.apply(criteria.getLoanFrom()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getLiabilityType()) &&
                condition.apply(criteria.getTotalLoanAmount()) &&
                condition.apply(criteria.getStartDate()) &&
                condition.apply(criteria.getEndDate()) &&
                condition.apply(criteria.getInterestPercentage()) &&
                condition.apply(criteria.getMonthlyPaymentAmount()) &&
                condition.apply(criteria.getPrincipalPaid()) &&
                condition.apply(criteria.getBalanceToPay()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<LiabilityLogCriteria> copyFiltersAre(
        LiabilityLogCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getBranchCode(), copy.getBranchCode()) &&
                condition.apply(criteria.getLiabilityCode(), copy.getLiabilityCode()) &&
                condition.apply(criteria.getLoanFrom(), copy.getLoanFrom()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getLiabilityType(), copy.getLiabilityType()) &&
                condition.apply(criteria.getTotalLoanAmount(), copy.getTotalLoanAmount()) &&
                condition.apply(criteria.getStartDate(), copy.getStartDate()) &&
                condition.apply(criteria.getEndDate(), copy.getEndDate()) &&
                condition.apply(criteria.getInterestPercentage(), copy.getInterestPercentage()) &&
                condition.apply(criteria.getMonthlyPaymentAmount(), copy.getMonthlyPaymentAmount()) &&
                condition.apply(criteria.getPrincipalPaid(), copy.getPrincipalPaid()) &&
                condition.apply(criteria.getBalanceToPay(), copy.getBalanceToPay()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
