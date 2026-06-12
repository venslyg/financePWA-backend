package com.gvsolutions.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.gvsolutions.domain.AssetRegister} entity.
 */
@Schema(description = "Asset Register & Depreciation")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AssetRegisterDTO implements Serializable {

    private Long id;

    private String branchCode;

    private String branchId;

    private String assetRegisterCode;

    private String assetCategoryCode;

    private String assetSubCategoryCode;

    private String assetName;

    private String category;

    private LocalDate purchaseDate;

    private BigDecimal purchaseCost;

    private BigDecimal currentValue;

    private BigDecimal depreciationRate;

    private BigDecimal accumulatedDepreciation;

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

    public String getAssetRegisterCode() {
        return assetRegisterCode;
    }

    public void setAssetRegisterCode(String assetRegisterCode) {
        this.assetRegisterCode = assetRegisterCode;
    }

    public String getAssetCategoryCode() {
        return assetCategoryCode;
    }

    public void setAssetCategoryCode(String assetCategoryCode) {
        this.assetCategoryCode = assetCategoryCode;
    }

    public String getAssetSubCategoryCode() {
        return assetSubCategoryCode;
    }

    public void setAssetSubCategoryCode(String assetSubCategoryCode) {
        this.assetSubCategoryCode = assetSubCategoryCode;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public BigDecimal getPurchaseCost() {
        return purchaseCost;
    }

    public void setPurchaseCost(BigDecimal purchaseCost) {
        this.purchaseCost = purchaseCost;
    }

    public BigDecimal getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(BigDecimal currentValue) {
        this.currentValue = currentValue;
    }

    public BigDecimal getDepreciationRate() {
        return depreciationRate;
    }

    public void setDepreciationRate(BigDecimal depreciationRate) {
        this.depreciationRate = depreciationRate;
    }

    public BigDecimal getAccumulatedDepreciation() {
        return accumulatedDepreciation;
    }

    public void setAccumulatedDepreciation(BigDecimal accumulatedDepreciation) {
        this.accumulatedDepreciation = accumulatedDepreciation;
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
        if (!(o instanceof AssetRegisterDTO)) {
            return false;
        }

        AssetRegisterDTO assetRegisterDTO = (AssetRegisterDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, assetRegisterDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetRegisterDTO{" +
            "id=" + getId() +
            ", branchCode='" + getBranchCode() + "'" +
            ", branchId='" + getBranchId() + "'" +
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
