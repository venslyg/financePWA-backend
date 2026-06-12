package com.gvsolutions.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.gvsolutions.domain.PettyCashLedger} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PettyCashLedgerDTO implements Serializable {

    private Long id;

    private String branchCode;

    private String branchId;

    private String pettyCashCode;

    private LocalDate date;

    private String pettyCashVoucherNo;

    private String description;

    private BigDecimal cashIn;

    private BigDecimal cashOut;

    private BigDecimal runningBalance;

    private String linkedAccountCode;

    private String referenceNo;

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

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getPettyCashCode() {
        return pettyCashCode;
    }

    public void setPettyCashCode(String pettyCashCode) {
        this.pettyCashCode = pettyCashCode;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getPettyCashVoucherNo() {
        return pettyCashVoucherNo;
    }

    public void setPettyCashVoucherNo(String pettyCashVoucherNo) {
        this.pettyCashVoucherNo = pettyCashVoucherNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getCashIn() {
        return cashIn;
    }

    public void setCashIn(BigDecimal cashIn) {
        this.cashIn = cashIn;
    }

    public BigDecimal getCashOut() {
        return cashOut;
    }

    public void setCashOut(BigDecimal cashOut) {
        this.cashOut = cashOut;
    }

    public BigDecimal getRunningBalance() {
        return runningBalance;
    }

    public void setRunningBalance(BigDecimal runningBalance) {
        this.runningBalance = runningBalance;
    }

    public String getLinkedAccountCode() {
        return linkedAccountCode;
    }

    public void setLinkedAccountCode(String linkedAccountCode) {
        this.linkedAccountCode = linkedAccountCode;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
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
        if (!(o instanceof PettyCashLedgerDTO)) {
            return false;
        }

        PettyCashLedgerDTO pettyCashLedgerDTO = (PettyCashLedgerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pettyCashLedgerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PettyCashLedgerDTO{" +
            "id=" + getId() +
            ", branchCode='" + getBranchCode() + "'" +
            ", branchId='" + getBranchId() + "'" +
            ", pettyCashCode='" + getPettyCashCode() + "'" +
            ", date='" + getDate() + "'" +
            ", pettyCashVoucherNo='" + getPettyCashVoucherNo() + "'" +
            ", description='" + getDescription() + "'" +
            ", cashIn=" + getCashIn() +
            ", cashOut=" + getCashOut() +
            ", runningBalance=" + getRunningBalance() +
            ", linkedAccountCode='" + getLinkedAccountCode() + "'" +
            ", referenceNo='" + getReferenceNo() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
