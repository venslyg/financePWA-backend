package com.gvsolutions.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MaintenanceLogCriteriaTest {

    @Test
    void newMaintenanceLogCriteriaHasAllFiltersNullTest() {
        var maintenanceLogCriteria = new MaintenanceLogCriteria();
        assertThat(maintenanceLogCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void maintenanceLogCriteriaFluentMethodsCreatesFiltersTest() {
        var maintenanceLogCriteria = new MaintenanceLogCriteria();

        setAllFilters(maintenanceLogCriteria);

        assertThat(maintenanceLogCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void maintenanceLogCriteriaCopyCreatesNullFilterTest() {
        var maintenanceLogCriteria = new MaintenanceLogCriteria();
        var copy = maintenanceLogCriteria.copy();

        assertThat(maintenanceLogCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(maintenanceLogCriteria)
        );
    }

    @Test
    void maintenanceLogCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var maintenanceLogCriteria = new MaintenanceLogCriteria();
        setAllFilters(maintenanceLogCriteria);

        var copy = maintenanceLogCriteria.copy();

        assertThat(maintenanceLogCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(maintenanceLogCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var maintenanceLogCriteria = new MaintenanceLogCriteria();

        assertThat(maintenanceLogCriteria).hasToString("MaintenanceLogCriteria{}");
    }

    private static void setAllFilters(MaintenanceLogCriteria maintenanceLogCriteria) {
        maintenanceLogCriteria.id();
        maintenanceLogCriteria.branchCode();
        maintenanceLogCriteria.branchId();
        maintenanceLogCriteria.maintenanceLogCode();
        maintenanceLogCriteria.logDate();
        maintenanceLogCriteria.logType();
        maintenanceLogCriteria.description();
        maintenanceLogCriteria.cost();
        maintenanceLogCriteria.vendor();
        maintenanceLogCriteria.nextServiceDate();
        maintenanceLogCriteria.note();
        maintenanceLogCriteria.createdBy();
        maintenanceLogCriteria.createdDate();
        maintenanceLogCriteria.lastModifiedBy();
        maintenanceLogCriteria.lastModifiedDate();
        maintenanceLogCriteria.assetId();
        maintenanceLogCriteria.distinct();
    }

    private static Condition<MaintenanceLogCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getBranchCode()) &&
                condition.apply(criteria.getBranchId()) &&
                condition.apply(criteria.getMaintenanceLogCode()) &&
                condition.apply(criteria.getLogDate()) &&
                condition.apply(criteria.getLogType()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getCost()) &&
                condition.apply(criteria.getVendor()) &&
                condition.apply(criteria.getNextServiceDate()) &&
                condition.apply(criteria.getNote()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getAssetId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<MaintenanceLogCriteria> copyFiltersAre(
        MaintenanceLogCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getBranchCode(), copy.getBranchCode()) &&
                condition.apply(criteria.getBranchId(), copy.getBranchId()) &&
                condition.apply(criteria.getMaintenanceLogCode(), copy.getMaintenanceLogCode()) &&
                condition.apply(criteria.getLogDate(), copy.getLogDate()) &&
                condition.apply(criteria.getLogType(), copy.getLogType()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getCost(), copy.getCost()) &&
                condition.apply(criteria.getVendor(), copy.getVendor()) &&
                condition.apply(criteria.getNextServiceDate(), copy.getNextServiceDate()) &&
                condition.apply(criteria.getNote(), copy.getNote()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getAssetId(), copy.getAssetId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
