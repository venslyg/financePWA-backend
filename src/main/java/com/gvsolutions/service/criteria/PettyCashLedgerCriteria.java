package com.gvsolutions.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.gvsolutions.domain.PettyCashLedger} entity. This class is used
 * in {@link com.gvsolutions.web.rest.PettyCashLedgerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /petty-cash-ledgers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PettyCashLedgerCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter branchCode;

    private StringFilter pettyCashCode;

    private LocalDateFilter date;

    private StringFilter pettyCashVoucherNo;

    private StringFilter description;

    private BigDecimalFilter cashIn;

    private BigDecimalFilter cashOut;

    private BigDecimalFilter runningBalance;

    private StringFilter linkedAccountCode;

    private StringFilter referenceNo;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private Boolean distinct;

    public PettyCashLedgerCriteria() {}

    public PettyCashLedgerCriteria(PettyCashLedgerCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.branchCode = other.optionalBranchCode().map(StringFilter::copy).orElse(null);
        this.pettyCashCode = other.optionalPettyCashCode().map(StringFilter::copy).orElse(null);
        this.date = other.optionalDate().map(LocalDateFilter::copy).orElse(null);
        this.pettyCashVoucherNo = other.optionalPettyCashVoucherNo().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.cashIn = other.optionalCashIn().map(BigDecimalFilter::copy).orElse(null);
        this.cashOut = other.optionalCashOut().map(BigDecimalFilter::copy).orElse(null);
        this.runningBalance = other.optionalRunningBalance().map(BigDecimalFilter::copy).orElse(null);
        this.linkedAccountCode = other.optionalLinkedAccountCode().map(StringFilter::copy).orElse(null);
        this.referenceNo = other.optionalReferenceNo().map(StringFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedBy = other.optionalLastModifiedBy().map(StringFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PettyCashLedgerCriteria copy() {
        return new PettyCashLedgerCriteria(this);
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

    public StringFilter getPettyCashCode() {
        return pettyCashCode;
    }

    public Optional<StringFilter> optionalPettyCashCode() {
        return Optional.ofNullable(pettyCashCode);
    }

    public StringFilter pettyCashCode() {
        if (pettyCashCode == null) {
            setPettyCashCode(new StringFilter());
        }
        return pettyCashCode;
    }

    public void setPettyCashCode(StringFilter pettyCashCode) {
        this.pettyCashCode = pettyCashCode;
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

    public StringFilter getPettyCashVoucherNo() {
        return pettyCashVoucherNo;
    }

    public Optional<StringFilter> optionalPettyCashVoucherNo() {
        return Optional.ofNullable(pettyCashVoucherNo);
    }

    public StringFilter pettyCashVoucherNo() {
        if (pettyCashVoucherNo == null) {
            setPettyCashVoucherNo(new StringFilter());
        }
        return pettyCashVoucherNo;
    }

    public void setPettyCashVoucherNo(StringFilter pettyCashVoucherNo) {
        this.pettyCashVoucherNo = pettyCashVoucherNo;
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

    public BigDecimalFilter getCashIn() {
        return cashIn;
    }

    public Optional<BigDecimalFilter> optionalCashIn() {
        return Optional.ofNullable(cashIn);
    }

    public BigDecimalFilter cashIn() {
        if (cashIn == null) {
            setCashIn(new BigDecimalFilter());
        }
        return cashIn;
    }

    public void setCashIn(BigDecimalFilter cashIn) {
        this.cashIn = cashIn;
    }

    public BigDecimalFilter getCashOut() {
        return cashOut;
    }

    public Optional<BigDecimalFilter> optionalCashOut() {
        return Optional.ofNullable(cashOut);
    }

    public BigDecimalFilter cashOut() {
        if (cashOut == null) {
            setCashOut(new BigDecimalFilter());
        }
        return cashOut;
    }

    public void setCashOut(BigDecimalFilter cashOut) {
        this.cashOut = cashOut;
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

    public StringFilter getLinkedAccountCode() {
        return linkedAccountCode;
    }

    public Optional<StringFilter> optionalLinkedAccountCode() {
        return Optional.ofNullable(linkedAccountCode);
    }

    public StringFilter linkedAccountCode() {
        if (linkedAccountCode == null) {
            setLinkedAccountCode(new StringFilter());
        }
        return linkedAccountCode;
    }

    public void setLinkedAccountCode(StringFilter linkedAccountCode) {
        this.linkedAccountCode = linkedAccountCode;
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
        final PettyCashLedgerCriteria that = (PettyCashLedgerCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(branchCode, that.branchCode) &&
            Objects.equals(pettyCashCode, that.pettyCashCode) &&
            Objects.equals(date, that.date) &&
            Objects.equals(pettyCashVoucherNo, that.pettyCashVoucherNo) &&
            Objects.equals(description, that.description) &&
            Objects.equals(cashIn, that.cashIn) &&
            Objects.equals(cashOut, that.cashOut) &&
            Objects.equals(runningBalance, that.runningBalance) &&
            Objects.equals(linkedAccountCode, that.linkedAccountCode) &&
            Objects.equals(referenceNo, that.referenceNo) &&
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
            pettyCashCode,
            date,
            pettyCashVoucherNo,
            description,
            cashIn,
            cashOut,
            runningBalance,
            linkedAccountCode,
            referenceNo,
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
        return "PettyCashLedgerCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalBranchCode().map(f -> "branchCode=" + f + ", ").orElse("") +
            optionalPettyCashCode().map(f -> "pettyCashCode=" + f + ", ").orElse("") +
            optionalDate().map(f -> "date=" + f + ", ").orElse("") +
            optionalPettyCashVoucherNo().map(f -> "pettyCashVoucherNo=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalCashIn().map(f -> "cashIn=" + f + ", ").orElse("") +
            optionalCashOut().map(f -> "cashOut=" + f + ", ").orElse("") +
            optionalRunningBalance().map(f -> "runningBalance=" + f + ", ").orElse("") +
            optionalLinkedAccountCode().map(f -> "linkedAccountCode=" + f + ", ").orElse("") +
            optionalReferenceNo().map(f -> "referenceNo=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalLastModifiedBy().map(f -> "lastModifiedBy=" + f + ", ").orElse("") +
            optionalLastModifiedDate().map(f -> "lastModifiedDate=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
