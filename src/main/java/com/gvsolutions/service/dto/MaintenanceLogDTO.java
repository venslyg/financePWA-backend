package com.gvsolutions.service.dto;

import com.gvsolutions.domain.enumeration.MaintenanceLogType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.gvsolutions.domain.MaintenanceLog} entity.
 */
@Schema(description = "Asset Maintenance Log")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MaintenanceLogDTO implements Serializable {

    private Long id;

    private String branchCode;

    private String branchId;

    private String maintenanceLogCode;

    private LocalDate logDate;

    private MaintenanceLogType logType;

    private String description;

    private BigDecimal cost;

    private String vendor;

    private LocalDate nextServiceDate;

    private String note;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private AssetRegisterDTO asset;

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

    public String getMaintenanceLogCode() {
        return maintenanceLogCode;
    }

    public void setMaintenanceLogCode(String maintenanceLogCode) {
        this.maintenanceLogCode = maintenanceLogCode;
    }

    public LocalDate getLogDate() {
        return logDate;
    }

    public void setLogDate(LocalDate logDate) {
        this.logDate = logDate;
    }

    public MaintenanceLogType getLogType() {
        return logType;
    }

    public void setLogType(MaintenanceLogType logType) {
        this.logType = logType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public LocalDate getNextServiceDate() {
        return nextServiceDate;
    }

    public void setNextServiceDate(LocalDate nextServiceDate) {
        this.nextServiceDate = nextServiceDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public AssetRegisterDTO getAsset() {
        return asset;
    }

    public void setAsset(AssetRegisterDTO asset) {
        this.asset = asset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MaintenanceLogDTO)) {
            return false;
        }

        MaintenanceLogDTO maintenanceLogDTO = (MaintenanceLogDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, maintenanceLogDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MaintenanceLogDTO{" +
            "id=" + getId() +
            ", branchCode='" + getBranchCode() + "'" +
            ", branchId='" + getBranchId() + "'" +
            ", maintenanceLogCode='" + getMaintenanceLogCode() + "'" +
            ", logDate='" + getLogDate() + "'" +
            ", logType='" + getLogType() + "'" +
            ", description='" + getDescription() + "'" +
            ", cost=" + getCost() +
            ", vendor='" + getVendor() + "'" +
            ", nextServiceDate='" + getNextServiceDate() + "'" +
            ", note='" + getNote() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", asset=" + getAsset() +
            "}";
    }
}
