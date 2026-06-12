package com.gvsolutions.service.criteria;

import com.gvsolutions.domain.enumeration.IncomeType;
import com.gvsolutions.domain.enumeration.PaymentMode;
import com.gvsolutions.domain.enumeration.SyncStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.gvsolutions.domain.IncomeEntry} entity. This class is used
 * in {@link com.gvsolutions.web.rest.IncomeEntryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /income-entries?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IncomeEntryCriteria implements Serializable, Criteria {

    /**
     * Class for filtering IncomeType
     */
    public static class IncomeTypeFilter extends Filter<IncomeType> {

        public IncomeTypeFilter() {}

        public IncomeTypeFilter(IncomeTypeFilter filter) {
            super(filter);
        }

        @Override
        public IncomeTypeFilter copy() {
            return new IncomeTypeFilter(this);
        }
    }

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

    private StringFilter branchId;

    private StringFilter accountCode;

    private StringFilter incomeCode;

    private StringFilter createdByUsername;

    private LocalDateFilter date;

    private StringFilter receiptNo;

    private StringFilter description;

    private IncomeTypeFilter incomeType;

    private BigDecimalFilter amount;

    private PaymentModeFilter paymentMethod;

    private StringFilter receivablePerson;

    private StringFilter receivedBy;

    private SyncStatusFilter syncStatus;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private Boolean distinct;

    public IncomeEntryCriteria() {}

    public IncomeEntryCriteria(IncomeEntryCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.branchCode = other.optionalBranchCode().map(StringFilter::copy).orElse(null);
        this.branchId = other.optionalBranchId().map(StringFilter::copy).orElse(null);
        this.accountCode = other.optionalAccountCode().map(StringFilter::copy).orElse(null);
        this.incomeCode = other.optionalIncomeCode().map(StringFilter::copy).orElse(null);
        this.createdByUsername = other.optionalCreatedByUsername().map(StringFilter::copy).orElse(null);
        this.date = other.optionalDate().map(LocalDateFilter::copy).orElse(null);
        this.receiptNo = other.optionalReceiptNo().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.incomeType = other.optionalIncomeType().map(IncomeTypeFilter::copy).orElse(null);
        this.amount = other.optionalAmount().map(BigDecimalFilter::copy).orElse(null);
        this.paymentMethod = other.optionalPaymentMethod().map(PaymentModeFilter::copy).orElse(null);
        this.receivablePerson = other.optionalReceivablePerson().map(StringFilter::copy).orElse(null);
        this.receivedBy = other.optionalReceivedBy().map(StringFilter::copy).orElse(null);
        this.syncStatus = other.optionalSyncStatus().map(SyncStatusFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedBy = other.optionalLastModifiedBy().map(StringFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public IncomeEntryCriteria copy() {
        return new IncomeEntryCriteria(this);
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

    public StringFilter getIncomeCode() {
        return incomeCode;
    }

    public Optional<StringFilter> optionalIncomeCode() {
        return Optional.ofNullable(incomeCode);
    }

    public StringFilter incomeCode() {
        if (incomeCode == null) {
            setIncomeCode(new StringFilter());
        }
        return incomeCode;
    }

    public void setIncomeCode(StringFilter incomeCode) {
        this.incomeCode = incomeCode;
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

    public StringFilter getReceiptNo() {
        return receiptNo;
    }

    public Optional<StringFilter> optionalReceiptNo() {
        return Optional.ofNullable(receiptNo);
    }

    public StringFilter receiptNo() {
        if (receiptNo == null) {
            setReceiptNo(new StringFilter());
        }
        return receiptNo;
    }

    public void setReceiptNo(StringFilter receiptNo) {
        this.receiptNo = receiptNo;
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

    public IncomeTypeFilter getIncomeType() {
        return incomeType;
    }

    public Optional<IncomeTypeFilter> optionalIncomeType() {
        return Optional.ofNullable(incomeType);
    }

    public IncomeTypeFilter incomeType() {
        if (incomeType == null) {
            setIncomeType(new IncomeTypeFilter());
        }
        return incomeType;
    }

    public void setIncomeType(IncomeTypeFilter incomeType) {
        this.incomeType = incomeType;
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

    public PaymentModeFilter getPaymentMethod() {
        return paymentMethod;
    }

    public Optional<PaymentModeFilter> optionalPaymentMethod() {
        return Optional.ofNullable(paymentMethod);
    }

    public PaymentModeFilter paymentMethod() {
        if (paymentMethod == null) {
            setPaymentMethod(new PaymentModeFilter());
        }
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentModeFilter paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public StringFilter getReceivablePerson() {
        return receivablePerson;
    }

    public Optional<StringFilter> optionalReceivablePerson() {
        return Optional.ofNullable(receivablePerson);
    }

    public StringFilter receivablePerson() {
        if (receivablePerson == null) {
            setReceivablePerson(new StringFilter());
        }
        return receivablePerson;
    }

    public void setReceivablePerson(StringFilter receivablePerson) {
        this.receivablePerson = receivablePerson;
    }

    public StringFilter getReceivedBy() {
        return receivedBy;
    }

    public Optional<StringFilter> optionalReceivedBy() {
        return Optional.ofNullable(receivedBy);
    }

    public StringFilter receivedBy() {
        if (receivedBy == null) {
            setReceivedBy(new StringFilter());
        }
        return receivedBy;
    }

    public void setReceivedBy(StringFilter receivedBy) {
        this.receivedBy = receivedBy;
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
        final IncomeEntryCriteria that = (IncomeEntryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(branchCode, that.branchCode) &&
            Objects.equals(branchId, that.branchId) &&
            Objects.equals(accountCode, that.accountCode) &&
            Objects.equals(incomeCode, that.incomeCode) &&
            Objects.equals(createdByUsername, that.createdByUsername) &&
            Objects.equals(date, that.date) &&
            Objects.equals(receiptNo, that.receiptNo) &&
            Objects.equals(description, that.description) &&
            Objects.equals(incomeType, that.incomeType) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(paymentMethod, that.paymentMethod) &&
            Objects.equals(receivablePerson, that.receivablePerson) &&
            Objects.equals(receivedBy, that.receivedBy) &&
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
            branchId,
            accountCode,
            incomeCode,
            createdByUsername,
            date,
            receiptNo,
            description,
            incomeType,
            amount,
            paymentMethod,
            receivablePerson,
            receivedBy,
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
        return "IncomeEntryCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalBranchCode().map(f -> "branchCode=" + f + ", ").orElse("") +
            optionalBranchId().map(f -> "branchId=" + f + ", ").orElse("") +
            optionalAccountCode().map(f -> "accountCode=" + f + ", ").orElse("") +
            optionalIncomeCode().map(f -> "incomeCode=" + f + ", ").orElse("") +
            optionalCreatedByUsername().map(f -> "createdByUsername=" + f + ", ").orElse("") +
            optionalDate().map(f -> "date=" + f + ", ").orElse("") +
            optionalReceiptNo().map(f -> "receiptNo=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalIncomeType().map(f -> "incomeType=" + f + ", ").orElse("") +
            optionalAmount().map(f -> "amount=" + f + ", ").orElse("") +
            optionalPaymentMethod().map(f -> "paymentMethod=" + f + ", ").orElse("") +
            optionalReceivablePerson().map(f -> "receivablePerson=" + f + ", ").orElse("") +
            optionalReceivedBy().map(f -> "receivedBy=" + f + ", ").orElse("") +
            optionalSyncStatus().map(f -> "syncStatus=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalLastModifiedBy().map(f -> "lastModifiedBy=" + f + ", ").orElse("") +
            optionalLastModifiedDate().map(f -> "lastModifiedDate=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
