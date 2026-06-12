package com.gvsolutions.service.dto;

import com.gvsolutions.domain.enumeration.StaffType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.gvsolutions.domain.ChurchStaff} entity.
 */
@Schema(description = "Staff and Worker Directories tracking variable skill roles")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ChurchStaffDTO implements Serializable {

    private Long id;

    private String staffCode;

    private String branchCode;

    private String branchId;

    private String fullName;

    private String position;

    private StaffType staffType;

    private String contactNumber;

    private BigDecimal hourlyRateOrMonthlySalary;

    private Boolean isActive;

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

    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public StaffType getStaffType() {
        return staffType;
    }

    public void setStaffType(StaffType staffType) {
        this.staffType = staffType;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public BigDecimal getHourlyRateOrMonthlySalary() {
        return hourlyRateOrMonthlySalary;
    }

    public void setHourlyRateOrMonthlySalary(BigDecimal hourlyRateOrMonthlySalary) {
        this.hourlyRateOrMonthlySalary = hourlyRateOrMonthlySalary;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChurchStaffDTO)) {
            return false;
        }

        ChurchStaffDTO churchStaffDTO = (ChurchStaffDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, churchStaffDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChurchStaffDTO{" +
            "id=" + getId() +
            ", staffCode='" + getStaffCode() + "'" +
            ", branchCode='" + getBranchCode() + "'" +
            ", branchId='" + getBranchId() + "'" +
            ", fullName='" + getFullName() + "'" +
            ", position='" + getPosition() + "'" +
            ", staffType='" + getStaffType() + "'" +
            ", contactNumber='" + getContactNumber() + "'" +
            ", hourlyRateOrMonthlySalary=" + getHourlyRateOrMonthlySalary() +
            ", isActive='" + getIsActive() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
