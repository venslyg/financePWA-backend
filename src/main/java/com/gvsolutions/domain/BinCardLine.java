package com.gvsolutions.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import org.springframework.data.domain.Persistable;

/**
 * A BinCardLine.
 */
@Entity
@Table(name = "bin_card_line")
@JsonIgnoreProperties(value = { "new" })
@org.springframework.data.elasticsearch.annotations.Document(indexName = "bincardline")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BinCardLine extends AbstractAuditingEntity<Long> implements Serializable, Persistable<Long> {

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

    @Column(name = "inventory_item_code")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String inventoryItemCode;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "reference_no")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String referenceNo;

    @Column(name = "description")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String description;

    @Column(name = "quantity_in", precision = 21, scale = 2)
    private BigDecimal quantityIn;

    @Column(name = "quantity_out", precision = 21, scale = 2)
    private BigDecimal quantityOut;

    @Column(name = "running_balance", precision = 21, scale = 2)
    private BigDecimal runningBalance;

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

    public BinCardLine id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBranchCode() {
        return this.branchCode;
    }

    public BinCardLine branchCode(String branchCode) {
        this.setBranchCode(branchCode);
        return this;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchId() {
        return this.branchId;
    }

    public BinCardLine branchId(String branchId) {
        this.setBranchId(branchId);
        return this;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getInventoryItemCode() {
        return this.inventoryItemCode;
    }

    public BinCardLine inventoryItemCode(String inventoryItemCode) {
        this.setInventoryItemCode(inventoryItemCode);
        return this;
    }

    public void setInventoryItemCode(String inventoryItemCode) {
        this.inventoryItemCode = inventoryItemCode;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public BinCardLine date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getReferenceNo() {
        return this.referenceNo;
    }

    public BinCardLine referenceNo(String referenceNo) {
        this.setReferenceNo(referenceNo);
        return this;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getDescription() {
        return this.description;
    }

    public BinCardLine description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getQuantityIn() {
        return this.quantityIn;
    }

    public BinCardLine quantityIn(BigDecimal quantityIn) {
        this.setQuantityIn(quantityIn);
        return this;
    }

    public void setQuantityIn(BigDecimal quantityIn) {
        this.quantityIn = quantityIn;
    }

    public BigDecimal getQuantityOut() {
        return this.quantityOut;
    }

    public BinCardLine quantityOut(BigDecimal quantityOut) {
        this.setQuantityOut(quantityOut);
        return this;
    }

    public void setQuantityOut(BigDecimal quantityOut) {
        this.quantityOut = quantityOut;
    }

    public BigDecimal getRunningBalance() {
        return this.runningBalance;
    }

    public BinCardLine runningBalance(BigDecimal runningBalance) {
        this.setRunningBalance(runningBalance);
        return this;
    }

    public void setRunningBalance(BigDecimal runningBalance) {
        this.runningBalance = runningBalance;
    }

    // Inherited createdBy methods
    public BinCardLine createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    // Inherited createdDate methods
    public BinCardLine createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    // Inherited lastModifiedBy methods
    public BinCardLine lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    // Inherited lastModifiedDate methods
    public BinCardLine lastModifiedDate(Instant lastModifiedDate) {
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

    public BinCardLine setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BinCardLine)) {
            return false;
        }
        return getId() != null && getId().equals(((BinCardLine) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BinCardLine{" +
            "id=" + getId() +
            ", branchCode='" + getBranchCode() + "'" +
            ", branchId='" + getBranchId() + "'" +
            ", inventoryItemCode='" + getInventoryItemCode() + "'" +
            ", date='" + getDate() + "'" +
            ", referenceNo='" + getReferenceNo() + "'" +
            ", description='" + getDescription() + "'" +
            ", quantityIn=" + getQuantityIn() +
            ", quantityOut=" + getQuantityOut() +
            ", runningBalance=" + getRunningBalance() +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
