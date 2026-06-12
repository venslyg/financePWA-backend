package com.gvsolutions.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import org.springframework.data.domain.Persistable;

/**
 * A BankLedger.
 */
@Entity
@Table(name = "bank_ledger")
@JsonIgnoreProperties(value = { "new" })
@org.springframework.data.elasticsearch.annotations.Document(indexName = "bankledger")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BankLedger extends AbstractAuditingEntity<Long> implements Serializable, Persistable<Long> {

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

    @Column(name = "bank_ledger_code")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String bankLedgerCode;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "reference_no")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String referenceNo;

    @Column(name = "description")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String description;

    @Column(name = "deposit_amount", precision = 21, scale = 2)
    private BigDecimal depositAmount;

    @Column(name = "withdrawal_amount", precision = 21, scale = 2)
    private BigDecimal withdrawalAmount;

    @Column(name = "running_balance", precision = 21, scale = 2)
    private BigDecimal runningBalance;

    @Column(name = "remark")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String remark;

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

    public BankLedger id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBranchCode() {
        return this.branchCode;
    }

    public BankLedger branchCode(String branchCode) {
        this.setBranchCode(branchCode);
        return this;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchId() {
        return this.branchId;
    }

    public BankLedger branchId(String branchId) {
        this.setBranchId(branchId);
        return this;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getBankLedgerCode() {
        return this.bankLedgerCode;
    }

    public BankLedger bankLedgerCode(String bankLedgerCode) {
        this.setBankLedgerCode(bankLedgerCode);
        return this;
    }

    public void setBankLedgerCode(String bankLedgerCode) {
        this.bankLedgerCode = bankLedgerCode;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public BankLedger date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getReferenceNo() {
        return this.referenceNo;
    }

    public BankLedger referenceNo(String referenceNo) {
        this.setReferenceNo(referenceNo);
        return this;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getDescription() {
        return this.description;
    }

    public BankLedger description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getDepositAmount() {
        return this.depositAmount;
    }

    public BankLedger depositAmount(BigDecimal depositAmount) {
        this.setDepositAmount(depositAmount);
        return this;
    }

    public void setDepositAmount(BigDecimal depositAmount) {
        this.depositAmount = depositAmount;
    }

    public BigDecimal getWithdrawalAmount() {
        return this.withdrawalAmount;
    }

    public BankLedger withdrawalAmount(BigDecimal withdrawalAmount) {
        this.setWithdrawalAmount(withdrawalAmount);
        return this;
    }

    public void setWithdrawalAmount(BigDecimal withdrawalAmount) {
        this.withdrawalAmount = withdrawalAmount;
    }

    public BigDecimal getRunningBalance() {
        return this.runningBalance;
    }

    public BankLedger runningBalance(BigDecimal runningBalance) {
        this.setRunningBalance(runningBalance);
        return this;
    }

    public void setRunningBalance(BigDecimal runningBalance) {
        this.runningBalance = runningBalance;
    }

    public String getRemark() {
        return this.remark;
    }

    public BankLedger remark(String remark) {
        this.setRemark(remark);
        return this;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    // Inherited createdBy methods
    public BankLedger createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    // Inherited createdDate methods
    public BankLedger createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    // Inherited lastModifiedBy methods
    public BankLedger lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    // Inherited lastModifiedDate methods
    public BankLedger lastModifiedDate(Instant lastModifiedDate) {
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

    public BankLedger setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BankLedger)) {
            return false;
        }
        return getId() != null && getId().equals(((BankLedger) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BankLedger{" +
            "id=" + getId() +
            ", branchCode='" + getBranchCode() + "'" +
            ", branchId='" + getBranchId() + "'" +
            ", bankLedgerCode='" + getBankLedgerCode() + "'" +
            ", date='" + getDate() + "'" +
            ", referenceNo='" + getReferenceNo() + "'" +
            ", description='" + getDescription() + "'" +
            ", depositAmount=" + getDepositAmount() +
            ", withdrawalAmount=" + getWithdrawalAmount() +
            ", runningBalance=" + getRunningBalance() +
            ", remark='" + getRemark() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
