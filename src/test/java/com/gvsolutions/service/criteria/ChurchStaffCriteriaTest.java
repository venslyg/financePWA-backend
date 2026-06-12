package com.gvsolutions.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ChurchStaffCriteriaTest {

    @Test
    void newChurchStaffCriteriaHasAllFiltersNullTest() {
        var churchStaffCriteria = new ChurchStaffCriteria();
        assertThat(churchStaffCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void churchStaffCriteriaFluentMethodsCreatesFiltersTest() {
        var churchStaffCriteria = new ChurchStaffCriteria();

        setAllFilters(churchStaffCriteria);

        assertThat(churchStaffCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void churchStaffCriteriaCopyCreatesNullFilterTest() {
        var churchStaffCriteria = new ChurchStaffCriteria();
        var copy = churchStaffCriteria.copy();

        assertThat(churchStaffCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(churchStaffCriteria)
        );
    }

    @Test
    void churchStaffCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var churchStaffCriteria = new ChurchStaffCriteria();
        setAllFilters(churchStaffCriteria);

        var copy = churchStaffCriteria.copy();

        assertThat(churchStaffCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(churchStaffCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var churchStaffCriteria = new ChurchStaffCriteria();

        assertThat(churchStaffCriteria).hasToString("ChurchStaffCriteria{}");
    }

    private static void setAllFilters(ChurchStaffCriteria churchStaffCriteria) {
        churchStaffCriteria.id();
        churchStaffCriteria.staffCode();
        churchStaffCriteria.branchCode();
        churchStaffCriteria.fullName();
        churchStaffCriteria.position();
        churchStaffCriteria.staffType();
        churchStaffCriteria.contactNumber();
        churchStaffCriteria.hourlyRateOrMonthlySalary();
        churchStaffCriteria.isActive();
        churchStaffCriteria.createdBy();
        churchStaffCriteria.createdDate();
        churchStaffCriteria.lastModifiedBy();
        churchStaffCriteria.lastModifiedDate();
        churchStaffCriteria.distinct();
    }

    private static Condition<ChurchStaffCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getStaffCode()) &&
                condition.apply(criteria.getBranchCode()) &&
                condition.apply(criteria.getFullName()) &&
                condition.apply(criteria.getPosition()) &&
                condition.apply(criteria.getStaffType()) &&
                condition.apply(criteria.getContactNumber()) &&
                condition.apply(criteria.getHourlyRateOrMonthlySalary()) &&
                condition.apply(criteria.getIsActive()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ChurchStaffCriteria> copyFiltersAre(ChurchStaffCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getStaffCode(), copy.getStaffCode()) &&
                condition.apply(criteria.getBranchCode(), copy.getBranchCode()) &&
                condition.apply(criteria.getFullName(), copy.getFullName()) &&
                condition.apply(criteria.getPosition(), copy.getPosition()) &&
                condition.apply(criteria.getStaffType(), copy.getStaffType()) &&
                condition.apply(criteria.getContactNumber(), copy.getContactNumber()) &&
                condition.apply(criteria.getHourlyRateOrMonthlySalary(), copy.getHourlyRateOrMonthlySalary()) &&
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
