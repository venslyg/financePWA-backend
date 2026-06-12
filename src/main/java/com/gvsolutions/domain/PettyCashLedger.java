package com.gvsolutions.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import org.springframework.data.domain.Persistable;

/**
 * A PettyCashLedger.
 */
@Entity
@Table(name = "petty_cash_ledger")
@JsonIgnoreProperties(value = { "new" })
@org.springframework.data.elasticsearch.annotations.Document(indexName = "pettycashledger")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PettyCashLedger extends AbstractAuditingEntity<Long> implements Serializable, Persistable<Long> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "branch_code")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String branchCode;

    @Column(name = "branch_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String branchId;

    @Column(name = "petty_cash_code")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String pettyCashCode;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "petty_cash_voucher_no")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String pettyCashVoucherNo;

    @Column(name = "description")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String description;

    @Column(name = "cash_in", precision = 21, scale = 2)
    private BigDecimal cashIn;

    @Column(name = "cash_out", precision = 21, scale = 2)
    private BigDecimal cashOut;

    @Column(name = "running_balance", precision = 21, scale = 2)
    private BigDecimal runningBalance;

    @Column(name = "linked_account_code")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String linkedAccountCode;

    @Column(name = "reference_no")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String referenceNo;

    // Inherited createdBy definition
    // Inherited createdDate definition
    // Inherited lastModifiedBy definition
    // Inherited lastModifiedDate definition
    @org.springframework.data.annotation.Transient
    @Transient
    private boolean isPersisted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PettyCashLedger id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBranchCode() {
        return this.branchCode;
    }

    public PettyCashLedger branchCode(String branchCode) {
        this.setBranchCode(branchCode);
        return this;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchId() {
        return this.branchId;
    }

    public PettyCashLedger branchId(String branchId) {
        this.setBranchId(branchId);
        return this;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getPettyCashCode() {
        return this.pettyCashCode;
    }

    public PettyCashLedger pettyCashCode(String pettyCashCode) {
        this.setPettyCashCode(pettyCashCode);
        return this;
    }

    public void setPettyCashCode(String pettyCashCode) {
        this.pettyCashCode = pettyCashCode;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public PettyCashLedger date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getPettyCashVoucherNo() {
        return this.pettyCashVoucherNo;
    }

    public PettyCashLedger pettyCashVoucherNo(String pettyCashVoucherNo) {
        this.setPettyCashVoucherNo(pettyCashVoucherNo);
        return this;
    }

    public void setPettyCashVoucherNo(String pettyCashVoucherNo) {
        this.pettyCashVoucherNo = pettyCashVoucherNo;
    }

    public String getDescription() {
        return this.description;
    }

    public PettyCashLedger description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getCashIn() {
        return this.cashIn;
    }

    public PettyCashLedger cashIn(BigDecimal cashIn) {
        this.setCashIn(cashIn);
        return this;
    }

    public void setCashIn(BigDecimal cashIn) {
        this.cashIn = cashIn;
    }

    public BigDecimal getCashOut() {
        return this.cashOut;
    }

    public PettyCashLedger cashOut(BigDecimal cashOut) {
        this.setCashOut(cashOut);
        return this;
    }

    public void setCashOut(BigDecimal cashOut) {
        this.cashOut = cashOut;
    }

    public BigDecimal getRunningBalance() {
        return this.runningBalance;
    }

    public PettyCashLedger runningBalance(BigDecimal runningBalance) {
        this.setRunningBalance(runningBalance);
        return this;
    }

    public void setRunningBalance(BigDecimal runningBalance) {
        this.runningBalance = runningBalance;
    }

    public String getLinkedAccountCode() {
        return this.linkedAccountCode;
    }

    public PettyCashLedger linkedAccountCode(String linkedAccountCode) {
        this.setLinkedAccountCode(linkedAccountCode);
        return this;
    }

    public void setLinkedAccountCode(String linkedAccountCode) {
        this.linkedAccountCode = linkedAccountCode;
    }

    public String getReferenceNo() {
        return this.referenceNo;
    }

    public PettyCashLedger referenceNo(String referenceNo) {
        this.setReferenceNo(referenceNo);
        return this;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    // Inherited createdBy methods
    public PettyCashLedger createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    // Inherited createdDate methods
    public PettyCashLedger createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    // Inherited lastModifiedBy methods
    public PettyCashLedger lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    // Inherited lastModifiedDate methods
    public PettyCashLedger lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    @PostLoad
    @PostPersist
    public void updateEntityState() {
        this.setIsPersisted();
    }

    @org.springframework.data.annotation.Transient
    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public PettyCashLedger setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PettyCashLedger)) {
            return false;
        }
        return getId() != null && getId().equals(((PettyCashLedger) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PettyCashLedger{" +
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
