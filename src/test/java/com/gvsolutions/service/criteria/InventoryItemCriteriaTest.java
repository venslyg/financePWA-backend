package com.gvsolutions.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class InventoryItemCriteriaTest {

    @Test
    void newInventoryItemCriteriaHasAllFiltersNullTest() {
        var inventoryItemCriteria = new InventoryItemCriteria();
        assertThat(inventoryItemCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void inventoryItemCriteriaFluentMethodsCreatesFiltersTest() {
        var inventoryItemCriteria = new InventoryItemCriteria();

        setAllFilters(inventoryItemCriteria);

        assertThat(inventoryItemCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void inventoryItemCriteriaCopyCreatesNullFilterTest() {
        var inventoryItemCriteria = new InventoryItemCriteria();
        var copy = inventoryItemCriteria.copy();

        assertThat(inventoryItemCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(inventoryItemCriteria)
        );
    }

    @Test
    void inventoryItemCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var inventoryItemCriteria = new InventoryItemCriteria();
        setAllFilters(inventoryItemCriteria);

        var copy = inventoryItemCriteria.copy();

        assertThat(inventoryItemCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(inventoryItemCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var inventoryItemCriteria = new InventoryItemCriteria();

        assertThat(inventoryItemCriteria).hasToString("InventoryItemCriteria{}");
    }

    private static void setAllFilters(InventoryItemCriteria inventoryItemCriteria) {
        inventoryItemCriteria.id();
        inventoryItemCriteria.branchCode();
        inventoryItemCriteria.branchId();
        inventoryItemCriteria.inventoryItemCode();
        inventoryItemCriteria.itemName();
        inventoryItemCriteria.category();
        inventoryItemCriteria.quantity();
        inventoryItemCriteria.unitPrice();
        inventoryItemCriteria.runningStockCount();
        inventoryItemCriteria.isActive();
        inventoryItemCriteria.createdBy();
        inventoryItemCriteria.createdDate();
        inventoryItemCriteria.lastModifiedBy();
        inventoryItemCriteria.lastModifiedDate();
        inventoryItemCriteria.distinct();
    }

    private static Condition<InventoryItemCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getBranchCode()) &&
                condition.apply(criteria.getBranchId()) &&
                condition.apply(criteria.getInventoryItemCode()) &&
                condition.apply(criteria.getItemName()) &&
                condition.apply(criteria.getCategory()) &&
                condition.apply(criteria.getQuantity()) &&
                condition.apply(criteria.getUnitPrice()) &&
                condition.apply(criteria.getRunningStockCount()) &&
                condition.apply(criteria.getIsActive()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<InventoryItemCriteria> copyFiltersAre(
        InventoryItemCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getBranchCode(), copy.getBranchCode()) &&
                condition.apply(criteria.getBranchId(), copy.getBranchId()) &&
                condition.apply(criteria.getInventoryItemCode(), copy.getInventoryItemCode()) &&
                condition.apply(criteria.getItemName(), copy.getItemName()) &&
                condition.apply(criteria.getCategory(), copy.getCategory()) &&
                condition.apply(criteria.getQuantity(), copy.getQuantity()) &&
                condition.apply(criteria.getUnitPrice(), copy.getUnitPrice()) &&
                condition.apply(criteria.getRunningStockCount(), copy.getRunningStockCount()) &&
                condition.apply(criteria.getIsActive(), copy.getIsActive()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
