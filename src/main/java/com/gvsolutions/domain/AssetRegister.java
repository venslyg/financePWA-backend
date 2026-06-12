package com.gvsolutions.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import org.springframework.data.domain.Persistable;

/**
 * Asset Register & Depreciation
 */
@Entity
@Table(name = "asset_register")
@JsonIgnoreProperties(value = { "new" })
@org.springframework.data.elasticsearch.annotations.Document(indexName = "assetregister")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AssetRegister extends AbstractAuditingEntity<Long> implements Serializable, Persistable<Long> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "branch_code")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String branchCode;

    @Column(name = "asset_register_code")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String assetRegisterCode;

    @Column(name = "asset_category_code")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String assetCategoryCode;

    @Column(name = "asset_sub_category_code")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String assetSubCategoryCode;

    @Column(name = "asset_name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String assetName;

    @Column(name = "category")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String category;

    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

    @Column(name = "purchase_cost", precision = 21, scale = 2)
    private BigDecimal purchaseCost;

    @Column(name = "current_value", precision = 21, scale = 2)
    private BigDecimal currentValue;

    @Column(name = "depreciation_rate", precision = 21, scale = 2)
    private BigDecimal depreciationRate;

    @Column(name = "accumulated_depreciation", precision = 21, scale = 2)
    private BigDecimal accumulatedDepreciation;

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

    public AssetRegister id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBranchCode() {
        return this.branchCode;
    }

    public AssetRegister branchCode(String branchCode) {
        this.setBranchCode(branchCode);
        return this;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getAssetRegisterCode() {
        return this.assetRegisterCode;
    }

    public AssetRegister assetRegisterCode(String assetRegisterCode) {
        this.setAssetRegisterCode(assetRegisterCode);
        return this;
    }

    public void setAssetRegisterCode(String assetRegisterCode) {
        this.assetRegisterCode = assetRegisterCode;
    }

    public String getAssetCategoryCode() {
        return this.assetCategoryCode;
    }

    public AssetRegister assetCategoryCode(String assetCategoryCode) {
        this.setAssetCategoryCode(assetCategoryCode);
        return this;
    }

    public void setAssetCategoryCode(String assetCategoryCode) {
        this.assetCategoryCode = assetCategoryCode;
    }

    public String getAssetSubCategoryCode() {
        return this.assetSubCategoryCode;
    }

    public AssetRegister assetSubCategoryCode(String assetSubCategoryCode) {
        this.setAssetSubCategoryCode(assetSubCategoryCode);
        return this;
    }

    public void setAssetSubCategoryCode(String assetSubCategoryCode) {
        this.assetSubCategoryCode = assetSubCategoryCode;
    }

    public String getAssetName() {
        return this.assetName;
    }

    public AssetRegister assetName(String assetName) {
        this.setAssetName(assetName);
        return this;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getCategory() {
        return this.category;
    }

    public AssetRegister category(String category) {
        this.setCategory(category);
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getPurchaseDate() {
        return this.purchaseDate;
    }

    public AssetRegister purchaseDate(LocalDate purchaseDate) {
        this.setPurchaseDate(purchaseDate);
        return this;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public BigDecimal getPurchaseCost() {
        return this.purchaseCost;
    }

    public AssetRegister purchaseCost(BigDecimal purchaseCost) {
        this.setPurchaseCost(purchaseCost);
        return this;
    }

    public void setPurchaseCost(BigDecimal purchaseCost) {
        this.purchaseCost = purchaseCost;
    }

    public BigDecimal getCurrentValue() {
        return this.currentValue;
    }

    public AssetRegister currentValue(BigDecimal currentValue) {
        this.setCurrentValue(currentValue);
        return this;
    }

    public void setCurrentValue(BigDecimal currentValue) {
        this.currentValue = currentValue;
    }

    public BigDecimal getDepreciationRate() {
        return this.depreciationRate;
    }

    public AssetRegister depreciationRate(BigDecimal depreciationRate) {
        this.setDepreciationRate(depreciationRate);
        return this;
    }

    public void setDepreciationRate(BigDecimal depreciationRate) {
        this.depreciationRate = depreciationRate;
    }

    public BigDecimal getAccumulatedDepreciation() {
        return this.accumulatedDepreciation;
    }

    public AssetRegister accumulatedDepreciation(BigDecimal accumulatedDepreciation) {
        this.setAccumulatedDepreciation(accumulatedDepreciation);
        return this;
    }

    public void setAccumulatedDepreciation(BigDecimal accumulatedDepreciation) {
        this.accumulatedDepreciation = accumulatedDepreciation;
    }

    // Inherited createdBy methods
    public AssetRegister createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    // Inherited createdDate methods
    public AssetRegister createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    // Inherited lastModifiedBy methods
    public AssetRegister lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    // Inherited lastModifiedDate methods
    public AssetRegister lastModifiedDate(Instant lastModifiedDate) {
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

    public AssetRegister setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssetRegister)) {
            return false;
        }
        return getId() != null && getId().equals(((AssetRegister) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetRegister{" +
            "id=" + getId() +
            ", branchCode='" + getBranchCode() + "'" +
            ", assetRegisterCode='" + getAssetRegisterCode() + "'" +
            ", assetCategoryCode='" + getAssetCategoryCode() + "'" +
            ", assetSubCategoryCode='" + getAssetSubCategoryCode() + "'" +
            ", assetName='" + getAssetName() + "'" +
            ", category='" + getCategory() + "'" +
            ", purchaseDate='" + getPurchaseDate() + "'" +
            ", purchaseCost=" + getPurchaseCost() +
            ", currentValue=" + getCurrentValue() +
            ", depreciationRate=" + getDepreciationRate() +
            ", accumulatedDepreciation=" + getAccumulatedDepreciation() +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
