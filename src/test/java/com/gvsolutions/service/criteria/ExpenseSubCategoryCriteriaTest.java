package com.gvsolutions.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ExpenseSubCategoryCriteriaTest {

    @Test
    void newExpenseSubCategoryCriteriaHasAllFiltersNullTest() {
        var expenseSubCategoryCriteria = new ExpenseSubCategoryCriteria();
        assertThat(expenseSubCategoryCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void expenseSubCategoryCriteriaFluentMethodsCreatesFiltersTest() {
        var expenseSubCategoryCriteria = new ExpenseSubCategoryCriteria();

        setAllFilters(expenseSubCategoryCriteria);

        assertThat(expenseSubCategoryCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void expenseSubCategoryCriteriaCopyCreatesNullFilterTest() {
        var expenseSubCategoryCriteria = new ExpenseSubCategoryCriteria();
        var copy = expenseSubCategoryCriteria.copy();

        assertThat(expenseSubCategoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(expenseSubCategoryCriteria)
        );
    }

    @Test
    void expenseSubCategoryCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var expenseSubCategoryCriteria = new ExpenseSubCategoryCriteria();
        setAllFilters(expenseSubCategoryCriteria);

        var copy = expenseSubCategoryCriteria.copy();

        assertThat(expenseSubCategoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(expenseSubCategoryCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var expenseSubCategoryCriteria = new ExpenseSubCategoryCriteria();

        assertThat(expenseSubCategoryCriteria).hasToString("ExpenseSubCategoryCriteria{}");
    }

    private static void setAllFilters(ExpenseSubCategoryCriteria expenseSubCategoryCriteria) {
        expenseSubCategoryCriteria.id();
        expenseSubCategoryCriteria.categoryCode();
        expenseSubCategoryCriteria.subCategoryCode();
        expenseSubCategoryCriteria.subCategoryName();
        expenseSubCategoryCriteria.createdBy();
        expenseSubCategoryCriteria.createdDate();
        expenseSubCategoryCriteria.lastModifiedBy();
        expenseSubCategoryCriteria.lastModifiedDate();
        expenseSubCategoryCriteria.categoryId();
        expenseSubCategoryCriteria.distinct();
    }

    private static Condition<ExpenseSubCategoryCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCategoryCode()) &&
                condition.apply(criteria.getSubCategoryCode()) &&
                condition.apply(criteria.getSubCategoryName()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getCategoryId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ExpenseSubCategoryCriteria> copyFiltersAre(
        ExpenseSubCategoryCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCategoryCode(), copy.getCategoryCode()) &&
                condition.apply(criteria.getSubCategoryCode(), copy.getSubCategoryCode()) &&
                condition.apply(criteria.getSubCategoryName(), copy.getSubCategoryName()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getCategoryId(), copy.getCategoryId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
