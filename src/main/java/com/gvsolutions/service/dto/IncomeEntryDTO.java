package com.gvsolutions.service.dto;

import com.gvsolutions.domain.enumeration.IncomeType;
import com.gvsolutions.domain.enumeration.PaymentMode;
import com.gvsolutions.domain.enumeration.SyncStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.gvsolutions.domain.IncomeEntry} entity.
 */
@Schema(description = "Income Entries (Basic Package & Donation Tracker)")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IncomeEntryDTO implements Serializable {

    private Long id;

    private String branchCode;

    private String accountCode;

    private String incomeCode;

    private String createdByUsername;

    private LocalDate date;

    private String receiptNo;

    private String description;

    private IncomeType incomeType;

    private BigDecimal amount;

    private PaymentMode paymentMethod;

    private String receivablePerson;

    private String receivedBy;

    private SyncStatus syncStatus;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getIncomeCode() {
        return incomeCode;
    }

    public void setIncomeCode(String incomeCode) {
        this.incomeCode = incomeCode;
    }

    public String getCreatedByUsername() {
        return createdByUsername;
    }

    public void setCreatedByUsername(String createdByUsername) {
        this.createdByUsername = createdByUsername;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public IncomeType getIncomeType() {
        return incomeType;
    }

    public void setIncomeType(IncomeType incomeType) {
        this.incomeType = incomeType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentMode getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMode paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getReceivablePerson() {
        return receivablePerson;
    }

    public void setReceivablePerson(String receivablePerson) {
        this.receivablePerson = receivablePerson;
    }

    public String getReceivedBy() {
        return receivedBy;
    }

    public void setReceivedBy(String receivedBy) {
        this.receivedBy = receivedBy;
    }

    public SyncStatus getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(SyncStatus syncStatus) {
        this.syncStatus = syncStatus;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IncomeEntryDTO)) {
            return false;
        }

        IncomeEntryDTO incomeEntryDTO = (IncomeEntryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, incomeEntryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IncomeEntryDTO{" +
            "id=" + getId() +
            ", branchCode='" + getBranchCode() + "'" +
            ", accountCode='" + getAccountCode() + "'" +
            ", incomeCode='" + getIncomeCode() + "'" +
            ", createdByUsername='" + getCreatedByUsername() + "'" +
            ", date='" + getDate() + "'" +
            ", receiptNo='" + getReceiptNo() + "'" +
            ", description='" + getDescription() + "'" +
            ", incomeType='" + getIncomeType() + "'" +
            ", amount=" + getAmount() +
            ", paymentMethod='" + getPaymentMethod() + "'" +
            ", receivablePerson='" + getReceivablePerson() + "'" +
            ", receivedBy='" + getReceivedBy() + "'" +
            ", syncStatus='" + getSyncStatus() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
