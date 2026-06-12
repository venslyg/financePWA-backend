package com.gvsolutions.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.gvsolutions.domain.ExpenseSubCategory} entity. This class is used
 * in {@link com.gvsolutions.web.rest.ExpenseSubCategoryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /expense-sub-categories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExpenseSubCategoryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter branchCode;

    private StringFilter branchId;

    private StringFilter categoryCode;

    private StringFilter subCategoryCode;

    private StringFilter subCategoryName;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private LongFilter categoryId;

    private Boolean distinct;

    public ExpenseSubCategoryCriteria() {}

    public ExpenseSubCategoryCriteria(ExpenseSubCategoryCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.branchCode = other.optionalBranchCode().map(StringFilter::copy).orElse(null);
        this.branchId = other.optionalBranchId().map(StringFilter::copy).orElse(null);
        this.categoryCode = other.optionalCategoryCode().map(StringFilter::copy).orElse(null);
        this.subCategoryCode = other.optionalSubCategoryCode().map(StringFilter::copy).orElse(null);
        this.subCategoryName = other.optionalSubCategoryName().map(StringFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedBy = other.optionalLastModifiedBy().map(StringFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.categoryId = other.optionalCategoryId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ExpenseSubCategoryCriteria copy() {
        return new ExpenseSubCategoryCriteria(this);
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

    public StringFilter getCategoryCode() {
        return categoryCode;
    }

    public Optional<StringFilter> optionalCategoryCode() {
        return Optional.ofNullable(categoryCode);
    }

    public StringFilter categoryCode() {
        if (categoryCode == null) {
            setCategoryCode(new StringFilter());
        }
        return categoryCode;
    }

    public void setCategoryCode(StringFilter categoryCode) {
        this.categoryCode = categoryCode;
    }

    public StringFilter getSubCategoryCode() {
        return subCategoryCode;
    }

    public Optional<StringFilter> optionalSubCategoryCode() {
        return Optional.ofNullable(subCategoryCode);
    }

    public StringFilter subCategoryCode() {
        if (subCategoryCode == null) {
            setSubCategoryCode(new StringFilter());
        }
        return subCategoryCode;
    }

    public void setSubCategoryCode(StringFilter subCategoryCode) {
        this.subCategoryCode = subCategoryCode;
    }

    public StringFilter getSubCategoryName() {
        return subCategoryName;
    }

    public Optional<StringFilter> optionalSubCategoryName() {
        return Optional.ofNullable(subCategoryName);
    }

    public StringFilter subCategoryName() {
        if (subCategoryName == null) {
            setSubCategoryName(new StringFilter());
        }
        return subCategoryName;
    }

    public void setSubCategoryName(StringFilter subCategoryName) {
        this.subCategoryName = subCategoryName;
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
        final ExpenseSubCategoryCriteria that = (ExpenseSubCategoryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(branchCode, that.branchCode) &&
            Objects.equals(branchId, that.branchId) &&
            Objects.equals(categoryCode, that.categoryCode) &&
            Objects.equals(subCategoryCode, that.subCategoryCode) &&
            Objects.equals(subCategoryName, that.subCategoryName) &&
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
            branchCode,
            branchId,
            categoryCode,
            subCategoryCode,
            subCategoryName,
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
        return "ExpenseSubCategoryCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalBranchCode().map(f -> "branchCode=" + f + ", ").orElse("") +
            optionalBranchId().map(f -> "branchId=" + f + ", ").orElse("") +
            optionalCategoryCode().map(f -> "categoryCode=" + f + ", ").orElse("") +
            optionalSubCategoryCode().map(f -> "subCategoryCode=" + f + ", ").orElse("") +
            optionalSubCategoryName().map(f -> "subCategoryName=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalLastModifiedBy().map(f -> "lastModifiedBy=" + f + ", ").orElse("") +
            optionalLastModifiedDate().map(f -> "lastModifiedDate=" + f + ", ").orElse("") +
            optionalCategoryId().map(f -> "categoryId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
