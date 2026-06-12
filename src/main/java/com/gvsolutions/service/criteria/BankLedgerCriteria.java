package com.gvsolutions.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.gvsolutions.domain.BankLedger} entity. This class is used
 * in {@link com.gvsolutions.web.rest.BankLedgerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /bank-ledgers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BankLedgerCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter branchCode;

    private StringFilter branchId;

    private StringFilter bankLedgerCode;

    private LocalDateFilter date;

    private StringFilter referenceNo;

    private StringFilter description;

    private BigDecimalFilter depositAmount;

    private BigDecimalFilter withdrawalAmount;

    private BigDecimalFilter runningBalance;

    private StringFilter remark;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private Boolean distinct;

    public BankLedgerCriteria() {}

    public BankLedgerCriteria(BankLedgerCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.branchCode = other.optionalBranchCode().map(StringFilter::copy).orElse(null);
        this.branchId = other.optionalBranchId().map(StringFilter::copy).orElse(null);
        this.bankLedgerCode = other.optionalBankLedgerCode().map(StringFilter::copy).orElse(null);
        this.date = other.optionalDate().map(LocalDateFilter::copy).orElse(null);
        this.referenceNo = other.optionalReferenceNo().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.depositAmount = other.optionalDepositAmount().map(BigDecimalFilter::copy).orElse(null);
        this.withdrawalAmount = other.optionalWithdrawalAmount().map(BigDecimalFilter::copy).orElse(null);
        this.runningBalance = other.optionalRunningBalance().map(BigDecimalFilter::copy).orElse(null);
        this.remark = other.optionalRemark().map(StringFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedBy = other.optionalLastModifiedBy().map(StringFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public BankLedgerCriteria copy() {
        return new BankLedgerCriteria(this);
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

    public StringFilter getBankLedgerCode() {
        return bankLedgerCode;
    }

    public Optional<StringFilter> optionalBankLedgerCode() {
        return Optional.ofNullable(bankLedgerCode);
    }

    public StringFilter bankLedgerCode() {
        if (bankLedgerCode == null) {
            setBankLedgerCode(new StringFilter());
        }
        return bankLedgerCode;
    }

    public void setBankLedgerCode(StringFilter bankLedgerCode) {
        this.bankLedgerCode = bankLedgerCode;
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

    public BigDecimalFilter getDepositAmount() {
        return depositAmount;
    }

    public Optional<BigDecimalFilter> optionalDepositAmount() {
        return Optional.ofNullable(depositAmount);
    }

    public BigDecimalFilter depositAmount() {
        if (depositAmount == null) {
            setDepositAmount(new BigDecimalFilter());
        }
        return depositAmount;
    }

    public void setDepositAmount(BigDecimalFilter depositAmount) {
        this.depositAmount = depositAmount;
    }

    public BigDecimalFilter getWithdrawalAmount() {
        return withdrawalAmount;
    }

    public Optional<BigDecimalFilter> optionalWithdrawalAmount() {
        return Optional.ofNullable(withdrawalAmount);
    }

    public BigDecimalFilter withdrawalAmount() {
        if (withdrawalAmount == null) {
            setWithdrawalAmount(new BigDecimalFilter());
        }
        return withdrawalAmount;
    }

    public void setWithdrawalAmount(BigDecimalFilter withdrawalAmount) {
        this.withdrawalAmount = withdrawalAmount;
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

    public StringFilter getRemark() {
        return remark;
    }

    public Optional<StringFilter> optionalRemark() {
        return Optional.ofNullable(remark);
    }

    public StringFilter remark() {
        if (remark == null) {
            setRemark(new StringFilter());
        }
        return remark;
    }

    public void setRemark(StringFilter remark) {
        this.remark = remark;
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
        final BankLedgerCriteria that = (BankLedgerCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(branchCode, that.branchCode) &&
            Objects.equals(branchId, that.branchId) &&
            Objects.equals(bankLedgerCode, that.bankLedgerCode) &&
            Objects.equals(date, that.date) &&
            Objects.equals(referenceNo, that.referenceNo) &&
            Objects.equals(description, that.description) &&
            Objects.equals(depositAmount, that.depositAmount) &&
            Objects.equals(withdrawalAmount, that.withdrawalAmount) &&
            Objects.equals(runningBalance, that.runningBalance) &&
            Objects.equals(remark, that.remark) &&
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
            bankLedgerCode,
            date,
            referenceNo,
            description,
            depositAmount,
            withdrawalAmount,
            runningBalance,
            remark,
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
        return "BankLedgerCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalBranchCode().map(f -> "branchCode=" + f + ", ").orElse("") +
            optionalBranchId().map(f -> "branchId=" + f + ", ").orElse("") +
            optionalBankLedgerCode().map(f -> "bankLedgerCode=" + f + ", ").orElse("") +
            optionalDate().map(f -> "date=" + f + ", ").orElse("") +
            optionalReferenceNo().map(f -> "referenceNo=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalDepositAmount().map(f -> "depositAmount=" + f + ", ").orElse("") +
            optionalWithdrawalAmount().map(f -> "withdrawalAmount=" + f + ", ").orElse("") +
            optionalRunningBalance().map(f -> "runningBalance=" + f + ", ").orElse("") +
            optionalRemark().map(f -> "remark=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalLastModifiedBy().map(f -> "lastModifiedBy=" + f + ", ").orElse("") +
            optionalLastModifiedDate().map(f -> "lastModifiedDate=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
