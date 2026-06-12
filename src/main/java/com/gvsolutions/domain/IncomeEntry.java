package com.gvsolutions.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gvsolutions.domain.enumeration.IncomeType;
import com.gvsolutions.domain.enumeration.PaymentMode;
import com.gvsolutions.domain.enumeration.SyncStatus;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import org.springframework.data.domain.Persistable;

/**
 * Income Entries (Basic Package & Donation Tracker)
 */
@Entity
@Table(name = "income_entry")
@JsonIgnoreProperties(value = { "new" })
@org.springframework.data.elasticsearch.annotations.Document(indexName = "incomeentry")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IncomeEntry extends AbstractAuditingEntity<Long> implements Serializable, Persistable<Long> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "branch_code")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String branchCode;

    @Column(name = "account_code")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String accountCode;

    @Column(name = "income_code")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String incomeCode;

    @Column(name = "created_by_username")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String createdByUsername;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "receipt_no")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String receiptNo;

    @Column(name = "description")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "income_type")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private IncomeType incomeType;

    @Column(name = "amount", precision = 21, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private PaymentMode paymentMethod;

    @Column(name = "receivable_person")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String receivablePerson;

    @Column(name = "received_by")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String receivedBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "sync_status")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private SyncStatus syncStatus;

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

    public IncomeEntry id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBranchCode() {
        return this.branchCode;
    }

    public IncomeEntry branchCode(String branchCode) {
        this.setBranchCode(branchCode);
        return this;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getAccountCode() {
        return this.accountCode;
    }

    public IncomeEntry accountCode(String accountCode) {
        this.setAccountCode(accountCode);
        return this;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getIncomeCode() {
        return this.incomeCode;
    }

    public IncomeEntry incomeCode(String incomeCode) {
        this.setIncomeCode(incomeCode);
        return this;
    }

    public void setIncomeCode(String incomeCode) {
        this.incomeCode = incomeCode;
    }

    public String getCreatedByUsername() {
        return this.createdByUsername;
    }

    public IncomeEntry createdByUsername(String createdByUsername) {
        this.setCreatedByUsername(createdByUsername);
        return this;
    }

    public void setCreatedByUsername(String createdByUsername) {
        this.createdByUsername = createdByUsername;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public IncomeEntry date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getReceiptNo() {
        return this.receiptNo;
    }

    public IncomeEntry receiptNo(String receiptNo) {
        this.setReceiptNo(receiptNo);
        return this;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public String getDescription() {
        return this.description;
    }

    public IncomeEntry description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public IncomeType getIncomeType() {
        return this.incomeType;
    }

    public IncomeEntry incomeType(IncomeType incomeType) {
        this.setIncomeType(incomeType);
        return this;
    }

    public void setIncomeType(IncomeType incomeType) {
        this.incomeType = incomeType;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public IncomeEntry amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentMode getPaymentMethod() {
        return this.paymentMethod;
    }

    public IncomeEntry paymentMethod(PaymentMode paymentMethod) {
        this.setPaymentMethod(paymentMethod);
        return this;
    }

    public void setPaymentMethod(PaymentMode paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getReceivablePerson() {
        return this.receivablePerson;
    }

    public IncomeEntry receivablePerson(String receivablePerson) {
        this.setReceivablePerson(receivablePerson);
        return this;
    }

    public void setReceivablePerson(String receivablePerson) {
        this.receivablePerson = receivablePerson;
    }

    public String getReceivedBy() {
        return this.receivedBy;
    }

    public IncomeEntry receivedBy(String receivedBy) {
        this.setReceivedBy(receivedBy);
        return this;
    }

    public void setReceivedBy(String receivedBy) {
        this.receivedBy = receivedBy;
    }

    public SyncStatus getSyncStatus() {
        return this.syncStatus;
    }

    public IncomeEntry syncStatus(SyncStatus syncStatus) {
        this.setSyncStatus(syncStatus);
        return this;
    }

    public void setSyncStatus(SyncStatus syncStatus) {
        this.syncStatus = syncStatus;
    }

    // Inherited createdBy methods
    public IncomeEntry createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    // Inherited createdDate methods
    public IncomeEntry createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    // Inherited lastModifiedBy methods
    public IncomeEntry lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    // Inherited lastModifiedDate methods
    public IncomeEntry lastModifiedDate(Instant lastModifiedDate) {
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

    public IncomeEntry setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IncomeEntry)) {
            return false;
        }
        return getId() != null && getId().equals(((IncomeEntry) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IncomeEntry{" +
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
