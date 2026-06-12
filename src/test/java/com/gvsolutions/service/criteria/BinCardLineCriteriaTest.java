package com.gvsolutions.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class BinCardLineCriteriaTest {

    @Test
    void newBinCardLineCriteriaHasAllFiltersNullTest() {
        var binCardLineCriteria = new BinCardLineCriteria();
        assertThat(binCardLineCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void binCardLineCriteriaFluentMethodsCreatesFiltersTest() {
        var binCardLineCriteria = new BinCardLineCriteria();

        setAllFilters(binCardLineCriteria);

        assertThat(binCardLineCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void binCardLineCriteriaCopyCreatesNullFilterTest() {
        var binCardLineCriteria = new BinCardLineCriteria();
        var copy = binCardLineCriteria.copy();

        assertThat(binCardLineCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(binCardLineCriteria)
        );
    }

    @Test
    void binCardLineCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var binCardLineCriteria = new BinCardLineCriteria();
        setAllFilters(binCardLineCriteria);

        var copy = binCardLineCriteria.copy();

        assertThat(binCardLineCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(binCardLineCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var binCardLineCriteria = new BinCardLineCriteria();

        assertThat(binCardLineCriteria).hasToString("BinCardLineCriteria{}");
    }

    private static void setAllFilters(BinCardLineCriteria binCardLineCriteria) {
        binCardLineCriteria.id();
        binCardLineCriteria.inventoryItemCode();
        binCardLineCriteria.date();
        binCardLineCriteria.referenceNo();
        binCardLineCriteria.description();
        binCardLineCriteria.quantityIn();
        binCardLineCriteria.quantityOut();
        binCardLineCriteria.runningBalance();
        binCardLineCriteria.createdBy();
        binCardLineCriteria.createdDate();
        binCardLineCriteria.lastModifiedBy();
        binCardLineCriteria.lastModifiedDate();
        binCardLineCriteria.distinct();
    }

    private static Condition<BinCardLineCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getInventoryItemCode()) &&
                condition.apply(criteria.getDate()) &&
                condition.apply(criteria.getReferenceNo()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getQuantityIn()) &&
                condition.apply(criteria.getQuantityOut()) &&
                condition.apply(criteria.getRunningBalance()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<BinCardLineCriteria> copyFiltersAre(BinCardLineCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getInventoryItemCode(), copy.getInventoryItemCode()) &&
                condition.apply(criteria.getDate(), copy.getDate()) &&
                condition.apply(criteria.getReferenceNo(), copy.getReferenceNo()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getQuantityIn(), copy.getQuantityIn()) &&
                condition.apply(criteria.getQuantityOut(), copy.getQuantityOut()) &&
                condition.apply(criteria.getRunningBalance(), copy.getRunningBalance()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
