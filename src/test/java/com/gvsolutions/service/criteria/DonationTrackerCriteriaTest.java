package com.gvsolutions.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DonationTrackerCriteriaTest {

    @Test
    void newDonationTrackerCriteriaHasAllFiltersNullTest() {
        var donationTrackerCriteria = new DonationTrackerCriteria();
        assertThat(donationTrackerCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void donationTrackerCriteriaFluentMethodsCreatesFiltersTest() {
        var donationTrackerCriteria = new DonationTrackerCriteria();

        setAllFilters(donationTrackerCriteria);

        assertThat(donationTrackerCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void donationTrackerCriteriaCopyCreatesNullFilterTest() {
        var donationTrackerCriteria = new DonationTrackerCriteria();
        var copy = donationTrackerCriteria.copy();

        assertThat(donationTrackerCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(donationTrackerCriteria)
        );
    }

    @Test
    void donationTrackerCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var donationTrackerCriteria = new DonationTrackerCriteria();
        setAllFilters(donationTrackerCriteria);

        var copy = donationTrackerCriteria.copy();

        assertThat(donationTrackerCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(donationTrackerCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var donationTrackerCriteria = new DonationTrackerCriteria();

        assertThat(donationTrackerCriteria).hasToString("DonationTrackerCriteria{}");
    }

    private static void setAllFilters(DonationTrackerCriteria donationTrackerCriteria) {
        donationTrackerCriteria.id();
        donationTrackerCriteria.branchCode();
        donationTrackerCriteria.donationIdCode();
        donationTrackerCriteria.date();
        donationTrackerCriteria.donorNameOrOrg();
        donationTrackerCriteria.contactDetails();
        donationTrackerCriteria.amount();
        donationTrackerCriteria.purpose();
        donationTrackerCriteria.receivedViaMode();
        donationTrackerCriteria.notes();
        donationTrackerCriteria.createdBy();
        donationTrackerCriteria.createdDate();
        donationTrackerCriteria.lastModifiedBy();
        donationTrackerCriteria.lastModifiedDate();
        donationTrackerCriteria.distinct();
    }

    private static Condition<DonationTrackerCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getBranchCode()) &&
                condition.apply(criteria.getDonationIdCode()) &&
                condition.apply(criteria.getDate()) &&
                condition.apply(criteria.getDonorNameOrOrg()) &&
                condition.apply(criteria.getContactDetails()) &&
                condition.apply(criteria.getAmount()) &&
                condition.apply(criteria.getPurpose()) &&
                condition.apply(criteria.getReceivedViaMode()) &&
                condition.apply(criteria.getNotes()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DonationTrackerCriteria> copyFiltersAre(
        DonationTrackerCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getBranchCode(), copy.getBranchCode()) &&
                condition.apply(criteria.getDonationIdCode(), copy.getDonationIdCode()) &&
                condition.apply(criteria.getDate(), copy.getDate()) &&
                condition.apply(criteria.getDonorNameOrOrg(), copy.getDonorNameOrOrg()) &&
                condition.apply(criteria.getContactDetails(), copy.getContactDetails()) &&
                condition.apply(criteria.getAmount(), copy.getAmount()) &&
                condition.apply(criteria.getPurpose(), copy.getPurpose()) &&
                condition.apply(criteria.getReceivedViaMode(), copy.getReceivedViaMode()) &&
                condition.apply(criteria.getNotes(), copy.getNotes()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
