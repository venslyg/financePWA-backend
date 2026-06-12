package com.gvsolutions.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class BranchCriteriaTest {

    @Test
    void newBranchCriteriaHasAllFiltersNullTest() {
        var branchCriteria = new BranchCriteria();
        assertThat(branchCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void branchCriteriaFluentMethodsCreatesFiltersTest() {
        var branchCriteria = new BranchCriteria();

        setAllFilters(branchCriteria);

        assertThat(branchCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void branchCriteriaCopyCreatesNullFilterTest() {
        var branchCriteria = new BranchCriteria();
        var copy = branchCriteria.copy();

        assertThat(branchCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(branchCriteria)
        );
    }

    @Test
    void branchCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var branchCriteria = new BranchCriteria();
        setAllFilters(branchCriteria);

        var copy = branchCriteria.copy();

        assertThat(branchCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(branchCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var branchCriteria = new BranchCriteria();

        assertThat(branchCriteria).hasToString("BranchCriteria{}");
    }

    private static void setAllFilters(BranchCriteria branchCriteria) {
        branchCriteria.id();
        branchCriteria.branchCode();
        branchCriteria.branchName();
        branchCriteria.location();
        branchCriteria.phoneNumber();
        branchCriteria.isActive();
        branchCriteria.createdBy();
        branchCriteria.createdDate();
        branchCriteria.lastModifiedBy();
        branchCriteria.lastModifiedDate();
        branchCriteria.distinct();
    }

    private static Condition<BranchCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getBranchCode()) &&
                condition.apply(criteria.getBranchName()) &&
                condition.apply(criteria.getLocation()) &&
                condition.apply(criteria.getPhoneNumber()) &&
                condition.apply(criteria.getIsActive()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<BranchCriteria> copyFiltersAre(BranchCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getBranchCode(), copy.getBranchCode()) &&
                condition.apply(criteria.getBranchName(), copy.getBranchName()) &&
                condition.apply(criteria.getLocation(), copy.getLocation()) &&
                condition.apply(criteria.getPhoneNumber(), copy.getPhoneNumber()) &&
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
