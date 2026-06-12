package com.gvsolutions.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AssetDepreciationHistoryCriteriaTest {

    @Test
    void newAssetDepreciationHistoryCriteriaHasAllFiltersNullTest() {
        var assetDepreciationHistoryCriteria = new AssetDepreciationHistoryCriteria();
        assertThat(assetDepreciationHistoryCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void assetDepreciationHistoryCriteriaFluentMethodsCreatesFiltersTest() {
        var assetDepreciationHistoryCriteria = new AssetDepreciationHistoryCriteria();

        setAllFilters(assetDepreciationHistoryCriteria);

        assertThat(assetDepreciationHistoryCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void assetDepreciationHistoryCriteriaCopyCreatesNullFilterTest() {
        var assetDepreciationHistoryCriteria = new AssetDepreciationHistoryCriteria();
        var copy = assetDepreciationHistoryCriteria.copy();

        assertThat(assetDepreciationHistoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(assetDepreciationHistoryCriteria)
        );
    }

    @Test
    void assetDepreciationHistoryCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var assetDepreciationHistoryCriteria = new AssetDepreciationHistoryCriteria();
        setAllFilters(assetDepreciationHistoryCriteria);

        var copy = assetDepreciationHistoryCriteria.copy();

        assertThat(assetDepreciationHistoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(assetDepreciationHistoryCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var assetDepreciationHistoryCriteria = new AssetDepreciationHistoryCriteria();

        assertThat(assetDepreciationHistoryCriteria).hasToString("AssetDepreciationHistoryCriteria{}");
    }

    private static void setAllFilters(AssetDepreciationHistoryCriteria assetDepreciationHistoryCriteria) {
        assetDepreciationHistoryCriteria.id();
        assetDepreciationHistoryCriteria.assetRegisterCode();
        assetDepreciationHistoryCriteria.depreciationDate();
        assetDepreciationHistoryCriteria.depreciationAmount();
        assetDepreciationHistoryCriteria.valueAfterDepreciation();
        assetDepreciationHistoryCriteria.processedBy();
        assetDepreciationHistoryCriteria.createdBy();
        assetDepreciationHistoryCriteria.createdDate();
        assetDepreciationHistoryCriteria.lastModifiedBy();
        assetDepreciationHistoryCriteria.lastModifiedDate();
        assetDepreciationHistoryCriteria.distinct();
    }

    private static Condition<AssetDepreciationHistoryCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getAssetRegisterCode()) &&
                condition.apply(criteria.getDepreciationDate()) &&
                condition.apply(criteria.getDepreciationAmount()) &&
                condition.apply(criteria.getValueAfterDepreciation()) &&
                condition.apply(criteria.getProcessedBy()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AssetDepreciationHistoryCriteria> copyFiltersAre(
        AssetDepreciationHistoryCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getAssetRegisterCode(), copy.getAssetRegisterCode()) &&
                condition.apply(criteria.getDepreciationDate(), copy.getDepreciationDate()) &&
                condition.apply(criteria.getDepreciationAmount(), copy.getDepreciationAmount()) &&
                condition.apply(criteria.getValueAfterDepreciation(), copy.getValueAfterDepreciation()) &&
                condition.apply(criteria.getProcessedBy(), copy.getProcessedBy()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
