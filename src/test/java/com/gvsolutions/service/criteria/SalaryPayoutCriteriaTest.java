package com.gvsolutions.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SalaryPayoutCriteriaTest {

    @Test
    void newSalaryPayoutCriteriaHasAllFiltersNullTest() {
        var salaryPayoutCriteria = new SalaryPayoutCriteria();
        assertThat(salaryPayoutCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void salaryPayoutCriteriaFluentMethodsCreatesFiltersTest() {
        var salaryPayoutCriteria = new SalaryPayoutCriteria();

        setAllFilters(salaryPayoutCriteria);

        assertThat(salaryPayoutCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void salaryPayoutCriteriaCopyCreatesNullFilterTest() {
        var salaryPayoutCriteria = new SalaryPayoutCriteria();
        var copy = salaryPayoutCriteria.copy();

        assertThat(salaryPayoutCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(salaryPayoutCriteria)
        );
    }

    @Test
    void salaryPayoutCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var salaryPayoutCriteria = new SalaryPayoutCriteria();
        setAllFilters(salaryPayoutCriteria);

        var copy = salaryPayoutCriteria.copy();

        assertThat(salaryPayoutCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(salaryPayoutCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var salaryPayoutCriteria = new SalaryPayoutCriteria();

        assertThat(salaryPayoutCriteria).hasToString("SalaryPayoutCriteria{}");
    }

    private static void setAllFilters(SalaryPayoutCriteria salaryPayoutCriteria) {
        salaryPayoutCriteria.id();
        salaryPayoutCriteria.branchCode();
        salaryPayoutCriteria.salaryPayoutCode();
        salaryPayoutCriteria.staffCode();
        salaryPayoutCriteria.payPeriod();
        salaryPayoutCriteria.baseSalary();
        salaryPayoutCriteria.allowances();
        salaryPayoutCriteria.deductions();
        salaryPayoutCriteria.netPay();
        salaryPayoutCriteria.payoutDate();
        salaryPayoutCriteria.createdBy();
        salaryPayoutCriteria.createdDate();
        salaryPayoutCriteria.lastModifiedBy();
        salaryPayoutCriteria.lastModifiedDate();
        salaryPayoutCriteria.distinct();
    }

    private static Condition<SalaryPayoutCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getBranchCode()) &&
                condition.apply(criteria.getSalaryPayoutCode()) &&
                condition.apply(criteria.getStaffCode()) &&
                condition.apply(criteria.getPayPeriod()) &&
                condition.apply(criteria.getBaseSalary()) &&
                condition.apply(criteria.getAllowances()) &&
                condition.apply(criteria.getDeductions()) &&
                condition.apply(criteria.getNetPay()) &&
                condition.apply(criteria.getPayoutDate()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SalaryPayoutCriteria> copyFiltersAre(
        SalaryPayoutCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getBranchCode(), copy.getBranchCode()) &&
                condition.apply(criteria.getSalaryPayoutCode(), copy.getSalaryPayoutCode()) &&
                condition.apply(criteria.getStaffCode(), copy.getStaffCode()) &&
                condition.apply(criteria.getPayPeriod(), copy.getPayPeriod()) &&
                condition.apply(criteria.getBaseSalary(), copy.getBaseSalary()) &&
                condition.apply(criteria.getAllowances(), copy.getAllowances()) &&
                condition.apply(criteria.getDeductions(), copy.getDeductions()) &&
                condition.apply(criteria.getNetPay(), copy.getNetPay()) &&
                condition.apply(criteria.getPayoutDate(), copy.getPayoutDate()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
