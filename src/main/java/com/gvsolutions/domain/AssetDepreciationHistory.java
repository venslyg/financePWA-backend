package com.gvsolutions.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import org.springframework.data.domain.Persistable;

/**
 * A AssetDepreciationHistory.
 */
@Entity
@Table(name = "asset_depreciation_history")
@JsonIgnoreProperties(value = { "new" })
@org.springframework.data.elasticsearch.annotations.Document(indexName = "assetdepreciationhistory")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AssetDepreciationHistory extends AbstractAuditingEntity<Long> implements Serializable, Persistable<Long> {

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

    @Column(name = "asset_register_code")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String assetRegisterCode;

    @Column(name = "depreciation_date")
    private LocalDate depreciationDate;

    @Column(name = "depreciation_amount", precision = 21, scale = 2)
    private BigDecimal depreciationAmount;

    @Column(name = "value_after_depreciation", precision = 21, scale = 2)
    private BigDecimal valueAfterDepreciation;

    @Column(name = "processed_by")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String processedBy;

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

    public AssetDepreciationHistory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBranchCode() {
        return this.branchCode;
    }

    public AssetDepreciationHistory branchCode(String branchCode) {
        this.setBranchCode(branchCode);
        return this;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchId() {
        return this.branchId;
    }

    public AssetDepreciationHistory branchId(String branchId) {
        this.setBranchId(branchId);
        return this;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getAssetRegisterCode() {
        return this.assetRegisterCode;
    }

    public AssetDepreciationHistory assetRegisterCode(String assetRegisterCode) {
        this.setAssetRegisterCode(assetRegisterCode);
        return this;
    }

    public void setAssetRegisterCode(String assetRegisterCode) {
        this.assetRegisterCode = assetRegisterCode;
    }

    public LocalDate getDepreciationDate() {
        return this.depreciationDate;
    }

    public AssetDepreciationHistory depreciationDate(LocalDate depreciationDate) {
        this.setDepreciationDate(depreciationDate);
        return this;
    }

    public void setDepreciationDate(LocalDate depreciationDate) {
        this.depreciationDate = depreciationDate;
    }

    public BigDecimal getDepreciationAmount() {
        return this.depreciationAmount;
    }

    public AssetDepreciationHistory depreciationAmount(BigDecimal depreciationAmount) {
        this.setDepreciationAmount(depreciationAmount);
        return this;
    }

    public void setDepreciationAmount(BigDecimal depreciationAmount) {
        this.depreciationAmount = depreciationAmount;
    }

    public BigDecimal getValueAfterDepreciation() {
        return this.valueAfterDepreciation;
    }

    public AssetDepreciationHistory valueAfterDepreciation(BigDecimal valueAfterDepreciation) {
        this.setValueAfterDepreciation(valueAfterDepreciation);
        return this;
    }

    public void setValueAfterDepreciation(BigDecimal valueAfterDepreciation) {
        this.valueAfterDepreciation = valueAfterDepreciation;
    }

    public String getProcessedBy() {
        return this.processedBy;
    }

    public AssetDepreciationHistory processedBy(String processedBy) {
        this.setProcessedBy(processedBy);
        return this;
    }

    public void setProcessedBy(String processedBy) {
        this.processedBy = processedBy;
    }

    // Inherited createdBy methods
    public AssetDepreciationHistory createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    // Inherited createdDate methods
    public AssetDepreciationHistory createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    // Inherited lastModifiedBy methods
    public AssetDepreciationHistory lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    // Inherited lastModifiedDate methods
    public AssetDepreciationHistory lastModifiedDate(Instant lastModifiedDate) {
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

    public AssetDepreciationHistory setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssetDepreciationHistory)) {
            return false;
        }
        return getId() != null && getId().equals(((AssetDepreciationHistory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetDepreciationHistory{" +
            "id=" + getId() +
            ", branchCode='" + getBranchCode() + "'" +
            ", branchId='" + getBranchId() + "'" +
            ", assetRegisterCode='" + getAssetRegisterCode() + "'" +
            ", depreciationDate='" + getDepreciationDate() + "'" +
            ", depreciationAmount=" + getDepreciationAmount() +
            ", valueAfterDepreciation=" + getValueAfterDepreciation() +
            ", processedBy='" + getProcessedBy() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
