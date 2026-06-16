package com.gvsolutions.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.gvsolutions.domain.AssetSubCategory} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AssetSubCategoryDTO implements Serializable {

    private Long id;

    private String branchCode;

    private String branchId;

    private String assetCategoryCode;

    private String assetSubCategoryCode;

    private String assetSubCategoryName;

    private Boolean isActive;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private AssetCategoryDTO category;

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

    public String getAssetSubCategoryName() {
        return assetSubCategoryName;
    }

    public void setAssetSubCategoryName(String assetSubCategoryName) {
        this.assetSubCategoryName = assetSubCategoryName;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
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

    public AssetCategoryDTO getCategory() {
        return category;
    }

    public void setCategory(AssetCategoryDTO category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssetSubCategoryDTO)) {
            return false;
        }

        AssetSubCategoryDTO assetSubCategoryDTO = (AssetSubCategoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, assetSubCategoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetSubCategoryDTO{" +
            "id=" + getId() +
            ", branchCode='" + getBranchCode() + "'" +
            ", branchId='" + getBranchId() + "'" +
            ", assetCategoryCode='" + getAssetCategoryCode() + "'" +
            ", assetSubCategoryCode='" + getAssetSubCategoryCode() + "'" +
            ", assetSubCategoryName='" + getAssetSubCategoryName() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", category=" + getCategory() +
            "}";
    }
}
