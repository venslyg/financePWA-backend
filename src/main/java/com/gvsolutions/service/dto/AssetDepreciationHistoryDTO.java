package com.gvsolutions.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.gvsolutions.domain.AssetDepreciationHistory} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AssetDepreciationHistoryDTO implements Serializable {

    private Long id;

    private String branchCode;

    private String branchId;

    private String assetRegisterCode;

    private LocalDate depreciationDate;

    private BigDecimal depreciationAmount;

    private BigDecimal valueAfterDepreciation;

    private String processedBy;

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

    public LocalDate getDepreciationDate() {
        return depreciationDate;
    }

    public void setDepreciationDate(LocalDate depreciationDate) {
        this.depreciationDate = depreciationDate;
    }

    public BigDecimal getDepreciationAmount() {
        return depreciationAmount;
    }

    public void setDepreciationAmount(BigDecimal depreciationAmount) {
        this.depreciationAmount = depreciationAmount;
    }

    public BigDecimal getValueAfterDepreciation() {
        return valueAfterDepreciation;
    }

    public void setValueAfterDepreciation(BigDecimal valueAfterDepreciation) {
        this.valueAfterDepreciation = valueAfterDepreciation;
    }

    public String getProcessedBy() {
        return processedBy;
    }

    public void setProcessedBy(String processedBy) {
        this.processedBy = processedBy;
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
        if (!(o instanceof AssetDepreciationHistoryDTO)) {
            return false;
        }

        AssetDepreciationHistoryDTO assetDepreciationHistoryDTO = (AssetDepreciationHistoryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, assetDepreciationHistoryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetDepreciationHistoryDTO{" +
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
