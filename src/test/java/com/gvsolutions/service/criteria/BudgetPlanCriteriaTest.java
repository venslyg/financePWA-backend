package com.gvsolutions.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class BudgetPlanCriteriaTest {

    @Test
    void newBudgetPlanCriteriaHasAllFiltersNullTest() {
        var budgetPlanCriteria = new BudgetPlanCriteria();
        assertThat(budgetPlanCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void budgetPlanCriteriaFluentMethodsCreatesFiltersTest() {
        var budgetPlanCriteria = new BudgetPlanCriteria();

        setAllFilters(budgetPlanCriteria);

        assertThat(budgetPlanCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void budgetPlanCriteriaCopyCreatesNullFilterTest() {
        var budgetPlanCriteria = new BudgetPlanCriteria();
        var copy = budgetPlanCriteria.copy();

        assertThat(budgetPlanCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(budgetPlanCriteria)
        );
    }

    @Test
    void budgetPlanCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var budgetPlanCriteria = new BudgetPlanCriteria();
        setAllFilters(budgetPlanCriteria);

        var copy = budgetPlanCriteria.copy();

        assertThat(budgetPlanCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(budgetPlanCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var budgetPlanCriteria = new BudgetPlanCriteria();

        assertThat(budgetPlanCriteria).hasToString("BudgetPlanCriteria{}");
    }

    private static void setAllFilters(BudgetPlanCriteria budgetPlanCriteria) {
        budgetPlanCriteria.id();
        budgetPlanCriteria.branchCode();
        budgetPlanCriteria.branchId();
        budgetPlanCriteria.accountCode();
        budgetPlanCriteria.budgetPlanCode();
        budgetPlanCriteria.departmentName();
        budgetPlanCriteria.year();
        budgetPlanCriteria.allocatedAmount();
        budgetPlanCriteria.spentAmount();
        budgetPlanCriteria.remainingAmount();
        budgetPlanCriteria.usedPercentage();
        budgetPlanCriteria.alertStatus();
        budgetPlanCriteria.createdBy();
        budgetPlanCriteria.createdDate();
        budgetPlanCriteria.lastModifiedBy();
        budgetPlanCriteria.lastModifiedDate();
        budgetPlanCriteria.distinct();
    }

    private static Condition<BudgetPlanCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getBranchCode()) &&
                condition.apply(criteria.getBranchId()) &&
                condition.apply(criteria.getAccountCode()) &&
                condition.apply(criteria.getBudgetPlanCode()) &&
                condition.apply(criteria.getDepartmentName()) &&
                condition.apply(criteria.getYear()) &&
                condition.apply(criteria.getAllocatedAmount()) &&
                condition.apply(criteria.getSpentAmount()) &&
                condition.apply(criteria.getRemainingAmount()) &&
                condition.apply(criteria.getUsedPercentage()) &&
                condition.apply(criteria.getAlertStatus()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<BudgetPlanCriteria> copyFiltersAre(BudgetPlanCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getBranchCode(), copy.getBranchCode()) &&
                condition.apply(criteria.getBranchId(), copy.getBranchId()) &&
                condition.apply(criteria.getAccountCode(), copy.getAccountCode()) &&
                condition.apply(criteria.getBudgetPlanCode(), copy.getBudgetPlanCode()) &&
                condition.apply(criteria.getDepartmentName(), copy.getDepartmentName()) &&
                condition.apply(criteria.getYear(), copy.getYear()) &&
                condition.apply(criteria.getAllocatedAmount(), copy.getAllocatedAmount()) &&
                condition.apply(criteria.getSpentAmount(), copy.getSpentAmount()) &&
                condition.apply(criteria.getRemainingAmount(), copy.getRemainingAmount()) &&
                condition.apply(criteria.getUsedPercentage(), copy.getUsedPercentage()) &&
                condition.apply(criteria.getAlertStatus(), copy.getAlertStatus()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
