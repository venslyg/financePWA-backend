package com.gvsolutions.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AssetSubCategoryCriteriaTest {

    @Test
    void newAssetSubCategoryCriteriaHasAllFiltersNullTest() {
        var assetSubCategoryCriteria = new AssetSubCategoryCriteria();
        assertThat(assetSubCategoryCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void assetSubCategoryCriteriaFluentMethodsCreatesFiltersTest() {
        var assetSubCategoryCriteria = new AssetSubCategoryCriteria();

        setAllFilters(assetSubCategoryCriteria);

        assertThat(assetSubCategoryCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void assetSubCategoryCriteriaCopyCreatesNullFilterTest() {
        var assetSubCategoryCriteria = new AssetSubCategoryCriteria();
        var copy = assetSubCategoryCriteria.copy();

        assertThat(assetSubCategoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(assetSubCategoryCriteria)
        );
    }

    @Test
    void assetSubCategoryCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var assetSubCategoryCriteria = new AssetSubCategoryCriteria();
        setAllFilters(assetSubCategoryCriteria);

        var copy = assetSubCategoryCriteria.copy();

        assertThat(assetSubCategoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(assetSubCategoryCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var assetSubCategoryCriteria = new AssetSubCategoryCriteria();

        assertThat(assetSubCategoryCriteria).hasToString("AssetSubCategoryCriteria{}");
    }

    private static void setAllFilters(AssetSubCategoryCriteria assetSubCategoryCriteria) {
        assetSubCategoryCriteria.id();
        assetSubCategoryCriteria.branchCode();
        assetSubCategoryCriteria.branchId();
        assetSubCategoryCriteria.assetCategoryCode();
        assetSubCategoryCriteria.assetSubCategoryCode();
        assetSubCategoryCriteria.assetSubCategoryName();
        assetSubCategoryCriteria.createdBy();
        assetSubCategoryCriteria.createdDate();
        assetSubCategoryCriteria.lastModifiedBy();
        assetSubCategoryCriteria.lastModifiedDate();
        assetSubCategoryCriteria.categoryId();
        assetSubCategoryCriteria.distinct();
    }

    private static Condition<AssetSubCategoryCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getBranchCode()) &&
                condition.apply(criteria.getBranchId()) &&
                condition.apply(criteria.getAssetCategoryCode()) &&
                condition.apply(criteria.getAssetSubCategoryCode()) &&
                condition.apply(criteria.getAssetSubCategoryName()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getCategoryId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AssetSubCategoryCriteria> copyFiltersAre(
        AssetSubCategoryCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getBranchCode(), copy.getBranchCode()) &&
                condition.apply(criteria.getBranchId(), copy.getBranchId()) &&
                condition.apply(criteria.getAssetCategoryCode(), copy.getAssetCategoryCode()) &&
                condition.apply(criteria.getAssetSubCategoryCode(), copy.getAssetSubCategoryCode()) &&
                condition.apply(criteria.getAssetSubCategoryName(), copy.getAssetSubCategoryName()) &&
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
