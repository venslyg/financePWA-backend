package com.gvsolutions.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.gvsolutions.domain.AssetSubCategory} entity. This class is used
 * in {@link com.gvsolutions.web.rest.AssetSubCategoryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /asset-sub-categories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AssetSubCategoryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter assetCategoryCode;

    private StringFilter assetSubCategoryCode;

    private StringFilter assetSubCategoryName;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private LongFilter categoryId;

    private Boolean distinct;

    public AssetSubCategoryCriteria() {}

    public AssetSubCategoryCriteria(AssetSubCategoryCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.assetCategoryCode = other.optionalAssetCategoryCode().map(StringFilter::copy).orElse(null);
        this.assetSubCategoryCode = other.optionalAssetSubCategoryCode().map(StringFilter::copy).orElse(null);
        this.assetSubCategoryName = other.optionalAssetSubCategoryName().map(StringFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedBy = other.optionalLastModifiedBy().map(StringFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.categoryId = other.optionalCategoryId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AssetSubCategoryCriteria copy() {
        return new AssetSubCategoryCriteria(this);
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

    public StringFilter getAssetSubCategoryName() {
        return assetSubCategoryName;
    }

    public Optional<StringFilter> optionalAssetSubCategoryName() {
        return Optional.ofNullable(assetSubCategoryName);
    }

    public StringFilter assetSubCategoryName() {
        if (assetSubCategoryName == null) {
            setAssetSubCategoryName(new StringFilter());
        }
        return assetSubCategoryName;
    }

    public void setAssetSubCategoryName(StringFilter assetSubCategoryName) {
        this.assetSubCategoryName = assetSubCategoryName;
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

    public LongFilter getCategoryId() {
        return categoryId;
    }

    public Optional<LongFilter> optionalCategoryId() {
        return Optional.ofNullable(categoryId);
    }

    public LongFilter categoryId() {
        if (categoryId == null) {
            setCategoryId(new LongFilter());
        }
        return categoryId;
    }

    public void setCategoryId(LongFilter categoryId) {
        this.categoryId = categoryId;
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
        final AssetSubCategoryCriteria that = (AssetSubCategoryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(assetCategoryCode, that.assetCategoryCode) &&
            Objects.equals(assetSubCategoryCode, that.assetSubCategoryCode) &&
            Objects.equals(assetSubCategoryName, that.assetSubCategoryName) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(categoryId, that.categoryId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            assetCategoryCode,
            assetSubCategoryCode,
            assetSubCategoryName,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            categoryId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetSubCategoryCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalAssetCategoryCode().map(f -> "assetCategoryCode=" + f + ", ").orElse("") +
            optionalAssetSubCategoryCode().map(f -> "assetSubCategoryCode=" + f + ", ").orElse("") +
            optionalAssetSubCategoryName().map(f -> "assetSubCategoryName=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalLastModifiedBy().map(f -> "lastModifiedBy=" + f + ", ").orElse("") +
            optionalLastModifiedDate().map(f -> "lastModifiedDate=" + f + ", ").orElse("") +
            optionalCategoryId().map(f -> "categoryId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
