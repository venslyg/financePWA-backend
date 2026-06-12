package com.gvsolutions.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AssetRegisterCriteriaTest {

    @Test
    void newAssetRegisterCriteriaHasAllFiltersNullTest() {
        var assetRegisterCriteria = new AssetRegisterCriteria();
        assertThat(assetRegisterCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void assetRegisterCriteriaFluentMethodsCreatesFiltersTest() {
        var assetRegisterCriteria = new AssetRegisterCriteria();

        setAllFilters(assetRegisterCriteria);

        assertThat(assetRegisterCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void assetRegisterCriteriaCopyCreatesNullFilterTest() {
        var assetRegisterCriteria = new AssetRegisterCriteria();
        var copy = assetRegisterCriteria.copy();

        assertThat(assetRegisterCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(assetRegisterCriteria)
        );
    }

    @Test
    void assetRegisterCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var assetRegisterCriteria = new AssetRegisterCriteria();
        setAllFilters(assetRegisterCriteria);

        var copy = assetRegisterCriteria.copy();

        assertThat(assetRegisterCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(assetRegisterCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var assetRegisterCriteria = new AssetRegisterCriteria();

        assertThat(assetRegisterCriteria).hasToString("AssetRegisterCriteria{}");
    }

    private static void setAllFilters(AssetRegisterCriteria assetRegisterCriteria) {
        assetRegisterCriteria.id();
        assetRegisterCriteria.branchCode();
        assetRegisterCriteria.branchId();
        assetRegisterCriteria.assetRegisterCode();
        assetRegisterCriteria.assetCategoryCode();
        assetRegisterCriteria.assetSubCategoryCode();
        assetRegisterCriteria.assetName();
        assetRegisterCriteria.category();
        assetRegisterCriteria.purchaseDate();
        assetRegisterCriteria.purchaseCost();
        assetRegisterCriteria.currentValue();
        assetRegisterCriteria.depreciationRate();
        assetRegisterCriteria.accumulatedDepreciation();
        assetRegisterCriteria.createdBy();
        assetRegisterCriteria.createdDate();
        assetRegisterCriteria.lastModifiedBy();
        assetRegisterCriteria.lastModifiedDate();
        assetRegisterCriteria.distinct();
    }

    private static Condition<AssetRegisterCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getBranchCode()) &&
                condition.apply(criteria.getBranchId()) &&
                condition.apply(criteria.getAssetRegisterCode()) &&
                condition.apply(criteria.getAssetCategoryCode()) &&
                condition.apply(criteria.getAssetSubCategoryCode()) &&
                condition.apply(criteria.getAssetName()) &&
                condition.apply(criteria.getCategory()) &&
                condition.apply(criteria.getPurchaseDate()) &&
                condition.apply(criteria.getPurchaseCost()) &&
                condition.apply(criteria.getCurrentValue()) &&
                condition.apply(criteria.getDepreciationRate()) &&
                condition.apply(criteria.getAccumulatedDepreciation()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AssetRegisterCriteria> copyFiltersAre(
        AssetRegisterCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getBranchCode(), copy.getBranchCode()) &&
                condition.apply(criteria.getBranchId(), copy.getBranchId()) &&
                condition.apply(criteria.getAssetRegisterCode(), copy.getAssetRegisterCode()) &&
                condition.apply(criteria.getAssetCategoryCode(), copy.getAssetCategoryCode()) &&
                condition.apply(criteria.getAssetSubCategoryCode(), copy.getAssetSubCategoryCode()) &&
                condition.apply(criteria.getAssetName(), copy.getAssetName()) &&
                condition.apply(criteria.getCategory(), copy.getCategory()) &&
                condition.apply(criteria.getPurchaseDate(), copy.getPurchaseDate()) &&
                condition.apply(criteria.getPurchaseCost(), copy.getPurchaseCost()) &&
                condition.apply(criteria.getCurrentValue(), copy.getCurrentValue()) &&
                condition.apply(criteria.getDepreciationRate(), copy.getDepreciationRate()) &&
                condition.apply(criteria.getAccumulatedDepreciation(), copy.getAccumulatedDepreciation()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
