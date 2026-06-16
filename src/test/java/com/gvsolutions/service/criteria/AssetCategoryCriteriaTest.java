package com.gvsolutions.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AssetCategoryCriteriaTest {

    @Test
    void newAssetCategoryCriteriaHasAllFiltersNullTest() {
        var assetCategoryCriteria = new AssetCategoryCriteria();
        assertThat(assetCategoryCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void assetCategoryCriteriaFluentMethodsCreatesFiltersTest() {
        var assetCategoryCriteria = new AssetCategoryCriteria();

        setAllFilters(assetCategoryCriteria);

        assertThat(assetCategoryCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void assetCategoryCriteriaCopyCreatesNullFilterTest() {
        var assetCategoryCriteria = new AssetCategoryCriteria();
        var copy = assetCategoryCriteria.copy();

        assertThat(assetCategoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(assetCategoryCriteria)
        );
    }

    @Test
    void assetCategoryCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var assetCategoryCriteria = new AssetCategoryCriteria();
        setAllFilters(assetCategoryCriteria);

        var copy = assetCategoryCriteria.copy();

        assertThat(assetCategoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(assetCategoryCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var assetCategoryCriteria = new AssetCategoryCriteria();

        assertThat(assetCategoryCriteria).hasToString("AssetCategoryCriteria{}");
    }

    private static void setAllFilters(AssetCategoryCriteria assetCategoryCriteria) {
        assetCategoryCriteria.id();
        assetCategoryCriteria.branchCode();
        assetCategoryCriteria.branchId();
        assetCategoryCriteria.assetCategoryCode();
        assetCategoryCriteria.assetCategoryName();
        assetCategoryCriteria.description();
        assetCategoryCriteria.isActive();
        assetCategoryCriteria.createdBy();
        assetCategoryCriteria.createdDate();
        assetCategoryCriteria.lastModifiedBy();
        assetCategoryCriteria.lastModifiedDate();
        assetCategoryCriteria.distinct();
    }

    private static Condition<AssetCategoryCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getBranchCode()) &&
                condition.apply(criteria.getBranchId()) &&
                condition.apply(criteria.getAssetCategoryCode()) &&
                condition.apply(criteria.getAssetCategoryName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getIsActive()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AssetCategoryCriteria> copyFiltersAre(
        AssetCategoryCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getBranchCode(), copy.getBranchCode()) &&
                condition.apply(criteria.getBranchId(), copy.getBranchId()) &&
                condition.apply(criteria.getAssetCategoryCode(), copy.getAssetCategoryCode()) &&
                condition.apply(criteria.getAssetCategoryName(), copy.getAssetCategoryName()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
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
