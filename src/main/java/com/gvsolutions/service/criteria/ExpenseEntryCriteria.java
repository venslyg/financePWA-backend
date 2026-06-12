package com.gvsolutions.service.criteria;

import com.gvsolutions.domain.enumeration.ApprovalStatus;
import com.gvsolutions.domain.enumeration.PaymentMode;
import com.gvsolutions.domain.enumeration.SyncStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.gvsolutions.domain.ExpenseEntry} entity. This class is used
 * in {@link com.gvsolutions.web.rest.ExpenseEntryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /expense-entries?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExpenseEntryCriteria implements Serializable, Criteria {

    /**
     * Class for filtering PaymentMode
     */
    public static class PaymentModeFilter extends Filter<PaymentMode> {

        public PaymentModeFilter() {}

        public PaymentModeFilter(PaymentModeFilter filter) {
            super(filter);
        }

        @Override
        public PaymentModeFilter copy() {
            return new PaymentModeFilter(this);
        }
    }

    /**
     * Class for filtering ApprovalStatus
     */
    public static class ApprovalStatusFilter extends Filter<ApprovalStatus> {

        public ApprovalStatusFilter() {}

        public ApprovalStatusFilter(ApprovalStatusFilter filter) {
            super(filter);
        }

        @Override
        public ApprovalStatusFilter copy() {
            return new ApprovalStatusFilter(this);
        }
    }

    /**
     * Class for filtering SyncStatus
     */
    public static class SyncStatusFilter extends Filter<SyncStatus> {

        public SyncStatusFilter() {}

        public SyncStatusFilter(SyncStatusFilter filter) {
            super(filter);
        }

        @Override
        public SyncStatusFilter copy() {
            return new SyncStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter branchCode;

    private StringFilter accountCode;

    private StringFilter expenseCode;

    private StringFilter expenseCategoryCode;

    private StringFilter expenseSubCategoryCode;

    private StringFilter createdByUsername;

    private LocalDateFilter date;

    private StringFilter voucherNo;

    private StringFilter description;

    private BigDecimalFilter amount;

    private PaymentModeFilter paymentMode;

    private ApprovalStatusFilter approvalStatus;

    private StringFilter approvedBy;

    private StringFilter vendor;

    private SyncStatusFilter syncStatus;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private Boolean distinct;

    public ExpenseEntryCriteria() {}

    public ExpenseEntryCriteria(ExpenseEntryCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.branchCode = other.optionalBranchCode().map(StringFilter::copy).orElse(null);
        this.accountCode = other.optionalAccountCode().map(StringFilter::copy).orElse(null);
        this.expenseCode = other.optionalExpenseCode().map(StringFilter::copy).orElse(null);
        this.expenseCategoryCode = other.optionalExpenseCategoryCode().map(StringFilter::copy).orElse(null);
        this.expenseSubCategoryCode = other.optionalExpenseSubCategoryCode().map(StringFilter::copy).orElse(null);
        this.createdByUsername = other.optionalCreatedByUsername().map(StringFilter::copy).orElse(null);
        this.date = other.optionalDate().map(LocalDateFilter::copy).orElse(null);
        this.voucherNo = other.optionalVoucherNo().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.amount = other.optionalAmount().map(BigDecimalFilter::copy).orElse(null);
        this.paymentMode = other.optionalPaymentMode().map(PaymentModeFilter::copy).orElse(null);
        this.approvalStatus = other.optionalApprovalStatus().map(ApprovalStatusFilter::copy).orElse(null);
        this.approvedBy = other.optionalApprovedBy().map(StringFilter::copy).orElse(null);
        this.vendor = other.optionalVendor().map(StringFilter::copy).orElse(null);
        this.syncStatus = other.optionalSyncStatus().map(SyncStatusFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedBy = other.optionalLastModifiedBy().map(StringFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ExpenseEntryCriteria copy() {
        return new ExpenseEntryCriteria(this);
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

    public StringFilter getAccountCode() {
        return accountCode;
    }

    public Optional<StringFilter> optionalAccountCode() {
        return Optional.ofNullable(accountCode);
    }

    public StringFilter accountCode() {
        if (accountCode == null) {
            setAccountCode(new StringFilter());
        }
        return accountCode;
    }

    public void setAccountCode(StringFilter accountCode) {
        this.accountCode = accountCode;
    }

    public StringFilter getExpenseCode() {
        return expenseCode;
    }

    public Optional<StringFilter> optionalExpenseCode() {
        return Optional.ofNullable(expenseCode);
    }

    public StringFilter expenseCode() {
        if (expenseCode == null) {
            setExpenseCode(new StringFilter());
        }
        return expenseCode;
    }

    public void setExpenseCode(StringFilter expenseCode) {
        this.expenseCode = expenseCode;
    }

    public StringFilter getExpenseCategoryCode() {
        return expenseCategoryCode;
    }

    public Optional<StringFilter> optionalExpenseCategoryCode() {
        return Optional.ofNullable(expenseCategoryCode);
    }

    public StringFilter expenseCategoryCode() {
        if (expenseCategoryCode == null) {
            setExpenseCategoryCode(new StringFilter());
        }
        return expenseCategoryCode;
    }

    public void setExpenseCategoryCode(StringFilter expenseCategoryCode) {
        this.expenseCategoryCode = expenseCategoryCode;
    }

    public StringFilter getExpenseSubCategoryCode() {
        return expenseSubCategoryCode;
    }

    public Optional<StringFilter> optionalExpenseSubCategoryCode() {
        return Optional.ofNullable(expenseSubCategoryCode);
    }

    public StringFilter expenseSubCategoryCode() {
        if (expenseSubCategoryCode == null) {
            setExpenseSubCategoryCode(new StringFilter());
        }
        return expenseSubCategoryCode;
    }

    public void setExpenseSubCategoryCode(StringFilter expenseSubCategoryCode) {
        this.expenseSubCategoryCode = expenseSubCategoryCode;
    }

    public StringFilter getCreatedByUsername() {
        return createdByUsername;
    }

    public Optional<StringFilter> optionalCreatedByUsername() {
        return Optional.ofNullable(createdByUsername);
    }

    public StringFilter createdByUsername() {
        if (createdByUsername == null) {
            setCreatedByUsername(new StringFilter());
        }
        return createdByUsername;
    }

    public void setCreatedByUsername(StringFilter createdByUsername) {
        this.createdByUsername = createdByUsername;
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

    public StringFilter getVoucherNo() {
        return voucherNo;
    }

    public Optional<StringFilter> optionalVoucherNo() {
        return Optional.ofNullable(voucherNo);
    }

    public StringFilter voucherNo() {
        if (voucherNo == null) {
            setVoucherNo(new StringFilter());
        }
        return voucherNo;
    }

    public void setVoucherNo(StringFilter voucherNo) {
        this.voucherNo = voucherNo;
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

    public BigDecimalFilter getAmount() {
        return amount;
    }

    public Optional<BigDecimalFilter> optionalAmount() {
        return Optional.ofNullable(amount);
    }

    public BigDecimalFilter amount() {
        if (amount == null) {
            setAmount(new BigDecimalFilter());
        }
        return amount;
    }

    public void setAmount(BigDecimalFilter amount) {
        this.amount = amount;
    }

    public PaymentModeFilter getPaymentMode() {
        return paymentMode;
    }

    public Optional<PaymentModeFilter> optionalPaymentMode() {
        return Optional.ofNullable(paymentMode);
    }

    public PaymentModeFilter paymentMode() {
        if (paymentMode == null) {
            setPaymentMode(new PaymentModeFilter());
        }
        return paymentMode;
    }

    public void setPaymentMode(PaymentModeFilter paymentMode) {
        this.paymentMode = paymentMode;
    }

    public ApprovalStatusFilter getApprovalStatus() {
        return approvalStatus;
    }

    public Optional<ApprovalStatusFilter> optionalApprovalStatus() {
        return Optional.ofNullable(approvalStatus);
    }

    public ApprovalStatusFilter approvalStatus() {
        if (approvalStatus == null) {
            setApprovalStatus(new ApprovalStatusFilter());
        }
        return approvalStatus;
    }

    public void setApprovalStatus(ApprovalStatusFilter approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public StringFilter getApprovedBy() {
        return approvedBy;
    }

    public Optional<StringFilter> optionalApprovedBy() {
        return Optional.ofNullable(approvedBy);
    }

    public StringFilter approvedBy() {
        if (approvedBy == null) {
            setApprovedBy(new StringFilter());
        }
        return approvedBy;
    }

    public void setApprovedBy(StringFilter approvedBy) {
        this.approvedBy = approvedBy;
    }

    public StringFilter getVendor() {
        return vendor;
    }

    public Optional<StringFilter> optionalVendor() {
        return Optional.ofNullable(vendor);
    }

    public StringFilter vendor() {
        if (vendor == null) {
            setVendor(new StringFilter());
        }
        return vendor;
    }

    public void setVendor(StringFilter vendor) {
        this.vendor = vendor;
    }

    public SyncStatusFilter getSyncStatus() {
        return syncStatus;
    }

    public Optional<SyncStatusFilter> optionalSyncStatus() {
        return Optional.ofNullable(syncStatus);
    }

    public SyncStatusFilter syncStatus() {
        if (syncStatus == null) {
            setSyncStatus(new SyncStatusFilter());
        }
        return syncStatus;
    }

    public void setSyncStatus(SyncStatusFilter syncStatus) {
        this.syncStatus = syncStatus;
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
        final ExpenseEntryCriteria that = (ExpenseEntryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(branchCode, that.branchCode) &&
            Objects.equals(accountCode, that.accountCode) &&
            Objects.equals(expenseCode, that.expenseCode) &&
            Objects.equals(expenseCategoryCode, that.expenseCategoryCode) &&
            Objects.equals(expenseSubCategoryCode, that.expenseSubCategoryCode) &&
            Objects.equals(createdByUsername, that.createdByUsername) &&
            Objects.equals(date, that.date) &&
            Objects.equals(voucherNo, that.voucherNo) &&
            Objects.equals(description, that.description) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(paymentMode, that.paymentMode) &&
            Objects.equals(approvalStatus, that.approvalStatus) &&
            Objects.equals(approvedBy, that.approvedBy) &&
            Objects.equals(vendor, that.vendor) &&
            Objects.equals(syncStatus, that.syncStatus) &&
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
            accountCode,
            expenseCode,
            expenseCategoryCode,
            expenseSubCategoryCode,
            createdByUsername,
            date,
            voucherNo,
            description,
            amount,
            paymentMode,
            approvalStatus,
            approvedBy,
            vendor,
            syncStatus,
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
        return "ExpenseEntryCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalBranchCode().map(f -> "branchCode=" + f + ", ").orElse("") +
            optionalAccountCode().map(f -> "accountCode=" + f + ", ").orElse("") +
            optionalExpenseCode().map(f -> "expenseCode=" + f + ", ").orElse("") +
            optionalExpenseCategoryCode().map(f -> "expenseCategoryCode=" + f + ", ").orElse("") +
            optionalExpenseSubCategoryCode().map(f -> "expenseSubCategoryCode=" + f + ", ").orElse("") +
            optionalCreatedByUsername().map(f -> "createdByUsername=" + f + ", ").orElse("") +
            optionalDate().map(f -> "date=" + f + ", ").orElse("") +
            optionalVoucherNo().map(f -> "voucherNo=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalAmount().map(f -> "amount=" + f + ", ").orElse("") +
            optionalPaymentMode().map(f -> "paymentMode=" + f + ", ").orElse("") +
            optionalApprovalStatus().map(f -> "approvalStatus=" + f + ", ").orElse("") +
            optionalApprovedBy().map(f -> "approvedBy=" + f + ", ").orElse("") +
            optionalVendor().map(f -> "vendor=" + f + ", ").orElse("") +
            optionalSyncStatus().map(f -> "syncStatus=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalLastModifiedBy().map(f -> "lastModifiedBy=" + f + ", ").orElse("") +
            optionalLastModifiedDate().map(f -> "lastModifiedDate=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
