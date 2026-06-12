package com.gvsolutions.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AccountSetCriteriaTest {

    @Test
    void newAccountSetCriteriaHasAllFiltersNullTest() {
        var accountSetCriteria = new AccountSetCriteria();
        assertThat(accountSetCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void accountSetCriteriaFluentMethodsCreatesFiltersTest() {
        var accountSetCriteria = new AccountSetCriteria();

        setAllFilters(accountSetCriteria);

        assertThat(accountSetCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void accountSetCriteriaCopyCreatesNullFilterTest() {
        var accountSetCriteria = new AccountSetCriteria();
        var copy = accountSetCriteria.copy();

        assertThat(accountSetCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(accountSetCriteria)
        );
    }

    @Test
    void accountSetCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var accountSetCriteria = new AccountSetCriteria();
        setAllFilters(accountSetCriteria);

        var copy = accountSetCriteria.copy();

        assertThat(accountSetCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(accountSetCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var accountSetCriteria = new AccountSetCriteria();

        assertThat(accountSetCriteria).hasToString("AccountSetCriteria{}");
    }

    private static void setAllFilters(AccountSetCriteria accountSetCriteria) {
        accountSetCriteria.id();
        accountSetCriteria.branchCode();
        accountSetCriteria.accountCode();
        accountSetCriteria.accountName();
        accountSetCriteria.accountType();
        accountSetCriteria.subCategory();
        accountSetCriteria.remark();
        accountSetCriteria.createdBy();
        accountSetCriteria.createdDate();
        accountSetCriteria.lastModifiedBy();
        accountSetCriteria.lastModifiedDate();
        accountSetCriteria.distinct();
    }

    private static Condition<AccountSetCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getBranchCode()) &&
                condition.apply(criteria.getAccountCode()) &&
                condition.apply(criteria.getAccountName()) &&
                condition.apply(criteria.getAccountType()) &&
                condition.apply(criteria.getSubCategory()) &&
                condition.apply(criteria.getRemark()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AccountSetCriteria> copyFiltersAre(AccountSetCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getBranchCode(), copy.getBranchCode()) &&
                condition.apply(criteria.getAccountCode(), copy.getAccountCode()) &&
                condition.apply(criteria.getAccountName(), copy.getAccountName()) &&
                condition.apply(criteria.getAccountType(), copy.getAccountType()) &&
                condition.apply(criteria.getSubCategory(), copy.getSubCategory()) &&
                condition.apply(criteria.getRemark(), copy.getRemark()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
