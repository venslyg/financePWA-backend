package com.gvsolutions.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ExpenseCategoryCriteriaTest {

    @Test
    void newExpenseCategoryCriteriaHasAllFiltersNullTest() {
        var expenseCategoryCriteria = new ExpenseCategoryCriteria();
        assertThat(expenseCategoryCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void expenseCategoryCriteriaFluentMethodsCreatesFiltersTest() {
        var expenseCategoryCriteria = new ExpenseCategoryCriteria();

        setAllFilters(expenseCategoryCriteria);

        assertThat(expenseCategoryCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void expenseCategoryCriteriaCopyCreatesNullFilterTest() {
        var expenseCategoryCriteria = new ExpenseCategoryCriteria();
        var copy = expenseCategoryCriteria.copy();

        assertThat(expenseCategoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(expenseCategoryCriteria)
        );
    }

    @Test
    void expenseCategoryCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var expenseCategoryCriteria = new ExpenseCategoryCriteria();
        setAllFilters(expenseCategoryCriteria);

        var copy = expenseCategoryCriteria.copy();

        assertThat(expenseCategoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(expenseCategoryCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var expenseCategoryCriteria = new ExpenseCategoryCriteria();

        assertThat(expenseCategoryCriteria).hasToString("ExpenseCategoryCriteria{}");
    }

    private static void setAllFilters(ExpenseCategoryCriteria expenseCategoryCriteria) {
        expenseCategoryCriteria.id();
        expenseCategoryCriteria.categoryCode();
        expenseCategoryCriteria.categoryName();
        expenseCategoryCriteria.description();
        expenseCategoryCriteria.createdBy();
        expenseCategoryCriteria.createdDate();
        expenseCategoryCriteria.lastModifiedBy();
        expenseCategoryCriteria.lastModifiedDate();
        expenseCategoryCriteria.distinct();
    }

    private static Condition<ExpenseCategoryCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCategoryCode()) &&
                condition.apply(criteria.getCategoryName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ExpenseCategoryCriteria> copyFiltersAre(
        ExpenseCategoryCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCategoryCode(), copy.getCategoryCode()) &&
                condition.apply(criteria.getCategoryName(), copy.getCategoryName()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
