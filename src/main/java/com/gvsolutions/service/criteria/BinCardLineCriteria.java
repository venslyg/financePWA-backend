package com.gvsolutions.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.gvsolutions.domain.BinCardLine} entity. This class is used
 * in {@link com.gvsolutions.web.rest.BinCardLineResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /bin-card-lines?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BinCardLineCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter inventoryItemCode;

    private LocalDateFilter date;

    private StringFilter referenceNo;

    private StringFilter description;

    private BigDecimalFilter quantityIn;

    private BigDecimalFilter quantityOut;

    private BigDecimalFilter runningBalance;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private Boolean distinct;

    public BinCardLineCriteria() {}

    public BinCardLineCriteria(BinCardLineCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.inventoryItemCode = other.optionalInventoryItemCode().map(StringFilter::copy).orElse(null);
        this.date = other.optionalDate().map(LocalDateFilter::copy).orElse(null);
        this.referenceNo = other.optionalReferenceNo().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.quantityIn = other.optionalQuantityIn().map(BigDecimalFilter::copy).orElse(null);
        this.quantityOut = other.optionalQuantityOut().map(BigDecimalFilter::copy).orElse(null);
        this.runningBalance = other.optionalRunningBalance().map(BigDecimalFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedBy = other.optionalLastModifiedBy().map(StringFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public BinCardLineCriteria copy() {
        return new BinCardLineCriteria(this);
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

    public StringFilter getInventoryItemCode() {
        return inventoryItemCode;
    }

    public Optional<StringFilter> optionalInventoryItemCode() {
        return Optional.ofNullable(inventoryItemCode);
    }

    public StringFilter inventoryItemCode() {
        if (inventoryItemCode == null) {
            setInventoryItemCode(new StringFilter());
        }
        return inventoryItemCode;
    }

    public void setInventoryItemCode(StringFilter inventoryItemCode) {
        this.inventoryItemCode = inventoryItemCode;
    }

    public LocalDateFilter getDate() {
        return date;
    }

    public Optional<LocalDateFilter> optionalDate() {
        return Optional.ofNullable(date);
    }

    public LocalDateFilter date() {
        if (date == null) {
            setDate(new LocalDateFilter());
        }
        return date;
    }

    public void setDate(LocalDateFilter date) {
        this.date = date;
    }

    public StringFilter getReferenceNo() {
        return referenceNo;
    }

    public Optional<StringFilter> optionalReferenceNo() {
        return Optional.ofNullable(referenceNo);
    }

    public StringFilter referenceNo() {
        if (referenceNo == null) {
            setReferenceNo(new StringFilter());
        }
        return referenceNo;
    }

    public void setReferenceNo(StringFilter referenceNo) {
        this.referenceNo = referenceNo;
    }

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public BigDecimalFilter getQuantityIn() {
        return quantityIn;
    }

    public Optional<BigDecimalFilter> optionalQuantityIn() {
        return Optional.ofNullable(quantityIn);
    }

    public BigDecimalFilter quantityIn() {
        if (quantityIn == null) {
            setQuantityIn(new BigDecimalFilter());
        }
        return quantityIn;
    }

    public void setQuantityIn(BigDecimalFilter quantityIn) {
        this.quantityIn = quantityIn;
    }

    public BigDecimalFilter getQuantityOut() {
        return quantityOut;
    }

    public Optional<BigDecimalFilter> optionalQuantityOut() {
        return Optional.ofNullable(quantityOut);
    }

    public BigDecimalFilter quantityOut() {
        if (quantityOut == null) {
            setQuantityOut(new BigDecimalFilter());
        }
        return quantityOut;
    }

    public void setQuantityOut(BigDecimalFilter quantityOut) {
        this.quantityOut = quantityOut;
    }

    public BigDecimalFilter getRunningBalance() {
        return runningBalance;
    }

    public Optional<BigDecimalFilter> optionalRunningBalance() {
        return Optional.ofNullable(runningBalance);
    }

    public BigDecimalFilter runningBalance() {
        if (runningBalance == null) {
            setRunningBalance(new BigDecimalFilter());
        }
        return runningBalance;
    }

    public void setRunningBalance(BigDecimalFilter runningBalance) {
        this.runningBalance = runningBalance;
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
        final BinCardLineCriteria that = (BinCardLineCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(inventoryItemCode, that.inventoryItemCode) &&
            Objects.equals(date, that.date) &&
            Objects.equals(referenceNo, that.referenceNo) &&
            Objects.equals(description, that.description) &&
            Objects.equals(quantityIn, that.quantityIn) &&
            Objects.equals(quantityOut, that.quantityOut) &&
            Objects.equals(runningBalance, that.runningBalance) &&
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
            inventoryItemCode,
            date,
            referenceNo,
            description,
            quantityIn,
            quantityOut,
            runningBalance,
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
        return "BinCardLineCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalInventoryItemCode().map(f -> "inventoryItemCode=" + f + ", ").orElse("") +
            optionalDate().map(f -> "date=" + f + ", ").orElse("") +
            optionalReferenceNo().map(f -> "referenceNo=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalQuantityIn().map(f -> "quantityIn=" + f + ", ").orElse("") +
            optionalQuantityOut().map(f -> "quantityOut=" + f + ", ").orElse("") +
            optionalRunningBalance().map(f -> "runningBalance=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalLastModifiedBy().map(f -> "lastModifiedBy=" + f + ", ").orElse("") +
            optionalLastModifiedDate().map(f -> "lastModifiedDate=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
