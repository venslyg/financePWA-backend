package com.gvsolutions.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.gvsolutions.domain.AssetRegister} entity. This class is used
 * in {@link com.gvsolutions.web.rest.AssetRegisterResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /asset-registers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AssetRegisterCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter branchCode;

    private StringFilter branchId;

    private StringFilter assetRegisterCode;

    private StringFilter assetCategoryCode;

    private StringFilter assetSubCategoryCode;

    private StringFilter assetName;

    private StringFilter category;

    private LocalDateFilter purchaseDate;

    private BigDecimalFilter purchaseCost;

    private BigDecimalFilter currentValue;

    private BigDecimalFilter depreciationRate;

    private BigDecimalFilter accumulatedDepreciation;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private Boolean distinct;

    public AssetRegisterCriteria() {}

    public AssetRegisterCriteria(AssetRegisterCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.branchCode = other.optionalBranchCode().map(StringFilter::copy).orElse(null);
        this.branchId = other.optionalBranchId().map(StringFilter::copy).orElse(null);
        this.assetRegisterCode = other.optionalAssetRegisterCode().map(StringFilter::copy).orElse(null);
        this.assetCategoryCode = other.optionalAssetCategoryCode().map(StringFilter::copy).orElse(null);
        this.assetSubCategoryCode = other.optionalAssetSubCategoryCode().map(StringFilter::copy).orElse(null);
        this.assetName = other.optionalAssetName().map(StringFilter::copy).orElse(null);
        this.category = other.optionalCategory().map(StringFilter::copy).orElse(null);
        this.purchaseDate = other.optionalPurchaseDate().map(LocalDateFilter::copy).orElse(null);
        this.purchaseCost = other.optionalPurchaseCost().map(BigDecimalFilter::copy).orElse(null);
        this.currentValue = other.optionalCurrentValue().map(BigDecimalFilter::copy).orElse(null);
        this.depreciationRate = other.optionalDepreciationRate().map(BigDecimalFilter::copy).orElse(null);
        this.accumulatedDepreciation = other.optionalAccumulatedDepreciation().map(BigDecimalFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedBy = other.optionalLastModifiedBy().map(StringFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AssetRegisterCriteria copy() {
        return new AssetRegisterCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getBranchCode() {
        return branchCode;
    }

    public Optional<StringFilter> optionalBranchCode() {
        return Optional.ofNullable(branchCode);
    }

    public StringFilter branchCode() {
        if (branchCode == null) {
            setBranchCode(new StringFilter());
        }
        return branchCode;
    }

    public void setBranchCode(StringFilter branchCode) {
        this.branchCode = branchCode;
    }

    public StringFilter getBranchId() {
        return branchId;
    }

    public Optional<StringFilter> optionalBranchId() {
        return Optional.ofNullable(branchId);
    }

    public StringFilter branchId() {
        if (branchId == null) {
            setBranchId(new StringFilter());
        }
        return branchId;
    }

    public void setBranchId(StringFilter branchId) {
        this.branchId = branchId;
    }

    public StringFilter getAssetRegisterCode() {
        return assetRegisterCode;
    }

    public Optional<StringFilter> optionalAssetRegisterCode() {
        return Optional.ofNullable(assetRegisterCode);
    }

    public StringFilter assetRegisterCode() {
        if (assetRegisterCode == null) {
            setAssetRegisterCode(new StringFilter());
        }
        return assetRegisterCode;
    }

    public void setAssetRegisterCode(StringFilter assetRegisterCode) {
        this.assetRegisterCode = assetRegisterCode;
    }

    public StringFilter getAssetCategoryCode() {
        return assetCategoryCode;
    }

    public Optional<StringFilter> optionalAssetCategoryCode() {
        return Optional.ofNullable(assetCategoryCode);
    }

    public StringFilter assetCategoryCode() {
        if (assetCategoryCode == null) {
            setAssetCategoryCode(new StringFilter());
        }
        return assetCategoryCode;
    }

    public void setAssetCategoryCode(StringFilter assetCategoryCode) {
        this.assetCategoryCode = assetCategoryCode;
    }

    public StringFilter getAssetSubCategoryCode() {
        return assetSubCategoryCode;
    }

    public Optional<StringFilter> optionalAssetSubCategoryCode() {
        return Optional.ofNullable(assetSubCategoryCode);
    }

    public StringFilter assetSubCategoryCode() {
        if (assetSubCategoryCode == null) {
            setAssetSubCategoryCode(new StringFilter());
        }
        return assetSubCategoryCode;
    }

    public void setAssetSubCategoryCode(StringFilter assetSubCategoryCode) {
        this.assetSubCategoryCode = assetSubCategoryCode;
    }

    public StringFilter getAssetName() {
        return assetName;
    }

    public Optional<StringFilter> optionalAssetName() {
        return Optional.ofNullable(assetName);
    }

    public StringFilter assetName() {
        if (assetName == null) {
            setAssetName(new StringFilter());
        }
        return assetName;
    }

    public void setAssetName(StringFilter assetName) {
        this.assetName = assetName;
    }

    public StringFilter getCategory() {
        return category;
    }

    public Optional<StringFilter> optionalCategory() {
        return Optional.ofNullable(category);
    }

    public StringFilter category() {
        if (category == null) {
            setCategory(new StringFilter());
        }
        return category;
    }

    public void setCategory(StringFilter category) {
        this.category = category;
    }

    public LocalDateFilter getPurchaseDate() {
        return purchaseDate;
    }

    public Optional<LocalDateFilter> optionalPurchaseDate() {
        return Optional.ofNullable(purchaseDate);
    }

    public LocalDateFilter purchaseDate() {
        if (purchaseDate == null) {
            setPurchaseDate(new LocalDateFilter());
        }
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateFilter purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public BigDecimalFilter getPurchaseCost() {
        return purchaseCost;
    }

    public Optional<BigDecimalFilter> optionalPurchaseCost() {
        return Optional.ofNullable(purchaseCost);
    }

    public BigDecimalFilter purchaseCost() {
        if (purchaseCost == null) {
            setPurchaseCost(new BigDecimalFilter());
        }
        return purchaseCost;
    }

    public void setPurchaseCost(BigDecimalFilter purchaseCost) {
        this.purchaseCost = purchaseCost;
    }

    public BigDecimalFilter getCurrentValue() {
        return currentValue;
    }

    public Optional<BigDecimalFilter> optionalCurrentValue() {
        return Optional.ofNullable(currentValue);
    }

    public BigDecimalFilter currentValue() {
        if (currentValue == null) {
            setCurrentValue(new BigDecimalFilter());
        }
        return currentValue;
    }

    public void setCurrentValue(BigDecimalFilter currentValue) {
        this.currentValue = currentValue;
    }

    public BigDecimalFilter getDepreciationRate() {
        return depreciationRate;
    }

    public Optional<BigDecimalFilter> optionalDepreciationRate() {
        return Optional.ofNullable(depreciationRate);
    }

    public BigDecimalFilter depreciationRate() {
        if (depreciationRate == null) {
            setDepreciationRate(new BigDecimalFilter());
        }
        return depreciationRate;
    }

    public void setDepreciationRate(BigDecimalFilter depreciationRate) {
        this.depreciationRate = depreciationRate;
    }

    public BigDecimalFilter getAccumulatedDepreciation() {
        return accumulatedDepreciation;
    }

    public Optional<BigDecimalFilter> optionalAccumulatedDepreciation() {
        return Optional.ofNullable(accumulatedDepreciation);
    }

    public BigDecimalFilter accumulatedDepreciation() {
        if (accumulatedDepreciation == null) {
            setAccumulatedDepreciation(new BigDecimalFilter());
        }
        return accumulatedDepreciation;
    }

    public void setAccumulatedDepreciation(BigDecimalFilter accumulatedDepreciation) {
        this.accumulatedDepreciation = accumulatedDepreciation;
    }

    public StringFilter getCreatedBy() {
        return createdBy;
    }

    public Optional<StringFilter> optionalCreatedBy() {
        return Optional.ofNullable(createdBy);
    }

    public StringFilter createdBy() {
        if (createdBy == null) {
            setCreatedBy(new StringFilter());
        }
        return createdBy;
    }

    public void setCreatedBy(StringFilter createdBy) {
        this.createdBy = createdBy;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public Optional<InstantFilter> optionalCreatedDate() {
        return Optional.ofNullable(createdDate);
    }

    public InstantFilter createdDate() {
        if (createdDate == null) {
            setCreatedDate(new InstantFilter());
        }
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public StringFilter getLastModifiedBy() {
        return lastModifiedBy;
    }

    public Optional<StringFilter> optionalLastModifiedBy() {
        return Optional.ofNullable(lastModifiedBy);
    }

    public StringFilter lastModifiedBy() {
        if (lastModifiedBy == null) {
            setLastModifiedBy(new StringFilter());
        }
        return lastModifiedBy;
    }

    public void setLastModifiedBy(StringFilter lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public InstantFilter getLastModifiedDate() {
        return lastModifiedDate;
    }

    public Optional<InstantFilter> optionalLastModifiedDate() {
        return Optional.ofNullable(lastModifiedDate);
    }

    public InstantFilter lastModifiedDate() {
        if (lastModifiedDate == null) {
            setLastModifiedDate(new InstantFilter());
        }
        return lastModifiedDate;
    }

    public void setLastModifiedDate(InstantFilter lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AssetRegisterCriteria that = (AssetRegisterCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(branchCode, that.branchCode) &&
            Objects.equals(branchId, that.branchId) &&
            Objects.equals(assetRegisterCode, that.assetRegisterCode) &&
            Objects.equals(assetCategoryCode, that.assetCategoryCode) &&
            Objects.equals(assetSubCategoryCode, that.assetSubCategoryCode) &&
            Objects.equals(assetName, that.assetName) &&
            Objects.equals(category, that.category) &&
            Objects.equals(purchaseDate, that.purchaseDate) &&
            Objects.equals(purchaseCost, that.purchaseCost) &&
            Objects.equals(currentValue, that.currentValue) &&
            Objects.equals(depreciationRate, that.depreciationRate) &&
            Objects.equals(accumulatedDepreciation, that.accumulatedDepreciation) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            branchCode,
            branchId,
            assetRegisterCode,
            assetCategoryCode,
            assetSubCategoryCode,
            assetName,
            category,
            purchaseDate,
            purchaseCost,
            currentValue,
            depreciationRate,
            accumulatedDepreciation,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetRegisterCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalBranchCode().map(f -> "branchCode=" + f + ", ").orElse("") +
            optionalBranchId().map(f -> "branchId=" + f + ", ").orElse("") +
            optionalAssetRegisterCode().map(f -> "assetRegisterCode=" + f + ", ").orElse("") +
            optionalAssetCategoryCode().map(f -> "assetCategoryCode=" + f + ", ").orElse("") +
            optionalAssetSubCategoryCode().map(f -> "assetSubCategoryCode=" + f + ", ").orElse("") +
            optionalAssetName().map(f -> "assetName=" + f + ", ").orElse("") +
            optionalCategory().map(f -> "category=" + f + ", ").orElse("") +
            optionalPurchaseDate().map(f -> "purchaseDate=" + f + ", ").orElse("") +
            optionalPurchaseCost().map(f -> "purchaseCost=" + f + ", ").orElse("") +
            optionalCurrentValue().map(f -> "currentValue=" + f + ", ").orElse("") +
            optionalDepreciationRate().map(f -> "depreciationRate=" + f + ", ").orElse("") +
            optionalAccumulatedDepreciation().map(f -> "accumulatedDepreciation=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalLastModifiedBy().map(f -> "lastModifiedBy=" + f + ", ").orElse("") +
            optionalLastModifiedDate().map(f -> "lastModifiedDate=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
